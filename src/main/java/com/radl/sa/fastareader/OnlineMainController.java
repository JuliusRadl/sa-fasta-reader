package com.radl.sa.fastareader;

import com.radl.sa.interfaces.*;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.swing.SwingWorker;

import com.radl.sa.interfaces.FastaControllable;
import com.radl.sa.server.ServerProtocol;
import com.radl.sa.server.ClientProtocol;
import com.radl.sa.server.ConnectionResourcesHandler;
import com.radl.sa.server.FileSender;
import com.radl.sa.services.BlastService;
import com.radl.sa.services.ByteSequenceReader;
import com.radl.sa.services.FastaParseConsumer;
import com.radl.sa.services.PropertiesLoader;

public class OnlineMainController {

	/*
	 * Controller for the client-server version of the fasta programm
	 * 
	 * Does not implement the controller interface of the local version, because the
	 * exercise requires a fundamentally different gui and controller
	 * 
	 * Establish individual socket connection per request to avoid threads
	 * interfering with each others data streams. chat system with with a single
	 * connection as described on moodle doesn't make sense because exercise requires
	 * multiple async server requests, which doesn't work with a single connection
	 * 
	 * No parsing or blasting function via server chat, because it makes no sense to
	 * first give a command sendFile and then have the user select a file via gui.
	 * Why would you go from a chat communication to a gui file selection and then
	 * later to a gui update with the results? Might be possible to have a separate
	 * function
	 * 
	 * Eine funktion a, die eine datei nimmt und einen swingworker startet eine
	 * funktion b, die auf button click einen file chooser aufruft und dann a
	 * aufruft Eine funktion c, die das kommando aufnimmt und ein filehandle erzeugt
	 * und damit a aufruft
	 */

	private FastaModellable fm;
	private OnlineView fv;

	private ClientProtocol cp;

	// TODO .env reference with hostnames and port etc

	private String fastaHost = "localhost";
	private int fastaPort = 12345;

	private Properties props;

	public OnlineMainController(FastaModellable fm, OnlineView fg) {

		setModel(fm);
		setView(fg);

		// TODO maybe unnecessary
		// initialize command handler
		cp = new ClientProtocol();

		// get properties
		// catch io error here so it is clear where it came from
		try {
			props = PropertiesLoader.loadProps();
		} catch (IOException e) {
			String msg = "Failed to read app.properties file";
			System.err.println(msg);
			e.printStackTrace();
		}
	}

	// send command
	public void pressedCommandButton() {
		// check if server connection established

		// open command pane

		// read command

		// send command to server

		// i won't implement the sendFile command, since it requires A checking
		// whether a sendFile command was sent client-side, then opening a file dialogue
		// before communicating with the server, or B first sending the sendFile
		// command, then pressing a button to open a file dialogue and sending
		// the file. Both options seem terrible

		// on the other hand, i always have to check the command client-side,
		// since the client needs to know what type of data the server will respond with
	}

	// TODO define client commands in client protocol
	public void interpretCommand(String command) {

		try {
			switch (command) {
			
			// simple commands are handled right here
			// resources are also created on case by case basis, in case
			// followup functions need to use and thus create resources in their
			// own thread. the alternatives are A try-with-resources before the
			// switch, which would conflict with functions using resources in
			// separate threads, or B having each case close the resources in
			// dividually
			case ServerProtocol.TIME_COMMAND: {
				try (ConnectionResourcesHandler crh = new ConnectionResourcesHandler(props)) {
					crh.getDos().writeUTF(ServerProtocol.TIME_COMMAND);
					System.out.println(crh.getDis().readUTF());
				}
			}
			
			// more complex commands require individual functions
			// TODO parse filepath instead of calling button function
			case ServerProtocol.PARSE_COMMAND : {
				pressedParseButton();
			}
			
			} // end of switch cases
		} catch (IOException e) {
			String msg = "Fehler beim Empfangen der Antwort.";
			System.err.println(msg);
			e.printStackTrace();
		}
	}

	public void pressedParseButton() {

		// get local file handle from view
		File f = fv.openFileDialogue();
		if (f == null)
			return;
		startParseWorker(f);
	}

	public void startParseWorker(File f) {
		
		fv.setButtonsEnabled(false);
		fv.displayProgress(true, "Server is parsing...");
		
		SwingWorker<String, Integer> sw = new SwingWorker<String, Integer>() {
			
			public String doInBackground() {
				
				// create connection resources in thread, since the thread knows
				// how long it needs them
				try (ConnectionResourcesHandler crh = new ConnectionResourcesHandler(props)) {

					// send command
					crh.getDos().writeUTF(ServerProtocol.PARSE_COMMAND);
					
					// send file to server
					FileSender fs = new FileSender(f, crh.getDos());
					fs.send();
					
					// receive sequence objects with consumer
					ByteSequenceReader bsr = new ByteSequenceReader(crh.getDis());
					FastaParseConsumer fpc = new FastaParseConsumer(bsr, fm.getSeqList());
					fpc.run();
					
					return "Server is done parsing.";
				// catch resource exceptions here
				} catch (UnknownHostException e) {
					String msg = "Unknown Host.";
					System.err.println(msg);
					e.printStackTrace();
					return msg;
				} catch (IOException e) {
					String msg = "Error using client-server data streams.";
					System.err.println(msg);
					e.printStackTrace();
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

	public void pressedSaveButton() {

		// get file
		File f = fv.openFileDialogue();

		// null check file
		if (f == null) {
			return;
		}

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
		if (!Desktop.isDesktopSupported()) {
			return;
		}
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
//			ProcessBuilder pb = new ProcessBuilder(
//					"rundll32",
//					"url.dll,FileProtocolHandler",
//					"http://www.ncbi.nlm.nih.gov/Genbank/");
//			Map<String, String> env = pb.environment();
//			env.forEach( (String key, String value) -> System.out.println(key + " = " + value));
//			try {
//				Process p = pb.start();
//				// kann Prozess beenden, aber der Default Browser wird sich 
//				// dadurch nicht schließen. Lösung mit WebView und JavaFx möglich
//				p.destroy();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	}

	// Berechnungen, die vom FastaView gefordert wurden,
	// bleiben auch im FastaController und gehen nicht in
	// den BlastTableController
	public void pressedBlastButton() {

		// get file
		File f = fv.openFileDialogue();

		// null check file
		if (f == null) {
			return;
		}

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

	public void setView(OnlineView fv) {
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
