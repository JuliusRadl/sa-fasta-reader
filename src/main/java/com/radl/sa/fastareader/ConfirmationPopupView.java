package com.radl.sa.fastareader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ConfirmationPopupView extends JFrame {
	
	private static final int size = 100;
	private static final int button_inset = 10;
	
	private JButton bConfirm;
	private JLabel popupText;
	
	private ConfirmationPopupController cpc;
	
	public ConfirmationPopupView(String notification) {
		super("1 neue Nachricht");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize((int) (size * 2), size);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
		setLayout(new FlowLayout());
		
		popupText = new JLabel(notification);
		add(popupText);
		
		bConfirm = new JButton("Ok");
		bConfirm.setMargin(new Insets(button_inset, button_inset, button_inset, button_inset));
		add(bConfirm);
		
		bConfirm.addActionListener( event -> {
			
			cpc.pressedConfirmButton();
		});
	}
	
	public void close() {
		
		this.dispose();
	}
	
	public void setController (ConfirmationPopupController cpc) {
		
		this.cpc = cpc;
	}
}

