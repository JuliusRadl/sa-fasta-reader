package com.radl.sa.interfaces;

import java.io.File;

import com.radl.sa.fastareader.FastaModel;
import com.radl.sa.fastareader.FastaView;

public interface FastaControllable {

	public void pressedParseButton(File f);
	
	public void pressedSaveButton(File f);
	
	public void setModel(FastaModel fm);
	
	public void setView(FastaView fv);
}
