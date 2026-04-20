package com.radl.sa.fastareader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FastaView extends JFrame {

	private DefaultListModel<Sequence> seqList;
	private FastaController fc;
	
	// File Chooser wiederverwenden!
	private final JFileChooser fileChooser;
	private final ExecutorService ex;

	private JLabel seqListLabel;
	private JList<Sequence> seqListList;
	private JTextArea selectedSeq;
	// Liste, um Buttons alle auf einmal zu disablen
	private ArrayList<JButton> bl;
	private JButton parse, save;

	public FastaView(String title, int hoehe) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize((int) (hoehe * 0.7), hoehe);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		setLayout(new BorderLayout());
		
		// ------ Initialisierung ----------------------------------------------
		seqList = new DefaultListModel<>();
		fileChooser = new JFileChooser();
		File cd = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(cd);
		ex = Executors.newFixedThreadPool(2);
		bl = new ArrayList<JButton>();

		JPanel north = new JPanel();
		seqListLabel = new JLabel("Liste der Sequenzobjekte");
		north.add(seqListLabel);
		add(north, BorderLayout.NORTH);

		// ------ Liste & ausgewählte Sequenz ---------------------------------
		JPanel center = new JPanel(new BorderLayout());
		
		seqListList = new JList<Sequence>(seqList);
		seqListList.setCellRenderer(new SequenceRenderer());
		center.add(new JScrollPane(seqListList), BorderLayout.NORTH);
		
		selectedSeq = new JTextArea();
		selectedSeq.setLineWrap(true);
		center.add(new JScrollPane(selectedSeq), BorderLayout.CENTER);
		
		center.setBackground(Color.red);
		add(center, BorderLayout.CENTER);
		
		// ----- Control-Buttons ----------------------------------------------
		JPanel south = new JPanel();
		parse = new JButton("FASTA einlesen");
		parse.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(parse);
		save = new JButton("Sequenz-Liste speichern");
		save.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(save);
		south.setLayout(new BoxLayout(south, BoxLayout.PAGE_AXIS));
		south.add(parse);
		south.add(save);
		south.setBackground(Color.black);
		add(south, BorderLayout.SOUTH);

		seqListList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Sequence seq = seqListList.getSelectedValue();
				if (seq != null) {
					selectedSeq.setText(seqListList.getSelectedValue().toString());
					selectedSeq.setCaretPosition(0);
				}
			}
		});
		
		// Listeners bleiben im View, keine UI-Logik oder Elemente im Controller
		// TODO als Model View Presenter umschreiben, dh auch der Aufruf des 
		// FileChoosers ist Sache des Presenters (Controller), View benachrichtigt
		// nur
		parse.addActionListener(event -> {
			
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            fc.pressedParseButton(file);
			}
			
		});
		
		save.addActionListener(event -> {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            fc.pressedSaveButton(file);
			}
		});

	}
	
	public void displayError(String error) {
		// TODO Fehler anzeigen
	}

	// TODO besser updaten, oder passt es schon so?
	public void updateSeqList(ArrayList<Sequence> seqList) {
		// Objekte in DefaultListModel schieben
		this.seqList.clear();
		for (Sequence seq : seqList) {
			this.seqList.addElement(seq);
		}
	}
	
	public ArrayList<Sequence> getSeqList() {
		ArrayList<Sequence> sl = new ArrayList<>();
		Iterator<Sequence> it = seqList.elements().asIterator();
		while(it.hasNext()) {
			sl.add(it.next());
		}
		return sl;
	}
	
	public void setController(FastaController fc) {
		this.fc = fc;
	}
	
	public void setButtonsEnabled(boolean enabled) {
		for (JButton b : bl) {
			b.setEnabled(enabled);
		}
	}
	
}

// Wir extenden den Default Renderer, um zb blaue Highlights zu haben
// Neuer Renderer, um was anderes als nur toString() der Sequenzen
// anzeigen zu können
class SequenceRenderer extends DefaultListCellRenderer {

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
