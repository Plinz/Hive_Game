/*

 */
package main.java.ia;

import java.util.List;
import java.util.Random;

import main.java.engine.Core;
import main.java.utils.Consts;

public class EasyAI extends AI {

    public EasyAI(Core core) {
        super(core);
        this.heuristics = new EasyHeuristics(core);
        this.choiceDuringOpening = new int[4];
    }

    @Override
    public String getNextMove() {
        this.AIPlayer = core.getCurrentPlayer();
        if (core.getTurn() <=  7){
            switch(core.getTurn()){
                case 0 :
                case 1 :
                    return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_1));
                case 2 :
                case 3 :
                    return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_2));
                case 4 :
                case 5 :
                    return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_3));
                case 6 :
                case 7 :
                    if (!core.isQueenOnBoard(core.getCurrentPlayer())){
                        return addPieceWherever(chooseAPiece(Consts.CHOOSE_QUEEN));
                    } else {
                        Random random = new Random();
                        double rand = random.nextDouble();
                        if (rand < Consts.EASY_TURN_4_CHOOSE_TO_ADD)
                            return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_4));
                        else
                            return movePieceNearOpponent(chooseWhateverFreeTileOnBoard(core.getCurrentPlayer()));
                    }
                default :
                    System.out.println("Erreur : Ne devrait jamais se produire");
            }
        }
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
        } else { //case -> only one more piece is needed to beat the player
            Random random = new Random();
            int rand = random.nextInt(possibleMovements.size());
            chosenMove = possibleMovements.get(rand).moveFromParent;
            chosenUnplay = possibleMovements.get(rand).moveToParent;
        }
        String moveAndUnplay = chosenMove + ";" + chosenUnplay;
        return moveAndUnplay;
    }
}

