/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.settings;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SettingsController implements Initializable {

    @FXML
    private TextField numDaysNoFine;
    @FXML
    private TextField finePerDay;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultPref();
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event){
           Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm save settings");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to save these settings?");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
        //getting the textfield input values
        int nDays = Integer.parseInt(this.numDaysNoFine.getText());
        float fine = Float.parseFloat(this.finePerDay.getText());
        String uName = username.getText();
        String pass = password.getText();
        
        //getting curret config, can assign pref to Preferences.getConfig(), because getConig() returns a Preferences Object type, and is static
        Preferences pref = Preferences.getConfig();
        //setting new values to our current object(pref)
        pref.setNumDaysWithoutFine(nDays);
        pref.setFinePerDay(fine);
        pref.setUsername(uName);
        pref.setPassword(pass);
        //now our values in our pref object are equal to the set values from our input textfields
        //now we need to save it by writing to our JSON file
        
        //we call our saveconffig method in our preferences class, which takes in a Preferences object, and stores the values
        // into our JSON file.
        
        
        //can call the saveConfig method because it is static, so we don't need to create an object to call saveConfig
        Preferences.saveConfig(pref);
        
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Settings saved");
                alert1.showAndWait();
        }else{
             Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Operation Cancelled");
                alert1.setHeaderText(null);
                alert1.setContentText("Settings cancelled");
                alert1.showAndWait();
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        ((Stage)numDaysNoFine.getScene().getWindow()).close();
    }

    
    
    /*this method is used to get all the values in our JSON file,
    in order to get the data stored in our JSON file, must create an instance/object of Preferences
    "Preferences preferences = Preferences.getConfig();" will call the method in our Preferences class
    
    
    */
    private void initDefaultPref() {
        //calls the getConfig() method which just reads everything from a JSON file
        //sets our created preferences object to equal the Preferences.getConfig(), because getConfig returns an object type.
        Preferences preferences = Preferences.getConfig();
        
        /*another way of doing the above*/
        
    //Preferences preferences = new Preferences();
    //preferences.getConfig();
    
    //gets the textfield inputs from above, and sets the text to a string converted value of our getters
    //for example:
    //numDaysNoFine.setText <- sets the text TO -> String.valueOf(converts to string) -> preferences.getNumDaysWithoutFine()
    //preferences calls the getter "getNumDaysWithoutFine" and it is equal to our values from our getConfig() method 
    //(which is default values so far) 
    this.numDaysNoFine.setText(String.valueOf(preferences.getNumDaysWithoutFine()));
    this.finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
    this.username.setText(String.valueOf(preferences.getUsername()));
    this.password.setText(String.valueOf(preferences.getPassword()));
    }

    
}
