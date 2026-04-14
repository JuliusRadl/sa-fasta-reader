package com.radl.sa.fastareader;

import com.formdev.flatlaf.FlatLightLaf;

public class App {

	public static void main(String[] args) throws Exception {
		
		FlatLightLaf.setup();
		FastaGUI fg = new FastaGUI("FASTA-Manager", 500);
		fg.setVisible(true);
		
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