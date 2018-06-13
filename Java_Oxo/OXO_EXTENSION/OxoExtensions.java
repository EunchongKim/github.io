import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* Make a game playing with AI using Graphics */
class OxoExtensions extends JPanel {

    private final int ROW = 3;
    private final int COL = 3;
    private JButton cells[] = new JButton[ROW*COL];
    private BoardExtensions boards = new BoardExtensions(cells);
    private SymbolExtensions player = SymbolExtensions.BLANK;

    OxoExtensions() {
        setLayout(new GridLayout(ROW,COL));
        initBoard();
    }

    void initBoard() {
        for(int i=0;i<ROW*COL;i++) {
            cells[i] = new JButton();
            cells[i].setText(player.getsymbol());
            cells[i].addActionListener(boards);
            cells[i].setFont(new Font("Helvetica", Font.PLAIN, 60));
            add(cells[i]);
        }
    }

    public static void main(String[] args) {
        JFrame grid = new JFrame("OXO");
        OxoExtensions play = new OxoExtensions();
        grid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grid.getContentPane().add(play);
        grid.setBounds(200,200,300,300);
        grid.setVisible(true);
    }
}