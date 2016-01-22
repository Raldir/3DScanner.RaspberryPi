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

//Die Klasse ist verantwortlich fuer die Auswertung der Punkte eines Bildes

/**
 * Klasse zur Analyse der Bilder bei einer vertikalen Scannung
 * @author Rami und Anton
 *
 */
public class VerticalAnalyser extends PicturePointsAnalyser{
	
	/**
	 * Bestimmt die benötigte Helligkeit[genauer RGB Wert], damit ein Pixel vom Algorithmus wahrgenommen wird.
	 */	
	
	public VerticalAnalyser(File f){
		setImage(f);
		Point hoehe = getLineOfLaser();
		System.out.println("Line" + hoehe.v1 + "  " +  hoehe.v2);
		setPunkte(finalPointCalculation(hoehe));
	}
	

	@Override
	public ArrayList<Vec2> PointsinLine(int line){
		int oSchwellenWert = getLinieSchwellenWert(line, getImage(),  Settings.obererSchwellenWert, 1);
		int uschwellenWert = (int) (0.5 * oSchwellenWert) + 1;
		System.out.println(oSchwellenWert);
		if(oSchwellenWert == 1){
			if(getPunktebefore() != null){
				return getPunktebefore();
			}else{
				System.out.println("						isnull");
			}
		}
		// Es wird nach dem ersten Punkt gesucht, welcher den erforderten RGB-Wert erfüllt.
		int ausgangspunkt = 0;
		ArrayList<Vec2> punkteLine = new ArrayList<Vec2>();
		for(int j = 0 ; j < getImage().getWidth(); j++){
			if(new Color(getImage().getRGB(j, line)).getRed() >= oSchwellenWert && new Color(getImage().getRGB(j, line)).getBlue() >= oSchwellenWert
					&& new Color(getImage().getRGB(j, line)).getGreen() >= oSchwellenWert){
//				System.out.println("punkt gespeichert");
				ausgangspunkt = j;
				punkteLine.add(new Vec2((j), line));
				break;
			}
		}
		//Daraufhin wird von dem oben gefundenen Punkt aus nach weiteren Punkten gesucht, die
		//nun mindestens einen etwas kleineren RGB-Wert haben und die dadurch entstandene Liste wird zurückgegeben
		int i = 0;
		while(ausgangspunkt + i < getImage().getWidth() && new Color(getImage().getRGB(ausgangspunkt + i, line)).getBlue() > uschwellenWert && new Color(getImage().getRGB(ausgangspunkt + i, line)).getRed() > uschwellenWert && new Color(getImage().getRGB(ausgangspunkt + i, line)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2((ausgangspunkt + i), line));
			i++;
		}
		
		i = 0;
		while(ausgangspunkt -i > 0 && new Color(getImage().getRGB(ausgangspunkt - i, line)).getBlue() > uschwellenWert && new Color(getImage().getRGB(ausgangspunkt - i, line)).getRed() > uschwellenWert && new Color(getImage().getRGB(ausgangspunkt - i, line)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2((ausgangspunkt - i), line));
			i++;
		}
		setPunktebefore(punkteLine);
		return punkteLine;
	}
	
	@Override
	public Vec2 getMiddlePoint(ArrayList<Vec2> points){
		if(points.size() == 0){
			System.out.println("size == 0");
		}
		float gesamthelligkeit = 0;
		for(int i = 0; i < points.size(); i++){
			gesamthelligkeit += getImage().getRGB((int)(points.get(i).x),(int)(points.get(i).y));
		}
		//Es wird der Durchschnitt der X-Koordinaten ermittelt. Jedoch fließen die einzelnen Werte unterschiedlich stark ein.
		//Die Koordinate wird mit dem Verhältnis der Helligkeit des Punktes zur Durchschnittshelligkeit multipliziert.
		//So fließt ein hellererPunkt staerker ein als ein vergleichsweise dunkler Punkt.
		float durchschnittshelligkeit = gesamthelligkeit / points.size();
		ArrayList<Float> values = new ArrayList<Float>();
		for(int i = 0; i< points.size(); i++){
			values.add(points.get(i).x * (getImage().getRGB((int)(points.get(i).x),(int)(points.get(i).y)) / durchschnittshelligkeit));
		}
		float gesamtvalue = 0;
		for(int i = 0; i < values.size(); i++){
			gesamtvalue += values.get(i);
		}
		return new Vec2((((gesamtvalue / values.size()))) /  Settings.skalierungswertX,  ((points.get(0).y)) /  Settings.skalierungswertY);
	}
	
	@Override
	public Point getLineOfLaser() {
		Point p = new Point();
		//Es wird nach dem ersten Punkt von oben und von unten des Bildes gesucht, welcher den benötigten RGB wert hat.
		out:
		for(int i = 0; i < getImage().getHeight(); i++){
			for(int j = 0 ; j <  getImage().getWidth(); j++){
				if(new Color( getImage().getRGB(j, i)).getBlue() >  Settings.obererSchwellenWert  && new Color( getImage().getRGB(j, i)).getRed() >   Settings.obererSchwellenWert && new Color( getImage().getRGB(j, i)).getGreen() >   Settings.obererSchwellenWert){
					p.v1 = i;
					break out;
				}
			}
		}
		out:
		for(int i =  getImage().getHeight() -1; i > 0; i--){
			for(int j = 0 ; j <  getImage().getWidth(); j++){
				if(new Color( getImage().getRGB(j, i)).getBlue() >   Settings.obererSchwellenWert  && new Color( getImage().getRGB(j, i)).getRed() >   Settings.obererSchwellenWert && new Color( getImage().getRGB(j, i)).getGreen() >   Settings.obererSchwellenWert){
					p.v2 = i;
					break out;
				}
			}
		}
		return p;
	}

	@Override
	public ArrayList<Vec2> finalPointCalculation(Point hoehe) {
		ArrayList<Vec2> list = new ArrayList<Vec2>();
		int anzahlPunkte = (Settings.polygonAnzahl / Settings.anzahlbilder);
		System.out.println("anzahlPunkte " + anzahlPunkte);
		double punktabstand = (double)(hoehe.v2 - hoehe.v1) * ((double) (1 / (double)(anzahlPunkte)));
		System.out.println("punktabstand " + punktabstand);
		for(double i = hoehe.v1; i + 0.001 < hoehe.v2; i+= punktabstand){
			int counter = 0;
			Vec2 temp = null;
			Point p = new Point();
			for(float j = 0; j < punktabstand; j+= Settings.bildskalierung){
				if((int)i + j <  getImage().getHeight())
				temp = getMiddlePoint( PointsinLine(((int)i + (int)j)));
				counter++;
				p.v1 += temp.x;
				p.v2 += temp.y;
			}
			list.add(new Vec2((p.v1 /  counter), (p.v2 / counter)));
		}
		return list;
	}
	
	public boolean isNull(){
		if(getLineOfLaser().v1 == 0 && getLineOfLaser().v2 == 0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public int getLinieSchwellenWert(int line, BufferedImage image, int obereGrenze, int untereGrenze) {
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

	public static void main(String[] args){
		VerticalAnalyser v = new VerticalAnalyser(new File("C:\\Users\\Ramor\\Desktop\\3DScanner.RaspberryPi\\core\\fotos\\img01.jpg"));
		ArrayList<Vec2> list = v.getPunkte();
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i).y);
		}
	}
}