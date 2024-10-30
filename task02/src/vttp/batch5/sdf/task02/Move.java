package vttp.batch5.sdf.task02;

public class Move {

    private int row;
    private int col;
    private int utility;

    public Move(int row, int col, int utility) {
        this.row = row;
        this.col = col;
        this.utility = utility;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    

}
