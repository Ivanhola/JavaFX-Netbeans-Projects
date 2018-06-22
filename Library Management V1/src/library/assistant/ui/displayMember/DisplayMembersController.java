
package library.assistant.ui.displayMember;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addMember.AddMembersController;
import library.assistant.ui.addbook.AddBookController;
import library.assistant.ui.displaybooks.DisplayBooksController;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssitantUtil;




public class DisplayMembersController implements Initializable {

    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> phoneNumCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    
    //create our observable list
    //our observable list uses our Member object
        ObservableList<Member> list = FXCollections.observableArrayList();

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initializes when on on this Controller
        loadDatabase();
       initColumn();
        
        
        
        
    }  
    
    
    //initializes our columns in our view table
    //it knows to look at our member class and getters, because in our view table, we set it to look for Members, String for each column
    //therefore it will look at each value from our Member class that contains ex: name, id, phoneNum, email.
    private void initColumn() {
      nameCol.setCellValueFactory(new PropertyValueFactory("name"));
      idCol.setCellValueFactory(new PropertyValueFactory("id"));
      phoneNumCol.setCellValueFactory(new PropertyValueFactory("phoneNum"));
      emailCol.setCellValueFactory(new PropertyValueFactory("email"));
    }
    
    
    //this method will load everything from our database
    private void loadDatabase(){
        //clear the list first, then add to it, to remove the possibility of accidentally duplicating
        //if this method is called more than once
        list.clear();
        //calling the method getInstance from the DatabaseHandler class
        //and returning the DatabaseHandler object returned from .getInstance/ assigning it to handler
        DatabaseHandler handler = DatabaseHandler.getInstance();
       
        String query = "SELECT * FROM MEMBER";
        
        ResultSet rs = handler.execQuery(query);
        try{
            //while there is still data in our Resultset object. 
            while(rs.next()){
             //check our resultset object, and set a string value to each rs.getString(DATABASEVALUES);
                String getName = rs.getString("name");
                String getId = rs.getString("id");
                String getPhone = rs.getString("phoneNum");
                String getEmail = rs.getString("email");
                
                //add a new Book using our Book(string string string string bool) constructor
                //using each string value from our resultset from title, author, id etc
                // NOTE we are not using getter methods, we are using strings named get to clarify whats being used in our constructor
               list.add(new Member(getName, getId, getPhone, getEmail)); 
                
                
            }
        }catch(SQLException ex){
           Logger.getLogger(DisplayMembersController.class.getName()).log(Level.SEVERE, null, ex);  
        }
        //this associates all of our columns with the main tableview which has an fx ID of "tableView" if we had 2 tableviews for example
        // all of our contents would only display on the "NAMEOFTABLEVIEW".getItems().setAll(list); that we choose here. VERY IMPORTANT THAT WE PUT THIS
        // OR ELSE OUR CONTENT WON'T SHOW ON THE DESIGNATED TABLEVIEW
        tableView.setItems(list);
    }

    @FXML
    private void contextMenuRefreshAction(ActionEvent event) {
        loadDatabase();
    }

    
    
    @FXML
    private void contextMenuEditAction(ActionEvent event) {
        //create a Member Object with the values from our selected TableView
                Member selectedMemberEdit = tableView.getSelectionModel().getSelectedItem();
        //if no item selected, return and do nothing
        
        if(selectedMemberEdit == null){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("no members were Selected");
                alert1.showAndWait(); 
                return;
        }
        
        try {
            //create FXML object loader getting our add_book fxml
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addMember/Add_Members.fxml"));
              //setting the FXML loader to parent (MUST HAVE PARENT BEFORE ADDING CONTROLLER)
              Parent parent = loader.load();
              //getting the controller from addBookController
              AddMembersController controller = (AddMembersController) loader.getController();
                //calling the insertFields method which will set the text to our current members object (selectedMemberEdit)
                //insertFields takes in a Member object, and gets the values and sets the textfield text to those values
              controller.insertFields(selectedMemberEdit);
              
             
              Stage stage = new Stage(StageStyle.DECORATED);
              stage.setTitle("Edit Member");
              stage.setScene(new Scene(parent));
              stage.show();
              LibraryAssitantUtil.setStageIcon(stage);
              
              
              //event listener that when the scene closes, will call the refresh button
              stage.setOnCloseRequest((e)->{
                contextMenuRefreshAction(new ActionEvent());
              });
              
              
          } catch (IOException ex) {
              Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
          }
    }

    @FXML
    private void contextMenuDeleteAction(ActionEvent event) {
          //get the selected row; returns member object with values depending on what item was selected 
        Member selectedForDelete = tableView.getSelectionModel().getSelectedItem();
        //if an item isnt selected and you try to delete, will alert user that item was not selected
        if(selectedForDelete == null){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Member is not Selected");
                alert1.showAndWait(); 
                return;
        }
        //checking if the member is issued, if it is issued it will return and won't do anything else
        boolean check = DatabaseHandler.getInstance().doesMemberHaveBooks(selectedForDelete);
        if(check){
                 Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Member has books issued");
                alert1.showAndWait(); 
            return;
        }
        
        
        //if member was chosen prompt user for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete the member " + selectedForDelete.getName() + "?");
        //optional button types(OK CANCEL)
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            //Delete the membeb, exec query deleting
            //deleteMember returns a boolean value if the object passed in was deleted successfully
            //if the value was true, the member was deleted and proceeds with the if(statement)
            
          boolean result = DatabaseHandler.getInstance().deleteMember(selectedForDelete);
          
          if(result){
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Member deleted");
                alert1.showAndWait(); 
                //must remove the chosen member from our list
                list.remove(selectedForDelete);
                
          }else{
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Member could not be deleted");
                alert1.showAndWait(); 
          }
        }else{
             Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Delete Cancelled");
                alert1.showAndWait(); 
        }
    }
    
    
    
    
    
    
    public static class Member{
        //create simplestringproperties for each of our columns, 
        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty phoneNum;
        private final SimpleStringProperty email;
        
        //constructor that will take in our Member names, id, phone, email.
       public Member(String memName, String memId, String memPhone, String memEmail){
            this.name = new SimpleStringProperty(memName);
            this.id = new SimpleStringProperty(memId);
            this.phoneNum = new SimpleStringProperty(memPhone);
            this.email = new SimpleStringProperty(memEmail);

            
            
            
        }
//we will need to return a string to read in our table view, in order to return a string must use.get();
        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getPhoneNum() {
            return phoneNum.get();
        }

        public String getEmail() {
            return email.get();
        }
       
    }
}
    

