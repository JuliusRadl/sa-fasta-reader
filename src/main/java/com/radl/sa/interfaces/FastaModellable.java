package com.radl.sa.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.radl.sa.services.Sequence;

public interface FastaModellable {

	public void parseFasta(File f) throws IOException;
	
	// Das Modell soll komplett unabhängig sein und nichts über View oder
	// Controller wissen, deshalb kein Setter
	// public void setController(FastaController fc);

	public ArrayList<Sequence> getSeqList();

	// TODO ist das normal, dass Interfaces die Exception definieren?
	public void saveSeqList(File f) throws IOException;
}
