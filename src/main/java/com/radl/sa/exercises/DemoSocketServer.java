package com.radl.sa.exercises;

/*
 * DemoSocketServer.java
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Eine Klasse zur Demonstration der Server Technologie bei einer Socket-Verbindung.
 * 
 */
public class DemoSocketServer {

	// der server socket
	private ServerSocket server;

	/**
	 * Konstruktor: erzeugt ein neues Server-Objekt fuer die angegebene Port-Nummer
	 * 
	 * @param portNummer: der Port, ueber den Server und Client miteinander  kommunizieren
	 */
	public DemoSocketServer(int portNummer) throws IOException {
		// Port @portNummer belegen:
		server = new ServerSocket(portNummer);
		
		System.out.println(server.getLocalSocketAddress());
		System.out.println("Server erfolgreich auf Port " + portNummer + " gestartet!");

		// Dauerschleife
		while (true) {
			// Auf Client-Anfragen warten:
			Socket clientAnfrage = server.accept();

			// Client-Anfrage bearbeiten:
//			SocketWorker worker = new SocketWorker(clientAnfrage);
//			worker.start();
		}
	}

	
	
	//---------- Hauptfunktion -------------------------------------

	/**
	 * Die Hauptfunktion.
	 * @param args: Kommandozeilen-Parameter
	 */
	public static void main(String args[]) {
		// Server starten:
		try {
			// Achtung: bitte Port-Nummer aendern
			new DemoSocketServer(2200);
		} catch (IOException e) {
			// Ein I/O-Fehler ist aufgetreten:
			System.out.println("Fehler: " + e);
		}
	}

	

}
