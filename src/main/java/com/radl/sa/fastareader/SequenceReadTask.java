package com.radl.sa.fastareader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class SequenceReadTask implements Runnable, Callable<ArrayList<Sequence>> {

	// File statt Path, um "Time Of Read vs Time of Use" Probleme zu vermeiden
	private File fastaFile;
	private ArrayList<Sequence> seqList;
	
	public SequenceReadTask(File fastaFile) {
		this.fastaFile = fastaFile;
	}

	public ArrayList<Sequence> call() {
		run();
		return(seqList);
	}

	public void run() {
		if (!fastaFile.exists()) {
			System.out.println("Keine gültige Datei");
			return;
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fastaFile));
			StringBuffer sb = new StringBuffer();
			seqList = new ArrayList<>();
			String filename = fastaFile.getName().toString();

			// Erste Zeile auslesen
			sb.append(br.readLine());
			System.out.println(sb);
			System.out.println("xx");
			
			// Datei Zeile für Zeile auslesen
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(">")) {

					// Sequenz-Objekt erzeugen
					String seqText = sb.toString();
					int seqPos = seqText.indexOf("]");
					String header = seqText.substring(0, seqPos + 1);
					String sequence = seqText.substring(seqPos + 1);
					Sequence sq = new Sequence(filename, header, sequence);
					seqList.add(sq);

					// StringBuffer zurücksetzen
					sb.setLength(0);
				}
				sb.append(line);
			}

			// Letztes Sequenz-Objekt erzeugen
			String seqText = sb.toString();
			int seqPos = seqText.indexOf("]");
			String header = seqText.substring(0, seqPos + 1);
			String sequence = seqText.substring(seqPos + 1);
			Sequence sq = new Sequence(filename, header, sequence);
			seqList.add(sq);

			// BufferedReader schließen
			br.close();

		} catch (IOException e) {
			System.err.println("Fehler beim Einlesen der Datei");
		}
	}

}
