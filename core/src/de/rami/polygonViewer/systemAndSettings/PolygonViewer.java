package de.rami.polygonViewer.systemAndSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.rami.polygonViewer.materials.Vec2;
import de.rami.polygonViewer.materials.Vertex;
import de.rami.polygonViewer.modelGenerator.HorizontalVerticesGeneration;
import de.rami.polygonViewer.modelGenerator.VerticalVerticesGeneration;
import de.rami.polygonViewer.modelGenerator.Vertices3DGeneration;
import de.rami.polygonViewer.pictureAnalyser.HorizontalAnalyser;
import de.rami.polygonViewer.pictureAnalyser.PicturePointsAnalyser;
import de.rami.polygonViewer.pictureAnalyser.VerticalAnalyser;
import de.rami.polygonViewer.serverSystem.Server;

/**
 * Erstellung der Umgebung in LibGDX zum Darstellen des Objektes in einer GUI.
 * 
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

	public static HashMap<Integer, ArrayList<Vec2>> pictureData = new HashMap<Integer, ArrayList<Vec2>>();
	public static ArrayList<File> bilder = new ArrayList<>();
	public int messuretype = 0; // Sp�ter vom Benutzer einstellbar, um
								// horizontale oder vertikale Messung bestimmen
								// zu k�nnen
	static PicturePointsAnalyser ppA;
	static Vertices3DGeneration vca;
	static int bildercount = 0;
	
	private Stage stage;

	/**
	 * Intiialisierung der Anwendung: Einrichten der libgdx Umgebung, starten
	 * des Servers
	 */
	@Override
	public void create() {
		// Erstellung einer Umgebung, um die Tiefe des Objektes sichtbar zu
		// machen.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.35f, 0.35f, 0.35f, 0.5f, -0.8f, 0.8f));
		// Erstellung der ModellBatch, zur welche sp�ter das zu rendernde Objekt
		// hinzugef�gt wird.
		modelBatch = new ModelBatch();
		// Kamera wird erstellt und ihre Eigenschaften werden festgelegt.
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
//		stage = new Stage();
//		BitmapFont font = new BitmapFont();
//		Skin skin = new Skin();
//		TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("images/Button.pack"));
//		skin.addRegions(buttonAtlas);
//		TextButtonStyle textButtonStyle = new TextButtonStyle();
//		textButtonStyle.font = font;
//		textButtonStyle.up = skin.getDrawable("black");
//		textButtonStyle.down = skin.getDrawable("black");
//		textButtonStyle.checked = skin.getDrawable("black");
//		TextButton button = new TextButton("Refresh", textButtonStyle);
//		stage.addActor(button);
//		button.setBounds(0, 0, 50, 50);
//		button.addListener(new ChangeListener(){
//
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				model.dispose();
//				renderNewModell();
//				
//			}
//			
//		});
		// Farbe des Objektes wird festgelegt
		material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
		// Der Modellbuilder hilft bei der Erstellung des Objektes
		modelBuilder = new ModelBuilder();
		// Kamera f�r den Nutzer steuerbar machen
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);
		// setupModel(VerticesGenerationTest.testVerts2());
		setupServer();
	}

	/**
	 * Server wird erstellt, um die Bilddaten zu empfangen. Des Weiteren wird
	 * festgelegt, was auf dem Server mit den Bild- daten passieren soll.
	 */
	public void setupServer() {
//		vca = new VerticalVerticesGeneration();
//		setupModel(vca.genVertices(null));
		try {
			Server server = new Server(1234);
			server.setReceiveAction((File f) -> {
				System.out.println("pictureReceived");
				// Die aus dem Bild ausgewerten Punkte werden abgespeichert
				if (messuretype == 0) {
					ppA = new VerticalAnalyser(f, pictureData.get(bildercount - 1));
					System.out.println("points calculated");
					if (ppA.getPunkte().size() > 0) {
						System.out.println(ppA.getClass().getName());
						pictureData.put(bildercount, ppA.getPunkte());
						bildercount++;
						bilder.add(f);
					}
				} else {
					ppA = new HorizontalAnalyser(f, pictureData.get(bildercount - 1));
					pictureData.put(bildercount, ppA.getPunkte());
					;
				}
			});
			server.setCloseAction(() -> {
				if (messuretype == 0) {
					vca = new VerticalVerticesGeneration();

				} else {
					vca = new HorizontalVerticesGeneration();
				}
				ArrayList<ArrayList<Vec2>> allpoints = new ArrayList<ArrayList<Vec2>>();
				System.out.println(bildercount);
				for (int i = 0; i < bildercount; i++) {
					allpoints.add(pictureData.get(i));
				}
				if (allpoints.size() == 0) {
					System.out.println("Keine Punkte gefunden");
				}
				setupModel(vca.genVertices(allpoints));
				server.closeServer();
			});
			server.listen();
		} catch (

		IOException e) {
			e.printStackTrace();
		}

	}

	public void refresh(){
		
		Gdx.app.postRunnable(new Runnable() {
	         @Override
	         public void run() {
	     		model.dispose();
	    		renderNewModell();
	         }
	});
	}
	/**
	 * Das Objekt wird mit den �bergebenen Eckpunkten erstellt und in der
	 * Umgebung dargestellt
	 * 
	 * @param verts
	 *            Eckpunkte des Objektes
	 */
	public void setupModel(ArrayList<Vertex> verts) {
		vertices = verts;
		Mesh mesh = new Mesh(true, 100000, 2000000,
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
				new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		float[] vertices = vca.genVertexAndNormalArray(verts);
		// Berechnung der Polygone des Modells
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

	public void setCameraPosition(ArrayList<Vertex> verts) {
		int xd = 0;
		int yd = 0;
		int zd = 0;
		for (Vertex v : verts) {
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

	/**
	 * Bei der Neuberechnung wird Multithreading verwendet, um den
	 * Berechnungsvorgang zu beschleunigen.
	 */
	public void renderNewModell() {
		ArrayList<Vertex> list = new ArrayList<Vertex>();
		pictureData.clear();
		int threadnumber = bilder.size();
		if (threadnumber > 100) {
			threadnumber = 100;
		}
		ExecutorService executor = Executors.newFixedThreadPool(threadnumber);
		for (int i = 0; i < bilder.size(); i++) {
			int bildnummer = i;
			Runnable worker = new MyRunnable(bildnummer);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("\nFinished all threads");
		ArrayList<ArrayList<Vec2>> allpoints = new ArrayList<ArrayList<Vec2>>();
		for (int i = 0; i < bildercount; i++) {
			if (pictureData.get(i) != null && pictureData.get(i).size() > 0) {
				allpoints.add(pictureData.get(i));
			}else{
				System.out.println("Woops...Ein Bild ist nicht verwendbar");
			}
		}
		list = new VerticalVerticesGeneration().genVertices(allpoints);
		setupModel(list);
		setCameraPosition(list);
	}

	public static class MyRunnable implements Runnable {
		private final int bildnummer;

		MyRunnable(int bildnummer) {
			this.bildnummer = bildnummer;
		}

		@Override
		public void run() {
			ArrayList<Vec2> punkte = new VerticalAnalyser(bilder.get(bildnummer), pictureData.get(bildnummer - 1)).getPunkte();
			pictureData.put(bildnummer, punkte);}
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
		if (instance != null) {
			modelBatch.render(instance, environment);
		}
		modelBatch.end();
//		stage.act();
//		stage.draw();
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