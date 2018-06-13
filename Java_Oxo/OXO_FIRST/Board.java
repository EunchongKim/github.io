import java.util.*;

/* A Board makes and represents the current state of the game,
 * checks fail cases and game results, prints them, changes the player's turn
 * Test units and print "Game Start" if passed */
public class Board {
    private int row, col;
    private boolean fail1 = false, fail2 = false, result = false;
    private Symbol player;

    Board(Symbol player) {
        this.player = player;
    }

    // Convert user input to a position of cell
    // Catch illegal inputs
    boolean makeInput(String pos) {
        if(pos.matches("[a-c][1-3]")) {
            row = pos.charAt(0) - 'a';
            col = Character.getNumericValue(pos.charAt(1)) - 1;
            return fail1=false;
        }
        return fail1=true;
    }

    // Occupy the position as requested
    // Catch overlapping inputs
    void makeCell(char[][] cells) {
        if(!fail1) {
            if(cells[row][col]!='.')
                fail2 = true;
            else cells[row][col] = player.getsymbol();
        }
    }

    // Change player's turn of game,
    // and make it finish if results are decided
    Symbol changePlayer() {
        if(result) player = Symbol.FINISH;
        else if(player==Symbol.XS && !fail1 && !fail2)
            player =  Symbol.OS;
        else if(player==Symbol.OS && !fail1 && !fail2)
            player =  Symbol.XS;
        return player;
    }

    boolean checkWin(char[][] cells) {
        int j=0;
        for(int i=0;i<cells.length;i++) {
            if((cells[i][j]==player.getsymbol() &&
               cells[i][j+1]==player.getsymbol() &&
               cells[i][j+2]==player.getsymbol()) ||
               (cells[j][i]==player.getsymbol() &&
                cells[j+1][i]==player.getsymbol() &&
                cells[j+2][i]==player.getsymbol())) {
                return true;
            }
        }
        if(cells[j+1][j+1]==player.getsymbol()) {
            if((cells[j][j]==player.getsymbol() &&
               cells[j+2][j+2]==player.getsymbol()) ||
               (cells[j][j+2]==player.getsymbol() &&
                cells[j+2][j]==player.getsymbol())) {
                return true;
            }
        }
        return false;
    }

    boolean checkDraw(char[][] cells) {
        for(int i=0;i<cells.length;i++) {
            for(int j=0;j<cells[i].length;j++) {
                if(cells[i][j]=='.') return false;
            }
        }
        return true;
    }

    // Check game results and print a message
    void printResult(char[][] cells) {
        if(checkWin(cells)) {
            System.out.println(">>Congrats! "+player.getsymbol()+" won<<");
            result = true;
        }
        else if(checkDraw(cells)) {
            System.out.println(">>This game is a draw<<");
            result = true;
        }
    }

    void fail1() {
        if(fail1)
            System.out.println("Try again : Select position within the range");
    }

    void fail2() {
        if(fail2)
            System.out.println("Try again : This cell is occupied");
    }

    void claim(boolean b) {
        if (!b) throw new Error("Test " + testNumber + " fails");
        testNumber++;
    }
    private int testNumber = 1;
    private final int ROW = 3;
    private final int COL = 3;
    private char[][] tests = new char[ROW][COL];

    // Test invalid inputs (tests 1 to 9)
    void testInvalid() {
        claim(makeInput("")==true);
        claim(makeInput(" ")==true);
        claim(makeInput("a")==true);
        claim(makeInput("1")==true);
        claim(makeInput("a4")==true);
        claim(makeInput("d1")==true);
        claim(makeInput("abcde")==true);
        claim(makeInput("11")==true);
        claim(makeInput("a11")==true);
    }

    // Test if inputs go through to right positions of board (tests 10 to 15)
    void testMakeBoard() {
        testInit();
        player = Symbol.OS;
        makeInput("a1"); makeCell(tests);
        claim(tests[0][0]==player.getsymbol());
        makeInput("a2"); makeCell(tests);
        claim(tests[0][1]==player.getsymbol());
        claim(tests[0][2]!=player.getsymbol());
        makeInput("c3"); makeCell(tests);
        claim(tests[2][2]==player.getsymbol());
        player = Symbol.XS;
        makeInput("b1"); makeCell(tests);
        claim(tests[1][0]==player.getsymbol());
        claim(tests[0][0]!=player.getsymbol());
    }

