package com.radl.sa.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.radl.sa.interfaces.SequenceWritable;

public class FastaParseProducer implements Runnable {

	// File statt Path, um "Time Of Read vs Time of Use" Probleme zu vermeiden
	// private File fastaFile;
	
	// BufferedReader instead of File so as to be independent of files, just parse text
	private BufferedReader br;
	
	// strategy pattern: exchangable method for writing sequence objects to different stream types
	private SequenceWritable sw;
	
	private String filename;
	
	public FastaParseProducer(BufferedReader br, SequenceWritable sw, String filename) {
		this.br = br;
		this.sw = sw;
		this.filename = filename;
	}

	public void run() {
		
		try {
			StringBuffer sb = new StringBuffer();

			// Checken ob die Fasta überhaupt was enthält
			// und das Zeug am Anfang wegschneiden
			String line = null;
			while(true) {
				line = br.readLine();
				// Rausspringen, falls Fasta leer
				if (line == null) {
					System.err.println("Fasta-Datei ist leer.\nEinlesen abgebrochen.");
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
						return;
					}					
					String header = seqText.substring(0, seqPos + 1);
					String sequence = seqText.substring(seqPos + 1);
					Sequence seq = new Sequence(filename, header, sequence);
					System.out.println(
									"Schreibe Sequenz " +
									seq.getHeader().substring(0, 20) + "...");
					
					// strategy pattern
					sw.write(seq);
					
					// TODO entfernen
					Thread.sleep(10);
				}
				// Beim Ende der Datei aus Schleife springen
				if (line == null) {
					// new solution to send eof signal: end token
					sw.signalEnd();
					
					System.out.println("Ende der Datei erreicht.\nEinlesen beendet.");
					break;
				}
				// Zeile an Stringbuilder anhängen
				sb.append(line);
			}
		} catch (IOException e) {
			System.err.println("Fehler beim Einlesen der Datei");
		} catch (InterruptedException e) {
			// Falls mein sleep interrupted wurde
			e.printStackTrace();
		} finally {
			try {
				sw.signalEnd();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Garantieren, dass Stream geschlossen wird, und zwar durch Producer,
		// um Kontrolle über EOF-Signal im Thread zu haben
//		finally {
//			try {
//				// OOS schließen, um EOF-Signal zu geben, dass keine weiteren Objekte kommen
//				// strategy pattern
//				sw.close();
//			} catch (IOException e) {
//				System.err.println("Fehler beim Schließen des Output-Streams.");
//				e.printStackTrace();
//			}
//		}
	}

}
