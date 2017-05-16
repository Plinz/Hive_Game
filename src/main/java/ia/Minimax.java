/*

 */
package main.java.ia;

import java.util.ArrayList;

public class Minimax {

    StoringConfig config;
    ArrayList<Minimax> children;
    int heuristicValue;
    int currentPlayer;
    int depth;
    int AIPlayer;
    int difficulty;

    public Minimax(StoringConfig stConfig) {
        this.config = stConfig;
        this.currentPlayer = stConfig.currentPlayer;
        this.depth = 1;
        this.AIPlayer = stConfig.currentPlayer;
        this.children = new ArrayList<>();
    }

    public Minimax(StoringConfig storingConfig, Minimax parent) {
        this.config = storingConfig;
        this.config.currentPlayer = 1-parent.currentPlayer;
        this.config.turn = storingConfig.turn +1;
        this.currentPlayer = 1-parent.currentPlayer;
        this.AIPlayer = parent.AIPlayer;
        this.children = new ArrayList<>();
        this.depth = parent.depth + 1;
    }

    
    public void getChildrenWithHeuristics(int maxDepth){
        System.out.println("depth ="+this.depth+this.config.toString());
        GameConfig gameConfig = new GameConfig(config);
        for (StoringConfig storingConfig : gameConfig.nextPossibleConfigs){
            Minimax newMinimax = new Minimax(storingConfig, this);
            children.add(newMinimax);
        }
        for (Minimax child : children){
            child.heuristicValue = child.getHeuristicsRecursive(maxDepth);
            System.out.println("Minimax: child heuristic = "+child.heuristicValue);
        }
    }
    
    public int getHeuristicsRecursive(int maxDepth) {
        //print
        for (int i=0 ; i<depth ; i++){
            System.out.print("\t");
        }
        System.out.println("depth ="+this.depth+this.config.toString());
        GameConfig gameConfig = new GameConfig(config);
        //End print
        
        
        if (this.depth == maxDepth) {
            this.heuristicValue = gameConfig.heuristicValue;
            return gameConfig.heuristicValue;
        }

        if (gameConfig.nextPossibleConfigs.isEmpty()) {
            this.currentPlayer = 1 - this.currentPlayer;
            return this.getHeuristicsRecursive(maxDepth);
        } else {
            for (StoringConfig stconf : gameConfig.nextPossibleConfigs) {
                Minimax newMinimax = new Minimax(stconf, this);
                children.add(newMinimax);
                newMinimax.heuristicValue = newMinimax.getHeuristicsRecursive(maxDepth);
            }
            if (currentPlayer == AIPlayer) {
                int bestHeuristic = children.get(0).heuristicValue;
                for (Minimax child : children) {
                    if (child.heuristicValue > bestHeuristic) {
                        bestHeuristic = child.heuristicValue;
                    }
                }
                return bestHeuristic;
            } else {
                int worstHeuristic = children.get(0).heuristicValue;
                for (Minimax child : children) {
                    if (child.heuristicValue < worstHeuristic) {
                        worstHeuristic = child.heuristicValue;
                    }
                }
                return worstHeuristic;
            }
        }
    }
}
