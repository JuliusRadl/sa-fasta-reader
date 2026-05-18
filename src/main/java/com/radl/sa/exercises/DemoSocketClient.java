package com.radl.sa.exercises;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.Socket;

/**
 * Eine Klasse zur Demonstration der Client Technologie bei einer Socket-Verbindung.
 * 
 */
public class DemoSocketClient {

	// ------------- Hauptfunktion -----------------------------
	
	/**
	 * @param args: Kommandozeilen-Parameter: der erste Parameter ist die Adresse des
	 *              Server-Rechner
	 */
	public static void main(String args[]) {

		String serverHost = args[0];
		int port = Integer.parseInt(args[1]);
		DemoSocketClient client = new DemoSocketClient();
		client.contactServer(args[0], port);
	}
	
	//----------------- Anfrage -----------------------------

	/**
	 * Methode, die die Kommunikation mit dem Server realisiert.
	 * 
	 * @param serverHost der DNS Name des Rechner smit dem Server-Programm
	 * @param port der Port auf dem das Server-Programm lauscht.
	 */
	public void contactServer(String serverHost, int port) {
		try {
			// Den Server kontaktieren...
			Socket clientSocket = new Socket(serverHost, port);
			
			System.out.println("Server kontaktiert...");

			// Streams oeffnen
			InputStream is = clientSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			OutputStream os = clientSocket.getOutputStream();
			BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(os));

			// Kommunikationsszenario abarbeiten:

			// 1.) Schicke Begruessung an den Server...
			bw.write("Hallo, Client hier! :-)");

			// 2.) Empfange Server-Antwort...
			String response = br.readLine();
			System.out.println(response);

			// 3.) Frage Server nach der Uhrzeit...
			bw.write("Uhrzeit, bitte! :-)");

			// 4.) Empfange Uhrzeit...
			response = br.readLine();
			System.out.println(response);

			// 5.) Liste Arbeitsverzeichnis auf Server -> erfordert Aenderung im Server
			System.out.println("Arbeitsverzeichnis auflisten, bitte! :-)");
			response = br.read;
			System.out.println(response);
			
			// 6.) Verwende den Statistik-Kalkulator (siehe tools) remote, d.h. Vektor erzuegen, an den Server 
			//     schicken, dort STD berechnen, Resultat zur ck
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

