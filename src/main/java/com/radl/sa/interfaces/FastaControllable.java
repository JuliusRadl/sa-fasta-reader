package com.radl.sa.interfaces;

import java.io.File;

import com.radl.sa.fastareader.FastaModel;
import com.radl.sa.fastareader.FastaView;

public interface FastaControllable {

	public void pressedParseButton();
	
	public void pressedSaveButton(File f);
	
	public void setModel(FastaModellable fm);
	
	public void setView(FastaViewable fv);

	public void pressedBlastButton(File file);

	public void pressedBrowserButton();
}
