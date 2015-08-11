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
import javafx.scene.control.Slider;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
	private Slider obererSchwellenWert, skalierungswertX, skalierungswertY, bildskalierung, polygonAnzahl;
	@FXML
	private Text oswVal, swXVal, swYVal, bildsVal, polygonAnzahlT;
	
	//Diese Loesung sind vorlaufig und haengen von der saeteren umsetzung der uebergabe ab
	
	public static int bilderZahl = 8;
	private float[] backupValues = {Settings.obererSchwellenWert, Settings.skalierungswertX, Settings.skalierungswertY, Settings.bildskalierung, Settings.polygonAnzahl};
	private PreferencesSaver prefsSaver; 
	private String qualitySelected;
	private CheckMenuItem selectedMenuItem;
	/**NOT Functional Yet
	 * handles the calibration provides a popup window with further information. Needs to be combined with acctual calibration...
	 * @param e
	 */
	@FXML
	private void handleCalibrate(ActionEvent e)
	{
		FileCreator.createFile(PolygonViewer.vertices);
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
	
	/**
	 * Handles the slider reset button. The setValue() method also fires the change listener that is attached to the buttons.
	 * therefore the everything should be changed as usual
	 * @param e
	 */
	@FXML 
	private void handleSliderReset(ActionEvent e){
		obererSchwellenWert.setValue(backupValues[0]);
		skalierungswertX.setValue(backupValues[1]);
		skalierungswertY.setValue(backupValues[2]);
		bildskalierung.setValue(backupValues[3]);
		polygonAnzahl.setValue(backupValues[4]);
		oswVal.setText(String.valueOf(Settings.obererSchwellenWert));
		swXVal.setText(String.valueOf(Settings.skalierungswertX));
		swYVal.setText(String.valueOf(Settings.skalierungswertY));
		bildsVal.setText(String.valueOf(Settings.bildskalierung));
		polygonAnzahlT.setText(String.valueOf(Settings.polygonAnzahl));
		//System.out.println(Settings.obererSchwellenWert);
	}
	
	/**
	 * Sets up one Slider and is used multiple times to do it for more sliders. Also handles what the sliders do and sets
	 * their dependancys on other sliders. Two special cases being bereichsSkalierung a Bildskalierung. They resive a warning 
	 * when set incorrectly but it might not even reach their maximum when scrolled quickly. Might be smart to set them to their
	 * max-possible values. Need to think on that =)
	 * @param slider
	 * @param val
	 * @param pos
	 */
	private void setUpSlider(Slider slider, Text val, int pos){
		slider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
			System.out.println(wasChanging);
		});
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
    	    System.out.println("Slider Value Changed (newValue: " + newValue.intValue() + ")");
    	    switch(pos){
    	    case 0: Settings.obererSchwellenWert = newValue.intValue();
    	    		val.setText(String.valueOf(newValue.intValue()));
    	    		prefsSaver.setPerf("obererSchwellenWert", newValue.intValue());
    	    		
    	    break;
    	    case 1: Settings.skalierungswertX = newValue.intValue();
    	    		val.setText(String.valueOf(newValue.intValue()));
    	    		prefsSaver.setPerf("skalierungswertX", newValue.intValue());
    	    break;
    	    case 2: Settings.skalierungswertY = newValue.intValue();
    	    		val.setText(String.valueOf(newValue.intValue()));;
    	    		prefsSaver.setPerf("skalierungswertY", newValue.intValue());
    	    break;
    	    case 3: 
    	    		Settings.bildskalierung = newValue.floatValue();
    	    		val.setText(String.valueOf(newValue.floatValue()));
    	    		prefsSaver.setPerfFloat("bildskalierung", newValue.floatValue());
    	    break;
    	    case 4: 
    	    		Settings.polygonAnzahl = newValue.intValue();
    	    		val.setText(String.valueOf(newValue.intValue()));
    	    		prefsSaver.setPerfFloat("polygonAnzahl", newValue.intValue());
    	    break;
    	    }
    	});
		
	}
	/**
	 * Sets up all the sliders and get the safed values
	 */
	private void setUpSliders(){
		System.out.println("ulameta");
		Slider[] sliderArray = {obererSchwellenWert, skalierungswertX, skalierungswertY, bildskalierung, polygonAnzahl};
		Text[] sliderVals = {oswVal, swXVal, swYVal, bildsVal, polygonAnzahlT};
		sliderArray[0].setValue(prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert));
		sliderArray[1].setValue(prefsSaver.getPref("skalierungswertX", Settings.skalierungswertX));
		sliderArray[2].setValue(prefsSaver.getPref("skalierungswertY", Settings.skalierungswertY));
		sliderArray[3].setValue(prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung));
		sliderArray[4].setValue(prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl));
		sliderVals[0].setText(String.valueOf(prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert)));
		sliderVals[1].setText(String.valueOf(prefsSaver.getPref("skalierungswertX", Settings.skalierungswertX)));
		sliderVals[2].setText(String.valueOf(prefsSaver.getPref("skalierungswertY", Settings.skalierungswertY)));
		sliderVals[3].setText(String.valueOf(prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung)));
		sliderVals[4].setText(String.valueOf(prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl)));
		
		Settings.obererSchwellenWert = prefsSaver.getPref("obererSchwellenWert", Settings.obererSchwellenWert);
		Settings.skalierungswertX = prefsSaver.getPref("skalierungswertX", Settings.skalierungswertX);
		Settings.skalierungswertY = prefsSaver.getPref("skalierungswertY", Settings.skalierungswertY);
		Settings.bildskalierung = prefsSaver.getPrefFloat("bildskalierung", Settings.bildskalierung);
		Settings.polygonAnzahl = prefsSaver.getPref("polygonAnzahl", Settings.polygonAnzahl);
		for(int i = 0; i < sliderArray.length; i++){
			setUpSlider(sliderArray[i], sliderVals[i], i);
		}
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
    	setUpSliders();
    	
    	
    	//System.out.println(sliderArray[1]);
    	//System.out.println(bilderZahl);
    	//System.out.println(obererSchwellenWert.getId());;
    	//sliderArray[0].setId("#penis");
    	//System.out.println(obererSchwellenWert.getId());;
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
