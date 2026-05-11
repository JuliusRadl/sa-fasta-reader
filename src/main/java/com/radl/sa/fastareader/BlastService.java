package com.radl.sa.fastareader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class BlastService {

	public static ArrayList<String[]> service(File f) throws IOException, InterruptedException {

		// tmp-Folder erzeugen
		Path tmpFolder = Files.createTempDirectory("tmp");
		System.out.println(tmpFolder.toString());
		Path tmpFile = tmpFolder.resolve(f.getName());
		System.out.println(tmpFile.toString());
		// File dorthin kopieren
		Files.copy(f.toPath(), tmpFile);

		// Datenbank erzeugen
		ProcessBuilder pc = new ProcessBuilder("makeblastdb", "-in", tmpFile.toString(), "-dbtype", "prot");
		// wd anpassen
		pc.directory(tmpFolder.toFile());
		Process p = pc.start();
		byte[] output = p.getInputStream().readAllBytes();
		System.out.println(new String(output));
		System.out.println(p.waitFor());

		// Blasten
		pc = new ProcessBuilder("blastp", "-query", tmpFile.toString(), "-db", tmpFile.toString(), "-outfmt", "6",
				"-evalue", "3e-18");
		pc.directory(tmpFolder.toFile());
		p = pc.start();

		// Kontrolloutput
//			output = p.getInputStream().readAllBytes();
//			for (byte b : Arrays.copyOfRange(output, 0, 500)) {
//				System.out.print( (char) b);
//			}

		// BufferedReader aufsetzen
		InputStream is = p.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		ArrayList<String[]> ls = new ArrayList<>();
		String line;

		while (true) {
			line = br.readLine();

			// wenn nicht null und nicht leer
			if (line != null && !line.isBlank()) {
				String[] vals = line.split("[ \t]+");
				ls.add(vals);
			}

			if (line == null) {
				break;
			}
		}

		p.waitFor();
		br.close();

		return ls;
	}
}
