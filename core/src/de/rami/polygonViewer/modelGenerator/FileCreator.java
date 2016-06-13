package de.rami.polygonViewer.modelGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector3;

import de.rami.polygonViewer.materials.Triangle;
import de.rami.polygonViewer.materials.Vertex;

/**
 * @author Rami und Anton
 * Generiert eine .obj Datei im übergebenen Pfad.
 */
public class FileCreator {
	
	///Eventuell sollte man hier den default pfad rausnehmen?
	public static String pfad = "C:\\Users\\Ramor\\Desktop\\test1.obj";
	public static int glaettungsfaktor = 8;
	
	
	public static void createFile(ArrayList<Vertex> vertices){
		ArrayList<Triangle> tris = new ArrayList<Triangle>();
		for(Vertex a : vertices){
			tris.addAll(a.getTriangles());
		}
		HashSet<Triangle> trisSet = new HashSet<Triangle>();
		trisSet.addAll(tris);
		tris.clear();
		tris.addAll(trisSet);
		File file = new File(pfad);
		try {
			FileOutputStream out = new FileOutputStream(file);
			BufferedWriter  bw = new BufferedWriter(new OutputStreamWriter(out));
			bw.write("o scannedObject");
			for(int i = 0;  i < vertices.size(); i++){
				bw.newLine();
				bw.write("v " + vertices.get(i).getX() + " " + vertices.get(i).getY() + " " + vertices.get(i).getZ());
			}
			bw.newLine();
			for(int i = 0; i < vertices.size(); i++){
				Vector3 normal =  vertices.get(i).genNormal();
				bw.write("vn " + -normal.x + " " + -normal.y + " " + -normal.z);
				bw.newLine();
			}
			bw.write("s " + glaettungsfaktor);
			for(Triangle t : tris){
				bw.newLine();
				bw.write("f " + (vertices.indexOf(t.a) + 1) + "//" + (vertices.indexOf(t.a) + 1) + " " + (vertices.indexOf(t.b) + 1)+ "//" + (vertices.indexOf(t.b) + 1) + " " + (vertices.indexOf(t.c) + 1)+ "//" + (vertices.indexOf(t.c) + 1));
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args){
//		createFile(VerticesGenerationTest.testVerts2());
//	}
}
