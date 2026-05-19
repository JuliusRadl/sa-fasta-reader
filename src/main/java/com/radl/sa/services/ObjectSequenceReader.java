package com.radl.sa.services;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.radl.sa.interfaces.SequenceReadable;

public class ObjectSequenceReader implements SequenceReadable {
	
	private ObjectInputStream ois;
	
	public ObjectSequenceReader(ObjectInputStream ois) {
		this.ois = ois;
	}
	
	public Sequence read() throws IOException, ClassNotFoundException, EOFException {
		
		Object obj = ois.readObject();
		if (obj instanceof EndOfStreamToken) {
			throw new EOFException();
		}
		return (Sequence) obj;
	}
}
