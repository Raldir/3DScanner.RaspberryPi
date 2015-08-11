package de.rami.polygonViewer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Line {
	
	public int y1, y2;
	
	public Line getHoehe(File f){
		BufferedImage image = null;
		try {
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Es wird nach dem ersten Punkt von oben und von unten des Bildes gesucht, welcher den benötigten RGB wert hat.
		out:
		for(int i = 0; i < image.getHeight(); i++){
			for(int j = 0 ; j < image.getWidth(); j++){
				if(new Color(image.getRGB(j, i)).getBlue() >  Settings.obererSchwellenWert  && new Color(image.getRGB(j, i)).getRed() >   Settings.obererSchwellenWert && new Color(image.getRGB(j, i)).getGreen() >   Settings.obererSchwellenWert){
					this.y1 = i;
					break out;
				}
			}
		}
		out:
		for(int i = image.getHeight() -1; i > 0; i--){
			for(int j = 0 ; j < image.getWidth(); j++){
				if(new Color(image.getRGB(j, i)).getBlue() >   Settings.obererSchwellenWert  && new Color(image.getRGB(j, i)).getRed() >   Settings.obererSchwellenWert && new Color(image.getRGB(j, i)).getGreen() >   Settings.obererSchwellenWert){
					this.y2 = i;
					break out;
				}
			}
		}
		return this;
	}
}
