package com.radl.sa.fastareader;

import java.io.File;
import java.io.IOException;

public class FastaController implements FastaControllable {

	private FastaModel fm;
	private FastaView fv;
	
	public FastaController (FastaModel fm, FastaView fg) {

		setModel(fm);
		setView(fg);
	}
	
	public void pressedParseButton(File f) {
		
		try {
			fm.parseFasta(f);
			fv.updateSeqList(fm.getSeqList());
		} catch (IOException e) {
			fv.displayError("IO-Fehler beim Parsen");
			e.printStackTrace();
		}
	}
	
	public void setView(FastaView fv) {
		this.fv = fv;
	}
	
	public void setModel(FastaModel fm) {
		this.fm = fm;
	}
}
