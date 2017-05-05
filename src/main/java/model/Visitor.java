/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.model;

/**
 *
 * @author duvernet
 */
public abstract class Visitor {

    protected abstract boolean visit(Board b);

    protected abstract boolean visit(Tile t);

    protected abstract boolean visit(Piece p);
     

}
