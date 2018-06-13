import java.util.*;

class Display {
    private Symbol player;
    private Scanner sc;

    String inputs(Symbol player) {
        this.player = player;
        sc = new Scanner(System.in);
        if(player == Symbol.XS) {
            System.out.print("Player X's move: ");
        }
        else if(player == Symbol.OS) {
            System.out.print("Player O's move: ");
        }
        String pos = sc.nextLine();

        return pos;
    }

    void printBoard(char[][] cells) {

        System.out.println("    123" + "\n");
        for(int i=0;i<cells.length;i++) {
            System.out.print(""+(char)('a'+i)+"   ");
            for(int j=0;j<cells[i].length;j++) {
                System.out.print(cells[i][j]);
            }
            System.out.print('\n');
        }
    }
}