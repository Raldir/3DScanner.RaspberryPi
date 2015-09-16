package de.rami.polygonViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector3;

public class HorizontalVerticesGeneration implements Vertices3DGeneration {
	
	public ArrayList<Vertex> testVerts(){
		ArrayList<ArrayList<Vec2>> list = new ArrayList<ArrayList<Vec2>>();
			ArrayList<Vec2> temp = new ArrayList<Vec2>();
			temp.add(Vec2.vec2(2 , 2 ));
			temp.add(Vec2.vec2(4 , 4));
			temp.add(Vec2.vec2(6 , 4.5f));
			temp.add(Vec2.vec2(8 ,  4));
			temp.add(Vec2.vec2(10 , 2 ));
			list.add(temp);
			ArrayList<Vec2> temp1 = new ArrayList<Vec2>();
			temp1.add(Vec2.vec2(4 , 2f));
			temp1.add(Vec2.vec2(5 , 3f));
			temp1.add(Vec2.vec2(6 , 4.2f));
			temp1.add(Vec2.vec2(7 , 3f));
			temp1.add(Vec2.vec2(8 ,  2f));
			list.add(temp1);
			ArrayList<Vec2> temp2 = new ArrayList<Vec2>();
			temp2.add(Vec2.vec2(2 , 2  ));
			temp2.add(Vec2.vec2(4 , 3.7f));
			temp2.add(Vec2.vec2(6 , 6.2f));
			temp2.add(Vec2.vec2(8 ,  3.7f));
			temp2.add(Vec2.vec2(10 , 2 ));
			list.add(temp2);
		ArrayList<Vertex> verts = genVertices(list);
//		int i = 0;
//		for(Vertex v : verts){
//			System.out.println( i + ".  " + v.getX() + "  " +v.getY() + "  " + v.getZ());
//			i++;
//		}
//		short[] test = readTriangleIndices(verts);
//		for(int in = 0; in < test.length; in+=3){
//			System.out.println(test[i] + "  " +  test[i+1] + "  " + test[i+2]);
//		}
		return verts;
	}
	
	
//	public static final ArrayList<Vertex> testVerts2(){
//		ArrayList<ArrayList<Vec2>> list = new ArrayList<ArrayList<Vec2>>();
//		for(int i = 0; i < 5; i++){
//			File f = new File("C:\\Users\\Ramor\\Desktop\\3DScanner.RaspberryPi\\core\\fotos\\" + i + ".jpg");
//			Line l = new Line().getWidth(f);
//			BildpunkteTest t = new BildpunkteTest(f, l);
//			for(int j = 0; j < t.getPunkte().size(); j++){
//				System.out.println(t.getPunkte().get(j).x + "  " + t.getPunkte().get(j).y);
//			}
//			list.add(t.getPunkte());
//		}
//		ArrayList<Vertex> verts = genVerticesTest(list);
//		return verts;
//	}
	
	
	/**
	 * Diese Methode stellt das Herzst�ck der Darstellung dar. Es werden mit den �bergebenen
	 * Punkten aus den Bildern die 3Dimensionalen Eckpunkte und Dreiecke des Objektes gebildet
	 * und zur�ckgegeben.
	 * @param results
	 * @return
	 */
	public ArrayList<Vertex> genVertices(ArrayList<ArrayList<Vec2>> results){
		ArrayList<Vertex> res = new ArrayList<Vertex>();
		float min = Float.MAX_VALUE;
		for(ArrayList<Vec2> list : results){
			for(Vec2 vec : list){
				min = Math.min(min, vec.y);
			}
		}
		//Sucht die zwei Vektoren mit den gr��ten bzw. niedrigsten z-Wert und erstellt zwei
		// Vektoren die sich jeweils mit der gefunden H�he im Mittelpunkt befinden.
		//Berechnet den Winkelabstand zwischen zwei Bildern
		ArrayList<Vertex> firstPic = new ArrayList<Vertex>();
		ArrayList<Vertex> lastPic = null;
//		Vertex temp = new Vertex(0,0,0);
//		res.add(temp);
		int i = 0;
		for(ArrayList<Vec2> list : results){
			int changefactor = 1;
			Vertex vet = null;
			i++;
			ArrayList<Vertex> curPic = new ArrayList<Vertex>();
			for(int j = 0; j < list.size(); j++){
				Vec2 buffer = new Vec2(0, 1).mul((list.get(j).y) - (min-1));
				Vertex tempVet = new Vertex(list.get(j).x, changefactor * i, buffer.y);
				Vertex temp = new Vertex(list.get(j).x, changefactor * i , 10);
				if(j == 0 && lastPic != null){
						lastPic.get(j).addTriangle(tempVet, temp);
						temp.addTriangle(res.get(res.indexOf(lastPic.get(j)) + 1), lastPic.get(j));
					}
//				Falls letzter Punkt im Bild, wird Dreieck mit top-Eckpunkt erstellt
				if(j == list.size() - 1 && lastPic != null){
						temp.addTriangle(tempVet, lastPic.get(j));
						temp.addTriangle(lastPic.get(j), res.get(res.indexOf(lastPic.get(j)) + 1));
					}
				if(lastPic != null && vet != null){
					vet.addTriangle(lastPic.get(j), tempVet);
					vet.addTriangle(lastPic.get(j - 1), lastPic.get(j));
					res.get(res.indexOf(vet) + 1).addTriangle(temp, res.get(res.indexOf(lastPic.get(j)) + 1) );
					res.get(res.indexOf(vet) + 1).addTriangle((res.get(res.indexOf(lastPic.get(j)) + 1)), (res.get(res.indexOf(lastPic.get(j -1)) + 1)));
					
				}
				curPic.add(tempVet);
				vet = tempVet;
				//F�gt den Eckpunkt der Ergebnisliste hinzu
				res.add(tempVet);
				res.add(temp);
			}
			lastPic = curPic;
			if(i == 1){
				firstPic = curPic;
			}
		}
		Vertex tempVert = null;
		//Erstellt die Dreiecke f�r das erste Bild und dem letzen
		for(Vertex v: lastPic){
			if(tempVert != null){
				res.get(res.indexOf(tempVert) + 1).addTriangle(tempVert, v);
				res.get(res.indexOf(v) + 1).addTriangle(res.get(res.indexOf(tempVert) + 1), v);
			}	
			tempVert = v;			
		}
		tempVert = null;
		for(Vertex v: firstPic){
			if(tempVert != null){
//				Vertex temp1 = new Vertex(tempVert.getX(), tempVert.getY(), -tempVert.getZ());
//				Vertex temp2 = new Vertex(v.getX(), v.getY(), -v.getZ());
//				res.add(temp1);
//				res.add(temp2);
				res.get(res.indexOf(tempVert) + 1).addTriangle(v, tempVert);
				res.get(res.indexOf(v) + 1).addTriangle(v, res.get(res.indexOf(tempVert) + 1));
			}	
			tempVert = v;		
		}
		return res;
	}
}
