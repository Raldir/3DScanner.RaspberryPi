package de.rami.polygonViewer.systemAndSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Alle wichtigen von außen veränderbare Variablen für das Projekt
 * @author Rami und Anton
 *
 */
public class Settings {
	
	//Zur Optimierung der Struktur wird fuer den Mittelwert eine Eigene Klasse festgelegt.
//	public static final float middle = 20.7077f;
//	public static float middle = 19.914f;
	
	public static int obererSchwellenWert = 253;
	
	public static String raspberryIP = "192.168.1.3";
	
	/**
	 * Anpassung der Verhaeltnisse von den X und Y Werten der Punkte
	 */
	public static float maxAbstandPunkte = 0.025f;
	public static float nearestNeighborDistance = 2.5f;
	
	public static int polygonAnzahl = 100;
	
	public static int anzahlbilder = 256;
	//256
	
	public static int belichtungsDauer = 0;
	
	public static float grunddicke = 1.0f;
	
	/**
	 * Skaliert das Objekt in Bezug auf die "Plygonanzahl": Umso grï¿½ï¿½er der Wert, umso weniger
	 * Polygone hat das Objekt (bildkskalierung 15 -> Objekt hat nur 1 / 15 Punkte, die es bei der standard-
	 * mï¿½ï¿½igen durchfuehrung[in x Richtung n Punkte, welche identisch zur Anzahl der Bilder ist, y die hoehe des ubergebenen Bildes] haette.
	 */
	public static float bildskalierung = 1.0f;

	
	/**
	 * Berechnet den Mittelpunkt des Objektes: Punkt, welcher die Tiefe null hat(dafuer Laser und Kamera Winkel = 0ï¿½), -> Gebrauchsanleitung
	 * @param f
	 * @return
	 * @throws IOException
	 */
//	public static ArrayList<Vec2> createMiddleHor(File f) throws IOException{
//		Line hoehe = new Line().getHoehe(f);
//		return new Bildpunkte(f, hoehe).skalierung2(hoehe);
//	}	
//	public static ArrayList<Vec2> createMiddleTemp(File f) throws IOException{
//		Line hoehe = new Line().getWidth(f);
//		return new BildpunkteTest(f, hoehe).skalierung2(hoehe);
//	}
//	
//	public static void main(String[] args) throws IOException{
//	 Vec2 middle = createMiddleTemp(new File("C:\\Users\\Ramor\\Desktop\\3DScanner.RaspberryPi\\core\\1.jpg")).get(0);
//	 System.out.println(middle.x + "   " + middle.y);
//}
	
}
