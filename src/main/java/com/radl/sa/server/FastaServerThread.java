package com.radl.sa.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.radl.sa.services.ByteSequenceReader;
import com.radl.sa.services.ByteSequenceWriter;
import com.radl.sa.services.FastaParseConsumer;
import com.radl.sa.services.FastaParseProducer;

public class FastaServerThread implements Runnable {
	
	/*
	 * One thread per socket / client. 
	 */

	private final ConnectionResourcesHandler crh;

	public FastaServerThread(ConnectionResourcesHandler crh) {

		this.crh = crh;
	}

	public void run() {

		// open input & output streams with try-with-resources to guarantee closing
		try (crh) {
			
			ServerProtocol sp = new ServerProtocol(crh);

			// command loop
			while (true) {
				String command = crh.getDis().readUTF();
				sp.processCommand(command);
			}
			// catch eof separately, even though it is a subclass of io exception,
			// because eof is regular exception we expect when the client closes
			// the socket
		} catch (EOFException e) {
			String msg = ("Connection was closed.");
			System.err.println(msg);
		} catch (IOException e) {
			String msg = ("IO error in server thread.");
			System.err.println(msg);
			e.printStackTrace();
		}
	}
}
