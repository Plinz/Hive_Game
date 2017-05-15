/*

 */
package main.java.ia;

import java.util.Random;
import main.java.model.State;

public class EasyAI extends AI {

    public EasyAI() {
        super();
    }

    public EasyAI(State state) {
        this.state = state;
        this.OriginalConfig = new StoringConfig(state);
    }

    @Override
    public AIMove getNextMove(State state) {
        AIMove result;
                
        this.state = state;
        this.OriginalConfig = new StoringConfig(state);
        GameConfig gameConfig = new GameConfig(OriginalConfig, OriginalConfig.turn);
        gameConfig.heuristics = new EasyHeuristics(gameConfig);
        gameConfig.calculateAll();

        int originalheuristic = gameConfig.heuristics.getHeuristicsValue();
        int localHeuristic, bestheuristic = originalheuristic;
        int originalPieceNb = gameConfig.heuristics.getNbPiecesOnBoard(gameConfig.currentPlayer);

        StoringConfig bestConfig = null;
        for (StoringConfig storingConfig : gameConfig.nextPossibleConfigs) {
            GameConfig newGameConfig = new GameConfig(storingConfig, storingConfig.turn);
            EasyHeuristics heuristic = new EasyHeuristics(newGameConfig);
            localHeuristic = heuristic.getHeuristicsValue();
            System.out.println("Heuristique easy : best ="+bestheuristic+"original="+originalheuristic+"local="+localHeuristic);
            if (localHeuristic > bestheuristic) {
                bestConfig = storingConfig;
                bestheuristic = localHeuristic;
            } else if (bestheuristic == originalheuristic) {
                int nbPiecesInNewConfig = heuristic.getNbPiecesOnBoard(gameConfig.currentPlayer);
                if (nbPiecesInNewConfig > originalPieceNb) {
                    bestConfig = storingConfig;
                }
            }
        }
        if (bestConfig != null) {
            result = new AIMove(OriginalConfig, bestConfig, state);
        } else { //cannot get a better heuristic or add a piece -> do whatever
            Random random = new Random();
            int randomMove = random.nextInt(gameConfig.nextPossibleConfigs.size());
            result = new AIMove(OriginalConfig, gameConfig.nextPossibleConfigs.get(randomMove), state);
        }
        return result;
    }
}
