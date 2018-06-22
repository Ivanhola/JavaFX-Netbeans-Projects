
package library.assistant.ui.addMember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.displayMember.DisplayMembersController;

public class AddMembersController implements Initializable {
    DatabaseHandler handler;
    
     @FXML
    private AnchorPane rootPane;
     
    @FXML
    private JFXTextField Name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField PhoneNum;
    @FXML
    private JFXTextField Email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    boolean isInEditMode = false;
    

    @FXML
    private void addMember(ActionEvent event) {
        String MemberName = Name.getText();
        String MemberId = id.getText();
        String MemberPhone = PhoneNum.getText();
        String MemberEmail = Email.getText();
        
        Boolean validate = MemberName.isEmpty()||MemberId.isEmpty()||MemberPhone.isEmpty()||MemberEmail.isEmpty();
        if(validate){
         Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a value for all fields");
            alert.showAndWait();
            return;
        }
        //calls the edit operation method
        if(isInEditMode){
            handleEditOperation();
            return;
        }
        
        /*
         stmt.execute("CREATE TABLE " + TABLE_NAME +"("
                    +" id varchar(200) primary key, \n"
                    +" name varchar(200),\n"
                    +" phoneNum varchar(20),\n"
                    +" email varchar(100),\n"                   
                    +" ) ");
        
        */
        //our insert statement string variable
        String statement = "INSERT INTO MEMBER VALUES (" + 
                "'" + MemberId +"'," +
                "'" + MemberName +"'," +      
                "'" + MemberPhone +"'," +
                "'" + MemberEmail +"'" +
                ")";
                System.out.println(statement);
                
                // call the execAction statement
                if(handler.execAction(statement)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            
                }else{
                  Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error Occured");
            alert.showAndWait();  
                }
    }
    
    public void insertFields(DisplayMembersController.Member member){
         Name.setText(member.getName());
        id.setText(member.getId());
        PhoneNum.setText(member.getPhoneNum());
        Email.setText(member.getEmail());
        id.setEditable(false);
        isInEditMode = true;
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
      handler = DatabaseHandler.getInstance();
    }   
//this method handls the edit operation, this method is called only when isInEditMode is set to true, called in the addMember function
    private void handleEditOperation() {
        //create a member object, and set that member object to equal the input fields text
        DisplayMembersController.Member member = new DisplayMembersController.Member(Name.getText(), id.getText(), PhoneNum.getText(), Email.getText());
        //if databasehandler.updateMember returns true/ will display alert saying it was success
        //the updateMember method, takes in a member object, and executes an UPDATE query with the objects values
        //the member object is taken from DisplayMembersController.Member member in the line above.
        if(handler.updateMember(member)){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Member was updated");
                alert1.showAndWait(); 
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Member was not updated");
                alert1.showAndWait(); 
        }
    }
    }
    

