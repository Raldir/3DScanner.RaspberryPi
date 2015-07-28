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
import de.rami.polygonViewer.PolygonViewer;
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
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class UIController implements Initializable  {
	
	@FXML
	private Button kalibrierenButton;
	@FXML
	private Button startButton;
	@FXML 
	private SplitMenuButton qualityChoose;
	@FXML
	private CheckMenuItem high;
	@FXML
	private CheckMenuItem medium;
	@FXML
	private CheckMenuItem low;
	@FXML
	private Button helpButton;
	@FXML
	private TextFlow helpText;
	@FXML
	private Text lastCalibrationDate;
	
	//Diese Loesung sind vorlaufig und haengen von der saeteren umsetzung der uebergabe ab
	
	public static int bilderZahl = 8;
	
	
	private String qualitySelected;
	private CheckMenuItem selectedMenuItem;
	/**
	 * handles the calibration provides a popup window with further information.
	 * @param e
	 */
	@FXML
	private void handleCalibrate(ActionEvent e)
	{
//		ArrayList <File> bilder = PolygonViewer.bilder;
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
			case 'l': bilderZahl = 16;
			break;
			case 'm': bilderZahl = 32;
			break;
			case 'h': bilderZahl = 64;
		}
		System.out.println(bilderZahl);
		qualityChoose.setText("Qualit‰t: "+ selectedMenuItem.getText());
		//System.out.println(qualitySelected + " Qualit√§t Selektiert");	
		System.out.println(selectedMenuItem.getText() + qualitySelected );
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//Auf grund massiver Spackungen muss der Text vom Knopf hier gesetzt werden, das sonst die if nicht funzt.
    	startButton.setText("Start");
    	helpButton.setTooltip(new Tooltip("Hilfe"));
    	//Inizialisiert den QualityChooser, aber irgendwie umstaendlich...
    	medium.setSelected(true);
    	selectedMenuItem = medium;
    	qualitySelected = medium.getId();
    	qualityChoose.setText("Qualit‰t: "+ selectedMenuItem.getText());
    	System.out.println(selectedMenuItem.getText() + qualitySelected );
    } 
    
    /**
     * Allows another Class to make an Alert. No idea if useful and properly done.
     * @param title
     * @param content
     */
    public static void alertTest(String title, String content){
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		alert.showAndWait();
    }
}
