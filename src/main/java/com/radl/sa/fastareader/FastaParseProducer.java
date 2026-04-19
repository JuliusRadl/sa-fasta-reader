package com.radl.sa.fastareader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FastaParseProducer implements Runnable {

	// File statt Path, um "Time Of Read vs Time of Use" Probleme zu vermeiden
	private File fastaFile;
	private ObjectOutputStream oos;
	
	public FastaParseProducer(File fastaFile, ObjectOutputStream oos) {
		this.fastaFile = fastaFile;
		this.oos = oos;
	}

	public void run() {
		if (!fastaFile.exists()) {
			System.out.println("Keine gültige Datei");
			return;
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fastaFile));
			StringBuffer sb = new StringBuffer();
			String filename = fastaFile.getName().toString();

			// Checken ob die Fasta überhaupt was enthält
			// und das Zeug am Anfang wegschneiden
			String line = null;
			while(true) {
				line = br.readLine();
				// Rausspringen, falls Fasta leer
				if (line == null) {
					System.err.println("Fasta-Datei ist leer.\nEinlesen abgebrochen.");
					br.close();
					return;
				}
				line.trim();
				if (line.startsWith(">")) {
					sb.append(line);
					break;
				}
			}

			// while (true) loop
			while(true) {
				// readline bis > oder bis readline == null
				line = br.readLine();
				if (line == null || line.startsWith(">")) {
					// sequenz objekt erzeugen und in die pipe stecken
					// dabei trailing spaces und linebreaks trimmen
					String seqText = sb.toString();
					// StringBuffer zurücksetzen
					sb.setLength(0);
					// white spaces entfernen (vorne können keine sein)
					seqText.stripTrailing();
					// Header extrahieren
					int seqPos = seqText.indexOf("]");
					// wenn Sequenz abgeschnitten, sofort rausspringen
					if (seqPos == -1) { 
						System.err.println(
								"Unvollständigen Header gelesen.\n" + 
								"Einlesen abgebrochen.");
						br.close();
						return;
					}					
					String header = seqText.substring(0, seqPos + 1);
					String sequence = seqText.substring(seqPos + 1);
					Sequence seq = new Sequence(filename, header, sequence);
					System.out.println(
									"Schreibe Sequenz " +
									seq.getHeader().substring(0, 20) + "...");
					oos.writeObject(seq);
					oos.flush();
					// TODO entfernen
					Thread.sleep(10);
				}
				// Beim Ende der Datei aus Schleife springen
				if (line == null) {
					System.out.println("Ende der Datei erreicht.\nEinlesen beendet.");
					break;
				}
				// Zeile an Stringbuilder anhängen
				sb.append(line);
			}
			// BufferedReader schließen
			br.close();
			// TODO in pipe umschreiben
		} catch (IOException e) {
			System.err.println("Fehler beim Einlesen der Datei");
		} catch (InterruptedException e) {
			// Falls mein sleep interrupted wurde
			e.printStackTrace();
		} finally {
			try {
				// OOS schließen, um Signal zu geben, dass keine weiteren Objekte kommen
				oos.close();
			} catch (IOException e) {
				System.err.println("Fehler beim Schließen des Output-Streams.");
				e.printStackTrace();
			}
		}
	}

}
