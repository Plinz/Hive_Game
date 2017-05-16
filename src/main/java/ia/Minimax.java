/*

 */
package main.java.ia;

import java.util.ArrayList;

import main.java.engine.Core;

public class Minimax {
    StoringConfig config;
    ArrayList<Minimax> children;
    Heuristics heuristics;
    int heuristicValue;
    int currentPlayer;
    int depth;
    
    

    public Minimax(StoringConfig stConfig){
        this.config = stConfig;
        this.currentPlayer = stConfig.currentPlayer;
    }
    
    /*public void makeChildren(){
        ArrayList<StoringConfig> childrenConfigs = this.config.getNextPossibleMoves();
        this.children = new ArrayList<>();
        for (StoringConfig stConfig : childrenConfigs){
            children.add(new Minimax(stConfig, 1-this.player));
        }
    }*/
}
