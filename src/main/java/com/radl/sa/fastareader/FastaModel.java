package com.radl.sa.fastareader;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

		FastaParseProducer producer = new FastaParseProducer(f, oos);
		FastaParseConsumer consumer = new FastaParseConsumer(ois, seqList);
		
		// CompletableFuture eig vor allem für Verkettung von async Tasks, ohne
		// wie beim Callable jedesmal mit get zu warten
		CompletableFuture<Void> prodFuture = CompletableFuture.runAsync(producer, ex);
		CompletableFuture<Void> conFuture = CompletableFuture.runAsync(consumer, ex);

		// Synchronisierung, damit aufrufbar von SwingWorker
		// Futures zu einem Objekt kombinieren und darauf warten
		CompletableFuture.allOf(prodFuture, conFuture).join();
	}
	
	public ArrayList<Sequence> getSeqList() {
		return seqList;
	}
}
