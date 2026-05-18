package com.radl.sa.interfaces;

import java.util.ArrayList;

import com.radl.sa.fastareader.FastaController;
import com.radl.sa.services.Sequence;

public interface FastaViewable {
	
	public void updateSeqList(ArrayList<Sequence> seqList);
	
	public void setController(FastaController fc);
	
	public void setButtonsEnabled(boolean enabled);
}
