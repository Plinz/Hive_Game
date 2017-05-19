/*

 */
package main.java.ia;

import java.util.ArrayList;
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
        //////////////////////////////////////////OPENING
        if (core.getTurn() <= 7) {
            switch (core.getTurn()) {
                case 0:
                case 1:
                    return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_1));
                case 2:
                case 3:
                    return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_2));
                case 4:
                case 5:
                    return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_3));
                case 6:
                case 7:
                    if (!core.isQueenOnBoard(core.getCurrentPlayer())) {
                        return addPieceWherever(chooseAPiece(Consts.CHOOSE_QUEEN));
                    } else {
                        Random random = new Random();
                        double rand = random.nextDouble();
                        if (rand < Consts.EASY_TURN_4_CHOOSE_TO_ADD) {
                            return addPieceWherever(chooseAPiece(Consts.EASY_ADD_TURN_4));
                        } else {
                            return movePieceNearOpponent(chooseWhateverFreeTileOnBoard(core.getCurrentPlayer()));
                        }
                    }
                default:
                    System.out.println("Erreur : Ne devrait jamais se produire");
            }
        }
        
        
        //////////////////////////////////MID GAME
        Random random = new Random();
        Minimax minimax = new Minimax(core, heuristics);
        double originalHeuristic = minimax.heuristics.getHeuristicsValue();

        double bestHeuristic = originalHeuristic;
        String chosenMove = null;
        String chosenUnplay = null;
        List<Minimax> possibleMovements = minimax.getChildrenWithHeuristics();

        //case -> less than 5 pieces around opposing queen -> try to get a move which ups heuristics
        ArrayList<Minimax> childWithBetterHeuristics = new ArrayList<>();
        if (minimax.heuristics.getNbPiecesAroundQueen(1 - AIPlayer) < 5) {
            for (Minimax child : possibleMovements) {
                if (child.heuristicValue > bestHeuristic) {
                    childWithBetterHeuristics.add(child);
                }
            }

            if (childWithBetterHeuristics.isEmpty()) {//cannot do better -> add or move a piece
                double r = random.nextDouble();
                if (r < Consts.EASY_MID_GAME_CHOOSE_TO_ADD && !core.getPlayers()[AIPlayer].getInventory().isEmpty()) { //add whatever wherever
                    return addPieceWherever(chooseAPiece(Consts.CHOOSE_WHATEVER));
                } else { //move whatever near opponent
                    return movePieceNearOpponent(chooseWhateverFreeTileOnBoard(AIPlayer));
                }
            } else { //can do better -> choose some better move randomly
                int rand = random.nextInt(childWithBetterHeuristics.size());
                chosenMove = childWithBetterHeuristics.get(rand).moveFromParent;
                chosenUnplay = childWithBetterHeuristics.get(rand).moveToParent;
            }
        } else { //case -> only one more piece is needed to beat the player
            int rand = random.nextInt(possibleMovements.size());
            chosenMove = possibleMovements.get(rand).moveFromParent;
            chosenUnplay = possibleMovements.get(rand).moveToParent;
        }
        String moveAndUnplay = chosenMove + ";" + chosenUnplay;
        return moveAndUnplay;
    }
}
