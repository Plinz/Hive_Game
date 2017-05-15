/*

 */
package main.java.ia;

import java.util.Random;
import main.java.model.Core;

public class EasyAI extends AI {

    public EasyAI() {
        super();
    }

    public EasyAI(Core core) {
        this.core = core;
        this.OriginalConfig = new StoringConfig(core);
    }

    @Override
    public AIMove getNextMove(Core core) {
        AIMove result;
                
        this.core = core;
        this.OriginalConfig = new StoringConfig(core);
        GameConfig gameConfig = new GameConfig(OriginalConfig);
        gameConfig.heuristics = new EasyHeuristics(gameConfig);
        
        gameConfig.calculateAll();

        int originalHeuristic = gameConfig.heuristics.getHeuristicsValue();
        int nextMoveHeuristic, bestHeuristic = originalHeuristic;
        int originalPieceNb = gameConfig.heuristics.getNbPiecesOnBoard(gameConfig.currentPlayer);

        StoringConfig bestConfig = null;
        
        for (StoringConfig storingConfig : gameConfig.nextPossibleConfigs) {
            GameConfig newGameConfig = new GameConfig(storingConfig);
            EasyHeuristics heuristic = new EasyHeuristics(newGameConfig);
            nextMoveHeuristic = heuristic.getHeuristicsValue();
            System.out.println("Heuristique easy : best ="+bestHeuristic+"original="+originalHeuristic+"local="+nextMoveHeuristic);
            if (nextMoveHeuristic > bestHeuristic) {
                bestConfig = storingConfig;
                bestHeuristic = nextMoveHeuristic;
            } else if (bestHeuristic == originalHeuristic) {
                int nbPiecesInNewConfig = heuristic.getNbPiecesOnBoard(gameConfig.currentPlayer);
                if (nbPiecesInNewConfig > originalPieceNb) {
                    bestConfig = storingConfig;
                }
            }
        }
        if (bestConfig != null) {
            result = new AIMove(OriginalConfig, bestConfig, core);
        } else { //cannot get a better heuristic or add a piece -> do whatever
            Random random = new Random();
            int randomMove = random.nextInt(gameConfig.nextPossibleConfigs.size());
            result = new AIMove(OriginalConfig, gameConfig.nextPossibleConfigs.get(randomMove), core);
        }
        return result;
    }
}
