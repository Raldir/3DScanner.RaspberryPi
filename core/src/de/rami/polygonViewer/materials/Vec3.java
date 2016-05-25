package de.rami.polygonViewer.materials;

/**
 * Wrapper-Klasse fuer einen dreidimensionalen Punkt
 *
 */
public class Vec3 {
	
	public float x, y, z = 0;
	
	public static Vec3 vec3(float x, float y, float z){
		return new Vec3(x, y, z);
	}
	
	private Vec3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 mul(float f){
		return vec3(this.x * f, this.y * f, this.z * f);
	}
}
