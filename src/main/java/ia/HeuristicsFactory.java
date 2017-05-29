/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.Consts;

public class HeuristicsFactory {

    public Heuristics buildHeuristics(int difficulty, Core core) {
        switch (difficulty) {
            case Consts.EASY:
                return new EasyHeuristics(core);
            case Consts.MEDIUM:
                return new MediumHeuristics(core);
            case Consts.HARD:
                return new MediumHeuristics(core, Consts.HARD);
            default:
                System.err.println("Erreur : niveau de difficulté inconnu dans la HeuristicsFactory");
                return new EasyHeuristics(core);
        }
    }
}
