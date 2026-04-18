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
		
		while(true) {
			try {
				Sequence seq = (Sequence) ois.readObject();
				System.out.println("Lese Sequenz " + seq.getHeader().substring(0, 20) + "...");
				seqList.add(seq);
				Thread.sleep(100);
			} catch (EOFException e) {
				System.out.println("ObjectOutputStream geschlossen, keine weiteren Sequenzen.");
				break;
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

}
