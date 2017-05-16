/*

 */
package main.java.ia;

public class MediumHeuristics extends Heuristics {

    public MediumHeuristics(GameConfig gameConfig) {
        super(gameConfig);
    }

    public int getHeuristicsValue() {
        return getNbPiecesAroundQueen(1 - gameConfig.currentPlayer) - getNbPiecesAroundQueen(gameConfig.currentPlayer);
    }
}
