package com.radl.sa.services;

import java.io.IOException;
import java.io.ObjectOutputStream;

import com.radl.sa.interfaces.SequenceWritable;

public class LocalSequenceWriter implements SequenceWritable {
	
	private final ObjectOutputStream oos;
	
	public LocalSequenceWriter(ObjectOutputStream oos) {
		
		this.oos = oos;
	}

	public void write(Sequence seq) throws IOException {
		
		oos.writeObject(seq);
		oos.flush();
	}
	
	public void close() throws IOException {
		
		oos.close();
	}
}

