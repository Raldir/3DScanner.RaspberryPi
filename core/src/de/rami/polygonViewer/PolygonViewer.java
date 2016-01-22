package de.rami.polygonViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;








import java.util.Collection;

import java.util.Collections;

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


/**
 * Erstellung der Umgebung in LibGDX zum Darstellen des Objektes in einer GUI.
 * @author Rami Aly, Anton von Welzien, Andre Schurat
 *
 */
public class PolygonViewer implements ApplicationListener {
	
	public static ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance instance;
	public Material material = null;
	public ModelBuilder modelBuilder = null;
	
	public ArrayList<ArrayList<Vec2>> pictureData = new ArrayList<>();
	public static ArrayList<File> bilder = new ArrayList<>();
	public int messuretype = 0;
	PicturePointsAnalyser ppA;
	
	/**
	 * Intiialisierung der Anwendung: Einrichten der libgdx Umgebung, starten des Servers
	 */
	@Override
	public void create() {
		//Erstellung einer Umgebung, um die Tiefe des Objektes sichtbar zu machen.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.35f, 0.35f, 0.35f, 0.5f, -0.8f, 0.8f));
		//Erstellung der ModellBatch, zur welche sp�ter das zu rendernde Objekt hinzugef�gt wird.
		modelBatch = new ModelBatch();
		//Kamera wird erstellt und ihre Eigenschaften werden festgelegt.
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		//Farbe des Objektes wird festgelegt
		material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		//Der Modellbuilder hilft bei der Erstellung des Objektes
        modelBuilder = new ModelBuilder();
        //Kamera f�r den Nutzer steuerbar machen
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
//        setupModel(VerticesGenerationTest.testVerts2());
        setupServer();
	}
	
	/**
	 * Server wird erstellt, um die Bilddaten zu empfangen. Des Weiteren wird festgelegt, was auf dem Server mit den Bild-
	 * daten passieren soll.
	 */
	public void setupServer(){
        try {
			Server server = new Server(1234);
			server.setReceiveAction((File f) -> {
				//Die aus dem Bild ausgewerten Punkte werden abgespeichert
				bilder.add(f);
				if(messuretype == 0){
					ppA = new VerticalAnalyser(f);
					System.out.println(ppA.getClass().getName());
					pictureData.add(ppA.getPunkte());
				}else{
					ppA = new HorizontalAnalyser(f);
					pictureData.add(ppA.getPunkte());
				}
			});
			server.setCloseAction(() -> {
				if(messuretype == 0){
					setupModel(new VerticalVerticesGeneration().genVertices(pictureData));
				}else{
					setupModel(new HorizontalVerticesGeneration().genVertices(pictureData));
				}

			});
			server.listen();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Das Objekt wird mit den �bergebenen Eckpunkten erstellt und in der Umgebung dargestellt
	 * 
	 * @param verts Eckpunkte des Objektes
	 */
	public void setupModel(ArrayList<Vertex> verts){
		vertices = verts;
		Mesh mesh = new Mesh(true, 100000, 2000000, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		float[] vertices =  Vertices3DGeneration.genVertexAndNormalArray(verts);
        //Berechnung der Polygone des Modells
		short[] indices = Vertices3DGeneration.readTriangleIndicies(verts);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        modelBuilder.begin();
        modelBuilder.part("asdf", mesh, GL20.GL_TRIANGLES, material);
        model = modelBuilder.end();
        model.meshes.reverse();
        instance = new ModelInstance(model);
        setCameraPosition(verts);
	}
	
	public void setCameraPosition(ArrayList<Vertex> verts){
        int xd = 0;
        int yd = 0;
        int zd = 0;
        for(Vertex v : verts){
        	xd += v.getX();
        	yd += v.getY();
        	zd += v.getZ();
        }
        xd /= verts.size();
        yd /= verts.size();
        zd /= verts.size();
        cam.lookAt(xd, yd, zd);
		cam.position.add(0, 0, 0);
	}
	
	public void renderNewModell(){
		pictureData.clear();
		ArrayList<Vertex> list = new ArrayList<Vertex>();
		for(int i = 0 ; i < bilder.size(); i++){
			if(ppA.getClass().getName() == "de.rami.polygonViewer.VerticalAnalyser"){
				System.out.println("hello");
				pictureData.add(new VerticalAnalyser(bilder.get(i)).getPunkte());
				list = new VerticalVerticesGeneration().genVertices(pictureData);
			}else{
				pictureData.add(new HorizontalAnalyser(bilder.get(i)).getPunkte());
				list = new HorizontalVerticesGeneration().genVertices(pictureData);
			}
		}
		setupModel(list);
		setCameraPosition(list);
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
		model.dispose();
		renderNewModell();
	}
}