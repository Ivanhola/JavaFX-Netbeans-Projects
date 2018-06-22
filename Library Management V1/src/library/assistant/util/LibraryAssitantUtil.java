
package library.assistant.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/*
Utilities class for handling things like the ICON for our application
*/
public class LibraryAssitantUtil {
    public static final String IMAGE_DIR = "/resources/libAssistanticon.png";
    
    
    //must be set to static to be called by any class, and wont need to create an object to call this function
    public static void setStageIcon(Stage stage){
        stage.getIcons().add(new Image(IMAGE_DIR));
    }
}
