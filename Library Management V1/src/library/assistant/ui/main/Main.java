/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.main;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssitantUtil;


public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/login/Login.fxml"));
        
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
        //creating a thread for optimization and speed of loading our main program
        new Thread(() -> {
            //initializes our database info at the beginning of the program
            DatabaseHandler.getInstance();
        }).start();
        
        //setting icon for our Login page
        LibraryAssitantUtil.setStageIcon(stage);
       
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}