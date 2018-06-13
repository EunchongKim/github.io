/* An item in an adventure game.  It has a boolean to indicates whether it is
being carried.  It responds to take and drop verbs. */

import java.io.*;
import java.util.*;

class Thing extends Entity {
    private boolean carried;

    Thing(String name, String description)
    {
        super(name, description);
    }

    // Respond to the verbs take and drop
    Place act(Place here, String verb, PrintStream out) {
        if (verb.equals("take")) take(here, out);
        else if (verb.equals("drop")) drop(here, out);
        else out.println("You can't " + verb + " it.");
        return here;
    }

    // Take the item by marking it as being carried
    private void take(Place here, PrintStream out) {
        if (carried) {
            out.println("You are already carrying it.");
            return;
        }
        out.println("You pick up the " + name);
        carried = true;
    }

    // Take the item by marking it as not being carried
    private void drop(Place here, PrintStream out) {
        if (! carried) {
            out.println("You aren't carrying it.");
            return;
        }
        out.println("You drop the " + name);
        carried = false;
    }

    // When the player moves, and this object is being carried, move
    // with the player.
    void move(Place here, Place there, PrintStream out) {
        if (! carried) return;
        String parse[] = name.split(" ");
        here.get(parse[parse.length-1]);
        there.put(this);
    }

    // When arriving in a place, announce things which aren't being carried.
    void arrive(Place here, PrintStream out) {
        if (carried) return;
        out.println(description);
    }
}
