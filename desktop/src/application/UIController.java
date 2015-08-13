package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.rami.polygonViewer.Exec;
import de.rami.polygonViewer.FileCreator;
import de.rami.polygonViewer.PolygonViewer;
import de.rami.polygonViewer.Settings;
import de.rami.polygonViewer.desktop.DesktopLauncher;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UIController implements Initializable  {
	
	@FXML
	private Button kalibrierenButton, startButton, helpButton, resetSliderButton;
	@FXML 
	private SplitMenuButton qualityChoose;
	@FXML
	private CheckMenuItem high, medium, low;
	@FXML
	private TextFlow helpText;
	@FXML
	private Text lastCalibrationDate;
	@FXML
	private Slider obererSchwellenWert, skalierungswertX, skalierungswertY, bildskalierung, polygonAnzahl, glaettungBlender;
	@FXML
	private Text oswVal, swXVal, swYVal, bildsVal, polygonAnzahlT;
	@FXML
	private Spinner<Integer> oswSpin, skalXSpin, skalYSpin, polyAnSpin, glaetBlenderSpin;
	@FXML
	private Spinner<Double> bildSkalSpin;
	@FXML
	private MenuItem closeButt, saveAs, saveSettings, loadSettings, howToUse, about;
	
	//Diese Loesung sind vorlaufig und haengen von der saeteren umsetzung der uebergabe ab
	
	public static int bilderZahl = 8;
	//final??
	private float[] backupValues = {Settings.obererSchwellenWert, Settings.skalierungswertX, Settings.skalierungswertY, Settings.bildskalierung, Settings.polygonAnzahl, FileCreator.glaettungsfaktor};
	private PreferencesSaver prefsSaver; 
	private String qualitySelected;
	private CheckMenuItem selectedMenuItem;
	private SettingsSaver savedSetts;
	
	/**
	 * 
	 * kuemmert sich um das speiche. sollt so funzen kann ich aber nicht checken. Man kann sicherlich noch verfeinerer
	 * Also bei mir erstellts sogar ein leres dokument grade gemerkt =)
	 * http://java-buddy.blogspot.de/2012/05/save-file-with-javafx-filechooser.html
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
	
	@FXML 
	private void handleSettingsSave(ActionEvent e){
		System.out.println("in the function");
		this.savedSetts = new SettingsSaver(FileCreator.glaettungsfaktor, Settings.obererSchwellenWert, Settings.skalierungswertX, Settings.skalierungswertY, Settings.polygonAnzahl, Settings.bildskalierung);
		savedSetts.saveSettings("name");
		System.out.println("done with method");
		
	}
	
	@FXML
	private void handleSettingsLoad(ActionEvent e){
		String nameOfSave = "name";
		this.savedSetts = savedSetts.loadSettings(nameOfSave);
		System.out.println(savedSetts);
		System.out.println(savedSetts.getBildskalierung());
		savedSetts.systout();
		setUpSliderFromSaved(nameOfSave);
	}
	
	private void setUpSliderFromSaved(String nameOfSave){
		//Dank des changelisteners und der settValueSet verknuepfung reicht es, die slider zu aendern....
		obererSchwellenWert.setValue(savedSetts.getObererSchwellenWert());
		System.out.println("obschwell"+savedSetts.getObererSchwellenWert());
		skalierungswertX.setValue(savedSetts.getSkalierungswertX());
		System.out.println("sX"+savedSetts.getSkalierungswertX());
		skalierungswertY.setValue(savedSetts.getSkalierungswertY());
		System.out.println("sy"+savedSetts.getSkalierungswertY());
		bildskalierung.setValue(savedSetts.getBildskalierung());
		polygonAnzahl.setValue(savedSetts.getPolygonAnzahl());
		//HIER GLAETTUNG falls was nicht funzt
		glaettungBlender.setValue(savedSetts.getGlaettungsfaktor());
	}
		
	
	/**NOT Functional Yet
	 * handles the calibration provides a popup window with further information. Needs to be combined with acctual calibration...
	 * @param e
	 */
	@FXML
	private void handleCalibrate(ActionEvent e)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Kalibrierungsanleitung");
		alert.setHeaderText(null);
		alert.setContentText("Kalibriert wird wie folgt....");

		alert.showAndWait();
		
		//only proof of concept for now, however, might be worth thinking about.
		Date currentDate = new Date();
	    System.out.println(currentDate.toString());
	    lastCalibrationDate.setText(currentDate.toString());
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
		qualitySelected = selectedMenuItem.getId();
		high.setSelected(false);
		medium.setSelected(false);
		low.setSelected(false);
		selectedMenuItem.setSelected(true);
		switch (qualitySelected.toLowerCase().charAt(0)){
			case 'l': bilderZahl = 8;
			break;
			case 'm': bilderZahl = 16;
			break;
			case 'h': bilderZahl = 32;
		}
		System.out.println(bilderZahl);
		qualityChoose.setText("Qualit\u00E4t: "+ selectedMenuItem.getText());
		//System.out.println(qualitySelected + " Qualität Selektiert");	
		System.out.println(selectedMenuItem.getText() + qualitySelected );
		// Btw \u00E4 das ist ein kleines ä in unicode http://www.bennyn.de/programmierung/java/umlaute-und-sonderzeichen-in-java.html
	}
	/**
	 * Handles the Start Button so that it changes the text from  Start to Abbrechen and back.
	 * @param e
	 */
	@FXML
	private void handleStart(ActionEvent e){
		if(startButton.getText() == "Start"){
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			new LwjglApplication(new PolygonViewer(), config);
			try {
				Settings.anzahlbilder = bilderZahl;
				Exec.connectfromPItoServer(bilderZahl);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			startButton.setText("Abbrechen");
		}
		else{
			startButton.setText("Start");
		}
	}
	
	@FXML
	private void handleHelp(ActionEvent e) throws IOException{
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("HelpUI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setTitle("test");
        stage.setScene(scene);
        stage.show();
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
	    case 1: Settings.skalierungswertX = newValue.intValue();
	    		prefsSaver.setPerf("skalierungswertX", newValue.intValue());
	    		this.skalierungswertX.setValue(newValue.doubleValue());
	    		this.skalXSpin.getValueFactory().setValue(newValue.intValue());
	    break;
	    case 2: Settings.skalierungswertY = newValue.intValue();
	    		prefsSaver.setPerf("skalierungswertY", newValue.intValue());
	    		this.skalierungswertY.setValue(newValue.doubleValue());
	    		this.skalYSpin.getValueFactory().setValue(newValue.intValue());
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
	    		//HIER GLAETTUNG falls was nicht funzt
	    		FileCreator.glaettungsfaktor = newValue.intValue();
	    		prefsSaver.setPerf("glaettungsfaktor", newValue.intValue());
	    		this.glaettungBlender.setValue(newValue.doubleValue());
	    		this.glaetBlenderSpin.getValueFactory().setValue(newValue.intValue());
	    break;
	    }	
	}

	private void setUpSettings(){
		//Spinner muessen zu erst erstellt werden weil Property Factory erstellt werden muss.. zur sicherheit..
		setUpSpinnerInt(oswSpin, 0, 0, 255, 1, prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert));
		setUpSpinnerInt(skalXSpin, 1, 1, 100, 1, prefsSaver.getPref("skalierungswertX", Settings.skalierungswertX));
		setUpSpinnerInt(skalYSpin, 2, 1, 100, 1, prefsSaver.getPref("skalierungswertY", Settings.skalierungswertY));
		setUpSpinnerDouble(bildSkalSpin, 3, 0.1, 10, 0.01, prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung));
		setUpSpinnerInt(polyAnSpin, 4, 100, 10000, 1, prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl));
		//HIER GLAETTUNG falls was nicht funzt
		setUpSpinnerInt(glaetBlenderSpin, 5, 1, 10, 1, prefsSaver.getPref("glaettungsfaktor", FileCreator.glaettungsfaktor));
		
		setUpSlider(obererSchwellenWert, 0, prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert));
		setUpSlider(skalierungswertX, 1, prefsSaver.getPref("skalierungswertX", Settings.skalierungswertX));
		setUpSlider(skalierungswertY, 2, prefsSaver.getPref("skalierungswertY", Settings.skalierungswertY));
		setUpSlider(bildskalierung, 3, prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung));
		setUpSlider(polygonAnzahl, 4, prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl));
		//HIER GLAETTUNG falls was nicht funzt
		setUpSlider(glaettungBlender, 5, prefsSaver.getPref("glaettungsfaktor", FileCreator.glaettungsfaktor));
		
		Settings.obererSchwellenWert = prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert);
		Settings.skalierungswertX = prefsSaver.getPref("skalierungswertX", Settings.skalierungswertX);
		Settings.skalierungswertY = prefsSaver.getPref("skalierungswertY", Settings.skalierungswertY);
		Settings.bildskalierung = prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung);
		Settings.polygonAnzahl = prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl);
		//HIER GLAETTUNG falls was nicht funzt
		FileCreator.glaettungsfaktor = prefsSaver.getPref("glaettungsfaktor", FileCreator.glaettungsfaktor);
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
		glaettungBlender.setValue(backupValues[5]);
	}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//Auf grund massiver Spackungen muss der Text vom Knopf hier gesetzt werden, das sonst die if nicht funzt.
    	startButton.setText("Start");
    	helpButton.setTooltip(new Tooltip("Hilfe"));
    	//Inizialisiert den QualityChooser, aber irgendwie umstaendlich...
    	medium.setSelected(true);
    	selectedMenuItem = medium;
    	qualitySelected = medium.getId();
    	qualityChoose.setText("Qualit\u00E4t: "+ selectedMenuItem.getText());
    	System.out.println(selectedMenuItem.getText() + qualitySelected );
    	System.out.println("haaaaaloooo");
    	prefsSaver = new PreferencesSaver();
    	
    	
    	//setUpSliders();
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
