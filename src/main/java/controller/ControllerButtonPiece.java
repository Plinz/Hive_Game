/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import com.sun.javafx.cursor.CursorFrame;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import main.java.engine.Core;
import main.java.utils.Consts;
import main.java.view.Highlighter;

public class ControllerButtonPiece implements EventHandler<MouseEvent> {
    
    private GameScreenController gameController;
    private Highlighter highlighted;
    private Core c;
    private int indexPiece;
    private int i;
    
    public ControllerButtonPiece(GameScreenController g,Highlighter highlighted,Core c, int index, int i ) {
        this.gameController = g;
        this.highlighted = highlighted;
        this.c = c;
        this.indexPiece = index;
        this.i = i;
    }
    
    

    @Override
    public void handle(MouseEvent t) {
        if(!gameController.isFreeze() && c.getState() == Consts.WAIT_FOR_INPUT){
            /* Ajout */
            gameController.resetPiece();
            gameController.getInventoryGroup().getToggles().get(i).setSelected(true);
            /**/
            gameController.setPieceToChoose(indexPiece);
            highlighted.setListTohighlight(c.getPossibleAdd(c.getCurrentPlayer()));
    }
    }
}
