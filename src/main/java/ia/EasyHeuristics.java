/*

 */
package main.java.ia;

import main.java.engine.Core;
import main.java.utils.Consts;


public class EasyHeuristics extends Heuristics {

    public EasyHeuristics(Core core) {
        super(core);
        this.maxdepth = Consts.DEPTH_EASY;
    }

    public int getHeuristicsValue() {
        return getNbPiecesAroundQueen(1-core.getCurrentPlayer()) - getNbPiecesAroundQueen(core.getCurrentPlayer());
    }
}
