/*

 */
package main.java.ia;

import main.java.model.Core;
import main.java.utils.Consts;

public class MediumAI extends AI{
    public MediumAI(){
        super();
    }
    
    public MediumAI(Core core){
        this.core = core;
        this.OriginalConfig = new StoringConfig(core, Consts.MEDIUM);
    }
    
    @Override
    public AIMove getNextMove(Core core){
        this.OriginalConfig = new StoringConfig(core, Consts.MEDIUM);
        Minimax minimax = new Minimax(OriginalConfig);
        minimax.getChildrenWithHeuristics(Consts.MEDIUM_MAX_DEPTH);
        if (minimax.children.isEmpty()){
            System.err.println("Erreur : l'IA ne peut jouer aucun coup !");
            return null;
        }
        Minimax bestNextConfig = minimax.children.get(0);
        for (Minimax child : minimax.children){
            if (child.heuristicValue > bestNextConfig.heuristicValue){
                bestNextConfig = child;
            }
        }
        return new AIMove(OriginalConfig, bestNextConfig.config, core);
    }
}
