package de.rami.polygonViewer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

//Die Klasse ist verantwortlich fuer die Auswertung der Punkte eines Bildes

public class Bildpunkte{
	
	static ArrayList<Integer> pointsX = new ArrayList<Integer>();
	static ArrayList<Integer> pointsY = new ArrayList<Integer>();
	static BufferedImage image;
	static LinkedList<Vec2> pointsbefore = new LinkedList<Vec2>();
	
	/**
	 * Bestimmt die benötigte Helligkeit[genauer RGB Wert], damit ein Pixel vom Algorithmus wahrgenommen wird.
	 */
	public static final int obererSchwellenWert = 250;
	public static final int untererschwellenWert = 150;
	
	/**
	 * Anpassung der Verhaeltnisse von den X und Y Werten der Punkte
	 */
	final static int skalierungswertX = 30;
	final static int skalierungswertY = 90;
	
	/**
	 * Skaliert das Objekt in Bezug auf die "Plygonanzahl": Umso größer der Wert, umso weniger
	 * Polygone hat das Objekt (bildkskalierung 15 -> Objekt hat nur 1 / 15 Punkte, die es bei der standard-
	 * mäßigen durchfuehrung[in x Richtung n Punkte, welche identisch zur Anzahl der Bilder ist, y die hoehe des ubergebenen Bildes] haette.
	 */
	public final static float bildskalierung = 15f;
	final static float bereichsSkalierung = 12;
	
	/**
	 * Der zu betrachtende Bereich des Bildes wird festgelegt, und mit der Information die Methode "Skalierung" aufgerufen
	 * @param f
	 * @return
	 */
	public static ArrayList<Vec2> getGesamtPoints(File f){
		ArrayList<Vec2> punkte= new ArrayList<Vec2>();
		Line hoehe = getHoehe(f);
		punkte = skalierung(bildskalierung, hoehe);
		return punkte;
	}
	
	/**
	 * Identische Methode, jedoch wird der zu betrachtende Bereich als Parameter uebergeben.
	 * @param f
	 * @param l
	 * @return
	 */
	public static ArrayList<Vec2> getGesamtPoints(File f, Line l){
		ArrayList<Vec2> punkte= new ArrayList<Vec2>();
		try {
			image = ImageIO.read(f);
			Line hoehe = l;
			System.out.println("----" + hoehe.y1 + "   " + hoehe.y2);
		punkte = skalierung(bildskalierung, hoehe);
		}catch (IOException e){
			e.printStackTrace();
		}
		return punkte;
	}
	
	static class Line{
		public int y1, y2;
	}
	
	/** 
	 * Die Methode sucht nach der Hoehe(y Achse des Bildes) des Objektes, in der Punkte gesucht werden.
	 * @param image
	 * @return
	 */
	public static Line getHoehe(File f){
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Es wird nach dem ersten Punkt von oben und von unten des Bildes gesucht, welcher den benötigten RGB wert hat.
		Line l = new Line();
		out:
		for(int i = 0; i< image.getHeight(); i++){
			for(int j = 0 ; j < image.getWidth(); j++){
				if(new Color(image.getRGB(j, i)).getBlue() > obererSchwellenWert  && new Color(image.getRGB(j, i)).getRed() > obererSchwellenWert && new Color(image.getRGB(j, i)).getGreen() > obererSchwellenWert){
					l.y1 = image.getHeight() - i;
					break out;
				}
			}
		}
		out:
		for(int i = image.getHeight() -1; i > 0; i--){
			for(int j = 0 ; j < image.getWidth(); j++){
				if(new Color(image.getRGB(j, i)).getBlue() > obererSchwellenWert  && new Color(image.getRGB(j, i)).getRed() > obererSchwellenWert && new Color(image.getRGB(j, i)).getGreen() > obererSchwellenWert){
					l.y2 = image.getHeight() - i;
					break out;
				}
			}
		}
		return l;
	}
	
