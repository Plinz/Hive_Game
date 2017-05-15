/*

 */
package main.java.ia;

import java.util.ArrayList;
import main.java.model.State;

public class Minimax {
    StoringConfig config;
    ArrayList<Minimax> children;
    Heuristics heuristics;
    int heuristicValue;
    int currentPlayer;
    int depth;
    
    
    public Minimax(State state){
        this.currentPlayer = state.getCurrentPlayer();
        this.config = new StoringConfig(state);
    }
    
    public Minimax(StoringConfig stConfig, int player){
        this.config = stConfig;
        this.currentPlayer = player;
    }
    
    /*public void makeChildren(){
        ArrayList<StoringConfig> childrenConfigs = this.config.getNextPossibleMoves();
        this.children = new ArrayList<>();
        for (StoringConfig stConfig : childrenConfigs){
            children.add(new Minimax(stConfig, 1-this.player));
        }
    }*/
}
