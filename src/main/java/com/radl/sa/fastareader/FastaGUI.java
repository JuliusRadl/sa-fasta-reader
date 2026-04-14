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

public class FastaGUI extends JFrame {

	private DefaultListModel<Sequence> seqList;
	
	// File Chooser wiederverwenden!
	private final JFileChooser fileChooser;
	private final ExecutorService ex;

	private JLabel seqListLabel;
	private JList<Sequence> seqListList;
	private JTextArea selectedSeq;
	private JButton parse, save, read;

	public FastaGUI(String title, int hoehe) {
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
		save = new JButton("Sequenz-Liste speichern");
		save.setAlignmentX(CENTER_ALIGNMENT);
		read = new JButton("Sequenz-Liste von Backup einlesen");
		read.setAlignmentX(CENTER_ALIGNMENT);
		south.setLayout(new BoxLayout(south, BoxLayout.PAGE_AXIS));
		south.add(parse);
		south.add(save);
		south.add(read);
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
		
		parse.addActionListener(event -> {
			
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
				Callable<ArrayList<Sequence>>srt = new SequenceReadTask(file);
				Future<ArrayList<Sequence>> future = ex.submit(srt);
				try {
					ArrayList<Sequence> seqList = future.get();
					setSeqList(seqList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		save.addActionListener(event -> {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
				ex.submit(new SaveListTask(getSeqList(), file));
				// nach shutdown() werden laufende Threads noch beendet,
				// aber keine neuen mehr angenommen
			}
		});
		
		read.addActionListener(event -> {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            
	            PipedOutputStream pos = new PipedOutputStream();
	    		PipedInputStream pis = new PipedInputStream();

	    		try {
					pis.connect(pos);
					ObjectOutputStream oos = new ObjectOutputStream(pos);
		    		ObjectInputStream ois = new ObjectInputStream(pis);
		    		
		    		RestoreSequenceListTask rs = new RestoreSequenceListTask(file, oos);
					ex.submit(rs);
					
					while(true) {
						try {
							Sequence seq = (Sequence) ois.readObject();
							System.out.println("Lese Sequenz " + seq.getHeader().substring(0, 20) + "...");
							seqList.addElement(seq);
						} catch (EOFException e) {
							System.out.println("ObjectOutputStream geschlossen, keine weiteren Sequenzen.");
							break;
						} catch (IOException e) {
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}			
					}
					ois.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	    		
			}
			
		});

	}

	public void setSeqList(ArrayList<Sequence> seqList) {
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
