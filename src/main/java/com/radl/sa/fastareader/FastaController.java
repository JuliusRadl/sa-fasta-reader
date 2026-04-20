package com.radl.sa.fastareader;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;

public class FastaController implements FastaControllable {

	private FastaModel fm;
	private FastaView fv;
	
	public FastaController (FastaModel fm, FastaView fg) {

		setModel(fm);
		setView(fg);
	}
	
	public void pressedParseButton(File f) {
		
		// Buttons im EDT disablen
		fv.setButtonsEnabled(false);
		// Parameter 1: Rückgabe-Wert
		// Parameter 2: Progress-Werte
		// Muss man beides nicht nutzen
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {

			public String doInBackground() {
				
				try {
					fm.parseFasta(f);
					return "Fertig";
				} catch (IOException e) {
					fv.displayError("IO-Fehler beim Parsen");
					return "IO-Fehler beim Parsen";
				}
			}
			
			// Wird immer aufgerufen, auch wenn gecancelt (aber nicht wenn gekillt)
			public void done() {
				
				fv.updateSeqList(fm.getSeqList());
				fv.setButtonsEnabled(true);
			}
		};
		sw.execute();
	}
	
	public void setView(FastaView fv) {
		this.fv = fv;
	}
	
	public void setModel(FastaModel fm) {
		this.fm = fm;
	}
}
