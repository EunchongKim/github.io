import java.io.*;
import java.util.*;

class Stag {
    Scanner scanner;
    Place here;
    String verb, noun;
    PrintStream out;

    void setup() {
        scanner = new Scanner(System.in);
        out = System.out;
        Place house = new Place("house",
            "You are Alice in the house in Wonderland.\n" +
            "You can see the weird room."
        );
        Place room = new Place("weird room",
            "You are in the weird room."
        );
        Place garden = new Place("beautiful garden of the Queen",
            "You are in the beautiful garden of the Queen."
        );
        Thing potion = new Thing("drink me potion",
            "There is a drink me potion lying on the table."
        );
        Thing key = new Thing("golden key",
            "There is a golden key lying on the table."
        );
        Thing water = new Thing("water",
            "You get a bottle of water. Now you can have the potion.\n" +
            "Try open the little door."
        );
        Thing pass = new Thing("pass",
            "You can pass the little door. Go beautiful garden of the Queen."
        );
        Puzzle box = new Puzzle("box",
            "There is a box here."
        );
        Puzzle door = new Puzzle("little door",
            "You can see the garden through the little door."
        );
        box.action0("open", "golden key", new Thing[] {water},
            "You need a key.",
            "You open the box."
        );
        door.action1("open", "drink me potion", "water", new Thing[] {pass},
            "You need a potion.", "You need a water to drink the potion.",
            "Your body shrinked."
        );
        house.links(room, garden, box, door);
        room.links(house, potion, key);
        garden.links(house);
        here = house;
    }

    void run() {
        setup();
        here.act(here, "go", out);
        while (true) {
            read();
            Entity e = here.find(noun);
            if (e == null) {
                out.println("What " + noun + "?");
                continue;
            }
            here = e.act(here, verb, out);
        }
    }

    void read() {
        System.out.print("> ");
        String line = scanner.nextLine();
        String[] words = line.split(" ");
        verb = words[0];
        noun = "";
        for(int i=1;i<words.length;i++) {
            noun += words[i];
            if(i!=words.length-1) noun += " ";
        }
    }

    public static void main(String[] args) {
        Stag program = new Stag();
        program.run();
    }
}
