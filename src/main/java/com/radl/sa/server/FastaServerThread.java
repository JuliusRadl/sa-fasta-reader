package com.radl.sa.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	private final Socket client;

	public FastaServerThread(Socket client) {

		this.client = client;
	}

	public void run() {

		// open input & output streams with try-with-resources to guarantee closing
		// also pass client to try with resources to guarantee closing
		try (DataInputStream dis = new DataInputStream(client.getInputStream());
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());
				client;) {
			// Data streams automatically send and read length for primitives and Strings
			// int greeting_length = dis.readInt();

			// receive file into tmp file
			FileReceiver fr = new FileReceiver(dis);
			File f = fr.receive();
			System.out.println(f.getAbsolutePath());

			// parse file
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				ByteSequenceWriter bsw = new ByteSequenceWriter(dos);
				FastaParseProducer fpp = new FastaParseProducer(br, bsw, f.getName());
				fpp.run();
			}

			String input = dis.readUTF();
			String output = FastaServerProtocol.processInput(input);
			dos.writeUTF(output);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
