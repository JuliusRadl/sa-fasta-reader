package com.radl.sa.fastareader;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FastaParseConsumer implements Runnable {

	private ObjectInputStream ois;
	private ArrayList<Sequence> seqList;

	public FastaParseConsumer(ObjectInputStream ois, ArrayList<Sequence> seqList) {
		this.ois = ois;
		this.seqList = seqList;
	}

	public void run() {

		try {
			while(true) {
				Sequence seq = (Sequence) ois.readObject();
				System.out.println("Lese Sequenz " + seq.getHeader().substring(0, 20) + "...");
				seqList.add(seq);
				Thread.sleep(10);
			}
		} catch (EOFException e) {
			System.out.println("ObjectOutputStream geschlossen, keine weiteren Sequenzen.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// Falls sleep interrupted
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				System.err.println("Fehler beim Schließen des Input-Streams");
				e.printStackTrace();
			}
		}
	}
}
