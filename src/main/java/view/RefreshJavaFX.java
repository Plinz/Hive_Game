/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import main.java.model.Core;

/**
 *
 * @author gontardb
 */
public class RefreshJavaFX extends AnimationTimer{

    Core core;
    BoardDrawer drawer;

    RefreshJavaFX(Core core, Canvas c) {
        this.core = core;
        drawer = new BoardDrawer(c);
    }

    @Override
    public void handle(long now) {
        if (core.accept(drawer)){
        }
    }
}
