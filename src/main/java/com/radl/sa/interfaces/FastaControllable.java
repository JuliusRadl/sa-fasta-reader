package com.radl.sa.interfaces;

import java.io.File;

import com.radl.sa.fastareader.FastaModel;
import com.radl.sa.fastareader.LocalView;

public interface FastaControllable {

	public void pressedParseButton();
	
	public void pressedSaveButton();

	public void pressedBlastButton();

	public void pressedBrowserButton();
	
	public void setModel(FastaModellable fm);
	
	public void setView(FastaViewable fv);

}
