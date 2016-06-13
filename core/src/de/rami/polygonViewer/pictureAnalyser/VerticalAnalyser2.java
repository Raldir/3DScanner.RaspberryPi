package de.rami.polygonViewer.pictureAnalyser;

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

import de.rami.polygonViewer.materials.Vec2;
import de.rami.polygonViewer.systemAndSettings.Settings;

//Die Klasse ist verantwortlich fuer die Auswertung der Punkte eines Bildes

/**
 * Klasse zur Analyse der Bilder bei einer vertikalen Scannung
 * 
 * @author Rami und Anton
 *
 */
public class VerticalAnalyser2 extends PicturePointsAnalyser {

	/**
	 * Bestimmt die benötigte Helligkeit[genauer RGB Wert], damit ein Pixel vom
	 * Algorithmus wahrgenommen wird.
	 * 
	 * @throws NoPointException
	 */

	public VerticalAnalyser2(File f, ArrayList<Vec2> pointsLastPicture) {
		super(pointsLastPicture);
		setImage(f);
		Point hoehe = getLineOfLaser();
		System.out.println("Line" + hoehe.v1 + "  " + hoehe.v2);
		setPunkte(finalPointCalculation(hoehe));
	}

	@Override
	public ArrayList<Vec2> PointsinLine(int line) throws NoPointException {
		int oSchwellenWert = getLinieSchwellenWert(line, getImage(), 250, 1);
		int uschwellenWert = (int) (0.5 * oSchwellenWert) + 1;
		 if(oSchwellenWert <= Settings.obererSchwellenWert * 3/5){
		 if(getPunktebefore().size() != 0){
		 return getPunktebefore();
		 }else{
		 throw new NoPointException();
		 }
		 }
		System.out.println(oSchwellenWert);
		// Es wird nach dem ersten Punkt gesucht, welcher den erforderten
		// RGB-Wert erfüllt.
		int ausgangspunkt = 0;
		ArrayList<Vec2> punkteLine = new ArrayList<Vec2>();
		for (int j = 0; j < getImage().getWidth(); j++) {
			if (higherThanSchwellenwert(oSchwellenWert, j, line)) {
				// System.out.println("punkt gespeichert");
				ausgangspunkt = j;
				punkteLine.add(new Vec2((j), line));
				break;
			}
		}
		// Daraufhin wird von dem oben gefundenen Punkt aus nach weiteren
		// Punkten gesucht, die
		// nun mindestens einen etwas kleineren RGB-Wert haben und die dadurch
		// entstandene Liste wird zurückgegeben
		int i = 0;
		while (ausgangspunkt + i < getImage().getWidth()
				&& higherThanSchwellenwert(uschwellenWert, ausgangspunkt + i, line)) {
			punkteLine.add(new Vec2((ausgangspunkt + i), line));
			i++;
		}

		i = 0;
		while (ausgangspunkt - i > 0 && higherThanSchwellenwert(uschwellenWert, ausgangspunkt - i, line)) {
			punkteLine.add(new Vec2((ausgangspunkt - i), line));
			i++;
		}
		if (punkteLine.size() == 0) {
			if (getPunktebefore().size() != 0) {
				return getPunktebefore();
			} else {
				throw new NoPointException();
			}
		} else {
			setPunktebefore(punkteLine);
		}
		return punkteLine;
	}

	@Override
	public Vec2 getMiddlePoint(ArrayList<Vec2> points) {
		float gesamthelligkeit = 0;
		for (int i = 0; i < points.size(); i++) {
			gesamthelligkeit += getImage().getRGB((int) (points.get(i).x), (int) (points.get(i).y));
		}
		// Es wird der Durchschnitt der X-Koordinaten ermittelt. Jedoch fließen
		// die einzelnen Werte unterschiedlich stark ein.
		// Die Koordinate wird mit dem Verhältnis der Helligkeit des Punktes zur
		// Durchschnittshelligkeit multipliziert.
		// So fließt ein hellererPunkt staerker ein als ein vergleichsweise
		// dunkler Punkt.
		float durchschnittshelligkeit = gesamthelligkeit / points.size();
		ArrayList<Float> values = new ArrayList<Float>();
		for (int i = 0; i < points.size(); i++) {
			values.add(points.get(i).x
					* (getImage().getRGB((int) (points.get(i).x), (int) (points.get(i).y)) / durchschnittshelligkeit));
		}
		float gesamtvalue = 0;
		for (int i = 0; i < values.size(); i++) {
			gesamtvalue += values.get(i);
		}
		return new Vec2((((gesamtvalue / values.size()))) / 200,
				((points.get(0).y)) / 200);
	}

	private boolean higherThanSchwellenwert(int value, int x, int y) {
		int redValue = new Color(getImage().getRGB(x, y)).getRed();
		return redValue >= value && new Color(getImage().getRGB(x, y)).getBlue() <= redValue / 1.7
				&& new Color(getImage().getRGB(x, y)).getGreen() <= redValue / 1.7;
	}

