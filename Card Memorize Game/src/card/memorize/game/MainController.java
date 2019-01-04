/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.memorize.game;

import card.memorize.game.Card.Card;
import card.memorize.game.Card.cardIndex;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ivans
 */
public class MainController implements Initializable {

    
      
    /*Method that executes upon opening this FXML file(window)*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //create our gameboard  
      start();
      //cardval Variable used to store the value of cardCount
      cardVal = new int[36];
      //initialize our grid
      grid = new GridPane();
      //cardIndex used to store the column index and row index of our imageArray
      cardIndx = new cardIndex[36];
      for(int row = 0;row<6;row++){
      for(int col = 0;col<6;col++){ 
      //cardVal[col is equal to cardCount(0) and then val is equal to cardCount(0) val = 0 etc etc important
      //because val stops at 35 instead of 36
      cardVal[col] = cardCount;
      val = cardVal[col];
      //we created CardIndex object to store col and row values, and store them into an array to sync them up
      //with our imageArr[]
      cardIndx[cardCount] = new cardIndex(col, row);
      imageArr[cardCount] = new ImageView(new Image("Cards/back.jpg"));
      //call our function to add the val to each Event in our image array
      setImageOnPress(imageArr[cardCount], val);
          
          grid.add(imageArr[cardCount],col,row);
          grid.setHgap(5);
          grid.setVgap(5);
          //System.out.println(grid);
          //grid.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");          
          //mainPane.getChildren().add(grid);
          
          cardCount++;
        }
      }
       vBoxPane.setAlignment(Pos.CENTER);
       vBoxPane.getChildren().add(grid);
       
    }
    
    @FXML
    private VBox vBoxPane;
    
    @FXML
    private AnchorPane mainPane;
    
    /*Variables (Global) that will be used in creating our program*/
            Card deck[];
            Card temparray[];
            Card gameBoard[];
            boolean clickable=true;
            int ctr=0;
            int correct=0;
            int clicked[]=new int[]{0,1};
            
            /*Initialize variables*/
            int cardCount = 0;
            cardIndex[] cardIndx;
            
            int cardVal[];
            int val;
            ImageView imageArr[] = new ImageView[36];
            GridPane grid;
   
    @FXML
    private void handleButtonAction(ActionEvent event) {
        for(int i = 0;i<36;i++){
            System.out.println(gameBoard[i].toString());
        }
    }
    
    public void setImageOnPress(ImageView image, int revealValue ){
         image.setOnMousePressed((MouseEvent event) -> {
                   /*TODO, this will display card 35, because it only reads the
                   last number that our CardCount variable currently is at
                   (vs the javascript that assigns each card a mouseevent, where they click,
                   and it assigns the card to the card it was assigned to)
                   find a way to assign each imageview/gridpane to each individual number
                   */
                   
                   revealImage(revealValue);         
       });
    }
    
