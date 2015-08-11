package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
    	System.out.println("hi");
        Parent root = FXMLLoader.load(getClass().getResource("Test_UI.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        stage.setTitle("SuperduperScanner");
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.setMaxWidth(600);
        stage.setMaxHeight(500);
        stage.show();
        
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
