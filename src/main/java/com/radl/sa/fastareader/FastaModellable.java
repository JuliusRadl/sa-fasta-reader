package com.radl.sa.fastareader;

import java.io.File;

public interface FastaModellable {

	public void parseFasta(File f);
	
	// Das Modell soll komplett unabhängig sein und nichts über View oder
	// Controller wissen, deshalb kein Setter
	// public void setController(FastaController fc);
}
