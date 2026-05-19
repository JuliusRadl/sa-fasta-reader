package com.radl.sa.interfaces;

import java.io.IOException;

import com.radl.sa.services.Sequence;

public interface SequenceWritable {
	
	public void write(Sequence seq) throws IOException;
	
	public void signalEnd() throws IOException;
}
