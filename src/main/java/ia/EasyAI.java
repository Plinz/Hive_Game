/*

 */
package main.java.ia;

import java.util.List;
import java.util.Random;

import main.java.engine.Core;
import main.java.utils.CoordGene;

public class EasyAI extends AI {

    public EasyAI() {
        super();
    }

    @Override
    public CoordGene<String> getNextMove(Core core) {

        Minimax minimax = new Minimax(core);
        int originalHeuristic = minimax.heuristics.getHeuristicsValue();

        int nextMoveHeuristic, bestHeuristic = originalHeuristic;
        String chosenMove = null;
        String chosenUnplay = null;
        List<Minimax> possibleMovements = minimax.getChildrenWithHeuristics();
        //case -> less than 5 pieces around opposing queen -> try to do better
        if (minimax.heuristics.getNbPiecesAroundQueen(1 - core.getCurrentPlayer()) < 5) {
            for (Minimax child : possibleMovements) {
                if (child.heuristicValue > bestHeuristic) {
                    chosenMove = child.moveFromParent;
                    chosenUnplay = child.moveToParent;
                    bestHeuristic = child.heuristicValue;
                }
            }

            if (bestHeuristic <= originalHeuristic) {
                Random random = new Random();
                int rand = random.nextInt(possibleMovements.size());
                chosenMove = possibleMovements.get(rand).moveFromParent;
                chosenUnplay = possibleMovements.get(rand).moveToParent;
            }
        } else {
            Random random = new Random();
            int rand = random.nextInt(possibleMovements.size());
            chosenMove = possibleMovements.get(rand).moveFromParent;
            chosenUnplay = possibleMovements.get(rand).moveToParent;
        }
        System.out.println("1-chosenMove = " + chosenMove.concat("|").concat(chosenUnplay));
        return new CoordGene<>(chosenMove, chosenUnplay);
    }
}

