package com.radl.sa.fastareader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FastaView extends JFrame {
	
	public final int BROWSER_SIZE = 500;
	public final int DEFAULT_INSET = 10;
	public final String DEFAULT_STATUS = "© Julius Radl";

	private DefaultListModel<Sequence> seqList;
	private FastaController fc;
	
	// File Chooser wiederverwenden!
	private final JFileChooser fileChooser;

	private JLabel seqListLabel;
	private JList<Sequence> seqListList;
	private JTextArea selectedSeq;
	private JProgressBar jpg;
	private JLabel taskDesc;
	
	// Liste, um Buttons alle auf einmal zu disablen
	private ArrayList<JButton> bl;
	private JButton bParse, bSave, bBrowser, bBlast;
	
	private BrowserWindow bw;

	public FastaView(String title, int hoehe) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize((int) (hoehe * 0.7), hoehe);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		setLayout(new GridBagLayout());
		
		// ------ Initialisierung ----------------------------------------------
		seqList = new DefaultListModel<>();
		fileChooser = new JFileChooser();
		File cd = new File(System.getProperty("user.dir"));
		fileChooser.setCurrentDirectory(cd);
		bl = new ArrayList<JButton>();
		
		// ------ Listenlabel --------------------------------------------------
		JPanel labelPanel= new JPanel();
		seqListLabel = new JLabel("Liste der Sequenzobjekte");
		labelPanel.add(seqListLabel);
		
		// Positionieren
		GridBagConstraints c = new GridBagConstraints();
		// Komponenten mit groesserem Weight als andere werden gestreckt,
		// andersrum gestaucht
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		add(labelPanel, c);

		// ------ Liste & ausgewählte Sequenz ---------------------------------
		JPanel listPanel= new JPanel(new GridLayout(0, 1));
		
		seqListList = new JList<Sequence>(seqList);
		seqListList.setCellRenderer(new SequenceRenderer());
		listPanel.add(new JScrollPane(seqListList));
		
		selectedSeq = new JTextArea();
		selectedSeq.setLineWrap(true);
		listPanel.add(new JScrollPane(selectedSeq));
		
		//Positionieren
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(listPanel, c);
		
		// ----- Control-Buttons -----------------------------------------------
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0, 2));
		
		bParse = new JButton("FASTA einlesen");
		bParse.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(bParse);
		
		bSave = new JButton("Sequenz-Liste speichern");
		bSave.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(bSave);
		
		bBrowser = new JButton("Browser öffnen");
		bBrowser.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(bBrowser);
		
		bBlast = new JButton("Blasten");
		bBlast.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(bBlast);
		
		// Zum Panel hinzufügen
		buttonPanel.add(bParse);
		buttonPanel.add(bSave);
		buttonPanel.add(bBrowser);
		buttonPanel.add(bBlast);
		
		// Positionieren
		c = new GridBagConstraints();
		c.insets = new Insets(DEFAULT_INSET, DEFAULT_INSET, DEFAULT_INSET, DEFAULT_INSET);
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 2;
		add(buttonPanel, c);
		
		// ------ Status-Anzeige -----------------------------------------------
		JPanel statusPanel = new JPanel(new BorderLayout());
		
		// Separator
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		statusPanel.add(sep, BorderLayout.NORTH);
		
		// Progress & Status-Panel
		JPanel progBarPanel = new JPanel();
		jpg = new JProgressBar();
		progBarPanel.add(jpg);
		jpg.setIndeterminate(true);
		jpg.setVisible(false);
		
		taskDesc = new JLabel(DEFAULT_STATUS);
		progBarPanel.add(taskDesc);		
		
		statusPanel.add(progBarPanel, BorderLayout.WEST);
		
		// Positionieren
		c = new GridBagConstraints();
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(DEFAULT_INSET, DEFAULT_INSET, DEFAULT_INSET, DEFAULT_INSET);
		add(statusPanel, c);

		// ------ Listenlistener -----------------------------------------------
		// TODO gehört der in den Controller?
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
		bParse.addActionListener(event -> {
			
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            fc.pressedParseButton(file);
			}
			
		});
		
		bSave.addActionListener(event -> {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            fc.pressedSaveButton(file);
			}
		});
		
		bBrowser.addActionListener(event -> {
			fc.pressedBrowserButton();
		});
		
		bBlast.addActionListener(event -> {
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            fc.pressedBlastButton(file);
			}
		});

	}
	
	public void displayError(String error) {
		// TODO Fehler anzeigen
	}
	
	public void displayProgress(boolean enabled, String taskDesc) {
		
		jpg.setVisible(enabled);
		this.taskDesc.setText(taskDesc);
	}

	public void updateSeqList(ArrayList<Sequence> seqList) {
		// Objekte in DefaultListModel schieben
		this.seqList.clear();
		for (Sequence seq : seqList) {
			this.seqList.addElement(seq);
		}
	}
	
	public void setController(FastaController fc) {
		this.fc = fc;
	}
	
	public void setButtonsEnabled(boolean enabled) {
		for (JButton b : bl) {
			b.setEnabled(enabled);
		}
	}
	
	public void openBrowser() {
		
		bw = BrowserWindow.getInstance("Browser", BROWSER_SIZE);
		bw.setVisible(true);
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

class BrowserWindow extends JFrame {
	
	private static BrowserWindow bw;
	
	public BrowserWindow (String title, int hoehe) {
		super(title);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize((int) (hoehe * 0.7), hoehe);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		setLayout(new BorderLayout());
	}
	
	// Singleton-Pattern: Nicht mehr als 1 Browser offen
	public static BrowserWindow getInstance(String title, int hoehe) {
		
		if (bw == null) {
			bw = new BrowserWindow(title, hoehe);
			return bw;
		}
		return bw;		
	}
}
