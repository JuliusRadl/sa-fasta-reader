package com.radl.sa.fastareader;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.radl.sa.services.Sequence;

//Wir extenden den Default Renderer, um zb blaue Highlights zu haben
//Neuer Renderer, um was anderes als nur toString() der Sequenzen
//anzeigen zu können
public class SequenceRenderer extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(
			JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		Sequence seq = (Sequence) value;
		setText(seq.getHeader());

		return this;
	}	
}