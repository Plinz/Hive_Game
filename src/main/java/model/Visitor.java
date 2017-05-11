package main.java.model;

import main.java.utils.CoordGene;

public abstract class Visitor {

    protected abstract boolean visit(Board b);

    protected abstract boolean visit(Tile t);

    protected abstract boolean visit(Piece p);
    
    public abstract boolean visit(CoordGene<Integer> c);

}
