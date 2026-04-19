package com.radl.sa.fastareader;

import java.io.File;

public interface FastaControllable {

	public void pressedParseButton(File f);
	
	public void setModel(FastaModel fm);
	
	public void setView(FastaView fv);
}
