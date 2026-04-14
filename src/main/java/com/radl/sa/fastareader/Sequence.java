package com.radl.sa.fastareader;

import java.io.Serializable;

public class Sequence implements Serializable {
	
	private String filename;
	private String header;
	private String sequence;
	
	public Sequence(String filename, String header, String sequence) {
		this.filename = filename;
		this.header = header;
		this.sequence = sequence;
	}
	
	public String getFilename() {
		return filename;
	}

	public String getHeader() {
		return header;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	@Override
	public String toString() {
		String seqText = String.format("Filename: %s\nHeader: %s\nSequence: %s",
				filename, header, sequence);
		return(seqText);
	}
	
}
