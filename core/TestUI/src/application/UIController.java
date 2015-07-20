package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
	
	private String qualitySelected;
	
	/**
	 * handles the calibration provides a popup window with further information.
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
	}
	/**
	 * Hacky solution for the quality selector, which makes it sure only one quality can be selectet at
	 * a time. The selected quality is stored as a String in the global variable qualitySelected for a still
	 * unsure purpose 
	 * @param e
	 */
	@FXML
	private void hadleQualitySelect(ActionEvent e){
		//System.out.println(e);
		//System.out.println();
		//System.out.println(e.getSource() + " source");
		Object source = e.getSource();
		CheckMenuItem selectedMenuItem = (CheckMenuItem) source;
		//System.out.println(selectedMenuItem.getId());
		qualitySelected = selectedMenuItem.getId();
		high.setSelected(false);
		medium.setSelected(false);
		low.setSelected(false);
		selectedMenuItem.setSelected(true);
		//System.out.println(qualitySelected + " Qualität Selektiert");	
	}
	
	/**
	 * Handles the Start Button so that it changes the text from  Start to Abbrechen and back.
	 * @param e
	 */
	@FXML
	private void handleStart(ActionEvent e){
		if(startButton.getText() == "Start"){
			//System.out.println("Ja wir warn da");
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
        // TODO
    	//Auf grund massiver Spackungen muss der Text vom Knopf hier gesetzt werden, das sonst die if nicht funzt.
    	startButton.setText("Start");
    	medium.setSelected(true);
    	helpButton.setTooltip(new Tooltip("Hilfe"));
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
