/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.view;

import java.util.ArrayList;
import java.util.List;
import main.java.model.Visitor;
import main.java.view.BoardDrawer;
import main.java.utils.Coord;

/**
 *
 * @author duvernet
 */
public class Highlighter {
     private List<Coord> listTohighlight;
     
     Highlighter(){}
     
     public boolean accept(Visitor v){
         if( listTohighlight != null){
            for(Coord c : getListTohighlight()){
                v.visit(c);
            }
         }
         return false;
     }

    /**
     * @return the listCoord
     */
    public List<Coord> getListTohighlight() {
        return listTohighlight;
    }

    /**
     * @param listCoord the listCoord to set
     */
    public void setListTohighlight(List<Coord> listCoord) {
        this.listTohighlight = listCoord;
    }
}
