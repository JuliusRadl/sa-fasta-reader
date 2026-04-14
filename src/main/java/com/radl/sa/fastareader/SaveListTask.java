package com.radl.sa.fastareader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveListTask implements Runnable {

	private File saveFile;
	private ArrayList<Sequence> seqList;
	
	public SaveListTask(ArrayList<Sequence> seqList, File saveFile) {
		this.saveFile = saveFile;
		this.seqList = seqList;
	}

	public void run() {
		
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fw = new FileWriter(saveFile);
			gson.toJson(seqList, fw);
			// Ganz wichtig: Writer schließen, sonst wird Json nicht richtig terminiert
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Fehler beim Speichern der Datei");
		}
	}

}
