package com.radl.sa.services;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import org.apache.commons.lang3.SerializationUtils;

import com.radl.sa.interfaces.SequenceReadable;

public class ByteSequenceReader implements SequenceReadable {
	
	private DataInputStream dis;
	
	public ByteSequenceReader(DataInputStream dis) {
		this.dis = dis;
	}
	
	public Sequence read() throws IOException, ClassNotFoundException, EOFException {
		
		int object_length = dis.readInt();
		byte[] bytes = dis.readNBytes(object_length);
		Object obj = SerializationUtils.deserialize(bytes);
		
		if (obj instanceof EndOfStreamToken) {
			throw new EOFException();
		}
		return (Sequence) obj;
	}
}
