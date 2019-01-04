/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.memorize.game.Card;

/**
 *
 * @author beandog
 */
public class cardIndex {
    private int colIndex;
    private int rowIndex;
    
    public cardIndex(int col, int row){
        this.colIndex = col;
        this.rowIndex = row;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    
    
}
