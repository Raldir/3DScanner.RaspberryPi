package application;

import de.rami.polygonViewer.FileCreator;
import de.rami.polygonViewer.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * @author Rami und Anton
 *
 */
public class SaveSettingsController {
	@FXML
	private TextField saveName; 
	@FXML
	private Button saveButton;
	
	private SettingsSaver savedSetts;
	private PreferencesSaver prefsSaver;
	
	@FXML
	private void handleSave(ActionEvent e){
		prefsSaver = new PreferencesSaver();
		boolean allreadyExists = false;
		//do some checking if the name allready exists
		for(int i = 1; i <= prefsSaver.getAmountPrefsSaved("key"); i++){
			prefsSaver.getSavedPreset(i);
			if(prefsSaver.getSavedPreset(i).equals(saveName.getText())){
				allreadyExists = true;
			}
		}
		if(allreadyExists == false){
			savedSetts = new SettingsSaver(Settings.obererSchwellenWert, Settings.skalierungswertX, Settings.skalierungswertY, Settings.polygonAnzahl, Settings.bildskalierung, Settings.grunddicke);
			savedSetts.saveSettings(saveName.getText());
			prefsSaver.savePreset(prefsSaver.getAmountPrefsSaved("key")+1, saveName.getText());
			prefsSaver.setAmountPrefsSaved("key", prefsSaver.getAmountPrefsSaved("key")+1);
			if(saveName.getText().equals("c")){
				prefsSaver.clear();
				System.out.println("cleared");
			}
			//TODO maybe add a small sign on the mainwindow that shows that everything was saved...
			
			//Stage stage = (Stage) saveButton.getScene().getWindow();
			//stage.close();
		} else{
			UIController.alertGeneral("Fehler!", "Diesen Namen gibt es schon, bitte einen anderen nehmen!");
		}
		
	}
	
	@FXML
    private void initialize() {
		//saveName.requestFocus();
    }


}
