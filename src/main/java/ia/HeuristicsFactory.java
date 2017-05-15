/*

 */
package main.java.ia;

import main.java.utils.Consts;

public class HeuristicsFactory {

    public Heuristics buildHeuristics(int difficulty, GameConfig config) {
        switch (difficulty) {
            case Consts.EASY:
                return new EasyHeuristics(config);
            case Consts.MEDIUM:
                return new MediumHeuristics(config);
            case Consts.HARD:
                return new HardHeuristics(config);
            default :
                System.err.println("Erreur : niveau de difficulté inconnu dans la HeuristicsFactory");
                return new EasyHeuristics(config);
        }
    }
}
