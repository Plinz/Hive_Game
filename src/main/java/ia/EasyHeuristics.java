/*

 */
package main.java.ia;

public class EasyHeuristics extends Heuristics {

    public EasyHeuristics(GameConfig gameConfig) {
        super(gameConfig);
    }

    public int getHeuristicsValue() {
        return getNbPiecesAroundQueen(1-gameConfig.currentPlayer) - getNbPiecesAroundQueen(gameConfig.currentPlayer);
    }
}
