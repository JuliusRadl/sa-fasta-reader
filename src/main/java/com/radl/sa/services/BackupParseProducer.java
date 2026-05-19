package com.radl.sa.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class BackupParseProducer implements Runnable {

	private BufferedReader br;
	private final ObjectOutputStream oos;
	
	public BackupParseProducer(BufferedReader br, ObjectOutputStream oos) {
		this.br = br;
		this.oos = oos;
	}

	public void run() {
		
		try {
			
			// reader is intentionally left open if error occurs in try block,
			// so the BufferedReader can be closed by its owner instead
			JsonReader jr = new JsonReader(br);
			
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
