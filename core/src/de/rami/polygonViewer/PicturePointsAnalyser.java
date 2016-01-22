package de.rami.polygonViewer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/**
 * Abstrakte Klasse für das Analysieren der Bilder
 * @author Rami und Anton
 *
 */
public abstract class PicturePointsAnalyser {

	private BufferedImage image;
	private ArrayList<Vec2> punkte = new ArrayList<Vec2>();
	private ArrayList<Vec2> punktebefore = new ArrayList<Vec2>();
	
	
	public static class Point{
		float v1, v2;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public void setImage(File f){
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setPunktebefore(ArrayList<Vec2> list){
		punktebefore = list;
	}
	public void setPunkte(ArrayList<Vec2> p){
		punkte = p;
	}
	
	public ArrayList<Vec2> getPunkte(){
		return punkte;
	}
	
	public ArrayList<Vec2> getPunktebefore(){
		return punktebefore;
	}
	/**
	 * Es wird eine Liste von Vektoren zurückgegeben, die auf einer Ebene den erforderten RGB-Wert haben.
	 * @param line
	 * @param image
	 * @return
	 */
	public abstract ArrayList<Vec2> PointsinLine(int line);
	
	
	/**
	 * Der Mittelpunkt auf der Ebene wird mit den uebergebenen Punkte, welche sich auf einer Ebene befinden
	 * ermittelt
	 * @param points
	 * @param image
	 * @return
	 */
	public abstract Vec2 getMiddlePoint(ArrayList<Vec2> points);
	
	
	public abstract Point getLineOfLaser();
	
	/**
	 * Diese Methode sorgt dafuer, dass ein Punkt fuer n Punkte erzeugt wird. Demzufolge stellt, ein Punkt
	 * der von Skalierung zurueckgegeben wird den Durchschnittswert von n Linien dar.
	 * JEDOCH wird nicht jeder Punkt in den n Punkten verwendet um den Durschnittspunkt zu erstellen, sondern jeder p-te Punkt wird verwendet um einen
	 * Punkt zu erzeugen, der n-Punkte ersetzt.
	 * @param skalierungswert
	 * @param hoehe
	 * @return
	 */
	public abstract ArrayList<Vec2> finalPointCalculation(Point hoehe);
	
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
	public abstract int getLinieSchwellenWert(int line, BufferedImage image, int obereGrenze, int untereGrenze);

}
	