	private boolean higherThanSchwellenwert2(int value, int x, int y) {
		int redValue = new Color(getImage().getRGB(x, y)).getRed();
		return redValue > value && new Color(getImage().getRGB(x, y)).getBlue() < redValue / 1.7
				&& new Color(getImage().getRGB(x, y)).getGreen() < redValue / 1.7;
	}

	// private boolean higherThanSchwellenwert3(int value, int x, int y){
	// int redValue = new Color(getImage().getRGB(x, y)).getRed();
	// return redValue > value && new Color(getImage().getRGB(x, y)).getBlue() <
	// redValue / 2 &&
	// new Color(getImage().getRGB(x, y)).getGreen() < redValue / 2;
	// }
	@Override
	public Point getLineOfLaser() {
		Point p = new Point();
		// Es wird nach dem ersten Punkt von oben und von unten des Bildes
		// gesucht, welcher den benötigten RGB wert hat.
		out: for (int i = 0; i < getImage().getHeight(); i++) {
			for (int j = 0; j < getImage().getWidth(); j++) {
				if (higherThanSchwellenwert2(Settings.obererSchwellenWert, j, i)) {
					p.v1 = i;
					break out;
				}
			}
		}
		out: for (int i = getImage().getHeight() - 1; i > 0; i--) {
			for (int j = 0; j < getImage().getWidth(); j++) {
				if (higherThanSchwellenwert2(Settings.obererSchwellenWert, j, i)) {
					p.v2 = i;
					break out;
				}
			}
		}
		return p;
	}

	@Override
	public ArrayList<Vec2> finalPointCalculation(Point hoehe) {
		if (hoehe.v1 - hoehe.v2 == 0) {
			return new ArrayList<Vec2>();
		}
		ArrayList<Vec2> list = new ArrayList<Vec2>();
		int anzahlPunkte = (Settings.polygonAnzahl / Settings.anzahlbilder);
		System.out.println("anzahlPunkte " + anzahlPunkte);
		double punktabstand = (double) (hoehe.v2 - hoehe.v1) * ((double) (1 / (double) (anzahlPunkte)));
		for (double i = hoehe.v1; Double.compare(i, hoehe.v2) <= 0; i += punktabstand) {
			int counter = 0;
			Vec2 temp = null;
			Point p = new Point();
			for (float j = 0; j < punktabstand; j += Settings.bildskalierung) {
				if ((int) i + j < getImage().getHeight()) {
					try {
						temp = getMiddlePoint(PointsinLine(((int) i + (int) j)));
					} catch (NoPointException e) {
						temp = getPunkteLastPicture().get(counter);
						e.printStackTrace();
					}
					counter++;
					p.v1 += temp.x;
					p.v2 += temp.y;
				}
			}
			System.out.println(p.v1 / counter + " " + p.v2 / counter);
			float p1 = p.v1 / counter;
			float p2 = (p.v2 / counter);
			if (p1 < 15) {
				if (list.size() - 1 > 0) {
					list.add(getPunkteLastPicture().get(list.size() - 1));
					continue;
				} else {
					list.add(getPunkteLastPicture().get(0));
					continue;
				}
			}
			list.add(new Vec2((p1), (p2)));
		}
		int difference = list.size() - anzahlPunkte;
		if (list.size() > 0 && difference > 0) {
			list.remove(list.size() - 1);
		}
		return list;
	}

	private ArrayList<Vec2> catchWrongPoints(ArrayList<Vec2> list) {
		int dvalue = 0;
		for (int i = 0; i < list.size(); i++) {
			dvalue += list.get(i).x;
		}
		dvalue /= list.size();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).x < dvalue * 0.5) {
				list.remove(i);
			}
		}
		return list;
	}

	@Override
	public int getLinieSchwellenWert(int line, BufferedImage image, int obereGrenze, int untereGrenze) {
		int max = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			if (higherThanSchwellenwert2(max, i, line)) {
				max = new Color(getImage().getRGB(i, line)).getRed();
			}
		}
		System.out.println("Schwellenwert ist:" + max);
		return max;
		// if(obereGrenze == (untereGrenze + 1)){
		// System.out.println("Schwellenwert ist:" + untereGrenze);
		// return untereGrenze;
		// }
		// int tempGrenze = untereGrenze + (obereGrenze - untereGrenze) / 2;
		// for(int i = 0; i < image.getWidth(); i++){
		// if(higherThanSchwellenwert2(obereGrenze, i, line)){
		// return getLinieSchwellenWert(line, image, obereGrenze, tempGrenze);
		// }
		// }
		// return getLinieSchwellenWert(line, image, tempGrenze, untereGrenze);
	}
}