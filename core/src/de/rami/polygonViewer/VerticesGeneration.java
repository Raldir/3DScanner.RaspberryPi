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

public class VerticesGeneration {
	
	public static final ArrayList<Vertex> testVerts(){
		ArrayList<ArrayList<Vec2>> list = new ArrayList<ArrayList<Vec2>>();
		for(int i = 1; i < 5; i++){
			ArrayList<Vec2> temp = new ArrayList<Vec2>();
			temp.add(Vec2.vec2(5, 2));
			temp.add(Vec2.vec2(5, 3));
			temp.add(Vec2.vec2(5, 4));
			temp.add(Vec2.vec2(5, 5));
			list.add(temp);
		}
		ArrayList<Vertex> verts = genVerticesTest(list);
		return verts;
	}
	
	
//	public static final ArrayList<Vertex> testVerts(){
//		ArrayList<ArrayList<Vec2>> list = new ArrayList<ArrayList<Vec2>>();
//		Bildpunkte.Line l = new Bildpunkte.Line();
//		l.y1= 410;
//		l.y2 = 155;
//		try {
//			l = Bildpunkte.getHoehe(ImageIO.read(new File("C:\\Users\\Ramor\\Desktop\\Shot00\\img000.png")));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//				for(int j = 0; j < 49; j++){
//					System.out.println(" ----------------------------------------" + j + "-------------------------------- ");
//					list.add(Bildpunkte.getGesamtPoints("C:\\Users\\Ramor\\Desktop\\Shot00\\img" + (j/100) + (j / 10) % 10 + j % 10 + ".png", l));
//				}
//				for(int j = 11; j >= 0; j--){
//					list.add(Bildpunkte.getGesamtPoints("C:\\Users\\Ramor\\Desktop\\Shot00\\img" + (j/100) + (j / 10) % 10 + j % 10 + ".png", l));
//				}
//					list.add(Bildpunkte.getGesamtPoints("C:\\Users\\Ramor\\Desktop\\Shot01\\img00.png"), l);
//					list.add(Bildpunkte.getGesamtPoints("C:\\Users\\Ramor\\Desktop\\testbild.png"));
//		setSameSize(list, 0);
//		ArrayList<ArrayList<Vec2>> list2 = new ArrayList<ArrayList<Vec2>>();
//		Collections.reverse(list2);
//		list.addAll(list2);
//		for(int i = 0; i < list.size(); i++){
//			System.out.println(list.get(i).size());
//		}
//		ArrayList<Vertex> verts = genVerticesTest(list);
//		return verts;
//	}
	
	/**
	 * Gibt die Indizes der Eckpunkte der übergebenen Liste in richtiger Reihenfolge zurück
	 * @param vertices
	 * @return
	 */
	public static final short[] readTriangleIndices(ArrayList<Vertex> vertices){
		ArrayList<Short> res = new ArrayList<Short>();
		ArrayList<Triangle> tris = new ArrayList<Triangle>();
		for(Vertex a : vertices){
			tris.addAll(a.getTriangles());
		}
		HashSet<Triangle> trisSet = new HashSet<Triangle>();
		trisSet.addAll(tris);
		tris.clear();
		tris.addAll(trisSet);
		
		for(Triangle t : tris){
			res.add((short) vertices.indexOf(t.a));
			res.add((short) vertices.indexOf(t.b));
			res.add((short) vertices.indexOf(t.c));
		}
		short[] arr = new short[res.size()];
		int i = 0;
		for(Short s : res){
			arr[i] = s;
			i++;
		}
		return arr;
	}
	
	/**
	 * Speichert die Koordinaten und die Normale eines Eckpunktes in eine floatArray und gibt diese zurück.
	 * @param vertices
	 * @return
	 */
	public static final float[] genVertexAndNormalArray(ArrayList<Vertex> vertices){
		float[] res = new float[vertices.size() * 6];
		int i = 0;
		for(Vertex v : vertices){
			res[i] 		= v.getX();
			res[i + 1] 	= v.getY();
			res[i + 2] 	= v.getZ();
			Vector3 vec = v.genNormal();
			res[i + 3]  = vec.x;
			res[i + 4]  = vec.y;
			res[i + 5]  = vec.z;
			i += 6;
		}
		return res;
	}
	
	/**
	 * Diese Methode stellt das Herzstück der Darstellung dar. Es werden mit den übergebenen
	 * Punkten aus den Bildern die 3Dimensionalen Eckpunkte und Dreiecke des Objektes gebildet
	 * und zurückgegeben.
	 * @param results
	 * @return
	 */
	public static final ArrayList<Vertex> genVerticesTest(ArrayList<ArrayList<Vec2>> results){
		ArrayList<Vertex> res = new ArrayList<Vertex>();
		//Sucht die zwei Vektoren mit den größten bzw. niedrigsten z-Wert und erstellt zwei
		// Vektoren die sich jeweils mit der gefunden Höhe im Mittelpunkt befinden.
		float max = 0;
		float min = Float.MAX_VALUE;
		for(ArrayList<Vec2> list : results){
			for(Vec2 vec : list){
				min = Math.min(min, vec.y);
				max = Math.max(max, vec.y);
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
				Vec2 buffer = dir.mul((vec.x - Settings.middle));
				//Erstellt einen Dreidimensionalen Vektor, welcher als z Wert den y Wert der Übergebenen Punkte verwendet
				Vertex tempVet = new Vertex(buffer.x, buffer.y, vec.y);
				//Falls erster Punkt im Bild, wird Dreieck mit bot-Eckpunkt erstellt
				if(j == 0){
					tempVet.connectWith(bot);
					if(lastPic != null){
						tempVet.addTriangle(lastPic.get(j), bot);
					}
				}
				//Falls letzter Punkt im Bild, wird Dreieck mit top-Eckpunkt erstellt
				if(j == list.size() - 1){
					tempVet.connectWith(top);
					if(lastPic != null){
						lastPic.get(j).addTriangle(tempVet, top);
					}
				}
				//Verbindet Eckpunkt mit Vorherigen Eckpunkt auf gleicher Höhe(nur verwendet für LinestripModell)
				if(vet != null){
					vet.connectWith(tempVet);
				}
				if(lastPic != null && lastPic.size() > j){
					tempVet.connectWith(lastPic.get(j));
					if(j > 0){
						tempVet.connectWith(lastPic.get(j - 1));
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
			if(i > 0){
				v.connectWith(firstPic.get(i - 1));
			}
			firstPic.get(i).connectWith(v);
			
			
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
