package com.radl.sa.server;

import com.radl.sa.services.ByteSequenceReader;
import com.radl.sa.services.FastaParseConsumer;

public class ClientParseService implements Runnable {

	private final DataOutputStream dos;
	private final DataInputStream dis;
	private final File f;
	
	public ClientParseService(DataOutputStream dos, DataInputStream dis,
			ArrayList<Sequence> seqList, File f) {
		this.dos = dos;
		this.dis = dis;
		this.f = f;
		this.seqList = seqList;
	}
	
	public void run() {
		// send file to server
		FileSender fs = new FileSender(f, dos);
		fs.send();
		
		// receive sequence objects with consumer
		ByteSequenceReader bsr = new ByteSequenceReader(dis);
		FastaParseConsumer fpc = new FastaParseConsumer(bsr, seqList);
		fpc.run();
	}
}
