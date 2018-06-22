/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import library.assistant.ui.displayMember.DisplayMembersController.Member;
import library.assistant.ui.displaybooks.DisplayBooksController.Book;


public class DatabaseHandler {
private static DatabaseHandler handler = null;
private static final String DB_URL = "jdbc:derby:database;create=true";
private static Connection conn = null;
private static Statement stmt = null;


//create a private constructor so that no class has direct access to this 
private DatabaseHandler(){
    createConnection();
    setupBookTable();
    setupMemberTable();
    createIssueTable();
}

//this method allows us to access the DatabaseHandler constructor if the handler is not null, meaning that it won't allow us to use this if the handler is in use by another program
public static DatabaseHandler getInstance()
{
    if(handler == null)
    {
        handler = new DatabaseHandler();
    }
    return handler;
}


//Method for creating connection to our database using a class in the derby client jar file
void createConnection(){
    try{
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        conn = DriverManager.getConnection(DB_URL);
       
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "can't load database", "Database error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
       
    }
}

//method for executing a query, for example SELECT * (Will return a resultset object)
public ResultSet execQuery(String query){
    ResultSet result;
    try{
        stmt = conn.createStatement();
        result = stmt.executeQuery(query);
        
    } catch(SQLException ex){
        System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
        return null;
        
    } finally{
        
    } return result;
}

//used for doing an action in the database, for example update/creating another table.
//can be used for getting data from the database
public boolean execAction(String query){
    try{
        //maybe can use preparedstatement here to prevent SQL injections
        stmt = conn.createStatement();
        stmt.execute(query);
        return true;
    } catch(SQLException ex){
        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error occured", JOptionPane.ERROR_MESSAGE);
        System.out.println("Exception at execAction:dataHandler" + ex.getLocalizedMessage());
        return false;
    }finally{
        
    }
}

void setupBookTable(){
    //Table name
    final String TABLE_NAME = "BOOK";
    try{
        //creates a statement on conn. Remember that conn is getting the DriverManager.getConnection in our createConnection method
        //so stmt = conn.createStatement is calling our statment from DB_URL
        stmt = conn.createStatement();
        
        //if there is a database that already exists, output that it already exists(tables.next())
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        if(tables.next()){
            System.out.println("Table "+ TABLE_NAME + "Already Exists.");
        }else{
            stmt.execute("CREATE TABLE " + TABLE_NAME +"("
                    +" id varchar(200) primary key, \n"
                    +" title varchar(200),\n"
                    +" author varchar(200),\n"
                    +" publisher varchar(100),\n"
                    +" isAvail boolean default true"
                    +" ) ");
        }
    } catch(SQLException e){
        System.err.println(e.getMessage() + "... SetupDatabase BOOK TABLE");
    } finally{
        
    }
}

void setupMemberTable(){
    //Table name
    final String TABLE_NAME = "MEMBER";
    try{
        //creates a statement on conn. Remember that conn is getting the DriverManager.getConnection in our createConnection method
        //so stmt = conn.createStatement is calling our statment from DB_URL
        stmt = conn.createStatement();
        
        //if there is a database that already exists, output that it already exists(tables.next())
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        if(tables.next()){
            System.out.println("Table "+ TABLE_NAME + "Already Exists.");
        }else{
            stmt.execute("CREATE TABLE " + TABLE_NAME +"("
                    +" id varchar(200) primary key, \n"
                    +" name varchar(200),\n"
                    +" phoneNum varchar(20),\n"
                    +" email varchar(100)\n"                   
                    +")");
        }
    } catch(SQLException e){
        System.err.println(e.getMessage() + "... SetupDatabase MEMBER TABLE");
    } finally{
        
    }
}


//method created for setting up a table that displays what book was issued to which member id
//foreign keys are used to reference that the book ID is the Book id from table BOOK
void createIssueTable(){
    final String TABLE_NAME = "ISSUE";
    try{
        //creates a statement on conn. Remember that conn is getting the DriverManager.getConnection in our createConnection method
        //so stmt = conn.createStatement is calling our statment from DB_URL
        stmt = conn.createStatement();
        
        //if there is a database that already exists, output that it already exists(tables.next())
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, TABLE_NAME.toUpperCase(), null);
        if(tables.next()){
            System.out.println("Table "+ TABLE_NAME + "Already Exists.");
        }else{
            stmt.execute("CREATE TABLE " + TABLE_NAME +"("
                    +" bookID varchar(200) primary key, \n"
                    +" memberID varchar(200),\n"
                    +" timeIssued timestamp default CURRENT_TIMESTAMP,\n"
                    +" renewCount integer default 0,\n"
                    +" FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                    +" FOREIGN KEY (memberID) REFERENCES MEMBER(id)"
                    +")");
        }
    } catch(SQLException e){
        System.err.println(e.getMessage() + "... SetupDatabase ISSUE TABLE");
    } finally{
        
    }
    
}


