package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.rami.polygonViewer.desktop.DesktopLauncher;
import de.rami.polygonViewer.modelGenerator.FileCreator;
import de.rami.polygonViewer.serverSystem.Exec;
import de.rami.polygonViewer.systemAndSettings.PolygonViewer;
import de.rami.polygonViewer.systemAndSettings.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author Anton und Rami
 *
 */
public class UIController implements Initializable  {
	
	@FXML
	private Button refreshButton, startButton, helpButton, resetSliderButton, SettSaveButton;
	@FXML 
	private SplitMenuButton qualityChoose, dauerChoose;
	@FXML
	private CheckMenuItem abnormalHigh, ultraHigh, veryHigh, high, medium, low, highd, mediumd, lowd;
	@FXML
	private TextFlow helpText;
	@FXML
	private Text lastCalibrationDate, actiontarget;
	@FXML 
	private TextField ipField;
	@FXML
	private Slider obererSchwellenWert, skalierungswertX, skalierungswertY, bildskalierung, polygonAnzahl, grunddicke;
	@FXML
	private Text oswVal, swXVal, swYVal, bildsVal, polygonAnzahlT;
	@FXML
	private Spinner<Integer> oswSpin,  polyAnSpin;
	@FXML
	private Spinner<Double> bildSkalSpin , glaetBlenderSpin, skalXSpin, skalYSpin;
	@FXML
	private MenuItem closeButt, saveAs, saveSettings, loadSettings, howToUse, about;
//	@FXML
//	private PasswordField passwordField;
	//Diese Loesung sind vorlaufig und haengen von der saeteren umsetzung der uebergabe ab
	
	public static int bilderZahl = Settings.anzahlbilder;
	public static int beleuchtungsDauer = 0;
	//final??
	private float[] backupValues = {Settings.obererSchwellenWert, Settings.maxAbstandPunkte, Settings.nearestNeighborDistance, Settings.bildskalierung, Settings.polygonAnzahl, Settings.grunddicke};
	private PreferencesSaver prefsSaver; 
	private String qualitySelected, dauerSelected, ip;
	private CheckMenuItem selectedMenuItem, dselectedMenuItem;
	private Main main;
	private PolygonViewer pv;
	private LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	//fueeeeeer ANTON

	
	public void setMain(Main main){
		this.main = main;
	}
	
	/** http://java-buddy.blogspot.de/2012/05/save-file-with-javafx-filechooser.html
	 * http://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
	 * Werde bei gelegeneiht noch default namen oder sowas rumwerkeln. Standart speicherort usw.
	 * @param e
	 */
	@FXML
	private void hadleSaveAs(ActionEvent e){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save As...");
		  
        //Set extension filter. Sorgt dafür das die richitige endung vorgegben ist
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Blender Datei", "*.obj");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File filePath = fileChooser.showSaveDialog(Main.stage);
        
        if(filePath != null){
        	//System.out.println(filePath);
        	System.out.println(filePath.toString());
        	FileCreator.pfad = filePath.toString();
        	FileCreator.createFile(PolygonViewer.vertices);
        }
    }
	
	/**
	 * I have excluded this from SettingsSave() because it might be useful elsewhere later on...
	 * @param fxmlName
	 * @param width
	 * @param height
	 * @param title
	 * @throws IOException
	 */
	private void showNewDialog(String fxmlName, int width, int height, String title) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setWidth(width);
        dialogStage.setHeight(height);
        dialogStage.setResizable(false);
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(Main.stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        dialogStage.setScene(scene);
        dialogStage.showAndWait();   
	}
	
	@FXML 
	private void handleSettingsSave(ActionEvent e) throws IOException{
		showNewDialog("Save_Settings.fxml", 286, 130,"Eintstellung speichern" );
	}
	
	/**
	 * this opens the Settings selector via the main class
	 * @param e
	 * @throws IOException
	 */
	@FXML
	private void handleSettingsLoad(ActionEvent e) throws IOException{
		main.settingsSelector();
	}
	
