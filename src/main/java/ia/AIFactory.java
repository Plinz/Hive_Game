/*

 */
package main.java.ia;

import main.java.model.State;
import main.java.utils.Consts;

public class AIFactory {
    
    public static AI buildAI(int difficulty){
        switch (difficulty){
            case Consts.EASY :
                return new RandomAI();
            default :
                System.err.println("Not implemented yet");
                return null;
        }
    }
}
