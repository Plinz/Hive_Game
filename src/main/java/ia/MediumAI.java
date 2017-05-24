/*

 */
package main.java.ia;

import java.util.List;
import main.java.engine.Core;
import main.java.engine.Notation;
import main.java.utils.Consts;

public class MediumAI extends AI {

    public MediumAI(Core core) {
        super(core);
        this.heuristics = new MediumHeuristics(core);
        this.choiceDuringOpening = new int[4];
    }

    @Override
    public String getNextMove() {
        this.AIPlayer = core.getCurrentPlayer();
        if (core.getTurn() <= 5) {
            switch (core.getTurn()) {
                case 0://turn 1 WHITE
                    return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T1));
                case 1://TURN 1 BLACK
                    if (core.isQueenOnBoard(0)) {
                        return addPieceWherever(chooseAPiece(HeuristicConst.B_MEDIUM_ADD_T1_IF_WQ_T1));
                    } else {
                        return addPieceWherever(chooseAPiece(HeuristicConst.B_MEDIUM_ADD_T1_ELSE));
                    }
                case 2://turn 2 WHITE
                    if (core.isQueenOnBoard(1)) {
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T2_IF_BQ_T1));
                    } else {
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T2_ELSE));
                    }
                case 3://TURN 2 BLACK
                    return addPieceWherever(chooseAPiece(HeuristicConst.B_MEDIUM_ADD_T2));
                case 4://TURN 3 WHITE
                    if (!core.isQueenOnBoard(0)) {
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T3_IF_NO_WQ));
                    } else {
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T3_ELSE));
                    }
                case 5://TURN 3 BLACK
                    if (whiteCanMoveAnt()) {
                        return addPieceWherever(chooseAPiece(HeuristicConst.B_MEDIUM_ADD_T3_IF_W_HAS_MOBILE_ANT));
                    } else {
                        return addPieceWherever(chooseAPiece(HeuristicConst.B_MEDIUM_ADD_T3_ELSE));
                    }
                case 6://TURN 7 & 8 -> ADD QUEEN IF IT S NOT THE CASE
                case 7:
                    if (!core.isQueenOnBoard(core.getCurrentPlayer())) {
                        return addPieceWherever(chooseAPiece(Consts.CHOOSE_QUEEN));
                    }
            }
        }
        
        
        
        Minimax minimax = new Minimax(core, heuristics);

        int sign;
        if (AIPlayer == 0) {
            sign = 1;
        } else {
            sign = -1;
        }

        List<Minimax> possibleMovements = minimax.getChildren();
        Minimax chosenOne = possibleMovements.get(0);
        double bestHeuristic = sign * chosenOne.heuristicValue;
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");

        for (Minimax child : possibleMovements) {
            System.out.println("child : move = " + Notation.getHumanDescription(child.moveFromParent, false) + " with h= " + child.heuristicValue);
            if (sign * child.heuristicValue > bestHeuristic) {
                bestHeuristic = sign * child.heuristicValue;
                chosenOne = child;
            }
        }
        
        if (isTimeToFinishOpponent() && Math.floor(chosenOne.heuristicValue) < 1200){
            String moveAndUnmove = tryGetFinishMove();
            if (moveAndUnmove != null){
                return moveAndUnmove;
            }
        }
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-__-_-_-_-_-_-_-_-_-_-_-_-_-");
        System.out.println("Chosen One : " + chosenOne.moveFromParent + " with heuristic = " + chosenOne.heuristicValue);
        return chosenOne.moveFromParent + ";" + chosenOne.moveToParent;
    }
}
