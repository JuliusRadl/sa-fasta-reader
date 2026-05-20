package com.radl.sa.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class ConnectionResourcesHandler implements AutoCloseable {
	
	/*
	 * Purpose: Avoid typing out resource creation every time
	 * 
	 * Implements AutoCloseable and thus can be used with try-with-resources
	 */


	// DataInputStreams/DataOutputStreams automatically send and read
	// length for primitives and Strings
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket client;
	
	// should i throw these exceptions or catch them here, leaving the
	// crh in an unusable state? => throw
	public ConnectionResourcesHandler(Properties props) throws UnknownHostException, IOException {
		this.client = new Socket(props.getProperty("hostname"),
				Integer.parseInt(props.getProperty("port")));
		this.dis = new DataInputStream(client.getInputStream());
		this.dos = new DataOutputStream(client.getOutputStream());
	}
	
	// second constructor for server with established socket connection
	public ConnectionResourcesHandler(Socket client) throws IOException {
		this.client = client;
		this.dis = new DataInputStream(client.getInputStream());
		this.dos = new DataOutputStream(client.getOutputStream());
	}
	
	public DataInputStream getDis() {
		return dis;
	}
	
	public DataOutputStream getDos() {
		return dos;
	}
	
	public void close() throws IOException {
		dis.close();
		dos.close();
		client.close();
	}
}
