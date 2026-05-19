package com.radl.sa.server;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		
		int fastaPort = 12345;

		try {
			
			FastaServer fs = new FastaServer(fastaPort);
		} catch (IOException e) {
			String msg = "Fehler beim Starten des Fasta-Servers.";
			System.err.println(msg);
			e.printStackTrace();
		}
	}
}
