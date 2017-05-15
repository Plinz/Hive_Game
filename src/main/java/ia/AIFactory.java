/*

 */
package main.java.ia;

import main.java.model.Core;
import main.java.utils.Consts;

public class AIFactory {
    
    public static AI buildAI(int difficulty, Core core){
        switch (difficulty){
            case Consts.EASY :
                return new EasyAI(core);
            default :
                System.err.println("Not implemented yet");
                return null;
        }
    }
}
