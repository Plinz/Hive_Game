/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import main.java.controller.GameScreenController;
import main.java.engine.Core;
import main.java.engine.OptionManager;
import main.java.utils.Consts;

/**
 *
 * @author gontardb
 */
public class RefreshJavaFX extends AnimationTimer{

    Core core;
    BoardDrawer drawer;
    Highlighter h;
    GameScreenController g;
    int time;
    public RefreshJavaFX(Core core, Canvas c, Highlighter h) {
        this.core = core;
        drawer = new BoardDrawer(c);
        this.h =h;
    }
    
    public RefreshJavaFX(Core core, Canvas c, Highlighter h,TraducteurBoard t,GameScreenController g) {
        this.core = core;
        drawer = new BoardDrawer(c,t);
        this.h =h; 
        this.g = g;
        time = 0;
    }

    @Override
    public void handle(long now) {
        if(core.getState() == Consts.READY_TO_CHANGE && time == Consts.FRAMES_TO_WAIT){
            if(!core.isGameFinish()){
                core.playNextTurn();
                g.initButtonByInventory();
            }
            else{
                core.setState(Consts.END_OF_THE_GAME);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        g.handleEndGame();                    
                    }
                });         
            }
            time = 0;
        }else if(core.getState() == Consts.READY_TO_CHANGE){
            time++;
        }
        
       core.accept(drawer);
       if (OptionManager.isHelpEnable() || (core.getTurn() == 0 && !OptionManager.isGridEnable()))
    	   h.accept(drawer);
    }
}
