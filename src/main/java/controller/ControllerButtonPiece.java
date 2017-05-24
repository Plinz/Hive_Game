/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import main.java.utils.Consts;

public class ControllerButtonPiece implements EventHandler<MouseEvent> {
    
    private GameScreenController gameController;
    private int indexPiece;
    private int indexOfButtonInInventory;
    
    public ControllerButtonPiece(GameScreenController g, int index, int i) {
        this.gameController = g;
        this.indexPiece = index;
        this.indexOfButtonInInventory = i;
    }
    
    

    @Override
    public void handle(MouseEvent t) {
        if(!gameController.isFreeze() && gameController.getCore().getState() == Consts.WAIT_FOR_INPUT){
            /* Ajout */
            gameController.resetPiece();
            gameController.getInventoryGroup().getToggles().get(indexOfButtonInInventory).setSelected(true);
            /**/
            gameController.setPieceToChoose(indexPiece);
            gameController.getHighlighted().setListTohighlight(gameController.getCore().getPossibleAdd(gameController.getCore().getCurrentPlayer()));
    }
    }
}
