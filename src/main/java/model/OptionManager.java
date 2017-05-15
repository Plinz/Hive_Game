/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.model;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author gontardb
 */

public final class OptionManager {
    
    private static int resolution = 1;
    private static boolean fullscreen = false;
    private static boolean helpEnable = true;
    
    public static void init() throws IOException{
        File file = new File("init.xml");
        if(file.exists()){
            System.out.println("Il existe !");
        }
        else{
            System.out.println("Création du fichier d'options");
            if(file.createNewFile()){
                System.out.println("Tout s'est bien passé");
            }
            else{
                System.out.println("Un problème est survenu lors de la création du fichier");
            }
        }
    }

    public static int getResolution() {
        return resolution;
    }

    public static void setResolution(int res) {
        resolution = res;
    }

    public static boolean isFullscreen() {
        return fullscreen;
    }

    public static void setFullscreen(boolean fs) {
        fullscreen = fs;
    }

    public static boolean isHelpEnabled() {
        return helpEnable;
    }

    public static void setHelp(boolean h) {
        helpEnable = h;
    }   
}
