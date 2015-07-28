package de.rami.polygonViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import de.rami.polygonViewer.BildData.Line;

public class Settings {
	
	//Zur Optimierung der Struktur wird für den Mittelwert eine Eigene Klasse festgelegt.
	public static final float middle = 10.36f;

	
	/**
	 * Berechnet den Mittelpunkt des Objektes: Punkt, welcher die Tiefe null hat(dafuer Laser und Kamera Winkel = 0°), -> Gebrauchsanleitung
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Vec2> createMiddle(File f) throws IOException{
		Line width = BildData.getWidth(f);
		Line hoehe = BildData.getHoehe(f);
		return new Bildpunkte(f, hoehe).skalierung(width.y1 - width.y2, hoehe);
	}
	
//	public static void main(String[] args) throws IOException{
//	 Vec2 middle = Bildpunkte.createMiddle(new File("C:\\Users\\Ramor\\Desktop\\3DScanner-RaspberryPI-master\\core\\1.jpg")).get(0);
//	 System.out.println(middle.x + "   " + middle.y);
//}
	
}
