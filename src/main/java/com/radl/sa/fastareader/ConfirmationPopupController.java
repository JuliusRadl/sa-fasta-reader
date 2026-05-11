package com.radl.sa.fastareader;

public class ConfirmationPopupController {

	private ConfirmationPopupView cpv;
	
	public ConfirmationPopupController(ConfirmationPopupView cpv) {
		
		setView(cpv);
	}
	
	public void pressedConfirmButton() {
		
		cpv.close();
	}
	
	public void setView(ConfirmationPopupView cpv) {
		
		this.cpv = cpv;
	}
}
