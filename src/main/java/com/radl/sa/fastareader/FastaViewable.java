package com.radl.sa.fastareader;

import java.util.ArrayList;

public interface FastaViewable {
	
	public void updateSeqList(ArrayList<Sequence> seqList);
	
	public void setController(FastaController fc);
}
