package de.rami.polygonViewer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import de.rami.polygonViewer.Line;

//Die Klasse ist verantwortlich fuer die Auswertung der Punkte eines Bildes

public class Bildpunkte{
	
	/**
	 * Bestimmt die benötigte Helligkeit[genauer RGB Wert], damit ein Pixel vom Algorithmus wahrgenommen wird.
	 */
	
	private File previousPicture;
	
	private BufferedImage image;
	private File f;
	private ArrayList<Vec2> pointsbefore = new ArrayList<Vec2>();
	private ArrayList<Vec2> punkte;
	
	public Bildpunkte(File f, Line hoehe){
		punkte = new ArrayList<Vec2>();
		this.f = f;
		try {
			image = ImageIO.read(f);
			System.out.println("----" + hoehe.y1 + "   " + hoehe.y2);
		punkte = skalierung2(hoehe);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<Vec2> getPunkte(){
		return punkte;
	}
	
	/**
	 * Es wird eine Liste von Vektoren zurückgegeben, die auf einer Ebene den erforderten RGB-Wert haben.
	 * @param line
	 * @param image
	 * @return
	 */
	public ArrayList<Vec2> PointsinLine(int line, BufferedImage image){
		int oSchwellenWert = getLinieSchwellenWert(line, image,  Settings.obererSchwellenWert, 1);
		int uschwellenWert = (int) (0.5 * oSchwellenWert);
		if(oSchwellenWert == 1){
			if(pointsbefore != null){	
				return pointsbefore;
			}
		}
		// Es wird nach dem ersten Punkt gesucht, welcher den erforderten RGB-Wert erfüllt.
		int ausgangspunkt = 0;
		ArrayList<Vec2> punkteLine = new ArrayList<Vec2>();
		for(int j = 0 ; j < image.getWidth(); j++){
			if(new Color(image.getRGB(j, line)).getRed() >= oSchwellenWert && new Color(image.getRGB(j, line)).getBlue() >= oSchwellenWert
					&& new Color(image.getRGB(j, line)).getGreen() >= oSchwellenWert){
//				System.out.println("punkt gespeichert");
				ausgangspunkt = j;
				punkteLine.add(new Vec2((j), line));
				break;
			}
		}
		//Daraufhin wird von dem oben gefundenen Punkt aus nach weiteren Punkten gesucht, die
		//nun mindestens einen etwas kleineren RGB-Wert haben und die dadurch entstandene Liste wird zurückgegeben
		int i = 0;
		while(ausgangspunkt + i < image.getWidth() && new Color(image.getRGB(ausgangspunkt + i, line)).getBlue() > uschwellenWert && new Color(image.getRGB(ausgangspunkt + i, line)).getRed() > uschwellenWert && new Color(image.getRGB(ausgangspunkt + i, line)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2((ausgangspunkt + i), line));
			i++;
		}
		
		i = 0;
		while(ausgangspunkt -i > 0 && new Color(image.getRGB(ausgangspunkt - i, line)).getBlue() > uschwellenWert && new Color(image.getRGB(ausgangspunkt - i, line)).getRed() > uschwellenWert && new Color(image.getRGB(ausgangspunkt - i, line)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2((ausgangspunkt - i), line));
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
	public Vec2 getMiddlePoint(ArrayList<Vec2> points, BufferedImage image){
		float gesamthelligkeit = 0;
		for(int i = 0; i < points.size(); i++){
			gesamthelligkeit += image.getRGB((int)(points.get(i).x),(int)(points.get(i).y));
		}
		//Es wird der Durchschnitt der X-Koordinaten ermittelt. Jedoch fließen die einzelnen Werte unterschiedlich stark ein.
		//Die Koordinate wird mit dem Verhältnis der Helligkeit des Punktes zur Durchschnittshelligkeit multipliziert.
		//So fließt ein hellererPunkt staerker ein als ein vergleichsweise dunkler Punkt.
		float durchschnittshelligkeit = gesamthelligkeit / points.size();
		ArrayList<Float> values = new ArrayList<Float>();
		for(int i = 0; i< points.size(); i++){
			values.add(points.get(i).x * (image.getRGB((int)(points.get(i).x),(int)(points.get(i).y)) / durchschnittshelligkeit));
		}
		float gesamtvalue = 0;
		for(int i = 0; i < values.size(); i++){
			gesamtvalue += values.get(i);
		}
		return new Vec2((((gesamtvalue / values.size()))) /  Settings.skalierungswertX,  ((points.get(0).y)) /  Settings.skalierungswertY);
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
//	public ArrayList<Vec2> skalierung(float skalierungswert, Line hoehe){
//		double nbereich =  Settings.bereichsSkalierung / 2;
//		int nbereich2 = 0;
//		if(nbereich % 2 != 0){
//			nbereich2 = (int) ( Settings.bereichsSkalierung / 2);
//			nbereich = nbereich2 + 1;
//		}
//		ArrayList<Vec2> punkte = new ArrayList<Vec2>();
//		for(int i = hoehe.y1; i < hoehe.y2; i+= skalierungswert){
//			Vec2 temp = null;
//			Point p = new Point();
//			for(int j = 0; j < nbereich; j++){
//				if((i + j) < image.getHeight()){
//					temp = getMiddlePoint(PointsinLine(image.getHeight() - (i + j), image), image);
//				}
//				p.v1 += temp.x;
//				p.v2 += temp.y;
//				
//			}
//			for(int j = 1; j < nbereich2; j++){
//				if((i - j) > 0){
//					temp = getMiddlePoint(PointsinLine(image.getHeight() - (i - j), image), image);
//				}
//				p.v1 += temp.x;
//				p.v2 += temp.y;
//			}
//			punkte.add(new Vec2((p.v1 /  Settings.bereichsSkalierung), (p.v2 / Settings.bereichsSkalierung)));
//		}
//		return punkte;
//	}
	
	public ArrayList<Vec2> skalierung2(Line hoehe){
		int anzahlPunkte = (Settings.polygonAnzahl / Settings.anzahlbilder);
		System.out.println("anzahlPunkte " + anzahlPunkte);
		double punktabstand = (double)(hoehe.y2 - hoehe.y1) * ((double) (1 / (double)(anzahlPunkte)));
		System.out.println("punktabstand " + punktabstand);
		for(double i = hoehe.y1; i + 0.001 < hoehe.y2; i+= punktabstand){
			int counter = 0;
			Vec2 temp = null;
			Point p = new Point();
			for(float j = 0; j < punktabstand; j+= Settings.bildskalierung){
				if((int)i + j < image.getHeight())
				temp = getMiddlePoint( PointsinLine(((int)i + (int)j), image), image);
				counter++;
				p.v1 += temp.x;
				p.v2 += temp.y;
			}
			punkte.add(new Vec2((p.v1 /  counter), (p.v2 / counter)));
		}
		return punkte;
	}
	


}