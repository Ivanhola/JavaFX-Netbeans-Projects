/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.displaybooks.DisplayBooksController;


/**
 * FXML Controller class
 *
 * @author Ivans
 */
public class AddBookController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    
    @FXML
    private AnchorPane rootPane;
    
    
    private boolean isInEditMode = false;

    DatabaseHandler databaseHandler;
    /**
     * Initializes the controller class.
     */ 
    @FXML
    private void addBook(ActionEvent event) {
        //getting text from our FXML file textfields
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();
        //validating to make sure that none of the fields are empty
        if(bookID.isEmpty()||bookAuthor.isEmpty()||bookName.isEmpty()||bookPublisher.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a value for all fields");
            alert.showAndWait();
            return;
        }
        //if the boolean value is in edit mode, it will only do the handleEditOperation and then return
        if(isInEditMode){
            handleEditOperation();
            return;
        }
        
        //create an SQL insert statement for adding all data into our database
        //make sure you are entering into the table name
        // can use prepared Statement in the future to prevent SQL Injections
        String query = "INSERT INTO BOOK VALUES ("+
                "'" + bookID +"'," +
                "'" + bookName +"'," +
                "'" + bookAuthor +"'," +
                "'" + bookPublisher +"'," +
                "" + true +"" +
                ")";
        System.out.println(query);
        //use execution action because this is not a query
        if(databaseHandler.execAction(query)){
            
            //if successful show an INFORMATION type alert
              Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }else{
              Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
        
   /*
        
        //referencing what values our table has
     stmt.execute("CREATE TABLE " + TABLE_NAME +"("
                    +" id varchar(200) primary key, \n"
                    +" title varchar(200),\n"
                    +" author varchar(200),\n"
                    +" publisher varchar(100),\n"
                    +" isAvail boolean default true"
                    +" ) ");
*/    
}

    //closes our window(current stage which is rootPane)
    @FXML
    private void cancelBook(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private void getData(){
        //selects everything (*) from the table BOOK
        String query = "SELECT * FROM BOOK";
        ResultSet rs = databaseHandler.execQuery(query);
        //executes the Query from our string query("SELECT * FROM BOOK") and stores it in our resultset
        try {
            //while our resultset has next lines in all of our query do the following
            while(rs.next()){
                //returns a ResultSet, gets the String from "author"
                //something like this can be used for our note app for returning specific things like title notes
                //String author = rs.getString("author"); //outputs the author and title together corresponding with its book
                String title = rs.getString("title");
                System.out.println(title);
                //System.out.println(author);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //whenever our program is started, execute the following
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        getData();
    }    

    
    
    // used for passing values from one controller to the other, when this is called it will
    //insert the values from our book object onto our current input fields(title, id, author, publisher)
    //this method is only called when using a controller/for editing purposes
    public void insertFields(DisplayBooksController.Book book){
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);
        isInEditMode = true;
        
    }
// method for handling edit operation in our add book controller
    private void handleEditOperation() {
        //create a new book object using our book constructor that will create a book with the input text values
        DisplayBooksController.Book book = new DisplayBooksController.Book(title.getText(), id.getText(), author.getText(), publisher.getText(),true);
        //if databasehandler.updateBook returns true, the book was successfully updated/ passes our book object into the method
        if(databaseHandler.updateBook(book)){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book was updated");
                alert1.showAndWait(); 
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Book was not updated");
                alert1.showAndWait(); 
        }
    }
   
}
