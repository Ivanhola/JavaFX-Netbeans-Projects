/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.MainController;
import org.apache.commons.codec.digest.DigestUtils;
import library.assistant.util.LibraryAssitantUtil;



public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    @FXML
    private Label loginLabel;
    
    //calling Preferences class with variable named pref/ hasn't been initialized yet.
    Preferences pref;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize our preferences object with the data from our JSON file.
        
        //pref is now equal to whatever is in the getConfig() method from our Preferences class(getting everything from json file.)
        
        pref = Preferences.getConfig();
        
    } 
    
      @FXML
    private void nextInputAction(ActionEvent event) {
        password.requestFocus();
    }
    
    

    @FXML
    private void handleLoginAction(ActionEvent event) {
        //get whatever is entered from our textfields in our program
        String user = username.getText();
        //gets the password and makes it into sha1hex
        //basically if this sha1hex matches the sha1hex in our JSON file, it will match and log in 
        String pass = DigestUtils.sha1Hex(password.getText());
        
        if(user.equals(pref.getUsername()) && pass.equals(pref.getPassword())){
            //log in
            closeStage();
            loadWindow("/library/assistant/ui/main/main.fxml", "Library Assistant");
            
            
        }else{
            username.clear();
            password.clear();
            loginLabel.setText("Invalid Credentials");
            loginLabel.setStyle("-fx-text-fill:red; -fx-background-color: black");
        }
        
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage)loginLabel.getScene().getWindow()).close();
       
    }
    
      private void loadWindow(String Location, String title){
          try {
              //standard operations for showing and opening a new window
              Parent parent = FXMLLoader.load(getClass().getResource(Location));
              Stage stage = new Stage(StageStyle.DECORATED);
              stage.setTitle(title);
              stage.setScene(new Scene(parent));
              stage.show();
              
              LibraryAssitantUtil.setStageIcon(stage);
              
          } catch (IOException ex) {
              Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
          }
    }

  
    
}
