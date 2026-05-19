package com.radl.sa.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class FastaServerThread implements Runnable {

	private Socket client;

	public FastaServerThread(Socket client) {

		this.client = client;
	}

	public void run() {

		// open input & output  streams with try-with-resources to guarantee closing
		// PrintWriter autoflushes with true as second argument
		try (
				DataInputStream dis = new DataInputStream(client.getInputStream());
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());
		) {
			// Data streams automatically send and read length for primitives and Strings
			// int greeting_length = dis.readInt();
			
			// initiate conversation with client
			String input = dis.readUTF();
			String output = FastaServerProtocol.processInput(input);
			dos.writeUTF(output);
			
			while (true) {
				input = dis.readUTF();
				
				output = FastaServerProtocol.processInput(input);
				dos.writeUTF(output);
				
				// end thread (depending on input, not output)
				if (input.equals(FastaServerProtocol.EXIT_SIGNAL)) {
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
