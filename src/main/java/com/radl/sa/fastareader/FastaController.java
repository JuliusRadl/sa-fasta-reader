package com.radl.sa.fastareader;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.swing.SwingWorker;

public class FastaController implements FastaControllable {

	private FastaModel fm;
	private FastaView fv;
	
	public FastaController (FastaModel fm, FastaView fg) {

		setModel(fm);
		setView(fg);
	}
	
	public void pressedParseButton(File f) {
		
		// Buttons im EDT disablen
		fv.setButtonsEnabled(false);
		// Progress-Anzeige an
		fv.displayProgress(true, "Parsing...");
		// Parameter 1: Rückgabe-Wert
		// Parameter 2: Progress-Werte
		// Muss man beides nicht nutzen
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {

			public String doInBackground() {
				
				try {
					fm.parseFasta(f);
					return "Fertig";
				} catch (IOException e) {
					String msg = "Fehler beim Einlesen";
					System.err.println(msg);
					e.printStackTrace();
					// Vielleicht noch nützlich zum Anzeigen des Fehlers?
					return msg;
				}
			}
			
			// Wird immer aufgerufen, auch wenn gecancelt (aber nicht wenn gekillt)
			public void done() {
				
				fv.updateSeqList(fm.getSeqList());
				fv.setButtonsEnabled(true);
				try {
					fv.displayProgress(false, get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		sw.execute();
	}
	
	public void pressedSaveButton(File f) {
		
		fv.setButtonsEnabled(false);
		fv.displayProgress(true, "Saving...");
		
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {
			
			public String doInBackground() {
				
				try {
					fm.saveSeqList(f);
					return "Erfolgreich abgespeichert";
				} catch (IOException e) {
					String msg = "Fehler beim Speichern";
					System.err.println(msg);
					e.printStackTrace();
					// Vielleicht noch nützlich zum Anzeigen des Fehlers?
					return msg;
				}
			}
			
			public void done() {
				
				fv.updateSeqList(fm.getSeqList());
				fv.setButtonsEnabled(true);
				try {
					fv.displayProgress(false, get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		sw.execute();
	}
	
	public void pressedBrowserButton() {
		//fv.openBrowser();

		// Flexibel
		if (!Desktop.isDesktopSupported()) { return; }
		try {
			Desktop.getDesktop().browse(new URI("http://www.ncbi.nlm.nih.gov/Genbank/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// wd des Prozesses anpassen
		// pb.directory(new File("~/");
		// Angehängte Environmental Variables anzeigen
		
		
		// Windows only Lösung mit Process Builder
//		ProcessBuilder pb = new ProcessBuilder(
//				"rundll32",
//				"url.dll,FileProtocolHandler",
//				"http://www.ncbi.nlm.nih.gov/Genbank/");
//		Map<String, String> env = pb.environment();
//		env.forEach( (String key, String value) -> System.out.println(key + " = " + value));
//		try {
//			Process p = pb.start();
//			// kann Prozess beenden, aber der Default Browser wird sich 
//			// dadurch nicht schließen. Lösung mit WebView und JavaFx möglich
//			p.destroy();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void pressedBlastButton(File f) {
		
		// Swingworker
		fv.setButtonsEnabled(false);
		fv.displayProgress(true, "Blasting...");
		
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {
			
			private byte[] output;
			
			public String doInBackground() {
				
				try {
					// tmp-Folder erzeugen
					Path tmpFolder = Files.createTempDirectory("tmp");
					System.out.println(tmpFolder.toString());
					Path tmpFile = tmpFolder.resolve(f.getName());
					System.out.println(tmpFile.toString());
					// File dorthin kopieren
					Files.copy(f.toPath(), tmpFile);
					
					// Datenbank erzeugen
					ProcessBuilder pc = new ProcessBuilder(
							"makeblastdb",
							"-in", tmpFile.toString(),
							"-dbtype", "prot");
					// wd anpassen
					pc.directory(tmpFolder.toFile());
					Process p = pc.start();
					output = p.getInputStream().readAllBytes();
					System.out.println(new String(output));
					System.out.println(p.waitFor());
					
					
					// Blasten
					pc = new ProcessBuilder(
							"blastp",
							"-query", tmpFile.toString(),
							"-db", tmpFile.toString(),
							"-outfmt", "6",
							"-evalue", "3e-18");
					pc.directory(tmpFolder.toFile());
					p = pc.start();
					output = p.getInputStream().readAllBytes();
					System.out.println(new String(output));
					p.waitFor();
					
					return "Fertig";
				} catch (IOException e) {
					String msg = "Fehler beim Erzeugen des Temp-Ordners";
					System.err.println(msg);
					e.printStackTrace();
					return msg;
				} catch (InterruptedException e) {
					String msg = "Prozess wurde unterbrochen";
					System.err.println(msg);
					e.printStackTrace();
					return msg;
				}
			}
			
			public void done() {
				
				fv.setButtonsEnabled(true);
				try {
					fv.displayProgress(false, get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		
		sw.execute();				
	}
	
	public void setView(FastaView fv) {
		this.fv = fv;
	}
	
	public void setModel(FastaModel fm) {
		this.fm = fm;
	}
}
