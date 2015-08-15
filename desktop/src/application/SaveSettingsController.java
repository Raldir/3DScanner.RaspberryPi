package application;

import de.rami.polygonViewer.FileCreator;
import de.rami.polygonViewer.Settings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SaveSettingsController {
	@FXML
	private TextField saveName; 
	@FXML
	private Button saveButton;
	
	private SettingsSaver savedSetts;
	private PreferencesSaver prefsSaver;
	//static int AmountPrefsSaved;
	@FXML
	private void handleSave(ActionEvent e){
		
		savedSetts = new SettingsSaver(FileCreator.glaettungsfaktor, Settings.obererSchwellenWert, Settings.skalierungswertX, Settings.skalierungswertY, Settings.polygonAnzahl, Settings.bildskalierung);
		savedSetts.saveSettings(saveName.getText());
		prefsSaver = new PreferencesSaver();
		prefsSaver.savePreset(prefsSaver.getAmountPrefsSaved("key")+1, saveName.getText());
		prefsSaver.setAmountPrefsSaved("key", prefsSaver.getAmountPrefsSaved("key")+1);
		if(saveName.getText().equals("c")){
			prefsSaver.clear();
			System.out.println("cleared");
		}
	}
	
	@FXML
    private void initialize() {
    }


}
