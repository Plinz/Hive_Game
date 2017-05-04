/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.java.model.Board;
import main.java.model.Core;

/**
 *
 * @author gontardb
 */
public class BoardDrawer {
    Canvas can;
    GraphicsContext gc;
    
    public BoardDrawer(Canvas c){
        this.can = c;
        this.gc = can.getGraphicsContext2D();   
    }
    
    public boolean visit(Core c){
        gc.clearRect(0, 0, can.getWidth(), can.getHeight());
        return false;
    }
    
    public boolean visit(Board b){
        
        return false;
    }
}
