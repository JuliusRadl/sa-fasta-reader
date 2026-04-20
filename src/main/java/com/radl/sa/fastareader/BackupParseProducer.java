package com.radl.sa.fastareader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class BackupParseProducer implements Runnable {

	private File f;
	private final ObjectOutputStream oos;
	
	public BackupParseProducer(File f, ObjectOutputStream oos) {
		this.f = f;
		this.oos = oos;
	}

	public void run() {
		
		if (!f.exists()) {
			System.out.println("Keine gültige Datei");
			return;
		}
		
		// try-with-resource: Resourcen werden garantiert geschlossen, auch
		// wenn im Try-Block ein Fehler auftritt
		// Musste zu Java 9+ wechseln im Buildpath, damit die Variable oos
		// mit try-with-resource geht
		try (JsonReader jr = new JsonReader(new FileReader(f)); oos) {
			
			Gson g = new Gson();
			jr.beginArray();
			while(jr.hasNext()) {
				Sequence seq = g.fromJson(jr, Sequence.class);
				System.out.println(
						"Schreibe Sequenz " +
						seq.getHeader().substring(0, 20) + "...");
				oos.writeObject(seq);
				oos.flush();
				Thread.sleep(10);
			}

		} catch (IOException e) {
			System.err.println("Fehler beim Einlesen der Datei");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.err.println("Thread-sleep unterbrochen");
			e.printStackTrace();
		}
	}

}
