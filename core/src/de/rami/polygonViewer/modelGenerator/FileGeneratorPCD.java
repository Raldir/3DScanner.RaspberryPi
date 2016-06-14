package de.rami.polygonViewer.modelGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.badlogic.gdx.math.Vector3;

import de.rami.polygonViewer.materials.Triangle;
import de.rami.polygonViewer.materials.Vec2;
import de.rami.polygonViewer.materials.Vertex;
import de.rami.polygonViewer.systemAndSettings.PolygonViewer;
import de.rami.polygonViewer.systemAndSettings.Settings;

/**
 * Doing the necassairy steps to integrate the Lib into our Program.
 * @author Rami, anton
 *
 */
public class FileGeneratorPCD {
	/// Eventuell sollte man hier den default pfad rausnehmen?
	public static String pfad = System.getProperty("user.home") + "\\temp.pcd";

	public static ArrayList<Vertex> createFile(ArrayList<ArrayList<Vec2>> vertices) {
		ArrayList<Vertex> vertlist = new ArrayList<Vertex>();
		float maxX = 0;
		for (ArrayList<Vec2> list : vertices) {
			for (Vec2 vec : list) {
				maxX = Math.max(maxX, vec.x);
			}
		}
		// Berechnet den Winkelabstand zwischen zwei Bildern
		float angfactor = 360f / (vertices.size());
		File file = new File(pfad);
		try (FileOutputStream out = new FileOutputStream(file);) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			bw.write("VERSION .7");
			bw.newLine();
			bw.write("FIELDS x y z");
			bw.newLine();
			bw.write("SIZE 4 4 4");
			bw.newLine();
			bw.write("TYPE F F F");
			bw.newLine();
			bw.write("COUNT 1 1 1");
			bw.newLine();
			int count = 0;
			for (ArrayList<Vec2> list : vertices) {
				count += list.size();
			}
			bw.write("WIDTH " + count);
			bw.newLine();
			bw.write("HEIGHT " + 1);
			bw.newLine();
			bw.write("POINTS " + count);
			bw.newLine();
			bw.write("DATA ascii");
			for (int i = 0; i < vertices.size(); i++) {
				Vec2 dir = Vec2.degreesToVec(angfactor * (float) (i));
				System.out.println("Dir" + " " + dir.x + " " + dir.y);
				for (int j = 0; j < vertices.get(i).size(); j++) {
					// Todo, keine Obejkte einscannbar, die Stellen haben
					// weniger dick als der nullpunkt(Michel)
					Vec2 buffer = dir.mul(((maxX + Settings.grunddicke) - vertices.get(i).get(j).x));
					System.out.println(buffer.x + " " + buffer.y);
					Vertex tempVet = new Vertex(buffer.x, buffer.y, vertices.get(i).get(j).y);
					vertlist.add(tempVet);
					bw.newLine();
					bw.write(tempVet.getX() + " " + tempVet.getY() + " " + tempVet.getZ());
				}
			}
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vertlist;
	}

	public static ArrayList<Vertex> createFileTest(ArrayList<ArrayList<Vec2>> vertices) {
		ArrayList<Vertex> vertlist = new ArrayList<Vertex>();
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("C:\\Users\\Ramor\\Desktop\\test.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				ArrayList<Float> values = new ArrayList<Float>();
				StringBuffer bf = new StringBuffer();
				for (int i = 0; i < sCurrentLine.length(); i++) {
					if (sCurrentLine.charAt(i) == ' ' || i == sCurrentLine.length() - 1) {
						values.add(Float.parseFloat(bf.toString()));
						bf.delete(0, bf.length());
					} else {
						bf.append(sCurrentLine.charAt(i));
					}
				}
				vertlist.add(new Vertex(values.get(0), values.get(1), values.get(2)));

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return vertlist;
	}

	public static void executExternalProgram() throws IOException {
		// Process process = new
		// ProcessBuilder("C:\\Users\\Ramor\\Desktop\\projection\\Debug\\greedy_projection.exe",
		// pfad).start();
		Process process = new ProcessBuilder("C:\\Users\\Ramor\\Desktop\\projection\\Debug\\greedy_projection.exe",
				pfad).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		System.out.println("Output of running is");
		while ((line = br.readLine()) != null) {
			for (int i = 0; i < line.length() - 1; i++) {
				System.out.print(line.charAt(i));
				if (line.charAt(i + 1) == 'T') {
					System.out.println();
				}
			}
		}
	}

	public static ArrayList<ArrayList<Integer>> retrieveValuesFromExternalProgram() throws IOException {
		ArrayList<ArrayList<Integer>> vertices = new ArrayList<ArrayList<Integer>>();

		String pathToJar = null;
		try {
			pathToJar = new File(
					FileGeneratorPCD.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
							.toString();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Beim export in ne jar den Wert hier von -9 auf - 11 ändern.
		Process process = new ProcessBuilder(pathToJar.substring(0, pathToJar.length() - 9) + "\\Release\\greedy_projection.exe", pfad,
				Settings.maxAbstandPunkte + "", Settings.nearestNeighborDistance + "").start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		System.out.println("Output is generating");
		System.out.println(Settings.maxAbstandPunkte + " " + Settings.nearestNeighborDistance);
		while ((line = br.readLine()) != null) {
			StringBuffer num = new StringBuffer();
			int count = 0;
			boolean isbuilding = false;
			for (int i = 0; i < line.length(); i++) {
				if ((line.charAt(i) == ',' || line.charAt(i) == 'T') && isbuilding) {
					vertices.get(count - 1).add(Integer.parseInt(num.toString()));
					num.delete(0, num.length());
					if (line.charAt(i) == 'T') {
						isbuilding = false;
					}
				} else if (line.charAt(i) == ':') {
					isbuilding = true;
					vertices.add(new ArrayList<Integer>());
					count++;

				} else if (isbuilding) {
					num.append(line.charAt(i));
					System.out.println(num);
				}
				if (i == (line.length() - 1)) {
					vertices.get(count - 1).add(Integer.parseInt(num.toString()));
					num.delete(0, num.length());
				}
			}
		}
		System.out.println("Output is finished");
		return vertices;
	}

	public static void main2(String[] args) {
		ArrayList<Vertex> order = null;
		// FileGeneratorPCD.executExternalProgram();
		order = FileGeneratorPCD.createFileTest(null);
		for (Vertex i : order) {
			System.out.println(i.getX() + " " + i.getY() + " " + i.getZ());
		}
		// VerticalVerticesGeneration vc = new VerticalVerticesGeneration();
		// createFile(vc.testVerts());
	}

	public static void main(String[] args) {
		ArrayList<ArrayList<Integer>> order = null;
		// FileGeneratorPCD.executExternalProgram();
		try {
			order = retrieveValuesFromExternalProgram();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ArrayList<Integer> list : order) {
			for (Integer i : list) {
				System.out.println(i);
			}
		}
		// VerticalVerticesGeneration vc = new VerticalVerticesGeneration();
		// createFile(vc.testVerts());
	}
}