    // Test if results of games are proper (tests 16 to 20)
    void testResult() {
        testBoard1();
        claim(checkWin(tests)==true);
        testBoard2();
        claim(checkWin(tests)==true);
        testBoard3();
        claim(checkWin(tests)==true);
        testBoard4();
        claim(checkWin(tests)==false);
        testBoard5();
        claim(checkDraw(tests)==true);
    }
 
    // Test overlapping is prevented (tests 21 to 23)
    void testOverlap() {
        testBoard1();
        makeInput("a1"); makeCell(tests);
        claim(fail2);
        makeInput("b1"); makeCell(tests);
        claim(fail2);
        makeInput("b2"); makeCell(tests);
        claim(fail2);
    }

    // Test changing players is proper (tests 24 to 26)
    void testPlayer() {
        player = Symbol.XS; fail1=false; fail2=false;
        claim(changePlayer()==Symbol.OS);
        claim(changePlayer()==Symbol.XS);
        result = true;
        claim(changePlayer()==Symbol.FINISH);

        System.out.println("Game Start !");
    }

    // Board : Win with straight line
    // XXX
    // OO.
    // XO.
    void testBoard1() {
        testInit();
        player = Symbol.OS;
        makeInput("b1"); makeCell(tests);
        makeInput("b2"); makeCell(tests);
        makeInput("c2"); makeCell(tests);
        player = Symbol.XS;
        makeInput("a1"); makeCell(tests);
        makeInput("a2"); makeCell(tests); 
        makeInput("a3"); makeCell(tests); 
        makeInput("c1"); makeCell(tests);
    }

    // Board : Win with diagonal line
    // X..
    // OXO
    // XOX
    void testBoard2() {
        testInit();
        player = Symbol.OS;
        makeInput("b1"); makeCell(tests);
        makeInput("b3"); makeCell(tests);
        makeInput("c2"); makeCell(tests);
        player = Symbol.XS;
        makeInput("a1"); makeCell(tests);
        makeInput("b2"); makeCell(tests); 
        makeInput("c1"); makeCell(tests); 
        makeInput("c3"); makeCell(tests);
    }

    // Board : Win in the last turn
    // XOO
    // XXX
    // OXO
    void testBoard3() {
        testInit();
        player = Symbol.OS;
        makeInput("a2"); makeCell(tests);
        makeInput("a3"); makeCell(tests);
        makeInput("c1"); makeCell(tests);
        makeInput("c3"); makeCell(tests);
        player = Symbol.XS;
        makeInput("a1"); makeCell(tests);
        makeInput("b1"); makeCell(tests);
        makeInput("b2"); makeCell(tests);
        makeInput("b3"); makeCell(tests);
        makeInput("c2"); makeCell(tests);
    }

    // Board : No winner, no draw
    // X..
    // OXO
    // ...
    void testBoard4() {
        testInit();
        player = Symbol.OS;
        makeInput("b1"); makeCell(tests);
        makeInput("b3"); makeCell(tests);
        player = Symbol.XS;
        makeInput("a1"); makeCell(tests);
        makeInput("b2"); makeCell(tests);
    }

    // Board : Draw
    // XOO
    // OXX
    // XXO
    void testBoard5() {
        testInit();
        player = Symbol.OS;
        makeInput("a2"); makeCell(tests);
        makeInput("a3"); makeCell(tests);
        makeInput("b1"); makeCell(tests);
        makeInput("c3"); makeCell(tests);
        player = Symbol.XS;
        makeInput("a1"); makeCell(tests);
        makeInput("b2"); makeCell(tests);
        makeInput("b3"); makeCell(tests);
        makeInput("c1"); makeCell(tests);
        makeInput("c2"); makeCell(tests);
    }

    void testInit() {
        player = Symbol.BLANK;
        for(int i=0; i<ROW; i++) {
            for(int j=0; j<COL; j++) {
                tests[i][j] = player.getsymbol();
            }
        }
    }
}
