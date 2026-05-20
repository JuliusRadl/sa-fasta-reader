package com.radl.sa.fastareader;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.swing.SwingWorker;

import com.radl.sa.interfaces.FastaControllable;
import com.radl.sa.interfaces.FastaModellable;
import com.radl.sa.interfaces.FastaViewable;
import com.radl.sa.services.BlastService;

public class LocalMainController implements FastaControllable {

	private FastaModellable fm;
	private FastaViewable fv;
	
	public LocalMainController (FastaModellable fm, FastaViewable fg) {

		setModel(fm);
		setView(fg);
	}
	
	public void pressedParseButton() {
		
		// get file
		File f = fv.openFileDialogue();
		
		// null check file
		if (f == null) { return; }
		
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
					
					// Bestätigungspopup
					ConfirmationPopupView cpv = new ConfirmationPopupView(
							"Parsing von " + f.getAbsolutePath() + " abgeschlossen.");
					cpv.setVisible(true);
					
					// Popup-Controller
					ConfirmationPopupController cpp = new ConfirmationPopupController(cpv);
					cpv.setController(cpp);
					
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
	
	public void pressedSaveButton() {
		
		// get file
		File f = fv.openFileDialogue();
		
		// null check file
		if (f == null) { return; }
		
		fv.setButtonsEnabled(false);
		fv.displayProgress(true, "Saving...");
		
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {
			
			public String doInBackground() {
				
				try {
					fm.saveSeqList(f);
					
					// Bestätigungspopup
					ConfirmationPopupView cpv = new ConfirmationPopupView(
							"Speichern unter " + f.getAbsolutePath() + " abgeschlossen.");
					cpv.setVisible(true);
					
					// Popup-Controller
					ConfirmationPopupController cpp = new ConfirmationPopupController(cpv);
					cpv.setController(cpp);
					
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
	
	// Berechnungen, die vom FastaView gefordert wurden,
	// bleiben auch im FastaController und gehen nicht in
	// den BlastTableController
	public void pressedBlastButton() {
		
		// get file
		File f = fv.openFileDialogue();
		
		// null check file
		if (f == null) { return; }
		
		// Swingworker
		fv.setButtonsEnabled(false);
		fv.displayProgress(true, "Blasting...");
		
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {
			
			public String doInBackground() {
				
				try {
					ArrayList<String[]> ls = BlastService.service(f);

					// Blast-Tabelle erzeugen und Daten übergeben
					BlastTableView btv = BlastTableView.getInstance();
					btv.setBlastTable(ls);
					btv.setVisible(true);
					
					// neuen Controller
					BlastTableController btc = new BlastTableController(btv);
					btv.setController(btc);
					
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
	
	public void setView(FastaViewable fv) {
		this.fv = fv;
	}
	
	public void setModel(FastaModellable fm) {
		this.fm = fm;
	}
	
	public void createPopUp(String title) {
		ConfirmationPopupView cpv = new ConfirmationPopupView(title);
		ConfirmationPopupController cpc = new ConfirmationPopupController(cpv);
		cpv.setController(cpc);
		cpv.setVisible(true);
	}
}
