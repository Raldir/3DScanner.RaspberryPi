package de.rami.polygonViewer;

import com.badlogic.gdx.math.Vector3;

/**
 * Einfache Dreiecksklasse
 * @author Rami Aly, Anton von Weltzien, Andre Schurat
 *
 */
public class Triangle {
	
	public Vertex a, b, c;
	private Vector3 normal;
	
	/**
	 * Erstellt ein Dreieck mit den uebergebenen drei Eckpunkten und berechnet die Flächennormale des Dreiecks.
	 * @param a
	 * @param b
	 * @param c
	 */
	public Triangle(Vertex a, Vertex b, Vertex c){
		this.a = a;
		this.b = b;
		this.c = c;
		Vector3 ab = a.toVector3().sub(b.toVector3());
		Vector3 ac = a.toVector3().sub(c.toVector3());
		ab = ab.nor();
		ac = ac.nor();
		normal = ac.crs(ab).nor();	
	}
	
	public Vector3 getNormal(){
		return normal;
	}

}