	/**
	 * identische Methode fuer die Breite.
	 * @param f
	 * @return
	 */
	public static Line getWidth(File f){
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Line l = new Line();
		out:
		for(int i = 0; i< image.getWidth(); i++){
			for(int j = 0 ; j < image.getHeight(); j++){
				if(new Color(image.getRGB(i, j)).getBlue() > obererSchwellenWert  && new Color(image.getRGB(i, j)).getRed() > obererSchwellenWert && new Color(image.getRGB(i, j)).getGreen() > obererSchwellenWert){
					l.y1 = image.getWidth() - i;
					break out;
				}
			}
		}
		out:
		for(int i = image.getWidth() -1; i > 0; i--){
			for(int j = 0 ; j < image.getHeight(); j++){
				if(new Color(image.getRGB(i, j)).getBlue() > obererSchwellenWert  && new Color(image.getRGB(i, j)).getRed() > obererSchwellenWert && new Color(image.getRGB(i, j)).getGreen() > obererSchwellenWert){
					l.y2 = image.getWidth() - i;
					break out;
				}
			}
		}
		return l;
	}
	
	/**
	 * Es wird eine Liste von Vektoren zurückgegeben, die auf einer Ebene den erforderten RGB-Wert haben.
	 * @param line
	 * @param image
	 * @return
	 */
	public static LinkedList<Vec2> getPointsinLine(int line, BufferedImage image){
		int oSchwellenWert = getLinieSchwellenWert(line, image, obererSchwellenWert, 1);
		int uschwellenWert = untererschwellenWert/obererSchwellenWert * oSchwellenWert;
		System.out.println(oSchwellenWert);
		if(oSchwellenWert == 1){
			return pointsbefore;
		}
		// Es wird nach dem ersten Punkt gesucht, welcher den erforderten RGB-Wert erfüllt.
		int ausgangspunkt = 0;
		LinkedList<Vec2> punkteLine = new LinkedList<Vec2>();
		for(int j = 0 ; j < image.getWidth(); j++){
			if(new Color(image.getRGB(j, line)).getRed() > oSchwellenWert && new Color(image.getRGB(j, line)).getBlue() > oSchwellenWert
					&& new Color(image.getRGB(j, line)).getGreen() > oSchwellenWert){
				System.out.println("punkt gespeichert");
				ausgangspunkt = j;
				punkteLine.add(new Vec2((image.getWidth() -1) - j, line));
				break;
			}
		}
		//Daraufhin wird von dem oben gefundenen Punkt aus nach weiteren Punkten gesucht, die
		//nun mindestens einen etwas kleineren RGB-Wert haben und die dadurch entstandene Liste wird zurückgegeben
		int i = 0;
		while(ausgangspunkt + i < image.getWidth() && new Color(image.getRGB(ausgangspunkt + i, line)).getBlue() > uschwellenWert && new Color(image.getRGB(ausgangspunkt + i, line)).getRed() > uschwellenWert && new Color(image.getRGB(ausgangspunkt + i, line)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2((image.getWidth() -1) - (ausgangspunkt + i), line));
			i++;
		}
		
		i = 0;
		while(ausgangspunkt -i > 0 && new Color(image.getRGB(ausgangspunkt - i, line)).getBlue() > uschwellenWert && new Color(image.getRGB(ausgangspunkt - i, line)).getRed() > uschwellenWert && new Color(image.getRGB(ausgangspunkt - i, line)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2((image.getWidth() -1) - (ausgangspunkt - i), line));
			i++;
		}
		pointsbefore = punkteLine;
		return punkteLine;
	}
	
	/**
	 * Der Mittelpunkt auf der Ebene wird mit den uebergebenen Punkte, welche sich auf einer Ebene befinden
	 * ermittelt
	 * @param points
	 * @param image
	 * @return
	 */
	public static Vec2 getMiddlePoint(LinkedList<Vec2> points, BufferedImage image){
		if(points.size() == 0){
			return null;
		}
		float gesamthelligkeit = 0;
		for(int i = 0; i < points.size(); i++){
			gesamthelligkeit += image.getRGB((int)(points.get(i).x),(int)(points.get(i).y));
		}
		//Es wird der Durchschnitt der X-Koordinaten ermittelt. Jedoch fließen die einzelnen Werte unterschiedlich stark ein.
		//Die Koordinate wird mit dem Verhältnis der Helligkeit des Punktes zur Durchschnittshelligkeit multipliziert.
		//So fließt ein hellererPunkt staerker ein als ein vergleichsweise dunkler Punkt.
		float durchschnittshelligkeit = gesamthelligkeit / points.size();
		LinkedList<Float> values = new LinkedList<Float>();
		for(int i = 0; i< points.size(); i++){
			values.add(points.get(i).x * (image.getRGB((int)(points.get(i).x),(int)(points.get(i).y)) / durchschnittshelligkeit));
		}
		float gesamtvalue = 0;
		for(int i = 0; i < values.size(); i++){
			gesamtvalue += values.get(i);
		}
		return new Vec2((image.getWidth() - ((gesamtvalue / values.size()))) / skalierungswertX, ((image.getHeight()- points.get(0).y)) / skalierungswertY);
	}
	
