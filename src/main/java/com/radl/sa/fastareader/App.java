package com.radl.sa.fastareader;

import com.formdev.flatlaf.FlatLightLaf;
import com.radl.sa.fastareader.*;

public class App {

	public static void main(String[] args) throws Exception {
		
		FastaModel fm = new FastaModel();
		
		FlatLightLaf.setup();
		FastaView fv = new FastaView("FASTA-Manager", 500);
		fv.setVisible(true);
		
		LocalMainController fc = new LocalMainController(fm, fv);
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