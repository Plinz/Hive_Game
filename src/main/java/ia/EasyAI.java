/*

 */
package main.java.ia;

import java.util.List;
import java.util.Random;

import main.java.engine.Core;

public class EasyAI extends AI {

    public EasyAI() {
        super();
    }

    @Override
    public String getNextMove(Core core) {

        Minimax minimax = new Minimax(core);
        int originalHeuristic = minimax.heuristics.getHeuristicsValue();

        int nextMoveHeuristic, bestHeuristic = originalHeuristic;
        String chosenMove = null;
        List<Minimax> possibleMovements = minimax.getChildrenWithHeuristics();
        //case -> less than 5 pieces around opposing queen -> try to do better
        if (minimax.heuristics.getNbPiecesAroundQueen(1 - core.getCurrentPlayer()) < 5) {
            for (Minimax child : possibleMovements ) {
                if (child.heuristicValue > bestHeuristic) {
                    chosenMove = child.moveFromParent;
                    bestHeuristic = child.heuristicValue;
                }
            }
            
            if (bestHeuristic > originalHeuristic){
                return chosenMove;
            } else {
                Random random = new Random();
                int rand = random.nextInt(possibleMovements.size());
                return possibleMovements.get(rand).moveFromParent;
            }
        } else {
            Random random = new Random();
                int rand = random.nextInt(possibleMovements.size());
                return possibleMovements.get(rand).moveFromParent;
        }

        /*
        
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
        return result;*/
    }
}
