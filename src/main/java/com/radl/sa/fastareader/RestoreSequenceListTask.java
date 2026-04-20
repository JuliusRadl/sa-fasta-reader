package com.radl.sa.fastareader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RestoreSequenceListTask implements Runnable {

	private File seqListFile;
	private ObjectOutputStream oos;
	
	public RestoreSequenceListTask(File seqListFile, ObjectOutputStream oos) {
		this.seqListFile = seqListFile;
		this.oos = oos;
	}

	public void run() {
		
		if (!seqListFile.exists()) {
			System.out.println("Keine gültige Datei");
			return;
		}
		
		try {
			//Sequenzlisten-JSON einlesen, dabei Type Erasure umgehen
			BufferedReader br = new BufferedReader(new FileReader(seqListFile));
			Gson gson = new Gson();
			Type ts = new TypeToken<ArrayList<Sequence>>() {}.getType();
			ArrayList<Sequence> seqList = gson.fromJson(br, ts);
			br.close();
			
			// An Pipe schicken
			for (Sequence seq : seqList) {
				System.out.println("Stopfe Sequenz " + seq.getHeader().substring(0, 20) + "..." + " in Pipe.");
				oos.writeObject(seq);
			}
		} catch (IOException e) {
			System.err.println("Fehler beim Einlesen der Datei");
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				System.err.println("Fehler beim Schließen des Streams");
				e.printStackTrace();
			}
		}
	}

}
