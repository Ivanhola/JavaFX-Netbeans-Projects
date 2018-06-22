
package library.assistant.ui.displaybooks;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.AddBookController;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssitantUtil;


public class DisplayBooksController implements Initializable {
    ObservableList<Book> list = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane rootPane;
     
     
    //our table view is going to look at our Book object, so we must enter Book in the table view
    
    @FXML
    private TableView<Book> tableView;
    
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, Boolean> avalabilityCol;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initColumn();
       
        loadData();
    }    
//method used for initializing or column by getting everything from the Book Class
    private void initColumn() {
      titleCol.setCellValueFactory(new PropertyValueFactory("title"));
      idCol.setCellValueFactory(new PropertyValueFactory("id"));
      authorCol.setCellValueFactory(new PropertyValueFactory("author"));
      publisherCol.setCellValueFactory(new PropertyValueFactory("publisher"));
      avalabilityCol.setCellValueFactory(new PropertyValueFactory("availability"));
    }
//this method will get everything from our Database
    private void loadData() {
        //clear list then add the stuff again
        list.clear();
       DatabaseHandler handler = DatabaseHandler.getInstance();
        //reused our code from AddBookController, which allows us to look into the Database and Query the results
        //look at comments from AddBookController for more info regarding what each thing does
        
        
        //gets all from book
        String query = "SELECT * FROM BOOK";
        //our result set gets everything from our query("SELECT * FROM BOOK"), our resultset object now has all the data
        ResultSet rs = handler.execQuery(query);
   
        try {
          //our result set is now going to be used to read the data corresponding to each string value
            while(rs.next()){
             //get resultset from title, author, id, publisher, and check if true
                String getTitle = rs.getString("title");
                String getAuthor = rs.getString("author");
                String getId = rs.getString("id");
                String getPublisher = rs.getString("publisher");
                Boolean getAvail = rs.getBoolean("isAvail");
                //System.out.println(title);
                
                //add a new Book using our Book(string string string string bool) constructor
                //using each string value from our resultset from title, author, id etc
                // NOTE we are not using getter methods, we are using strings named get to clarify whats being used in our constructor
               list.add(new Book(getTitle, getId, getAuthor, getPublisher, getAvail)); 
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //taken the data from the database, added it to our list and now associated it with our tableview
        //tableView.getItems().setAll(list);
        //sets items from our list
        tableView.setItems(list);
    }

    
    //method used when right clicking on a book item, will perform the following
    @FXML
    private void contextMenuDeleteBook(ActionEvent event) {
        //get the selected row; returns Book object with values depending on what item was selected 
        Book selectedForDelete = tableView.getSelectionModel().getSelectedItem();
        //if an item isnt selected and you try to delete, will alert user that item was not selected
        if(selectedForDelete == null){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Book is not Selected");
                alert1.showAndWait(); 
                return;
        }
        //checking if the book is issued, if it is issued it will return and won't do anything else
        boolean check = DatabaseHandler.getInstance().isBookIssued(selectedForDelete);
        if(check){
                 Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Book is already issued");
                alert1.showAndWait(); 
            return;
        }
        
        
        //if book was chosen prompt user for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setContentText("Are you sure you want to delete the book " + selectedForDelete.getTitle() + "?");
        //optional button types(OK CANCEL)
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK){
            //Delete the book, exec query deleting
            //boolean to tell whether the function was executed true or false, deleteBook() returns a boolean value
            //gets an instance of our database, calls the deletebook function by passing in a book object(selectedForDelete)
            //which calls a query to delete based on the passed book value
          boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDelete);
          
          if(result){
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book deleted");
                alert1.showAndWait(); 
                //must remove the chosen book from our list
                list.remove(selectedForDelete);
                
          }else{
              Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
              alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book could not be deleted");
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

    //method when right clicking on our list, will open up edit and can edit books
    @FXML
    private void contextMenuEditBook(ActionEvent event) {
         Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        //if no item selected, return and do nothing
        if(selectedForEdit == null){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error");
                alert1.setHeaderText(null);
                alert1.setContentText("Book is not Selected");
                alert1.showAndWait(); 
                return;
        }
        
        try {
            //create FXML object loader getting our add_book fxml
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"));
              //setting the FXML loader to parent (MUST HAVE PARENT BEFORE ADDING CONTROLLER)
              Parent parent = loader.load();
              //getting the controller from addBookController
              AddBookController controller = (AddBookController) loader.getController();
                //calling the insertFields method which will set the text to our current book object (selected for Edit)
              controller.insertFields(selectedForEdit);
              
             
              Stage stage = new Stage(StageStyle.DECORATED);
              stage.setTitle("Edit book");
              stage.setScene(new Scene(parent));
              stage.show();
              LibraryAssitantUtil.setStageIcon(stage);
              
              
              //event listener that when the scene closes, will call the refresh button
              stage.setOnCloseRequest((e)->{
                contextMenuRefresh(new ActionEvent());
              });
              
              
          } catch (IOException ex) {
              Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
          }
        
    }

    @FXML
    private void contextMenuRefresh(ActionEvent event) {
        loadData();
    }
    
    
    //in this method, when the constructor is called, it will set the SimpleString Variables to the constructor
    
    //make it a static class so that it is accesible to all classes
    public static class Book{
        //create simplestringproperties for each of our columns, 
        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty availability;
        //constructor that will take in a title, id, author, publisher, avail boolean value
       public Book(String title, String id, String author, String publisher, Boolean availability){
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(availability);
            
            
            
        }
//we will need to return a string so we get rid of SimpleStringProperty and leave string with .get() which returns a string value
        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public Boolean getAvailability() {
            return availability.get();
        }
    }
}
