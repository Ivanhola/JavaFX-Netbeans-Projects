
package library.assistant.ui.main;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import com.jfoenix.effects.JFXDepthManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssitantUtil;

public class MainController implements Initializable {
    
    /*******************FXML VARIABLES***************************/
    @FXML
    private TextField bookIDInput;
 
    @FXML
    private JFXTextField renew_bookid;
    
    @FXML
    private TextField memberIdInput;
   
    @FXML
    private HBox book_info;

    @FXML
    private HBox member_info;
    
    @FXML
    private Text bookStatus;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookName;

    @FXML
    private Text memberName;
    
    @FXML
    private Text memberPhoneNum;
    
    @FXML
    private ListView<String> renewList;
    
    //this variable is used to access our DatabaseHandler functions and tables. Must use with getinstance (which is used in our initialize function)
    DatabaseHandler handler;
    
    Boolean isReadyForSubmission = false;
    @FXML
    private StackPane rootPane;
    
    /*******************METHODS USED IN BOOK ISSUE TAB***************************/
    
    
    //this method is used for displaying the book information on our BOOK info page of out HBOX this is called when u press
    //enter on the input field for BookID
    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookCache();
        //this gets the input from our textfield
        String id = bookIDInput.getText();
        //prepare a query
        String query = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        //stores the query results in this object(rs)
        ResultSet rs = handler.execQuery(query);
        //validater variable
        Boolean flag = false;
        try{
        while(rs.next()){
            
            //gets the results of our resultset Query
            String nameOfBook = "Book ID " + id + " is " + rs.getString("title");
            String nameOfAuthor = rs.getString("author");
            Boolean bookStat = rs.getBoolean("isAvail");
            flag = true;
            //setting our text with the information from our query
            bookName.setText(nameOfBook);
            bookAuthor.setText(nameOfAuthor);
            //if the book is available? display available, if not display not available
            String status = (bookStat)?"Available":"Not Available";
            bookStatus.setText(status);
        }
        
        //if none of the book exists, will display that the book is not available
        if(!flag){
            bookName.setText("No Such book is available!");
            clearBookCache();
        }
        
        }catch(SQLException ex){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
    //clears author and status text
    void clearBookCache(){
           bookAuthor.setText("");
            bookStatus.setText("");
    }
    //clears the member text 
    void clearMemberCache(){
        memberPhoneNum.setText("");
    }
    
    //this method is used to call the member ID information when hitting Enter the keyboard in the member id inputfield
    //it will load member information based on the things inside the database.
        @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMemberCache();
        //this gets the input from our textfield
        String id = memberIdInput.getText();
        //prepare a query (this query gets all MEMBERS on the entered ID)
        String query = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        //stores the query results in this object(rs)
        ResultSet rs = handler.execQuery(query);
        //validater variable
        Boolean flag = false;
        try{
        while(rs.next()){
            
            //gets the results of our resultset Query
            String nameOfMember = rs.getString("name");
            String memberContactNumber = rs.getString("phoneNum");
            
            flag = true;
            //setting our text with the information from our query
            memberName.setText(nameOfMember);
            memberPhoneNum.setText(memberContactNumber);
            //if the book is available? display available, if not display not available
           // String status = (bookStat)?"Available":"Not Available";
           // bookStatus.setText(status);
        }
        
        //if none of the book exists, will display that the book is not available
        if(!flag){
            memberName.setText("That member does not exist!");
            clearMemberCache();
        }
        
        }catch(SQLException ex){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null,ex);
        }
    }
    
    //this method will call the issueBook method triggered by pressing the issue button.
    //will issue a book to our member id
      @FXML
    private void issueBookOperation(ActionEvent event) {
        
        //gets the Ids from our textfields
        String memId = memberIdInput.getText();
        String bkId = bookIDInput.getText();
        
        //prompts an alert box that asks the user to confirm book issue
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm issue operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book " + bookName.getText() + "\n to " + memberName.getText() + "?");
        
        //creates an options type in the alert box
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
            //if the button OK is clicked, will execute 2 action statemnts onto our database
            String insert = "INSERT INTO ISSUE(memberID, bookID) VALUES ( "
                   + "'" + memId + "',"
                   + "'" + bkId + "')";
            
            String insert2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bkId + "'";
            
            System.out.println(insert + " and " + insert2);
            //makes sure that both the statements are validated and correct, so it wont accidentally only use one
            if(handler.execAction(insert)&&handler.execAction(insert2)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Operation Complete");
                alert1.setHeaderText(null);
                alert1.setContentText("Book issue complete");
                
                alert1.showAndWait();
        //if an operation failed display the following
            }else{
                 Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Operation Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book issue operation Failed");
                 alert1.showAndWait();
            }
            //this is if the user presses cancel
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Operation Cancelled");
                alert1.setHeaderText(null);
                alert1.setContentText("Book issue operation cancelled");
                alert1.showAndWait(); 
        }
        
    }
    
    
    
    

    /*******************METHODS USED IN THE RENEW/SUBMISSION TAB***************************/
    
    
    //method for calling the book information in our Renew/Submission section
                @FXML
    private void renewLoadBookInfo(ActionEvent event) {
        //create an observable list of type string to store the data that will be displayed on our listview
        ObservableList<String> issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false; 
        
        //string variable for obtaining our input book ID
        String bookIdInput = renew_bookid.getText();
        
        //this query gets everything from the database table ISSUE, and returns data where the id is what the user entered
        //B100 in this case returns everything that the user that issued B100 has 
        String query = "SELECT * FROM ISSUE WHERE bookID = '" + bookIdInput + "'";
        
        //stores the result in our resultset variable (rs)
        ResultSet rs = handler.execQuery(query);
         try {
             while(rs.next()){
                 //gets the data from the resultset
                 String resultBookID = rs.getString("bookID");
                 String resultmemberID = rs.getString("memberID");
                 Timestamp memIssueTime = rs.getTimestamp("timeIssued");
                 int memRenewCount = rs.getInt("renewCount");
                 
                 //adds data to our  observable list
                 issueData.add("Time and Date: " + memIssueTime.toGMTString());
                 issueData.add("Renew Count: " + memRenewCount);
                 issueData.add("Book Information: ");
                 
                 //takes our query string that we created earlier to call on another query
                 query = "SELECT * FROM BOOK WHERE id = '" + resultBookID + "'";
                 
                 //create another resultset to store our information
                 ResultSet rs2 = handler.execQuery(query);
                 while(rs2.next()){
                     //gets the new data in our new resultset, and gets the data from our book database table.
                     //add it to our observable list
                     issueData.add("Book Name: "+ rs2.getString("title"));
                     issueData.add("Book ID: "+ rs2.getString("id"));
                     issueData.add("Book Author: "+ rs2.getString("author"));
                     issueData.add("Book Publisher: "+ rs2.getString("publisher"));
                 }
                 
                 //same thing here as mentioned above
                 query = "SELECT * FROM MEMBER WHERE ID = '" + resultmemberID + "'";
                 rs2 = handler.execQuery(query);
                 issueData.add("Member Information: ");
                 while(rs2.next()){
                     issueData.add("Name: " + rs2.getString("name"));
                     issueData.add("Name: " + rs2.getString("phoneNum"));
                     issueData.add("Name: " + rs2.getString("email"));
                 }
                 isReadyForSubmission = true;
             }
             
         } catch (SQLException ex) {
             Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
         }
         //this action gets all items from issueData, and adds it to our renewList variable(which is a list view)
        renewList.getItems().setAll(issueData);
    }
    
    
    
    
    //method for loading submission button on button click / the submission button returns the book back to the library
                @FXML
    private void loadSubmissionOperation(ActionEvent event) {
        //in the load book info we are checking using the boolean isReadyForSubmission to check whether or not a book is ready to be returned
        //this if statements simply gives an alert saying that if the book is not ready, or hasnt been selected, to choose a book to be returned
        if(!isReadyForSubmission){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select book for submission");
            alert.showAndWait();
            
            return;
        }
        //if a book has been chosen for submission, it will perform the following code
        
        
         //prompts an alert box that asks the user to confirm book issue
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return the book?");
        
        //creates an options type in the alert box
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
            //gets the bookId from the text box
        String bookId = renew_bookid.getText();
        
        //querys or "actions" that will tell the database what to do(deleting and updating depending on what is chosen)
        String action1 = "DELETE FROM ISSUE WHERE bookID = '" + bookId +"'";
        String action2 = "UPDATE BOOK SET isAvail = TRUE WHERE ID = '"+ bookId + "'";
        
        
        
        //if no errors or exceptions when using these queries perform the following
        if(handler.execAction(action1)&&handler.execAction(action2))
        {
            //success alert box
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Success");
            alert1.setHeaderText(null);
            alert1.setContentText("Book has been submitted!");
            alert1.showAndWait();
        }else{
            //failed alert box if an exception has occured
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Submission has failed");
            alert1.showAndWait();
            
            
        }
        //if cancelled
        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Operation Cancelled");
                alert1.setHeaderText(null);
                alert1.setContentText("Book submission operation cancelled");
                alert1.showAndWait(); 
        }
    }
    
       @FXML
    private void loadRenewOperation(ActionEvent event) {
        if(!isReadyForSubmission){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please select book for renewal");
            alert.showAndWait();
            
            return;
        }
        
        
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm renew operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to renew the book?");
        
        //creates an options type in the alert box
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
            String action = "UPDATE ISSUE SET timeIssued = CURRENT_TIMESTAMP, renewCount = renewCount+1 WHERE bookID = '"+ renew_bookid.getText() +"'";
            System.out.println(action);
            if(handler.execAction(action)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book renew operation was successful");
                alert1.showAndWait(); 
            }else{
                 Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book renew operation has failed");
                alert1.showAndWait(); 
            }
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Operation Cancelled");
                alert1.setHeaderText(null);
                alert1.setContentText("Book renew operation cancelled");
                alert1.showAndWait(); 
        }
    }
    
    /*******************METHODS USED IN OUR SIDE BUTTONS TO OPEN UP DIFFERENT SCENES***************************/
    
    //methods for showing and opening the windows for each tab button. uses our loadwindow method with the paths to the fxml files
      @FXML
    private void promptAddMember(ActionEvent event) {
loadWindow("/library/assistant/ui/addMember/Add_Members.fxml","Add New Member");
    }

    @FXML
    private void promptAddBook(ActionEvent event) {
loadWindow("/library/assistant/ui/addbook/add_book.fxml","Add New Book");
    }

    @FXML
    private void displayMembers(ActionEvent event) {
loadWindow("/library/assistant/ui/displayMember/DisplayMembers.fxml","List of Members");
    }

    @FXML
    private void displayBooks(ActionEvent event) {
loadWindow("/library/assistant/ui/displaybooks/Display_Books.fxml","List of Books");
    }
    
    @FXML
    private void loadSettings(ActionEvent event){
        loadWindow("/library/assistant/settings/Settings.fxml", "Settings Menu");
    }
   
   
    //method for calling each window
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

     /*******************OUR INITIALIZE METHOD, WHENEVER THIS SCENE/WINDOW IS OPEN WILL INSTANTLY CALL ANYTHING IN HERE***************************/
  
  //initializes our depth, and starts up a handler object for viewing the books
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(book_info, 1);
        JFXDepthManager.setDepth(member_info, 1);
        
        handler = DatabaseHandler.getInstance();
    }   

    
    
    
    /******************************TOP MENU BAR EVENTS*********************************************/
    @FXML
    private void menuBarCloseAction(ActionEvent event) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void menuBarFullScreenAction(ActionEvent event) {
        Stage stage = ((Stage)rootPane.getScene().getWindow());
        //sets full screen if it is not full screen/ can set back if is fullscreen
        stage.setFullScreen(!stage.isFullScreen());
       

    }

 
  


}


/* this method is used to backup the database, can be used on an action button (probably should use this on the DatabaseHandler or something
//then call this method when clickin on a button

public static void backUpDatabase(Connection conn)throws SQLException

{
//the directory location in which it will back up/ will add a date to the backup name
   String backupdirectory ="c:/mybackups/"+JCalendar.getToday();
//used to create a backup
   CallableStatement cs = conn.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)"); 
   cs.setString(1, backupdirectory);
   cs.execute(); 
   cs.close();
   System.out.println("backed up database to "+backupdirectory);
}
*/