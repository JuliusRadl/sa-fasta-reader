package com.radl.sa.interfaces;

import java.io.EOFException;
import java.io.IOException;

import com.radl.sa.services.Sequence;

public interface SequenceReadable {
	
	public Sequence read() throws IOException, EOFException, ClassNotFoundException;
}
