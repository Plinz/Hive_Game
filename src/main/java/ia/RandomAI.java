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

    @Override
    public AIMove getNextMove(State state) {
        this.state = state;
        this.OriginalConfig = new StoringConfig(state);
        ArrayList<StoringConfig> possibleGameConfigs = OriginalConfig.getNextPossibleMoves();
        Random random = new Random();
        System.err.println("possible game config size :"+possibleGameConfigs.size());
        int randomMove = random.nextInt() % possibleGameConfigs.size();
        AIMove nextMove = new AIMove(OriginalConfig, possibleGameConfigs.get(randomMove), state);
        //System.err.println(nextMove.toString());
        return nextMove;
    }
}
