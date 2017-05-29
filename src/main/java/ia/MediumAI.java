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

    public MediumAI(Core core, int difficulty) {
        super(core);
        this.choiceDuringOpening = new int[4];
        if (difficulty == Consts.HARD) {
            this.heuristics = new MediumHeuristics(core, difficulty);
        } else {
            this.heuristics = new MediumHeuristics(core);
        }
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
                    if (core.isQueenOnBoard(1) && !core.isQueenOnBoard(0)) {
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T2_IF_BQ_T1));
                    } else {
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T2_ELSE));
                    }
                case 3://TURN 2 BLACK
                    if (core.isQueenOnBoard(1)){
                        return addPieceWherever(chooseAPiece(HeuristicConst.W_MEDIUM_ADD_T3_ELSE));
                    }else {
                        return addPieceWherever(chooseAPiece(HeuristicConst.B_MEDIUM_ADD_T2));
                    }                   
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
                        return addPieceWherever(chooseAPiece(HeuristicConst.CHOOSE_QUEEN));
                    }
            }
        }

        if (isTimeToFinishOpponent()) {
            String moveAndUnmove = tryGetFinishMove();
            if (moveAndUnmove != null) {
                return moveAndUnmove;
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

        for (Minimax child : possibleMovements) {
            if (sign * child.heuristicValue > bestHeuristic) {
                bestHeuristic = sign * child.heuristicValue;
                chosenOne = child;
            }
        }

        if (isASuicideMove(chosenOne)) {
            chosenOne = chooseDecentMove(possibleMovements);
        }
        return chosenOne.moveFromParent + ";" + chosenOne.moveToParent;
    }

}
