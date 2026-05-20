package com.radl.sa.server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.radl.sa.services.ByteSequenceWriter;
import com.radl.sa.services.FastaParseProducer;

public class ServerProtocol {
	
	/*
	 * Defines acceptable commands for the server thread
	 * 
	 * Defines how the server thread reacts to such commands
	 * 
	 * Avoids bloat in server thread class
	 */
	
	public final static String GREET_MESSAGE = "Hallo vom Server!";
	
	// exit command unnecessary, since client closing socket throws
	// exception, exiting command loop of server thread
	// public final static String EXIT_COMMAND = "EXIT";
	// public final static String EXIT_MESSAGE = "Tschüss vom Server!";
	
	public final static String TIME_COMMAND = "TIME";
	public final static String SET_USER_COMMAND = "SET_USER";
	public final static String PARSE_COMMAND = "PARSE";
	
	private final ConnectionResourcesHandler crh;
	
	public ServerProtocol(ConnectionResourcesHandler crh) {
		this.crh = crh;
	}
	
	// static method, no need to instantiate object
	public void processCommand(String command) throws IOException {
		
		switch (command) {
		// parse fasta
		case PARSE_COMMAND : {
			parseFasta();
		}
		}
	}
	
	public void parseFasta() throws IOException {
		// receive file into tmp file
		FileReceiver fr = new FileReceiver(crh.getDis());
		File f = fr.receive();
		System.out.println(f.getAbsolutePath());

		// parse file
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			ByteSequenceWriter bsw = new ByteSequenceWriter(crh.getDos());
			FastaParseProducer fpp = new FastaParseProducer(br, bsw, f.getName());
			fpp.run();
			// catch eof separately, even tho it is a subclass of io exception,
			// because eof is regular exception we want and expect when reading a file
		} catch (EOFException e) {
			e.printStackTrace();
		}
	}
}
