import java.util.*;

/* A Oxo manages whole process of game step by step including tests
 * Fisrt player is X */
class Oxo {
    private final int ROW = 3;
    private final int COL = 3;
    private Display displays;
    private Symbol player = Symbol.XS;
    private char[][] cells = new char[ROW][COL];

    void run(String[] args) {
        displays = new Display();
        
        if (args.length != 0) {
            System.err.println("Use: java Oxo");
            System.exit(1);
        }
        test();
        init();
        player = Symbol.XS;
        while(player != Symbol.FINISH) {
            String pos = displays.inputs(player);
            Board boards = new Board(player);
            boards.makeInput(pos);
            boards.fail1();
            boards.makeCell(cells);
            boards.fail2();
            boards.printResult(cells);
            displays.printBoard(cells);
            player = boards.changePlayer();
        }
    }

    void init() {
        player = Symbol.BLANK;
        for(int i=0; i<ROW; i++) {
            for(int j=0; j<COL; j++) {
                cells[i][j] = player.getsymbol();
            }
        }
        displays.printBoard(cells);
    }
    
    void test() {
        Board fortest = new Board(player);
        fortest.testInvalid();
        fortest.testMakeBoard();
        fortest.testResult();
        fortest.testOverlap();
        fortest.testPlayer();
    }
    
    public static void main(String[] args) {
        Oxo play = new Oxo();
        play.run(args);
    }
}