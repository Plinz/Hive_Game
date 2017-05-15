/*

 */
package main.java.ia;

import main.java.model.Core;

public abstract class AI {

    Core core;
    StoringConfig OriginalConfig;
    Heuristics heuristics;
    public AIMove getNextMove(Core core) {
        System.err.println("Erreur : AI abstraite");
        return null;
    }
}
