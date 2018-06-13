/* A puzzle entity in an adventure game. It has a custom verb which it respons
to, e.g. "open".  It has an item which the player must have in order to solve
the puzzle, e.g. a key.  It has a failure message printed out if the player
doesn't have the needed item, and a success message if the puzzle is solved.
When the puzzle is solved, some entities are added to the current place,
e.g. things or exits which are now revealed, or a replacement for the puzzle to
change its state. */

import java.util.*;
import java.io.*;

class Puzzle extends Entity {
    private String verb, need, need0, need1, failure, failure0, failure1, success;
    private List<Thing> contents;

    Puzzle(String name, String description)
    {
        super(name, description);
        contents = new ArrayList<Thing>();
    }

    void action0(String verb, String need, Thing[] contents,
        String failure, String success)
    {
        this.verb = verb;
        this.need = need;
        this.failure = failure;
        this.success = success;
        for (Thing x: contents) this.contents.add(x);
    }

    // After constructing the puzzle, set up its parameters.
    void action1(String verb, String need0, String need1, Thing[] contents,
        String failure0, String failure1, String success)
    {
        this.verb = verb;
        this.need0 = need0;
        this.need1 = need1;
        this.failure0 = failure0;
        this.failure1 = failure1;
        this.success = success;
        for (Thing x: contents) this.contents.add(x);
    }

    // Announce the puzzle when the player finds it
    void arrive(Place here, PrintStream out) {
        out.println(description);
    }

    // Respond to the triggering verb
    Place act(Place here, String verb, PrintStream out) {
        if (! verb.equals(this.verb)) {
            out.println("Do what to the " + name + "?");
            return here;
        }
        if (here.find(need)==null && failure!=null) {
            out.println(failure);
            return here;
        }
        if (here.find(need0)==null && here.find(need1)==null && failure0!=null) {
            out.println(failure0);
            out.println(failure1);
            return here;
        }
        if (here.find(need0)==null && failure0!=null) {
            out.println(failure0);
            return here;
        }
        if (here.find(need1)==null && failure1!=null) {
            out.println(failure1);
            return here;
        }
        System.out.println(success);
        for (Thing c: contents) {
            out.println(c.description);
            here.put(c);
        }
        return here;
    }
}
