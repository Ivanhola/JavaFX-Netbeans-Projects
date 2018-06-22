/*
THIS CLASS IS USED FOR SETTING THE SETTINGS PAGE IN OUR SETTINGS FXML FILE. THIS WILL CONTAIN FORMATTING FOR 
SAVING INTO OUR JSON FILE.
 */
package library.assistant.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Ivans
 */
public class Preferences {
    public static final String CONFIG_FILE  = "config.txt";
    
    //variables that are used to determine the types of preferences we are going to use
    //4 variables because this will be used in our SettingsController class that has 4 inputfields
    public int numDaysWithoutFine;
    public float finePerDay;
    public String username;
    public String password;
    
    
    //default constructor assigning default values to our variables from above
    public Preferences(){
        this.numDaysWithoutFine = 14;
        this.finePerDay = 2;
        this.username = "admin";
        //calling the set password method(this is the default password when we change it it won't set to admin)
        setPassword("admin");
    }

    
                                           /**************SETTERS**************/   
    
    public void setNumDaysWithoutFine(int numDaysWithoutFine) {
        this.numDaysWithoutFine = numDaysWithoutFine;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    //use DigestUtils to HASH our password
    public void setPassword(String password) {
        if(password.length() < 16){
        //creates the password in sha1hex format
        this.password = DigestUtils.sha1Hex(password);
        }else{
            this.password = password;
        }
        }
    
    
                                            /**************GETTERS**************/  
    
    public int getNumDaysWithoutFine() {
        return numDaysWithoutFine;
    }
   
    public float getFinePerDay() {
        return finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
//initializes our preferences (used to write file)
    public static void initConfig(){
        //writer instance to write onto file/ has nothing in it here(null)
        Writer writer = null;
        try {
            //create an object with default values(default constructor)
            Preferences preferences = new Preferences();
            //Gson method used to call everything in the Gson class
            Gson gson = new Gson();
            //our writer will create a new document in the location of CONFIG_FILE
            writer = new FileWriter(CONFIG_FILE);
            //this gson method writes everything in our preferences object(which is default so far) 
            //into JSON format in our writer file location
            gson.toJson(preferences,writer);
        } 
        catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try{
            writer.close();
                        }catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                     } 
          }
   
    }
    
    //this method reads the configuration from our JSON file, and sets the preferences object we create
    // to equal that of the json file
    public static Preferences getConfig(){
        //calling Gson
        Gson gson = new Gson();
       
        //Preferences pref = new Preferences(); DEFAULT VALUES FROM OUR CONSTRUCTOR   
         //creating a preferences object that has default values SO FAR
        Preferences preferences = new Preferences();
     
        try {
            //setting that preferences object to equal everything in the JSON file
            //the values in preferences are now equal to the values in our JSON file
             preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            //will create a file if not found
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        //returns a preference object with data associated with it
        return preferences;
        
        
       //return pref; RETURNS OBJECT WITH DEFAULT VALUES FROM CONSTRUCTOR
    }
    
    //this method allows us to take a Preferences object, and pass that object and its values into our JSON file 
    public static void saveConfig(Preferences pref){
        //empty writer class
        Writer writer = null;
        try {
            
            //Gson method used to call everything in the Gson class
            Gson gson = new Gson();
            //our writer will create a new document in the location of CONFIG_FILE
            writer = new FileWriter(CONFIG_FILE);
            //******takes all the values and our object passed in, and writes those values onto the file.****
            gson.toJson(pref,writer);
        } 
        catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try{
            writer.close();
                        }catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
                     } 
          }
    }
    
}
