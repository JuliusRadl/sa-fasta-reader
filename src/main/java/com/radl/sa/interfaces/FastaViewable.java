package com.radl.sa.interfaces;

import java.io.File;
import java.util.ArrayList;

import com.radl.sa.fastareader.LocalMainController;
import com.radl.sa.services.Sequence;

public interface FastaViewable {
	
	public void updateSeqList(ArrayList<Sequence> seqList);
	
	public void setController(FastaControllable fc);
	
	public void setButtonsEnabled(boolean enabled);
	
	public File openFileDialogue();

	public void displayProgress(boolean b, String string);
}
