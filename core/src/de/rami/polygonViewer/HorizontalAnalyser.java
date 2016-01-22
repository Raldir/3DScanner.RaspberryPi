package de.rami.polygonViewer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.rami.polygonViewer.PicturePointsAnalyser.Point;


/**
 * Horizontale Scannung des Objektes[Work in Progress]
 * @author Rami und Anton
 *
 */
public class HorizontalAnalyser extends PicturePointsAnalyser{
	/**
	 * Bestimmt die benötigte Helligkeit[genauer RGB Wert], damit ein Pixel vom Algorithmus wahrgenommen wird.
	 */
	
	
	public HorizontalAnalyser(File f){
		setImage(f);
		Point hoehe = getLineOfLaser();
		setPunkte(finalPointCalculation(hoehe));
	}
	
	public ArrayList<Vec2> PointsinLine(int line){
		int oSchwellenWert = getLinieSchwellenWert(line, getImage(),  Settings.obererSchwellenWert, 1);
		int uschwellenWert = (int) (0.5 * oSchwellenWert);
		if(oSchwellenWert == 1){
			if(getPunktebefore() != null){	
				return getPunktebefore();
			}
		}
		// Es wird nach dem ersten Punkt gesucht, welcher den erforderten RGB-Wert erfüllt.
		int ausgangspunkt = 0;
		ArrayList<Vec2> punkteLine = new ArrayList<Vec2>();
		for(int j = 0 ; j < getImage().getHeight(); j++){
			if(new Color(getImage().getRGB(line, j)).getRed() >= oSchwellenWert && new Color(getImage().getRGB(line, j)).getBlue() >= oSchwellenWert
					&& new Color(getImage().getRGB(line, j)).getGreen() >= oSchwellenWert){
//				System.out.println("punkt gespeichert");
				ausgangspunkt = j;
				punkteLine.add(new Vec2(line, j));
				break;
			}
		}
		//Daraufhin wird von dem oben gefundenen Punkt aus nach weiteren Punkten gesucht, die
		//nun mindestens einen etwas kleineren RGB-Wert haben und die dadurch entstandene Liste wird zurückgegeben
		int i = 0;
		while(ausgangspunkt + i < getImage().getHeight() && new Color(getImage().getRGB(line, ausgangspunkt + i)).getBlue() > uschwellenWert && new Color(getImage().getRGB(line, ausgangspunkt + i)).getRed() > uschwellenWert && new Color(getImage().getRGB(line, ausgangspunkt + i)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2(line, ausgangspunkt + i));
			i++;
		}
		
		i = 0;
		while(ausgangspunkt -i > 0 && new Color(getImage().getRGB(line, ausgangspunkt - i)).getBlue() > uschwellenWert && new Color(getImage().getRGB(line, ausgangspunkt - i)).getRed() > uschwellenWert && new Color(getImage().getRGB(line, ausgangspunkt - i)).getGreen() > uschwellenWert){
			punkteLine.add(new Vec2(line, ausgangspunkt - i));
			i++;
		}
		setPunktebefore(punkteLine);
		return punkteLine;
	}
	
	public Vec2 getMiddlePoint(ArrayList<Vec2> points){
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
			values.add(points.get(i).y * (getImage().getRGB((int)(points.get(i).x),(int)(points.get(i).y)) / durchschnittshelligkeit));
		}
		float gesamtvalue = 0;
		for(int i = 0; i < values.size(); i++){
			gesamtvalue += values.get(i);
		}
		return new Vec2((points.get(0).x) /  Settings.skalierungswertX,  (gesamtvalue / values.size()) /  Settings.skalierungswertY);
	}
	
	@Override
	public Point getLineOfLaser() {
		Point p = new Point();
		out:
		for(int i = 0; i< getImage().getWidth(); i++){
			for(int j = 0 ; j < getImage().getHeight(); j++){
				if(new Color(getImage().getRGB(i, j)).getBlue() >  Settings.obererSchwellenWert  && new Color(getImage().getRGB(i, j)).getRed() > Settings.obererSchwellenWert && new Color(getImage().getRGB(i, j)).getGreen() >  Settings.obererSchwellenWert){
					p.v1 = i;
					break out;
				}
			}
		}
		out:
		for(int i = getImage().getWidth() -1; i > 0; i--){
			for(int j = 0 ; j < getImage().getHeight(); j++){
				if(new Color(getImage().getRGB(i, j)).getBlue() >  Settings.obererSchwellenWert  && new Color(getImage().getRGB(i, j)).getRed() >  Settings.obererSchwellenWert && new Color(getImage().getRGB(i, j)).getGreen() >  Settings.obererSchwellenWert){
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
				if((int)i + j < getImage().getWidth()){
					temp = getMiddlePoint( PointsinLine(((int)i + (int)j)));
					counter++;
					p.v1 += temp.x;
					p.v2 += temp.y;
				}
			}
			System.out.println(p.v1 / counter + "   " + p.v2 / counter);
			list.add(new Vec2((p.v1 /  counter), (p.v2 / counter)));
		}
		return list;
	}
	
//	public static void main(String[] args){
//		File f = new File("C:\\Users\\Ramor\\Desktop\\3DScanner.RaspberryPi\\core\\fotos\\6.jpg");
//		Line l = new Line().getWidth(f);
//		HorizontalBildpunkte t = new HorizontalBildpunkte(f, l);
//	}

	@Override
	public int getLinieSchwellenWert(int line, BufferedImage image, int obereGrenze, int untereGrenze) {
		if(obereGrenze == (untereGrenze + 1)){
			return untereGrenze;
		}
		int tempGrenze = untereGrenze + (obereGrenze - untereGrenze) / 2;
		for(int i = 0; i < image.getHeight(); i++){
			if(new Color(image.getRGB(line, i)).getGreen() > obereGrenze &&
					new Color(image.getRGB(line, i)).getRed() > obereGrenze &&
					new Color(image.getRGB(line, i)).getBlue() > obereGrenze){
				return getLinieSchwellenWert(line, image, obereGrenze, tempGrenze);
			}
		}
		return getLinieSchwellenWert(line, image,  tempGrenze, untereGrenze);
		}
	
}
