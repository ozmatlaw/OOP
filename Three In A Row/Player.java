import java.util.*;

public class Player {

    Scanner in = new Scanner(System.in);
    public Player(){}

    public void playTurn(Board board, Mark mark){
        while(true){
            System.out.print("enter coordinates: ");
            int coordinates = in.nextInt();
            int col = coordinates%10 -1;
            int row = coordinates/10 -1;
            if (board.putMark(mark,row ,col)) {
                break;
            }
            else{
                System.out.println("invalid coordinates please enter valid coordinates");
            }
        }
    }
}

