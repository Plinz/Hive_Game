/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.Consts;

public class AIFactory {
    
    public static AI buildAI(int difficulty){
        switch (difficulty){
            case Consts.EASY :
                return new EasyAI();
            default :
                System.err.println("Not implemented yet");
                return null;
        }
    }
}
