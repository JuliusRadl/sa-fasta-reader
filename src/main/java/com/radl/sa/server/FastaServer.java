package com.radl.sa.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FastaServer {

	// TODO might be better as singleton
	private ServerSocket server;
	
	// threadpool
	private ExecutorService ex;

	public FastaServer(int port) throws IOException {
			
		server = new ServerSocket(port);
		ex = Executors.newFixedThreadPool(5);
		
		//
		System.out.println(server.getLocalSocketAddress());
		System.out.println("FastaServer auf Port " + port + " gestartet!");
		
		while (true) {
			// connect to Client
			Socket client = server.accept();
			ConnectionResourcesHandler crh = new ConnectionResourcesHandler(client);
			
			// status update
			System.out.println("Anfrage von " + client.getLocalAddress().toString());
			
			// start individual server thread
			FastaServerThread fsv = new FastaServerThread(crh);
			ex.submit(fsv);
		}
	}
}
