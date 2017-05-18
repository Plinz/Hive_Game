/*

 */
package main.java.ia;

import main.java.engine.Core;

public abstract class AI {

    Core core;
    Heuristics heuristics;

    public AI(Core core){
        this.core = core;
    }

    public String getNextMove() {
        System.err.println("Erreur : AI abstraite");
        return null;
    }
}
