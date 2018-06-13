import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* Make a board of user and AI in a Graphic mode,
 * find the best choice of AI in each turn,
 * and check winner or draw */
class BoardExtensions implements ActionListener {

    private final int ROW = 3;
    private final int COL = 3;
    private int turn = 0, option = 0;
    private boolean checkRep = false;
    private JButton board[] = new JButton[ROW*COL];
    private String copy[] = new String[ROW*COL];
    private String s = "";
    private SymbolExtensions player;

    BoardExtensions(JButton board[]) {
        this.board = board;
    }

    void resetBoard() {
        player = SymbolExtensions.BLANK;
        for(int i=0;i<ROW*COL;i++) {
            board[i].setText(player.getsymbol());
        }
        turn = 0;
    }

    int chooseClose() {
        Object[] options = {"Close", "Continue"};
        option = JOptionPane.showOptionDialog(null, s, "FINISHED",
                 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                 null, options, options[1]);
        if(option == 0) System.exit(0);
        return option;
    }

    // Make actions of each button
    public void actionPerformed(ActionEvent e) {
        JButton inputs = (JButton)e.getSource();
        makeBoard(inputs);
        if(turn!=0 && checkRep!=true) makeAIBoard();
        if(turn > ROW*COL/2) {
            s = "Draw. Bye!";
            chooseClose();
            resetBoard();
        }
    }

    // Make boards of user inputs and check if user wins or not,
    // prevent overlapped inputs
    void makeBoard(JButton inputs) {
        player = SymbolExtensions.XS;
        if(inputs.getText() != "") {
            JOptionPane.showConfirmDialog(null, "Choose another cell");
            turn--;
            checkRep = true;
        }
        else {
            inputs.setText(player.getsymbol());
            setColor(inputs);
            checkRep = false;
        }
        turn++;
        copyBoard();
        if(checkWin()==true) {
            s = "Congrats! YOU won";
            chooseClose();
            resetBoard();            
        }
    }

    // In each iteration, calculate current empty cells' scores according to win strategies,
    // firstly occupy center if it is empty.
    // Make boards of AI and check if AI wins or not
    void makeAIBoard() {
        player = SymbolExtensions.OS;
        int i = 0;
        int[] rating = new int[ROW*COL];
        initRating(rating);
        while(i<ROW*COL) {
            if(Cell(i).equals("")) {rating[i] = checkStrategy(i);}
            i++;
        }
        i = findMax(rating);
        if(Cell(4).equals("")) {i = 4;}
        if(Cell(i).equals("")) {
            board[i].setText(player.getsymbol());
            setColor(board[i]);
        }
        copyBoard();
        if(checkWin()==true) {
            s = "Sorry, AI won";
            chooseClose();
            resetBoard();            
        }
    }

    // Fill the actual board copied from temp_copy string board
    void copyBoard() {
        for(String s : copy) s = "";
        for(int i=0;i<ROW*COL;i++) copy[i] = Cell(i);
    }

    // If this cell can make AI win, give a score 100,
    // if this cell can prevent user win, give a score 50,
    // basic scores on empty cell is 10
    int checkStrategy(int i) {
        int score=10;
        player = SymbolExtensions.OS;
        if(twoInaRow(i)) score+=100;
        player = SymbolExtensions.XS;
        if(twoInaRow(i)) score+=50;
        player = SymbolExtensions.OS;
        return score;
    }

    boolean twoInaRow(int i) {
        copyBoard();
        copy[i] = player.getsymbol();
        if(checkWin()==true) return true;
        return false;
    }

    int findMax(int rating[]) {
        int max=0;
        for(int i=0;i<rating.length;i++) {
            max = rating[i] > rating[max] ? i : max;
        }
        return max;
    }

    void initRating(int rating[]) {
        for(int i=0;i<rating.length;i++) {rating[i] = 0;}
    }

    void setColor(JButton inputs) {
        if(player==SymbolExtensions.XS) inputs.setForeground(Color.BLUE);
        else if(player==SymbolExtensions.OS) inputs.setForeground(Color.RED);
    }

    boolean checkWin() {
        if(checkCOL()==true) return true;
        if(checkROW()==true) return true;
        if(checkDiagonal()==true) return true;
        else return false;         
    }

    boolean checkCOL() {
        int i=0;
        while(i<ROW*COL) {
            if(!copy[i].equals("") && copy[i].equals(copy[i+1]) &&
                copy[i+1].equals(copy[i+2])) return true;
            i = i+COL;
        }
        return false;      
    }

    boolean checkROW() {
        int i=0;
        while(i<ROW) {
            if(!copy[i].equals("") && copy[i].equals(copy[i+ROW]) &&
                copy[i+ROW].equals(copy[i+ROW*2])) return true;
            i++;
        }
        return false;     
    }

    boolean checkDiagonal() {
        if(!copy[4].equals("") && copy[0].equals(copy[4]) &&
            copy[4].equals(copy[8])) return true;
        if(!copy[4].equals("") && copy[2].equals(copy[4]) &&
            copy[4].equals(copy[6])) return true;
        else return false;    
    }
        
    String Cell(int i) {
        return board[i].getText();
    }        
}
