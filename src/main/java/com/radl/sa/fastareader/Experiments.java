package com.radl.sa.fastareader;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Experiments {

	// Experiment: Datei einlesen
		static void doExperiment1() throws Exception {

			// Erzeugt File-Objekt, egal ob Datei existiert
			File f = new File(
					"C:\\Users\\juliu\\My Drive\\Bildung\\HSWT\\4. Semester\\Software-Architekturen\\AB1 - Streams & Threads\\mgProteome.fasta");
			System.out.println("Dateiname: " + f.getName());
			System.out.println("Datei existiert? " + (f.exists() ? "Ja" : "Nein"));
			System.out.println("Dateipfad: " + f.getAbsolutePath());
			System.out.println("Haben wir Schreibrechte? " + (f.canWrite() ? "Ja" : "Nein"));

			// Streams für Binary, Reader für Zeichen
			// Zeichen auslesen mit FileReader
			FileReader fr = new FileReader(f);
			System.out.println("Zeichen mit FileReader auslesen: " + fr.read());
			System.out.println("Encoding: " + fr.getEncoding());

			// Effizienter in Blöcken auslesen mit BufferedReader
			BufferedReader br = new BufferedReader(fr);
			System.out.println("Hashcode: " + br.hashCode());
			System.out.println("Zeichen mit BufferedReader auslesen: " + fr.read());
			System.out.println("Ganze Zeile: " + br.readLine());

			// Mit Scanner noch einfacher auslesen
			// aber Scanner wird nicht empfohlen heutzutage
			Scanner sc = new Scanner(f);
			sc.useDelimiter(">");
			System.out.println(sc.next());

			// Neue Datei erzeugen
			File d = new File(
					"C:\\Users\\juliu\\My Drive\\Bildung\\HSWT\\4. Semester\\Software-Architekturen\\AB1 - Streams & Threads\\test.txt");
			FileWriter fw = new FileWriter(d);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print(br.readLine());

			// Objekte als json serialisieren und speichern
			Point p = new Point(2, 3);
			Gson gs = new Gson();
			System.out.println(gs.toJson(p));
			FileWriter jsonWriter = new FileWriter("Point.json");
			gs.toJson(p, jsonWriter);

			// Writer werden beim schließen automatisch geflusht und damit
			// alles auf die Festplatte geschrieben
			// jsonWriter.flush();
			// pw.flush();

			// Reader & Writer schließen (der äußerste reicht)
			pw.close();
			jsonWriter.close();
			br.close();
			sc.close();
		}

		// Experiment: Dateien rekursiv auflisten (siehe FileLister.java)
		static void doExperiment2() {
			File d = new File("C:\\Users\\juliu\\Desktop");
			FileLister.listRec(d, 2);
		}

		// Experiment: Systemeigenschaften auslesen (zb path separator)
		static void doExperiment3() {
			System.getProperties().list(System.out);
		}

		// Experiment: nio besser kennenlernen
		static void doExperimentNio() throws Exception {

			// Pfad-Objekt erzeugen
			Path fasta = Paths.get("src/main/resources/mgProteome.fasta");
			System.out.println(fasta.getFileName());

			// Datei komplett auslesen
			// System.out.println(Files.readString(fasta));

			// BufferedReader schnell erzeugen
			BufferedReader br = Files.newBufferedReader(fasta);
			System.out.println(br.readLine());

			br.close();
		}

		static void doExperimentSeqReader() throws Exception {
			// Path definieren
			Path fastaPath = Paths.get("src/main/resources/mgProteome.fasta");
			File fastaFile = fastaPath.toFile();

			// In eigenem Thread einlesen
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Callable<ArrayList<Sequence>> task = new SequenceReadTask(fastaFile);
			Future<ArrayList<Sequence>> future = ex.submit(task);
			ArrayList<Sequence> seqList = future.get();

			// Anzeigen lassen
			for (Sequence seq : seqList) {
				System.out.println(seq.toString() + "\n");
			}

			// Liste abspeichern
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter fw = new FileWriter("seqListBackup.json");
			gson.toJson(seqList, fw);
			// Ganz wichtig: Writer schließen, sonst wird Json nicht richtig terminiert
			fw.close();

			// Liste wieder einlesen

			BufferedReader br = Files.newBufferedReader(Paths.get("seqListBackup.json"));
			ArrayList<Sequence> myList = gson.fromJson(br, new TypeToken<ArrayList<Sequence>>() {
			}.getType());
			System.out.println("Nach dem Einlesen: " + myList.getFirst().toString());
			br.close();
		}

		static void doExperimentSerializable() throws Exception {
			// Path definieren
			Path fastaPath = Paths.get("src/main/resources/mgProteome.fasta");
			File fastaFile = fastaPath.toFile();

			// In eigenem Thread einlesen
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Callable<ArrayList<Sequence>> task = new SequenceReadTask(fastaFile);
			Future<ArrayList<Sequence>> future = ex.submit(task);
			ArrayList<Sequence> seqList = future.get();
			
			FileOutputStream fos = new FileOutputStream("sequence.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(seqList.getFirst());
			oos.close();
		}
		
		static void doExperimentPipe() throws Exception {
			
			Path seqListPath = Paths.get("seqListBackup.json");
			File seqListFile = seqListPath.toFile();
			
			PipedOutputStream pos = new PipedOutputStream();
			PipedInputStream pis = new PipedInputStream();

			pis.connect(pos);
			
			ObjectOutputStream oos = new ObjectOutputStream(pos);
			ObjectInputStream ois = new ObjectInputStream(pis);
			
			RestoreSequenceListTask rs = new RestoreSequenceListTask(seqListFile, oos);
			Thread t = new Thread(rs);
			t.start();
			
			while(true) {
				try {
					Sequence seq = (Sequence) ois.readObject();
					System.out.println("Lese Sequenz " + seq.getHeader().substring(0, 20) + "...");
				} catch (EOFException e) {
					System.out.println("ObjectOutputStream geschlossen, keine weiteren Sequenzen.");
					break;
				} catch (IOException e) {
				}			
			}
			
			ois.close();
			
		}
		
		static void doExperimentGUI() {
			// GUI aufrufen
			FlatLightLaf.setup();
			FastaGUI fg = new FastaGUI("FASTA-Manager", 500);
			fg.setVisible(true);

			// Sequenzen einlesen
			Path fastaPath = Paths.get("src/main/resources/mgProteome.fasta");
			File fastaFile = fastaPath.toFile();
			
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Callable<ArrayList<Sequence>> task = new SequenceReadTask(fastaFile);
			Future<ArrayList<Sequence>> future = ex.submit(task);
			try {
				ArrayList<Sequence> seqList = future.get();
				fg.setSeqList(seqList);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(UIManager.getLookAndFeel().getName());
		}
}
