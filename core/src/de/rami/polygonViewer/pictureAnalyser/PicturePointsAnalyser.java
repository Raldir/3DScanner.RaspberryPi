package de.rami.polygonViewer.pictureAnalyser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.rami.polygonViewer.materials.Vec2;
import de.rami.polygonViewer.systemAndSettings.NoPointException;


/**
 * Abstrakte Klasse für das Analysieren der Bilder
 * @author Rami und Anton
 *
 */
public abstract class PicturePointsAnalyser {

	private BufferedImage image;
	private ArrayList<Vec2> punkte = new ArrayList<Vec2>();
	private ArrayList<Vec2> punktebefore = new ArrayList<Vec2>();
	private ArrayList<Vec2>	punkteLastPicture = new ArrayList<Vec2>();
	
	public ArrayList<Vec2> getPunkteLastPicture() {
		return punkteLastPicture;
	}

	public void setPunkteLastPicture(ArrayList<Vec2> punkteLastPicture) {
		this.punkteLastPicture = punkteLastPicture;
	}

	public PicturePointsAnalyser(ArrayList<Vec2> pointsLastPicture){
		punkteLastPicture = pointsLastPicture;
	}
	
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
	 * @throws NoPointException
	 */
	public abstract ArrayList<Vec2> PointsinLine(int line) throws NoPointException;
	
	
	/**
	 * Der Mittelpunkt auf der Ebene wird mit den uebergebenen Punkte, welche sich auf einer Ebene befinden
	 * ermittelt
	 * @param points
	 * @param image
	 * @return
	 * @throws NoPointException 
	 */
	public abstract Vec2 getMiddlePoint(ArrayList<Vec2> points) throws NoPointException;
	
	
	public abstract Point getLineOfLaser();
	
	/**
	 * Diese Methode sorgt dafuer, dass ein Punkt fuer n Punkte erzeugt wird. Demzufolge stellt, ein Punkt
	 * der von Skalierung zurueckgegeben wird den Durchschnittswert von n Linien dar.
	 * JEDOCH wird nicht jeder Punkt in den n Punkten verwendet um den Durschnittspunkt zu erstellen, sondern jeder p-te Punkt wird verwendet um einen
	 * Punkt zu erzeugen, der n-Punkte ersetzt.
	 * @param skalierungswert
	 * @param hoehe
	 * @return
	 * @throws NoPointException 
	 */
	public abstract ArrayList<Vec2> finalPointCalculation(Point hoehe) throws NoPointException;
	
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
	