    public void start(){
        /*Set our background*/
        setBackground();
        /*Set logic for our cards*/
        int numOfCards = 52;
        //initialize our deck array
        deck = new Card[numOfCards];
        /*Our gameboard will hold 36 cards*/
        gameBoard = new Card[36];
        temparray = new Card[36];
        
        
        //create an array of objects (cards)
        Card cards[] = new Card[numOfCards];
        //iterate through the length of our object array, and assign each object of the array into a new Card(i) object value
        for(int i = 0;i<cards.length;i++){
            cards[i] = new Card(i);
            
        }
        /*shuffle the cards by creating an index array*/
        int index[] = new int[numOfCards];
        /*fill the array with values 0-52*/
        for(int i = 0;i<index.length;i++){
            index[i] = i;
        }
        
        /*mix up the indexed array*/
       for(int i = 0;i<index.length;i++){
           //Create a random value from 1-52
           int temp = (int) (Math.random()*51);
          //System.out.println("temp value: " + temp);
          //create a variable named value, and set it equal to whatever the index is at
           int val = index[i];
           //set whatever index we're at equal to the index of the random number
           index[i] = index[temp];
           //set the random index equal to the value we were at originally
           index[temp] = val;
           //System.out.println(val);
       }//end of for loop
       
       /*put the cards in the deck*/
       for(int i = 0;i<52;i++){
           //System.out.println(index[i]);

           /*We set the deck, equal to the randomized cards, making the deck equal to random cards in order*/
           deck[i] = cards[index[i]];
           
           /*This will print out a set of 52 cards in random order in our deck*/
           //System.out.println(deck[i].toString());
                      
       }//end of for loop
       
       /*Place the cards onto the gameboard 36 is the size of our gameboard(6x6)*/
       for(int i = 0;i<36;i++){
           if(i<18){
               gameBoard[i] = deck[index[i]];
               //System.out.println(gameBoard[i].toString());
           }else{
               gameBoard[i] = deck[index[i-18]];
               //System.out.println(gameBoard[i].toString());

           }
       } //end of for loop
       
       randomize();
    }
    
   
    /*this method will take our temparray variable, and mix up the card order from our gameBoard array*/
    public void randomize(){
        int random =  (int) (Math.random()*36);
        for(int i = 0;i<7;i++){
            for(int j = 0;j<36;j++){
               temparray[1] = gameBoard[random];
               temparray[2] = gameBoard[j];
               gameBoard[j] = temparray[1];
               gameBoard[random] = temparray[2];
               
            }
        }
        
    }
    
   
    /*This method will reveal the image on our board*/
    public void revealImage(int current){
        System.out.println(gameBoard[current].getPicture());
        if(clickable){
            clickable = false;
            /*Getting the image in our ImageArray[current] and setting the image to the new image from our
            card Object in our gameBoard, adding the new image to the grid, to display, and using cardIndx
            object to store the col Index and Row Index*/
            imageArr[current] = new ImageView(new Image(gameBoard[current].getPicture()));
            grid.add(imageArr[current], cardIndx[current].getColIndex(), cardIndx[current].getRowIndex());
 
            if(ctr == 0){
                clicked[0] = current;
                clickable = true;
                ctr++;
            }
            
           else{
               imageArr[current] = new ImageView(new Image(gameBoard[current].getPicture()));
               grid.add(imageArr[current], cardIndx[current].getColIndex(), cardIndx[current].getRowIndex());
               
               clicked[1] = current;
               ctr = 0; 
               try {
                    
                    Thread.sleep(300);
                    compare();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
            
              }
             
        
        
    }
    
    public void compare(){
        if(gameBoard[clicked[0]] == gameBoard[clicked[1]]){
            correct++;
            clickable = true;
        }else if(gameBoard[0] != gameBoard[1]){
            imageArr[clicked[0]] = new ImageView(new Image("Cards/back.jpg"));
            imageArr[clicked[1]] = new ImageView(new Image("Cards/back.jpg"));
            grid.add(imageArr[clicked[0]], cardIndx[clicked[0]].getColIndex(), cardIndx[clicked[0]].getRowIndex());
            grid.add(imageArr[clicked[1]], cardIndx[clicked[1]].getColIndex(), cardIndx[clicked[1]].getRowIndex());
            setImageOnPress(imageArr[clicked[0]], clicked[0]);
            setImageOnPress(imageArr[clicked[1]], clicked[1]);
            clickable = true;
        }
    }
    
    
    
     /*method that Sets the background for our MainPane*/
    public void setBackground(){
    Image background = new Image("Cards/table.jpg", 192, 80, false, true);
    BackgroundImage backgroundImg = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT, null);
    mainPane.setBackground(new Background(backgroundImg));
    }
    
    
    
    
    
    
    
    
    
      //method that will test the cards and create a list of cards
    public void test(){
         int nCards = 52;
         Card deck[] = new Card[nCards];
        //for loop that iterates the size of nCards(52)
           for(int i = 0; i<nCards;i++){
               //create a new Card object, and set it equal to (i) print out the current card.
               deck[i] = new Card(i);
               System.out.print(deck[i].toString());
            }
    }

}


/*
 ImageView tst;
     
      for(int row = 0;row<6;row++){
         //tst= new ImageView(new Image("Cards/back.jpg"));
          //tst.setLayoutY(100);

          //document.write(<tr>);
          for(int col = 0;col<6;col++){
               //testing out different ways to output our cards. still have to find way 
               //to add an event listener to the tst/hook each card to an object in our gameboard

              tst = new ImageView(new Image("Cards/back.jpg"));
               tst.setLayoutX(10 * cardCount);
               
               tst.setOnMousePressed((MouseEvent event) -> {
                   /*TODO, this will display card 35, because it only reads the
                   last number that our CardCount variable currently is at
                   (vs the javascript that assigns each card a mouseevent, where they click,
                   and it assigns the card to the card it was assigned to)
                   find a way to assign each imageview/gridpane to each individual number
                   */

                  /* revealImage(cardCount);         
       });
               mainPane.getChildren().add(tst);
               
              cardCount++;
              
          }
          //tst.setLayoutY(100 * row);
//document.write(</tr>);
      }


*/