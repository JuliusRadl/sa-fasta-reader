package com.radl.sa.server;

public class FastaServerProtocol {
	
	public final static String GREET_MESSAGE = "Hallo vom Server!";
	
	public final static String EXIT_SIGNAL = "exit";
	public final static String EXIT_MESSAGE = "Tschüss vom Server!";
	
	// static method, no need to instantiate object
	public static String processInput(String input) {
		
		if (input.equals(EXIT_SIGNAL)) {
			return EXIT_MESSAGE;
		}
		return GREET_MESSAGE;
	}
}
