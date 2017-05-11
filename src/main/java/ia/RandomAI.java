/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.Random;
import main.java.model.State;

public class RandomAI extends AI {

    public RandomAI(){
        
    }
    
    public RandomAI(State state) {
        this.state = state;
        this.OriginalConfig = new StoringConfig(state);
    }

    public AIMove getNextMove(State state) {
        this.state = state;
        ArrayList<StoringConfig> PossibleGameConfigs = OriginalConfig.getNextPossibleMoves();
        Random random = new Random();
        int randomMove = random.nextInt() % PossibleGameConfigs.size();
        AIMove nextMove = new AIMove(OriginalConfig, PossibleGameConfigs.get(randomMove), state);
        return nextMove;
    }
}
