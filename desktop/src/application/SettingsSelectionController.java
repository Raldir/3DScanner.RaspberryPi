package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class SettingsSelectionController {
	
	@FXML
	private Button deleteButton, selectButton;
	@FXML
	private ListView<String> settingsList;
	
	private PreferencesSaver prefsSaver; 
	private SettingsSaver savedSetts;
	private UIController uiController;
	
	@FXML
	private void handleDelete(ActionEvent e){
		System.out.println("deeleters");
		System.out.println(settingsList.getSelectionModel().getSelectedItem());
	}
	
	public void setUIController(UIController controller){
		this.uiController = controller;
	}
	
	@FXML
	private void handleSelect(ActionEvent e){
		//have a try catch?? if nothing is selected
		System.out.println("seeeelected");
		settingsList.getSelectionModel().getSelectedItem();
		System.out.println(settingsList.getSelectionModel().getSelectedItem());
		this.savedSetts = new SettingsSaver();
		this.savedSetts = this.savedSetts.loadSettings(settingsList.getSelectionModel().getSelectedItem());
		this.uiController.setUpSliderFromSaved(this.savedSetts);
	}
	
	@FXML
    private void initialize() {
		this.prefsSaver = new PreferencesSaver();
		
		ObservableList<String> data = FXCollections.observableArrayList();
		//Attation cannot be zero indexed because 0 has the meaning that nothing is there.. therefor +1
	    if(prefsSaver.getAmountPrefsSaved("key") > 0){
			for(int i = 1; i <= prefsSaver.getAmountPrefsSaved("key"); i++){
		    	data.add(prefsSaver.getSavedPreset(i));
		    }
	    }
	    System.out.println(data);
	    settingsList.setItems(data);
	    //setts the first to always be selected on start
	    settingsList.getSelectionModel().selectFirst();
	    //selectButton.requestFocus();
	    //TODO ad some way this is fireable with enter key all the time
    }	

}