	public void setUpSliderFromSaved(SettingsSaver savedSetts){
		//Dank des changelisteners und der settValueSet verknuepfung reicht es, die slider zu aendern....
		System.out.println("obschwell"+savedSetts.getObererSchwellenWert());
		obererSchwellenWert.setValue(savedSetts.getObererSchwellenWert());
		System.out.println("obschwell"+savedSetts.getObererSchwellenWert());
		skalierungswertX.setValue(savedSetts.getSkalierungswertX());
		System.out.println("sX"+savedSetts.getSkalierungswertX());
		skalierungswertY.setValue(savedSetts.getSkalierungswertY());
		System.out.println("sy"+savedSetts.getSkalierungswertY());
		bildskalierung.setValue(savedSetts.getBildskalierung());
		polygonAnzahl.setValue(savedSetts.getPolygonAnzahl());
		//HIER GLAETTUNG falls was nicht funzt
		grunddicke.setValue(savedSetts.getgrunddicke());
	}
		
	/**
	 * Hacky solution for the quality selector, which makes it sure only one quality can be selectet at
	 * a time. The selected quality is stored as a String in the global variable qualitySelected for a still
	 * unsure purpose 
	 * @param e
	 */
	@FXML
	private void hadleQualitySelect(ActionEvent e){
		Object source = e.getSource();
		selectedMenuItem = (CheckMenuItem) source;
		abnormalHigh.setSelected(false);
		ultraHigh.setSelected(false);
		veryHigh.setSelected(false);
//		high.setSelected(false);
//		medium.setSelected(false);
//		low.setSelected(false);
		selectedMenuItem.setSelected(true);
		qualitySelected = selectedMenuItem.getId();
		switch (qualitySelected.toLowerCase().charAt(0)){
			case 'l': bilderZahl = 16;
			System.out.println("hello777");
			break;
			case 'm': bilderZahl = 32;
			System.out.println("hello123");
			break;
			case 'h': bilderZahl = 64;
			break;
			case 'v': bilderZahl = 128;
			break;
			case 'u': bilderZahl = 256;
			break;
			case 'a': bilderZahl = 512;
			break;	
		}
		System.out.println(bilderZahl);
		qualityChoose.setText("Qualit\u00E4t: "+ selectedMenuItem.getText());
		//System.out.println(qualitySelected + " Qualität Selektiert");	
		System.out.println(selectedMenuItem.getText() + qualitySelected );
		// Btw \u00E4 das ist ein kleines ä in unicode http://www.bennyn.de/programmierung/java/umlaute-und-sonderzeichen-in-java.html
	}
	
	/**
	 * Hacky solution for the quality selector, which makes it sure only one quality can be selectet at
	 * a time. The selected quality is stored as a String in the global variable qualitySelected for a still
	 * unsure purpose 
	 * @param e
	 */
	@FXML
	private void hadleDauerSelect(ActionEvent e){
		Object source = e.getSource();
		dselectedMenuItem = (CheckMenuItem) source;
		high.setSelected(false);
		medium.setSelected(false);
		low.setSelected(false);
		dselectedMenuItem.setSelected(true);
		dauerSelected = dselectedMenuItem.getId();
		switch (dauerSelected.toLowerCase().charAt(0)){
			case 'l': beleuchtungsDauer = 0;
			System.out.println("hello777");
			break;
			case 'm': beleuchtungsDauer = 1;
			System.out.println("hello123");
			break;
			case 'h': beleuchtungsDauer = 2;
			break;
		}
		System.out.println(beleuchtungsDauer);
		dauerChoose.setText("Beleuchtung: "+ dselectedMenuItem.getText());
		//System.out.println(qualitySelected + " Qualität Selektiert");	
		// Btw \u00E4 das ist ein kleines ä in unicode http://www.bennyn.de/programmierung/java/umlaute-und-sonderzeichen-in-java.html
	}
	
