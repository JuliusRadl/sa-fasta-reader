package com.radl.sa.fastareader;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.formdev.flatlaf.FlatLightLaf;

public class App {

	public static void main(String[] args) throws Exception {
		
//		FlatLightLaf.setup();
//		FastaGUI fg = new FastaGUI("FASTA-Manager", 500);
//		fg.setVisible(true);
		
//		Experiments.doExperiment1();
//		Experiments.doExperiment2();
//		Experiments.doExperiment3();
//		Experiments.doExperimentNio();
		Experiments.doExperimentSeqReader();
//		Experiments.doExperimentSerializable();
//		Experiments.doExperimentPipe();
//		Experiments.doExperimentGUI();
	}
	
	public void processFasta(File f, ArrayList<Sequence> seqList) throws IOException {
		
		// Pipe aufbauen
		PipedOutputStream pos = new PipedOutputStream();
		PipedInputStream pis = new PipedInputStream();
		pis.connect(pos);
		ObjectOutputStream oos = new ObjectOutputStream(pos);
		ObjectInputStream ois = new ObjectInputStream(pis);
		
		FastaParseProducer producer = new FastaParseProducer(f, oos);
		FastaParseConsumer consumer = new FastaParseConsumer(ois, seqList);
		
		// TODO Executors ins Model übertragen als Instanzvariable
		ExecutorService ex = Executors.newFixedThreadPool(2);
		ex.submit(producer);
		ex.submit(consumer);
		
		// Die Pipe verschwindet beim Verlassen der Methode nicht,
		// weil die Threads immer noch Referenzen darauf halten
	}
}