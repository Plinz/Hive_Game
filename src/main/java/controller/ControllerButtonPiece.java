/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import main.java.model.Core;
import main.java.view.Highlighter;
import ihm_test_version.InterfaceJavaFX;

/**
 *
 * @author duvernet
 */
public class ControllerButtonPiece implements EventHandler<MouseEvent> {
    
    private GameScreenController gameController;
    private Highlighter highlighted;
    private Core c;
    private int i;
    
    public ControllerButtonPiece(GameScreenController g,Highlighter highlighted,Core c, int i) {
        this.gameController = g;
        this.highlighted = highlighted;
        this.c = c;
        this.i = i;
    }
    
    

    @Override
    public void handle(MouseEvent t) {
        /* Ajout */
        gameController.resetPiece();
        /**/
        gameController.setPieceToChoose(i);
        highlighted.setListTohighlight(c.getPossibleAdd());
        
    }
}
