package com.radl.sa.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
		// TODO debuggen
		System.out.println("thread gestartet");

		// open input & output  streams with try-with-resources to guarantee closing
		// PrintWriter autoflushes with true as second argument
		try (
				BufferedReader br = new BufferedReader(
						new InputStreamReader(client.getInputStream()));
				PrintWriter pw = new PrintWriter(
						new OutputStreamWriter(client.getOutputStream()), true);
		) {
			// TODO debuggen, hier hängts
			System.out.println("initiate conversation");
			
			// initiate conversation with client
			String input = br.readLine();
			String output = FastaServerProtocol.processInput(input);
			pw.println(output);
			
			// TODO debuggen, wird nicht erreicht
			System.out.println("conversation initiated");
			
			while (true) {
				input = br.readLine();
				output = FastaServerProtocol.processInput(input);
				pw.println(output);
				
				// end thread
				if (output.equals(FastaServerProtocol.EXIT_SIGNAL)) {
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
