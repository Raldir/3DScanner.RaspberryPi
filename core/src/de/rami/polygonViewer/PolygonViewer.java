package de.rami.polygonViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;






import javax.imageio.ImageIO;
import javax.management.RuntimeErrorException;





import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.rami.polygonViewer.Bildpunkte.Line;

public class PolygonViewer implements ApplicationListener {
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	public Material material = null;
	public ModelBuilder modelBuilder = null;
	public Server server = null;
	public ArrayList<ArrayList<Vec2>> pictureData = new ArrayList<>();
	public int pictureCounter = 0;
	public boolean readHoehe = true;
	public Bildpunkte.Line l = new Bildpunkte.Line();
	public ArrayList<File> bilder = new ArrayList<>();
	
	/**
	 * Intiialisierung der Anwendung: Einrichten der libgdx Umgebung, starten des Servers
	 */
	@Override
	public void create() {
		//Erstellung einer Umgebung, um die Tiefe des Objektes sichtbar zu machen.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.35f, 0.35f, 0.35f, 0.5f, -0.8f, 0.8f));
		//Erstellung der ModellBatch, zur welche später das zu rendernde Objekt hinzugefügt wird.
		modelBatch = new ModelBatch();
		//Kamera wird erstellt und ihre Eigenschaften werden festgelegt.
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		//Farbe des Objektes wird festgelegt
		material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		//Der Modellbuilder hilft bei der Erstellung des Objektes
        modelBuilder = new ModelBuilder();
        //Kamera für den Nutzer steuerbar machen
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
        setupServer();
	}
	
	/**
	 * Server wird erstellt, um die Bilddaten zu empfangen. Des Weiteren wird festgelegt, was auf dem Server mit den Bild-
	 * daten passieren soll.
	 */
	public void setupServer(){
        try {
			server = new Server(1234);
			server.setReceiveAction((File f) -> {
				if(readHoehe){
					l = Bildpunkte.getHoehe(f);
					if(l.y1 - l.y2 < Bildpunkte.bildskalierung){
						System.out.println("der ausgewählte Schewellenwert ist zu hoch oder das Bild zu dunkel");
					}
					readHoehe = false;
				}
				bilder.add(f);
//				int number = pictureCounter;
				pictureCounter++;
				//Die aus dem Bild ausgewerten Punkte werden abgespeichert
				ArrayList<Vec2> points = Bildpunkte.getGesamtPoints(f, l);
				
//				if(pictureData.size() == number){
					pictureData.add(points);
//				}
			});
			server.setCloseAction(() -> {
				setupModel(VerticesGeneration.genVerticesTest(pictureData));
			});
			server.listen();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Das Objekt wird mit den übergebenen Eckpunkten erstellt und in der Umgebung dargestellt
	 * 
	 * @param verts Eckpunkte des Objektes
	 */
	public void setupModel(ArrayList<Vertex> verts){
		Mesh mesh = new Mesh(true, 100000, 2000000, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		float[] vertices =  VerticesGeneration.genVertexAndNormalArray(verts);
        //Berechnung der Polygone des Modells
		short[] indices = VerticesGeneration.readTriangleIndices(verts);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        modelBuilder.begin();
        modelBuilder.part("asdf", mesh, GL20.GL_TRIANGLES, material);
        model = modelBuilder.end();
        instance = new ModelInstance(model);
	}
	
	/**
	 * Rendern des Models auf der Batch und aktualisieren der Kamera.
	 */
	@Override
	public void render() {
		camController.update();
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(cam);
        if(instance != null){
        	modelBatch.render(instance, environment);
        }
        modelBatch.end();
	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}