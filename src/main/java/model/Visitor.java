/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.model;

import main.java.model.*;
import main.java.utils.Coord;
import main.java.view.Highlighter;

/**
 *
 * @author duvernet
 */
public abstract class Visitor {

    protected abstract boolean visit(Board b);

    protected abstract boolean visit(Tile t);

    protected abstract boolean visit(Piece p);
    
    public abstract boolean visit(Coord c);

}
