/*

 */
package main.java.ia;

import main.java.model.State;

public abstract class AI {

    State state;
    StoringConfig OriginalConfig;
    Heuristics heuristics;
    public AIMove getNextMove(State state) {
        System.err.println("Erreur : AI abstraite");
        return null;
    }
}
