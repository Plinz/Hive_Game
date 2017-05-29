/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.Consts;

public class AIFactory {

    public static AI buildAI(int difficulty, Core core) {
        switch (difficulty) {
            case Consts.EASY:
                return new EasyAI(core);
            case Consts.MEDIUM:
                return new MediumAI(core);
            case Consts.HARD:
                return new MediumAI(core, Consts.HARD);
            default:
                System.err.println("Erreur : niveau de difficulté inconnu");
                return new EasyAI(core);
        }
    }
}
