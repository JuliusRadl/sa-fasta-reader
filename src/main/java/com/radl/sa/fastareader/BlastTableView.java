package com.radl.sa.fastareader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

// 1:1 Beziehung zwischen Views und Controllern!
public class BlastTableView extends JFrame {
	
	private static final int size = 700;
	private static final String title = "Blast-Tabelle";
	
	// Blast-Tabellenheader
	private Object[] blastHeaders = new Object[] {
	        "Query",
	        "Subject",
	        "% Identity",
	        "Alignment Length",
	        "Mismatches",
	        "Gap Opens",
	        "Q Start",
	        "Q End",
	        "S Start",
	        "S End",
	        "E-Value",
	        "Bit Score"
	};
	
	private static BlastTableView btv;
	private DefaultTableModel mBlastTable;
	
	private BlastTableController btc;
	
	public BlastTableView () {
		super(title);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize((int) (size * 1.5), size);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		setLayout(new BorderLayout());
		
		// ----- Blast-Tabelle -------------------------------------------------
		mBlastTable = new DefaultTableModel(blastHeaders, 0);
		JTable blastTable = new JTable(mBlastTable);
		JScrollPane tableScrollPane = new JScrollPane(blastTable);
		blastTable.setFillsViewportHeight(true);
		add(tableScrollPane, BorderLayout.CENTER);
	}
	
	// Singleton-Pattern: Nicht mehr als 1 Tabelle offen
	public static BlastTableView getInstance() {
		
		if (btv == null) {
			btv = new BlastTableView();
			return btv;
		}
		return btv;		
	}
	
	public void addBlastRow(Object[] values) {
		
		mBlastTable.addRow(values);
	}
	
	public void setBlastTable(ArrayList<String[]> ls) {

		clearBlastTable();
		for (String[] row : ls) {
			addBlastRow(row);
		}
	}
	
	public DefaultTableModel getBlastTable() {
		
		return mBlastTable;
	}
	
	public void clearBlastTable() {
		
		mBlastTable.setRowCount(0);
	}
	
	public void setController(BlastTableController btc) {
		
		this.btc = btc;
	}
}