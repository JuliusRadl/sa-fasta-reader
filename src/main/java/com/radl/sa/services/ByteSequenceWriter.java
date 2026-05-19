package com.radl.sa.services;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.commons.lang3.SerializationUtils;

import com.radl.sa.interfaces.SequenceWritable;

public class ByteSequenceWriter implements SequenceWritable {

	private final DataOutputStream dos;
	
	public ByteSequenceWriter(DataOutputStream dos) {
		
		this.dos = dos;
	}
	
	public void write (Sequence seq) throws IOException {
		
		byte[] bytes = SerializationUtils.serialize(seq);
		dos.writeInt(bytes.length);
		dos.write(bytes);
	}
	
	public void signalEnd() throws IOException {
		
		byte[] bytes = SerializationUtils.serialize(new EndOfStreamToken());
		dos.writeInt(bytes.length);
		dos.write(bytes);
	}
}
