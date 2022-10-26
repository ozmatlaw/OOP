
public class Board {

    public static final int SIZE = 4;
    public static final int WIN_STREAK = 3;
    public static Mark[][] board = new Mark[SIZE][SIZE];
    public static Mark winner;

    public Board(){
        for(int row =0; row<SIZE; row++){
            for(int col = 0; col< SIZE; col++){
                board[row][col] = Mark.BLANK;
            }
        }
        winner = Mark.BLANK;
    }

    public boolean putMark(Mark mark, int row, int col){
        if(row>= SIZE || row< 0 || col<0 || col>= SIZE || getMark(row,col)!=Mark.BLANK){
            return false;
        }
        board[row][col]= mark;
        if(isGameOver(mark, row, col)){
            winner= mark;
        }
        return true;
    }

    private int countMarkInDirection(int row, int col, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while(row < SIZE && row >= 0 && col < SIZE && col >= 0 && board[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    boolean isGameOver(Mark mark, int row, int col){
        int count = 0;
        int count2 = 0;
        count = countMarkInDirection(row, col, -1, 0, mark)+
                countMarkInDirection(row, col, 1, 0, mark)-1;
        if(inARow(count))return true;// up and down
        count = countMarkInDirection(row, col, 0, -1, mark)+
                countMarkInDirection(row, col, 0, 1, mark)-1;
        if(inARow(count))return true; // sides
        count = countMarkInDirection(row, col, 1, 1, mark)+
                countMarkInDirection(row, col, -1, -1, mark)-1;
        count2 = countMarkInDirection(row, col, 1, -1, mark)+
                countMarkInDirection(row, col, -1, 1, mark)-1;

        return inARow(count) || inARow(count2); // diagonals
    }

    boolean gameEnded(){
        return getWinner() != Mark.BLANK;
    }

    boolean inARow(int count){

        return count >= WIN_STREAK;
    }

    public Mark getMark(int row, int col)
    {
        if(row>=Board.SIZE || col>=Board.SIZE || row<0 || col<0) {
            return Mark.BLANK;
        }
        return board[row][col];
    }

    Mark getWinner()
    {
        return winner;
    }

}
