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
import main.java.utils.CoordGene;

/**
 *
 * @author duvernet
 */
public class Highlighter {
     private List<CoordGene<Integer>> listTohighlight;
     
     public Highlighter(){}
     
     public boolean accept(Visitor v){
         if( listTohighlight != null){
            for(CoordGene<Integer> c : getListTohighlight()){
                v.visit(c);
            }
         }
         return false;
     }

    /**
     * @return the listCoord
     */
    public List<CoordGene<Integer>> getListTohighlight() {
        return listTohighlight;
    }

    /**
     * @param listCoord the listCoord to set
     */
    public void setListTohighlight(List<CoordGene<Integer>> listCoord) {
        this.listTohighlight = listCoord;
    }
}
