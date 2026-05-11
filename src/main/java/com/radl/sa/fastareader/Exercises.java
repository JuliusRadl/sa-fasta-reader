package com.radl.sa.fastareader;

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
}
