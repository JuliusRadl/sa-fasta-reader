package com.radl.sa.services;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.radl.sa.interfaces.SequenceReadable;

public class FastaParseConsumer implements Runnable {

	private SequenceReadable sr;
	private ArrayList<Sequence> seqList;

	public FastaParseConsumer(SequenceReadable sr, ArrayList<Sequence> seqList) {
		this.sr = sr;
		this.seqList = seqList;
	}

	public void run() {

		try {
			while(true) {
				Sequence seq = sr.read();
				System.out.println("Lese Sequenz " + seq.getHeader().substring(0, 20) + "...");
				seqList.add(seq);
				Thread.sleep(10);
			}
		} catch (EOFException e) {
			System.out.println("Keine weiteren Sequenzen.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// Falls sleep interrupted
			e.printStackTrace();
		}
	}
}