//method used for deleteing books, this one is using a prepared statement whereas the others used simple queries
public boolean deleteBook(Book book){
    //boolean to tell whether the statement executed properly
    boolean confirm = false;
    try {
        //use preparedstatement for this query (?)
        String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
        //prepared statement action 
        PreparedStatement stmt = conn.prepareStatement(deleteStatement);
        stmt.setString(1, book.getId());
        int result = stmt.executeUpdate();
        //used for diagnosing if true
        System.out.println(result);
        if(result == 1){
        confirm = true;
        }
        
    } catch (SQLException ex) {
        confirm = false;
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return confirm;
}

//this method is used to delete a MEMBER by calling the ID by passing a Member object
public boolean deleteMember(Member member){
    //boolean to tell whether the statement executed properly
    boolean confirm = false;
    try {
        //use preparedstatement for this query (?)
        String deleteStatement = "DELETE FROM MEMBER WHERE ID = ?";
        //prepared statement action 
        PreparedStatement stmt = conn.prepareStatement(deleteStatement);
        stmt.setString(1, member.getId());
        int result = stmt.executeUpdate();
        //used for diagnosing if true
        System.out.println(result);
        if(result == 1){
        confirm = true;
        }
        
    } catch (SQLException ex) {
        confirm = false;
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return confirm;
}
//this method checks if the book is issued by getting the book id from the ISSUE table, and seeing if there is a value>0;
public boolean isBookIssued(Book book){
      try {
        //use preparedstatement for this query (?)
        String checkstatement = "SELECT COUNT(*) FROM ISSUE WHERE bookID = ?";
        //prepared statement action 
        PreparedStatement stmt = conn.prepareStatement(checkstatement);
        stmt.setString(1, book.getId());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            int count = rs.getInt(1);
             System.out.println(count);
             //if count > 0 return true
            return(count>0);
           
           
        }
      
        
        
    } catch (SQLException ex) {
       
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
         
    }
      //else return false;
    return false;
}

//this method checks to see if any member has books issued by checking if the count is more than 0
//by default the count is 0 so if the count is > 0 that means there is a book issued to that member
public boolean doesMemberHaveBooks(Member member){
      try {
        //use preparedstatement for this query (?)
        String checkstatement = "SELECT COUNT(*) FROM ISSUE WHERE memberID = ?";
        //prepared statement action 
        PreparedStatement stmt = conn.prepareStatement(checkstatement);
        stmt.setString(1, member.getId());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            int count = rs.getInt(1);
             System.out.println(count);
             //if count > 0 return true
            return(count>0);
           
           
        }
      
        
        
    } catch (SQLException ex) {
       
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
         
    }
      //else return false;
    return false;
}

//method used for updating book by passing in a book object and using the book information to fill in the passed values
public boolean updateBook(Book book){
    try {
        String edit = "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PUBLISHER = ? WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(edit);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setString(3, book.getPublisher());
        stmt.setString(4, book.getId());
        int res = stmt.executeUpdate();
        return (res>0);
        
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
        return false;
}

//method used for executing update statement by passing in a Member object
//gets the member object passed by calling this method, then based on the values of the member, 
//will execute the following statement
public boolean updateMember(Member member){
      try {
          //prepared statement UPDATE statement
        String edit = "UPDATE MEMBER SET NAME = ?, PHONENUM = ?, EMAIL = ? WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(edit);
        //gets the name phone email and id to set it to the question marks values
        stmt.setString(1, member.getName());
        stmt.setString(2, member.getPhoneNum());
        stmt.setString(3, member.getEmail());
        stmt.setString(4, member.getId());
        int res = stmt.executeUpdate();
        //if the result is not 0 will execute true and execute the function if result is 0 will return false
        return (res>0);
        
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
        return false;
          
}

}
