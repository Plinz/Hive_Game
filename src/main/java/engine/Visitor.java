package main.java.engine;

import main.java.model.Board;
import main.java.model.HelpMove;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.CoordGene;

public abstract class Visitor {

    protected abstract boolean visit(Board b);

    protected abstract boolean visit(Tile t);

    protected abstract boolean visit(Piece p);
    
    public abstract boolean visit(CoordGene<Integer> c);
    
    public abstract boolean visit(HelpMove h);

}
