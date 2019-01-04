
package card.memorize.game.Card;

/*Card Class*/
public class Card {
    /*a Deck of cards has 52 Card in it, we make the MAX amount of cards in a deck 52(CONSTANT)*/
    private final int MAXCARDS = 52;
    private final int MINCARDS = 0;
    /*Our variables, a card has a number, face value, suit, name, and a picture*/
    private int number;
    private int faceVal;
    private String suit;
    private String name;
    private String picture;
    
    /*constructor will take in a number Greater than 0 and less than 52*/
    public Card(int num){
        /*if the number passed in is greater than 0 and less than 52, it will set the number variable to the num
        and depending on the number variable now set, will perform the methods accordingly*/
        if(num>=MINCARDS&&num<MAXCARDS){
            this.number = num;
            setFace();
            setSuit();
            setName();
            setPicture();
        }else{
            this.number = -1;
            this.faceVal = -1;
            this.suit = "none";
            this.name = "none";
            this.picture = "none";
        }
    }
    //Method to set the Face of the card depending on our number passed in
    public void setFace(){
        int num = this.number%13+1;
        if(num > 10){num = 10;}
        else if(num == 1){num = 11;}
        if(num>=2&&num<=11){this.faceVal=num;}
        else{this.faceVal=-2;}
    }
    //Method to set the Suit of the card depending on our number passed in
    public void setSuit(){
    if(this.number<13){this.suit = "Spades";}
    else if(this.number<26){this.suit = "Diamonds";}
    else if(this.number<39){this.suit = "Clubs";}
    else if(this.number<52){this.suit = "Hearts";}
    else{this.suit = "Bad Value";}
    }
    
    
    //Method to set the Name of our card depending on the Number variable
    public void setName(){
        switch(this.number%13){
            case 0: this.name = "Ace"; break;
            case 1: this.name = "Two"; break;
            case 2: this.name = "Three"; break;
            case 3: this.name = "Four"; break;
            case 4: this.name = "Five"; break;
            case 5: this.name = "Six"; break;
            case 6: this.name = "Seven"; break;
            case 7: this.name = "Eight"; break;
            case 8: this.name = "Nine"; break;
            case 9: this.name = "Ten"; break;
            case 10: this.name = "Jack"; break;
            case 11: this.name = "Queen"; break;
            case 12: this.name = "King"; break;
            default: this.name = "Bad Value";
        }
    }
    
    public void setPicture(){
        this.picture = "Cards/" + this.name + this.suit + ".jpg";
    }

    @Override
    public String toString() {
        return "<img src =" + this.picture + " />"
                +"\n Number = " + this.number
                +"\n Name = " + this.name
                +"\n Suit = " + this.suit
                +"\n Face Value = "+ this.faceVal
                +"\n \n";
    }
    
    
    
                    /*Getters*/
    public int getNumber() {return number;}
    public int getFaceVal() {return faceVal;}
    public String getSuit() {return suit;}
    public String getName() {return name;}
    public String getPicture() {return picture;}
}
