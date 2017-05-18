/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.CoordGene;

public abstract class AI {

    Core core;
    Heuristics heuristics;
    public CoordGene<String> getNextMove(Core core) {
        System.err.println("Erreur : AI abstraite");
        return null;
    }
}
