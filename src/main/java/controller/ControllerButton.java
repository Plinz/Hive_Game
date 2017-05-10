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
import main.java.view.InterfaceJavaFX;

/**
 *
 * @author duvernet
 */
public class ControllerButton implements EventHandler<MouseEvent> {
    
    private InterfaceJavaFX inter;
    private Highlighter highlighted;
    private Core c;
    private int i;
    
    public ControllerButton(InterfaceJavaFX inter,Highlighter highlighted,Core c, int i) {
        this.inter = inter;
        this.highlighted = highlighted;
        this.c = c;
        this.i = i;
    }

    @Override
    public void handle(MouseEvent t) {
        inter.setPieceToChoose(i);
        highlighted.setListTohighlight(c.getPossibleAdd());
    }
}
