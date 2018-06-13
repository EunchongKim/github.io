/* A place or thing or character in a game.  This class can be used directly
for scenery that does nothing.  It can be extended, to add data and override
the act method, to respond to different actions. */

import java.io.*;

class Entity {
    final String name, description;

    Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Carry out the action on the entity, assuming that the verb
    // and noun have already been matched
    Place act(Place here, String verb, PrintStream out) {
        out.println("You can't " + verb + " the " + name);
        return here;
    }

    // When the player is moving away from a place, ask an entity in the old
    // place if it wants to do anything such as move with the player.
    // The default is to do nothing.
    void move(Place here, Place there, PrintStream out) {
    }

    // When the player arrives at a new place, ask the entity if it wants to
    // do anything such as announce itself.  The default is to do nothing.
    void arrive(Place here, PrintStream out) {
    }
}
