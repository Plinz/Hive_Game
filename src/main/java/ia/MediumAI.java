/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.java.engine.Core;
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
        if (core.getTurn() <= 7) {
            switch (core.getTurn()) {
                case 0://turn 1
                case 1:
                    return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_1));
                case 2://turn 2
                case 3:
                    switch (choiceDuringOpening[0]) {
                        case 0://queen on turn 1
                            return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_2_IF_QUEEN_ON_1));
                        case 1://spider on turn 1
                            return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_2_IF_SPIDER_ON_1));
                        case 2://grasshopper on turn 1
                            return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_2_IF_GRASSHOPPER_ON_1));
                        case 3://beetle on turn 1
                        case 4://ant on turn 1
                            return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_2_IF_BETTLE_OR_ANT_ON_1));
                        default:
                            System.out.println("Erreur : Ne devrait jamais se produire");
                    }
                case 4://turn 3
                case 5:
                    return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_3));
                case 6:
                case 7:
                    if (!core.isQueenOnBoard(core.getCurrentPlayer())) {
                        return addPieceWherever(chooseAPiece(Consts.CHOOSE_QUEEN));
                    } else {
                        Random random = new Random();
                        double rand = random.nextDouble();
                        if (rand < Consts.MEDIUM_TURN_4_CHOOSE_TO_ADD) {
                            return addPieceWherever(chooseAPiece(Consts.MEDIUM_ADD_TURN_4));
                        } else {
                            return movePieceNearOpponent(chooseWhateverFreeTileOnBoard(core.getCurrentPlayer()));
                        }
                    }
                default:
                    System.out.println("Erreur : Ne devrait jamais se produire");
                    return null;
            }
        } else {
            Minimax minimax = new Minimax(core, heuristics);

            List<Minimax> possibleMovements = minimax.getChildren();
            Minimax chosenOne = possibleMovements.get(0);
            double bestHeuristic = Consts.BEST_HEURISTICS;
            for (Minimax child : possibleMovements) {
                if (child.heuristicValue<bestHeuristic){
                    bestHeuristic = child.heuristicValue;
                    chosenOne = child;
                }
            }
            System.out.println("$$$$$$$$$$$$\n$$$$$$$$$$\n$$$$$$$$$$\nChosenOne : "+chosenOne.moveFromParent+"\n$$$$$$$$$$$$\n_____________________________________________________________");
            return chosenOne.moveFromParent+";"+chosenOne.moveToParent;
        }
    }
}