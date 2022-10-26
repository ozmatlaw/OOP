
public class Game {

//    Player[] players = {playerX, playerO};
    Mark[] marks = {Mark.X,Mark.O};
    public  Renderer renderer;
    public  Player p1;
    public  Player p2;



    public Game(Renderer renderer, Player p1, Player p2){
        this.renderer = renderer;
        this.p1 = p1;
        this.p2 = p2;
    }

    public static void main(String[] args) {
	// write your code here
        Renderer renderer = new Renderer();
        Player p1 = new Player();
        Player p2 = new Player();
        Game game = new Game(renderer,p1,p2);
        System.out.println("Game over! The winner is "+game.run());
    }

    public Mark run(){
        int counter = 0;
        Board board = new Board();
        this.renderer.renderBoard(board);
        Player player = new Player();
        while(board.getWinner() == Mark.BLANK){
            if(counter%2 == 0){
                player.playTurn(board,Mark.O);
            }else{
                player.playTurn(board,Mark.X);
            }
            this.renderer.renderBoard(board);
            counter++;
        }
        return board.getWinner();
    }
}
