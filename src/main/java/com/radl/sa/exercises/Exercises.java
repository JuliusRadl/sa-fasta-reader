package com.radl.sa.exercises;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Exercises {
	
	public static void doA3_1() {

		try {
			InetAddress localhost = InetAddress.getLocalHost();
			InetAddress hswt_server = InetAddress.getByName("www.hswt.de");
			System.out.println("Lokale IP: " + localhost.getHostAddress());
			System.out.println("IP von " + hswt_server.getHostName() + ": " + hswt_server.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static void doA3_2() {

		try {
			URI hswt_ai_uri = new URI("https://www.hswt.de/studium/studienangebot/bachelor/applied-informatics");
			URL hswt_ai_url = hswt_ai_uri.toURL();
			InputStream is = hswt_ai_url.openStream();
			byte[] website_bytes = is.readAllBytes();
			String website_html = new String(website_bytes);
//			int start = website_html.indexOf("<HTML>");
//			website_html = website_html.substring(start);

			JFrame frame = new JFrame("Website");
			JLabel website = new JLabel(website_html);
			frame.add(website);
			frame.setSize(400, 400);
			frame.setVisible(true);

		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void doA3_3() {
		
		// declare port
		int timePort = 13;
		
		// declare host (internet time server from https://tf.nist.gov/tf-cgi/servers.cgi)
		String timeHost = "time-a-g.nist.gov";
		
		// create client socket
		try {
			Socket clientSocket = new Socket(timeHost, timePort);			
			// setup input stream
			InputStream is = clientSocket.getInputStream();
			
			// read bytes
			byte[] response = is.readAllBytes();
			
			// convert to string
			String time = new String(response);
			
			// print to screen
			System.out.println(time);
			
			// close socket
			clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void doA3_4() {
		
		
	}
}
