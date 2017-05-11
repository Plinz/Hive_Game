/*

 */
package main.java.ia;

import main.java.model.State;

public abstract class AI {

    State state;
    StoringConfig OriginalConfig;

    public AIMove getNextMove(State state) {
        System.err.println("Erreur : classe non instanciée");
        return null;
    }
}
