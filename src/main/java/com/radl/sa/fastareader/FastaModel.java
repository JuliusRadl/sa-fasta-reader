package com.radl.sa.fastareader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FastaModel {

	private final ArrayList<Sequence> seqList;
	private final ExecutorService ex;

	public FastaModel() {
		this.seqList = new ArrayList<Sequence>();
		this.ex = Executors.newFixedThreadPool(2);
	}

	// Lass Fehler den Controller handeln, um sie im UI anzeigen zu können
	public void parseFasta(File f) throws IOException {

		// Pipe aufbauen
		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream();
		pis.connect(pos);
		ObjectOutputStream oos = new ObjectOutputStream(pos);
		ObjectInputStream ois = new ObjectInputStream(pis);

		// kein Input Validation hier, ist glaub ich eher Aufgabe des Controllers
		int extPos = f.getName().lastIndexOf(".");
		String extension = f.getName().substring(extPos + 1);
		Runnable producer;
		// nicht == verwenden, das testet, ob es dasselbe Objekt ist
		if (extension.equals("fasta")) {
			producer = new FastaParseProducer(f, oos);
		} else {
			producer = new BackupParseProducer(f, oos);
		}
		Runnable consumer = new FastaParseConsumer(ois, seqList);
	

		// CompletableFuture eig vor allem für Verkettung von async Tasks, ohne
		// wie beim Callable jedesmal mit get zu warten
		// TODO je nach File extension anderen Producer aufrufen
		CompletableFuture<Void> prodFuture = CompletableFuture.runAsync(producer, ex);		
		CompletableFuture<Void> conFuture = CompletableFuture.runAsync(consumer, ex);

		// Synchronisierung, damit aufrufbar von SwingWorker
		// Futures zu einem Objekt kombinieren und darauf warten
		CompletableFuture.allOf(prodFuture, conFuture).join();
	}
	
	
	public void saveSeqList(File f) throws IOException {
		// Eigener Thread macht hier eig keinen Sinn, wenn ich die Methode
		// später in einem SwingWorker aufrufe. Ich will ja gar nicht, dass
		// zb parseFasta gleichzeitig aufgerufen werden kann. Diese Methode
		// darf ruhig blockieren
		// Die Aufgabenstellung ist mit dem SwingWorker schon erfüllt: Das
		// Abspeichern passiert nicht im Event Dispatch Thread (GUI)
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fw = new FileWriter(f);
		gson.toJson(seqList, fw);
		// Ganz wichtig: Writer schließen, sonst wird Json nicht richtig terminiert
		fw.close();
	}
	
	public ArrayList<Sequence> getSeqList() {
		return seqList;
	}
}
