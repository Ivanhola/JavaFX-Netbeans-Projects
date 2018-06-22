/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.settings;

import library.assistant.ui.main.*;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;


public class SettingLoader extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/settings/Settings.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        
        //creating a thread for optimization and speed of loading our main program
        new Thread(() -> {
            //initializes our database info at the beginning of the program
            DatabaseHandler.getInstance();
        }).start();
        
        /* no longer need to create a file because the file is already created, if you want to make sure the file is created, you can call upon this 
        Preference method in our initializer at the SettingsController.
        
        
        //creates our json text file/initializes our preferences config with a default config
        // basically making sure there is a file when running program
        Preferences.initConfig();
        */
       
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}