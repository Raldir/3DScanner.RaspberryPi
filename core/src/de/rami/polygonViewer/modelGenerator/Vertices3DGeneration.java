package de.rami.polygonViewer.modelGenerator;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector3;

import de.rami.polygonViewer.materials.Triangle;
import de.rami.polygonViewer.materials.Vec2;
import de.rami.polygonViewer.materials.Vertex;

/**
 * 
 * @author Andre Schurat, Anton von Weltzien, Rami Aly
 *
 */
public abstract class Vertices3DGeneration{
	
	/**
	 * Gibt die Indizes der Eckpunkte der übergebenen Liste in richtiger Reihenfolge zurück
	 * @param vertices
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
	 * Speichert die Koordinaten und die Normale eines Eckpunktes in eine floatArray und gibt diese zurück.
	 * @param vertices
	 * @return
	 */
	public float[] genVertexAndNormalArray(ArrayList<Vertex> vertices){
		float[] res = new float[vertices.size() * 6];
		int i = 0;
		for(Vertex v : vertices){
			res[i] 		= v.getX();
			res[i + 1] 	= v.getY();
			res[i + 2] 	= v.getZ();
			Vector3 vec = v.genNormal();
//			System.out.println(vec.x + " " + vec.y + " " + vec.z);
			res[i + 3]  = vec.x;
			res[i + 4]  = vec.y;
			res[i + 5]  = vec.z;
			i += 6;
		}
		return res;
	}
	
	public ArrayList<Vertex> genVertices(ArrayList<ArrayList<Vec2>> results){
		return null;
	}
}
