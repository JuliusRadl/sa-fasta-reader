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
		
		FastaModel fm = new FastaModel();
		
		FlatLightLaf.setup();
		FastaView fv = new FastaView("FASTA-Manager", 500);
		fv.setVisible(true);
		
		FastaController fc = new FastaController(fm, fv);
		// TODO soll der View wirklich was vom Controller wissen?
		fv.setController(fc);
		
//		Experiments.doExperiment1();
//		Experiments.doExperiment2();
//		Experiments.doExperiment3();
//		Experiments.doExperimentNio();
//		Experiments.doExperimentSeqReader();
//		Experiments.doExperimentSerializable();
//		Experiments.doExperimentPipe();
//		Experiments.doExperimentGUI();
	}
}