package de.rami.polygonViewer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector3;

/**
 * Generierung des 3DModelles der vertikalen Scannung
 * @author Rami Aly, Anton von Weltzien und Andre Schurat
 *
 */
public class VerticalVerticesGeneration implements Vertices3DGeneration {
	
	public final ArrayList<Vertex> testVerts(){
		ArrayList<ArrayList<Vec2>> list = new ArrayList<ArrayList<Vec2>>();
		for(int i = 1; i < 5; i++){
			ArrayList<Vec2> temp = new ArrayList<Vec2>();
			temp.add(Vec2.vec2(2, 3));
			temp.add(Vec2.vec2(2, 6));
			list.add(temp);
		}
		ArrayList<Vertex> verts = genVertices(list);
		return verts;
	}
	
	/**
	 * Diese Methode stellt das Herzstück der Darstellung dar. Es werden mit den übergebenen
	 * Punkten aus den Bildern die 3Dimensionalen Eckpunkte und Dreiecke des Objektes gebildet
	 * und zurückgegeben.
	 * @param results
	 * @return
	 */
	public ArrayList<Vertex> genVertices(ArrayList<ArrayList<Vec2>> results){
		ArrayList<Vertex> res = new ArrayList<Vertex>();
		//Sucht die zwei Vektoren mit den größten bzw. niedrigsten z-Wert und erstellt zwei
		// Vektoren die sich jeweils mit der gefunden Höhe im Mittelpunkt befinden.
		float max = 0;
		float min = Float.MAX_VALUE;
		float maxX = 0;
		for(ArrayList<Vec2> list : results){
			for(Vec2 vec : list){
				min = Math.min(min, vec.y);
				max = Math.max(max, vec.y);
				maxX = Math.max(maxX, vec.x);
			}
		}
		Vertex bot = new Vertex(0, 0, min);
		Vertex top = new Vertex(0, 0, max);
		res.add(top);
		res.add(bot);
		//Berechnet den Winkelabstand zwischen zwei Bildern
		float angfactor = 360f / (results.size());
		int i = 0;
		ArrayList<Vertex> firstPic = null;
		ArrayList<Vertex> lastPic = null;
		for(ArrayList<Vec2> list : results){
			//Erzeugt aus einem Winkel einen passenden Normalvektor
			Vec2 dir = Vec2.degreesToVec(angfactor * (float)(i));
			i++;
			Vertex vet = null;
			int j = 0;
			ArrayList<Vertex> curPic = new ArrayList<Vertex>();
			if(i == 1){
				firstPic = curPic;
			}
			for(Vec2 vec : list){
				//Erstellt einen Vektor, in dem der Normalenvektor des Winkels auf die Dicke des Objektes skaliert wird.
				Vec2 buffer = dir.mul(((maxX + Settings.grunddicke) - vec.x));
				//Erstellt einen Dreidimensionalen Vektor, welcher als z Wert den y Wert der Übergebenen Punkte verwendet
				Vertex tempVet = new Vertex(buffer.x, buffer.y, vec.y);
				//Falls erster Punkt im Bild, wird Dreieck mit bot-Eckpunkt erstellt
				if(j == 0){
					if(lastPic != null){
						tempVet.addTriangle(lastPic.get(j), bot);
					}
				}
				//Falls letzter Punkt im Bild, wird Dreieck mit top-Eckpunkt erstellt
				if(j == list.size() - 1){
					if(lastPic != null){
						lastPic.get(j).addTriangle(tempVet, top);
					}
				}
				//Fügt dem vorherigen Eckpunkt zwei Dreiecke hinzu
				//Die Reihenfolge der angebenen Eckpunkte ist essenziell! Diese beeinflusst die Flächennormalen und das Facing(ggf. falschherum)
				if(lastPic != null && vet != null){
					vet.addTriangle(tempVet, lastPic.get(j));
					vet.addTriangle(lastPic.get(j), lastPic.get(j - 1));
				}
				curPic.add(tempVet);
				vet = tempVet;
				//Fügt den Eckpunkt der Ergebnisliste hinzu
				res.add(tempVet);
				j++;
			}
			lastPic = curPic;
		}
		lastPic.get(results.get(0).size() - 1).addTriangle(firstPic.get(results.get(0).size() - 1), top);
		firstPic.get(0).addTriangle(lastPic.get(0), bot);
		i = 0;
		Vertex tempVert = null;
		//Erstellt die Dreiecke für das erste Bild und dem letzen
		for(Vertex v: lastPic){
			if(tempVert != null){
				firstPic.get(i).addTriangle(v, tempVert);
				firstPic.get(i).addTriangle(tempVert, firstPic.get(i - 1));
			}
			
			
			tempVert = v;
			i++;				
		}
		return res;
	}

}