	/**
	 * Handles the Start Button so that it changes the text from  Start to Abbrechen and back.
	 * @param e
	 */
	@FXML
	private void handleStart(ActionEvent e){
		
	
		if(startButton.getText() == "Start"){
			pv = new PolygonViewer();
			config.x = Main.xPos;
			config.y = Main.yPos;
			new LwjglApplication(pv, config);
			try {
				Settings.anzahlbilder = bilderZahl;
				Exec.connectfromPItoServer(bilderZahl, beleuchtungsDauer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//this will never be executed or maybe at he wrong time. Needs to be reworked.
			startButton.setText("Abbrechen");
		}
		else{
			startButton.setText("Start");
		}
	}
	
	@FXML
	private void handleHelp(ActionEvent e) throws IOException{
		showNewDialog("HelpUI.fxml", 300, 400, "Hilfe" );
	}
	
	@FXML
	private void handleRefresh(ActionEvent e) throws IOException{
		config.x = Main.xPos;
		config.y = Main.yPos;
		pv.refresh();
	}
	
	@FXML protected void handleSubmitButtonAction(ActionEvent event) {
        ip  = ipField.getText().toString();
        prefsSaver.setPerfString("ip", ip);
		Settings.raspberryIP = prefsSaver.getPref("ip", Settings.raspberryIP);
        System.out.println(Settings.raspberryIP);
    }
	
	private void setUpSpinnerInt(Spinner<Integer> spinner, int pos, int min, int max, int increment, int savedSet ){
		IntegerSpinnerValueFactory oswFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, savedSet, increment);
		spinner.setValueFactory(oswFactory);
		spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
	    System.out.println("New value: "+newValue);
	    // hier könnte es rundungsfehler von double auf Number geben
	    setValueSettings(pos, newValue);
		});
	}
	
	private void setUpSpinnerDouble(Spinner<Double> spinner, int pos, double min, double max, double increment, double savedSet){
		DoubleSpinnerValueFactory oswFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, savedSet, increment);
		spinner.setValueFactory(oswFactory);
		spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
	    System.out.println("New value: "+newValue);
	    // hier könnte es rundungsfehler von double auf Number geben
	    setValueSettings(pos, newValue);
		});
		
	}

	/**
	 * Sets up one Slider and is used multiple times to do it for more sliders. Also handles what the sliders do
	 * TODO perhaps add the same parameters like setUpSpinner() to have save min and max because while the values stick to the 
	 * rules of setUpSpinner it can show funny stuff if not correctly done in FXML
	 * @param slider
	 * @param val
	 * @param pos
	 */
	private void setUpSlider(Slider slider, int pos, double savedSet){
		slider.setValue(savedSet);
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
    	    System.out.println("Slider Value Changed (newValue in Integer: " + newValue.intValue() + ")");
    	    setValueSettings(pos, newValue);
    	});
		
	}
	
	private void setValueSettings(int pos, Number newValue) {
		switch(pos){
	    case 0: Settings.obererSchwellenWert = newValue.intValue();
	    		prefsSaver.setPerf("obererSchwellenWert", newValue.intValue());
	    		this.obererSchwellenWert.setValue(newValue.doubleValue());
	    		this.oswSpin.getValueFactory().setValue(newValue.intValue());
	    break;
	    case 1: Settings.maxAbstandPunkte = newValue.intValue();
	    		prefsSaver.setPerf("skalierungswertX", newValue.intValue());
	    		this.skalierungswertX.setValue(newValue.doubleValue());
	    		this.skalXSpin.getValueFactory().setValue((double) newValue.floatValue());
	    break;
	    case 2: Settings.nearestNeighborDistance = newValue.intValue();
	    		prefsSaver.setPerf("skalierungswertY", newValue.intValue());
	    		this.skalierungswertY.setValue(newValue.doubleValue());
	    		this.skalYSpin.getValueFactory().setValue((double) newValue.floatValue());
	    break;
	    case 3: 
	    		Settings.bildskalierung = newValue.floatValue();
	    		prefsSaver.setPerfFloat("bildskalierung", newValue.floatValue());
	    		this.bildskalierung.setValue(newValue.doubleValue());
	    		this.bildSkalSpin.getValueFactory().setValue((Double) newValue);
	    break;
	    case 4: 
	    		Settings.polygonAnzahl = newValue.intValue();
	    		prefsSaver.setPerf("polygonAnzahl", newValue.intValue());
	    		this.polygonAnzahl.setValue(newValue.doubleValue());
	    		this.polyAnSpin.getValueFactory().setValue(newValue.intValue());
	    break;
	    case 5:
	    		//HIER Grunddicke falls etwas nicht funtzt // Rami eddited von Gl�ttungsfaktor
	    		Settings.grunddicke = newValue.floatValue();
	    		prefsSaver.setPerfFloat("grunddicke", newValue.floatValue());
	    		this.grunddicke.setValue(newValue.doubleValue());
	    		this.glaetBlenderSpin.getValueFactory().setValue((Double) newValue);
	    break;
	    }	
	}

	private void setUpSettings(){
		//Spinner muessen zu erst erstellt werden weil Property Factory erstellt werden muss.. zur sicherheit..
		setUpSpinnerInt(oswSpin, 0, 0, 255, 1, prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert));
		setUpSpinnerDouble(skalXSpin, 1, 0.02, 1000, 0.01, prefsSaver.getPrefFloat("skalierungswertX", Settings.maxAbstandPunkte));
		setUpSpinnerDouble(skalYSpin, 2, 1, 1000, 1, prefsSaver.getPrefFloat("skalierungswertY", Settings.nearestNeighborDistance));
		setUpSpinnerDouble(bildSkalSpin, 3, 0.1, 100, 0.01, prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung));
		setUpSpinnerInt(polyAnSpin, 4, 100, 100000, 1, prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl));
		//HIER GLAETTUNG falls was nicht funzt
		setUpSpinnerDouble(glaetBlenderSpin, 5, 0.1, 100, 0.2, prefsSaver.getPrefFloat("grunddicke", Settings.grunddicke));
		
		setUpSlider(obererSchwellenWert, 0, prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert));
		setUpSlider(skalierungswertX, 1, prefsSaver.getPrefFloat("skalierungswertX", Settings.maxAbstandPunkte));
		setUpSlider(skalierungswertY, 2, prefsSaver.getPrefFloat("skalierungswertY", Settings.nearestNeighborDistance));
		setUpSlider(bildskalierung, 3, prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung));
		setUpSlider(polygonAnzahl, 4, prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl));
		//HIER GLAETTUNG falls was nicht funzt --again eddited by Rami zu grunddicke
		setUpSlider(grunddicke, 5, prefsSaver.getPrefFloat("grunddicke", Settings.grunddicke));
		
		Settings.obererSchwellenWert = prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert);
		Settings.maxAbstandPunkte = prefsSaver.getPrefFloat("skalierungswertX", Settings.maxAbstandPunkte);
		Settings.nearestNeighborDistance = prefsSaver.getPrefFloat("skalierungswertY", Settings.nearestNeighborDistance);
		Settings.bildskalierung = prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung);
		Settings.polygonAnzahl = prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl);
		//HIER GLAETTUNG falls was nicht funzt --again eddited by Rami zu grunddicke
		Settings.grunddicke = prefsSaver.getPrefFloat("grunddicke", Settings.grunddicke);
		Settings.raspberryIP = prefsSaver.getPref("ip", Settings.raspberryIP);
	}
	
	/**
	 * Handles the slider reset button. The setValue() method also fires the change listener that is attached to the buttons.
	 * therefore the everything should be changed as usual
	 * @param e
	 */
	@FXML 
	private void handleSliderReset(ActionEvent e){
		//Dank des changelisteners und der settValueSet verknuepfung reicht es, die slider zu aendern....
		obererSchwellenWert.setValue(backupValues[0]);
		skalierungswertX.setValue(backupValues[1]);
		skalierungswertY.setValue(backupValues[2]);
		bildskalierung.setValue(backupValues[3]);
		polygonAnzahl.setValue(backupValues[4]);
		//HIER GLAETTUNG falls was nicht funzt
		grunddicke.setValue(backupValues[5]);
	}
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//Auf grund massiver Spackungen muss der Text vom Knopf hier gesetzt werden, das sonst die if nicht funzt.
    	startButton.setText("Start");
    	helpButton.setTooltip(new Tooltip("Hilfe"));
    	refreshButton.setText("Refresh");
    	//Inizialisiert den QualityChooser, aber irgendwie umstaendlich...
    	ultraHigh.setSelected(true);
    	selectedMenuItem = ultraHigh;
    	qualitySelected = ultraHigh.getId();
    	qualityChoose.setText("Qualit\u00E4t: "+ selectedMenuItem.getText());
//    	dselectedMenuItem = lowd;
//    	dauerSelected = lowd.getId();
//    	dauerChoose.setText("Beleuchtung: "+ dselectedMenuItem.getText());
//    	System.out.println(selectedMenuItem.getText() + qualitySelected );
//    	System.out.println("haaaaaloooo");
    	prefsSaver = new PreferencesSaver();
    	setUpSettings();
    } 
    
    /**
     * Allows another Class to make an Alert. No idea if useful and properly done.
     * @param title
     * @param content
     */
    public static void alertGeneral(String title, String content){
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		alert.showAndWait();
    }
}
