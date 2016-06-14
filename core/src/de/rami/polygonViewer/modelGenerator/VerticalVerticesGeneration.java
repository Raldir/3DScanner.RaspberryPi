package de.rami.polygonViewer.modelGenerator;
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

import de.rami.polygonViewer.materials.Vec2;
import de.rami.polygonViewer.materials.Vertex;
import de.rami.polygonViewer.systemAndSettings.Settings;

/**
 * Generierung des 3DModelles der vertikalen Scannung
 * @author Rami Aly, Anton von Weltzien
 *
 */
public class VerticalVerticesGeneration extends Vertices3DGeneration {

	
	/**
	 * Testmethode, die implementierte Form mit einfacher Figur testet.
	 */
	public final ArrayList<ArrayList<Vec2>> testVerts(){
		ArrayList<ArrayList<Vec2>> list = new ArrayList<ArrayList<Vec2>>();
		for(int i = 1; i < 5; i++){
			ArrayList<Vec2> temp = new ArrayList<Vec2>();
			temp.add(Vec2.vec2(2, 3));
			temp.add(Vec2.vec2(2, 6));
			list.add(temp);
		}
		
		return list;
	}
	
	/**
	 * Die berechnete Indizierung der Dreiecke wird hier verwendet, um die Dreiecke zu speichern.
	 * @param results
	 * @return
	 * @Override
	 */
	public ArrayList<Vertex> genVertices(ArrayList<ArrayList<Vec2>> results){
		ArrayList<Vertex> verticies = FileGeneratorPCD.createFile(results);
		ArrayList<ArrayList<Integer>> indizes = new ArrayList<ArrayList<Integer>>();
		try {
			indizes = FileGeneratorPCD.retrieveValuesFromExternalProgram();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ArrayList<Integer> order : indizes){
			verticies.get(order.get(2)).addTriangle(verticies.get(order.get(1)), verticies.get(order.get(0)));
//			verticies.get(order.get(0)).addTriangle(verticies.get(order.get(2)), verticies.get(order.get(1)));
			
		}
		return verticies;
	}


}