	public static void main(String[] args) throws IOException{
		 Vec2 middle = Bildpunkte.createMiddle(new File("/Users/schueler/Desktop/ServerSystem-3DScannerRPi-master/core/src/de/rami/polygonViewer/1.jpg")).get(0);
		 System.out.println(middle.x + "   " + middle.y);
	}
	
	public static class Point{
		float v1, v2;
	}
	
	/**
	 * Diese Methode verwendet die binaere Suche, um die RGB-Wert-Grenze fuer eine Line im Bild zu finden. Finden
	 * moechte man einen RGB-Wert, welcher fuer die Linie, die betrachtet wird, den hoechst moeglichen Schwellenwert findet, bei dem ein Punkt die
	 * Bedingung des Schwellenwerts erfuellt.
	 * @param line
	 * @param image
	 * @param obereGrenze
	 * @param untereGrenze
	 * @return
	 */
	public static int getLinieSchwellenWert(int line, BufferedImage image, int obereGrenze, int untereGrenze){
		if(obereGrenze == (untereGrenze + 1)){
			return untereGrenze;
		}
		int tempGrenze = untereGrenze + (obereGrenze - untereGrenze) / 2;
		for(int i = 0; i < image.getWidth(); i++){
			if(new Color(image.getRGB(i, line)).getGreen() > obereGrenze &&
					new Color(image.getRGB(i, line)).getRed() > obereGrenze &&
					new Color(image.getRGB(i, line)).getBlue() > obereGrenze){
				return getLinieSchwellenWert(line, image, obereGrenze, tempGrenze);
			}
		}
		return getLinieSchwellenWert(line, image,  tempGrenze, untereGrenze);
		}
	
	/**
	 * Diese Methode sorgt dafuer, dass ein Punkt fuer n Punkte erzeugt wird. Demzufolge stellt, ein Punkt
	 * der von Skalierung zurueckgegeben wird den Durchschnittswert von n Linien dar.
	 * JEDOCH wird nicht jeder Punkt in den n Punkten verwendet um den Durschnittspunkt zu erstellen, sondern jeder p-te Punkt wird verwendet um einen
	 * Punkt zu erzeugen, der n-Punkte ersetzt.
	 * @param skalierungswert
	 * @param hoehe
	 * @return
	 */
	public static ArrayList<Vec2> skalierung(float skalierungswert, Line hoehe){
		double nbereich = bereichsSkalierung / 2;
		int nbereich2 = 0;
		if(nbereich % 2 != 0){
			nbereich2 = (int) (bereichsSkalierung / 2);
			nbereich = nbereich2 + 1;
		}
		ArrayList<Vec2> punkte = new ArrayList<Vec2>();
		for(int i = hoehe.y2; i < hoehe.y1; i+= skalierungswert){
			Vec2 temp = null;
			Point p = new Point();
			for(int j = 0; j < nbereich; j++){
				if((i + j) < image.getHeight()){
					temp = getMiddlePoint(getPointsinLine(image.getHeight() - (i + j), image), image);
				}
				p.v1 += temp.x;
				p.v2 += temp.y;
				
			}
			for(int j = 1; j < nbereich2; j++){
				if((i + j) < image.getHeight()){
					temp = getMiddlePoint(getPointsinLine(image.getHeight() - (i - j), image), image);
				}
				p.v1 += temp.x;
				p.v2 += temp.y;
			}
			punkte.add(new Vec2((p.v1 / bereichsSkalierung), (p.v2 / bereichsSkalierung)));
		}
		return punkte;
	}
	
	/**
	 * Berechnet den Mittelpunkt des Objektes: Punkt, welcher die Tiefe null hat(dafuer Laser und Kamera Winkel = 0°), -> Gebrauchsanleitung
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Vec2> createMiddle(File f) throws IOException{
		Line width = getWidth(f);
		Line hoehe = getHoehe(f);
		return skalierung(width.y1 - width.y2, hoehe);
	}
}