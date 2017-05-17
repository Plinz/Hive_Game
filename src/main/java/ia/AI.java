/*

 */
package main.java.ia;

import main.java.engine.Core;

public abstract class AI {

    Core core;
    Heuristics heuristics;
    public String getNextMove(Core core) {
        System.err.println("Erreur : AI abstraite");
        return null;
    }
}
