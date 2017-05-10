/*

 */
package main.java.ia;

import java.util.ArrayList;
import main.java.model.State;

public class Minimax {
    StoringConfig config;
    ArrayList<Minimax> children;
    int heuristicValue;
    int player;
    
    public Minimax(State state){
        this.player = state.getCurrentPlayer();
        this.config = new StoringConfig(state);
    }
    
    public Minimax(StoringConfig stConfig, int player){
        this.config = stConfig;
        this.player = player;
    }
    
    public void makeChildren(){
        ArrayList<StoringConfig> childrenConfigs = this.config.getNextPossibleMoves();
        this.children = new ArrayList<>();
        for (StoringConfig stConfig : childrenConfigs){
            children.add(new Minimax(stConfig, 1-this.player));
        }
    }
}
