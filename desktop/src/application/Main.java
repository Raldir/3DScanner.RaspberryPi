package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;



public class Main extends Application {
    static Stage stage;
    private SettingsSelectionController controller;
    @Override
    public void start(Stage stage) throws Exception {
    	Main.stage = stage;
    	/*
    	System.out.println("hi");
        Parent root = FXMLLoader.load(getClass().getResource("Test_UI.fxml"));
        Scene scene = new Scene(root);*/
        
        FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Test_UI.fxml"));
		Parent root = (Parent) loader.load();
		Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setTitle("SuperduperScanner");
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.setMaxWidth(600);
        stage.setMaxHeight(500);
        stage.show();
        UIController controller = loader.getController();
        controller.setMain(this);
    }
    
    public boolean isSelectedSettings(){
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Settings_Selection.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setWidth(250);
        dialogStage.setHeight(250);
        dialogStage.setResizable(false);
        dialogStage.setTitle("Edit Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(Main.stage);
		try {
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        dialogStage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        controller = loader.getController();
		System.out.println(controller.isSelClicked());
		dialogStage.showAndWait(); 
		return controller.isSelClicked();
    }
    
    public SettingsSaver getSavedSetts(){
    	SettingsSaver savedSetts = controller.getChoosenSett();
    	return savedSetts;
    }
 
    public static void main(String[] args) {
//    	9,99999
//    	9 + 9 * 1/10^1
//    	9/ 9- 1/10
//    	9* 1/10 ^1 + 9 * 1/10 ^2 = 9 * (1/10^1 + 1/10 ^2....)
//    	0,1 + 0,01 + 0,001 + 1000 = sum(10i) = n * 
//    	a - k^n / a-k
//    	double i =  9 * 0.111111111111111;
//    	System.out.println(i);
        launch(args);
    }
}
