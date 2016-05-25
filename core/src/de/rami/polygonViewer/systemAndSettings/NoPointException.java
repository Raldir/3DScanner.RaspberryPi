package de.rami.polygonViewer.systemAndSettings;

public class NoPointException extends NullPointerException{

	public NoPointException(int oS, int uS){
		System.out.println("Schwellenwerte: " + oS + "  " + uS);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
