/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.Random;
import main.java.model.Core;

public class RandomAI extends AI {

    public RandomAI(){
        
    }
    
    public RandomAI(Core core) {
        this.core = core;
        this.OriginalConfig = new StoringConfig(core);
    }

    @Override
    public AIMove getNextMove(Core core) {
        this.core = core;
        this.OriginalConfig = new StoringConfig(core);
        GameConfig gameConfig = new GameConfig(OriginalConfig);
        ArrayList<StoringConfig> possibleGameConfigs = gameConfig.nextPossibleConfigs;
        Random random = new Random();
                System.err.println("possible game config size :"+possibleGameConfigs.size());
        int randomMove = random.nextInt(possibleGameConfigs.size());
        System.err.println("possible game config size :"+possibleGameConfigs.size()+"random : get"+randomMove);
        AIMove nextMove = new AIMove(OriginalConfig, possibleGameConfigs.get(randomMove), core);
        //System.err.println(nextMove.toString());
        return nextMove;
    }
}
