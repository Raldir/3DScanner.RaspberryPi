package de.rami.polygonViewer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class FileCreator {
	
	public static String pfad = "C:\\Users\\Ramor\\Desktop\\test1.obj";
	
	
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
			bw.write("mtllib Cube.mtl");
			bw.newLine();
			bw.write("o scannedObject");
			for(int i = 0;  i < vertices.size(); i++){
				bw.newLine();
				bw.write("v " + vertices.get(i).getX() + " " + vertices.get(i).getY() + " " + vertices.get(i).getZ());
			}
			bw.newLine();
			for(int i = 0; i < vertices.size(); i++){
				bw.write("vn " + vertices.get(i).genNormal().x + " " + vertices.get(i).genNormal().y + " " + vertices.get(i).genNormal().y);
				bw.newLine();
			}
			bw.write("g Cube_Cube_Material");
			bw.newLine();
			bw.write("usemtl Material");
			bw.newLine();
			bw.write("s 8");
			for(Triangle t : tris){
				bw.newLine();
				bw.write("f " + (vertices.indexOf(t.a) + 1) + " " + (vertices.indexOf(t.b) + 1) + " " + (vertices.indexOf(t.c) + 1));
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		createFile(VerticesGeneration.testVerts());
	}
}
