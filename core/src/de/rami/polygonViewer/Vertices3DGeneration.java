package de.rami.polygonViewer;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector3;

public interface Vertices3DGeneration{
	
	/**
	 * Gibt die Indizes der Eckpunkte der �bergebenen Liste in richtiger Reihenfolge zur�ck
	 * @param vertices
	 * @author Andre Schurat
	 * @return
	 */
	public static short[] readTriangleIndicies(ArrayList<Vertex> vertices){
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
	 * Speichert die Koordinaten und die Normale eines Eckpunktes in eine floatArray und gibt diese zur�ck.
	 * @param vertices
	 * @author Andre Schurat
	 * @return
	 */
	public static float[] genVertexAndNormalArray(ArrayList<Vertex> vertices){
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
	
	public ArrayList<Vertex> genVertices(ArrayList<ArrayList<Vec2>> results);
}