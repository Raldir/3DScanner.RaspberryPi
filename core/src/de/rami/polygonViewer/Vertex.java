package de.rami.polygonViewer;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;


public class Vertex implements Comparable<Vertex> {

	private Vector3 pos;
	//Um den NormalenVektor des Eckpunktes zu berechen, muessen alle Flaechennormalen der an dem Punkt anliegenden
	//Dreiecke bekannt sein. Deshalb werden alle beruehrenden Dreiecke in eine Liste gespeichert.
	private ArrayList<Vertex> connections = new ArrayList<Vertex>();
	private ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	
	
	/**
	 * Erstellt einen Eckpunkt mit den uebergebenen Koordinaten
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vertex(float x, float y, float z){
		pos = new Vector3(x, y, z);
	}
	
	private void addVertex(Vertex v){
		connections.add(v);
	}
	
	public void removeVertex(Vertex v){
		connections.remove(v);
	}
	
	public void detachAll(){
		connections.clear();
	}
	
	public void detach(Vertex v){
		removeVertex(v);
		v.removeVertex(this);
	}
	
	public float getX(){
		return pos.x;
	}
	
	public float getY(){
		return pos.y;
	}
	
	public float getZ(){
		return pos.z;
	}
	
	public void connectWith(Vertex v){
		addVertex(v);
		v.addVertex(this);
	}
	
	public ArrayList<Vertex> getConnections(){
		return connections;
	}
	
	private int getVertexCount(){
		return connections.size();
	}
	
	/**
	 * Erstellt ein Dreick mit den uebergebenen Eckpunkten und wird in die Liste der drei Eckpunkte gespeichert.
	 * @param b
	 * @param c
	 */
	public void addTriangle(Vertex b, Vertex c){
		Triangle t = new Triangle(this, b, c);
		triangles.add(t);
		b.triangles.add(t);
		c.triangles.add(t);
	}
	
	public ArrayList<Triangle> getTriangles(){
		return triangles;
	}
	
	/**
	 * Erzeugt die Normale des Eckpunktes, indem der Durchschnittswert von allen Normalen der angrenzenden Dreiecke gebildet wird.
	 * @return
	 */
    public Vector3 genNormal(){
    	Vector3 res = new Vector3();
    	for(Triangle t : triangles){
    		res.add(t.getNormal());
    	}
    	return res.nor();
   }

	
	public Vector3 toVector3(){
		return new Vector3(pos);
	}
	
	public String toString(){
		return pos.toString();
	}
	
	public String getConnectedVerticies(){
		return connections.toString();
	}
	
	@Override
	public int compareTo(Vertex o) {
		return getVertexCount() - o.getVertexCount();
	}
}
