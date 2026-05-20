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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.radl.sa.interfaces.FastaControllable;
import com.radl.sa.interfaces.FastaViewable;
import com.radl.sa.services.Sequence;

public class OnlineView extends JFrame {
	
	public final int DEFAULT_INSET = 10;
	public final String DEFAULT_STATUS = "© Julius Radl";

	private DefaultListModel<Sequence> mSeqList;
	private OnlineMainController fc;
	
	// File Chooser wiederverwenden!
	private final JFileChooser fileChooser;

	private JLabel seqListLabel;
	private JList<Sequence> seqListList;
	private JTextArea selectedSeq;
	private JProgressBar jpg;
	private JLabel taskDesc;
	
	// Liste, um Buttons alle auf einmal zu disablen
	private ArrayList<JButton> bl;
	private JButton bConnect, bSave, bBrowser, bBlast;

	public OnlineView(String title, int hoehe) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize((int) (hoehe * 0.7), hoehe);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		setLayout(new GridBagLayout());
		
		// ------ Initialisierung ----------------------------------------------
		mSeqList = new DefaultListModel<>();
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
		
		seqListList = new JList<Sequence>(mSeqList);
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
		
		bConnect = new JButton("Verbinden");
		bConnect.setAlignmentX(CENTER_ALIGNMENT);
		bl.add(bConnect);
		
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
		buttonPanel.add(bConnect);
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
		bConnect.addActionListener(event -> {
			fc.pressedConnectButton();			
		});
		
		bSave.addActionListener(event -> {
            fc.pressedSaveButton();
		});
		
		bBrowser.addActionListener(event -> {
			fc.pressedBrowserButton();
		});
		
		bBlast.addActionListener(event -> {
            fc.pressedBlastButton();
		});
	}
	
	public void displayProgress(boolean enabled, String taskDesc) {
		
		jpg.setVisible(enabled);
		this.taskDesc.setText(taskDesc);
	}

	public void updateSeqList(ArrayList<Sequence> seqList) {
		// Objekte in DefaultListModel schieben
		this.mSeqList.clear();
		for (Sequence seq : seqList) {
			this.mSeqList.addElement(seq);
		}
	}
	
	public void setController(OnlineMainController fc) {
		this.fc = fc;
	}
	
	public void setButtonsEnabled(boolean enabled) {
		for (JButton b : bl) {
			b.setEnabled(enabled);
		}
	}
	
	// Ist Model View Presenter, dh auch der Aufruf des FileChoosers ist Sache
	// des Presenters (Controller), View benachrichtigt nur	
	public File openFileDialogue() {
		
		int status = fileChooser.showOpenDialog(this);
		if (status == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			// TODO könnte man auch per Exception behandeln
			return null;
		}
	}
}