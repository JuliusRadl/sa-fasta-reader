package com.radl.sa.fastareader;

import com.formdev.flatlaf.FlatLightLaf;
import com.radl.sa.fastareader.*;

public class App {

	public static void main(String[] args) {
		
		FastaModel fm = new FastaModel();
		
		FlatLightLaf.setup();
		OnlineView fv = new OnlineView("FASTA-Manager", 500);
		fv.setVisible(true);
		
		OnlineMainController fc = new OnlineMainController(fm, fv);
		// TODO soll der View wirklich was vom Controller wissen?
		fv.setController(fc);
		
		
//		Experiments.doExperimentInetAdress();
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