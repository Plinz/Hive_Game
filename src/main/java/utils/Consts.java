
package main.java.utils;

public class Consts {
    
    //MACROS PIECES -> PLEASE DO NOT MODIFY /!\
    public static final int QUEEN = 0;
    public static final int SPIDER1 = 1;
    public static final int SPIDER2 = 2;
    public static final int GRASSHOPPER1 = 3;
    public static final int GRASSHOPPER2 = 4;
    public static final int GRASSHOPPER3 = 5;
    public static final int BEETLE1 = 6;
    public static final int BEETLE2 = 7;
    public static final int ANT1 = 8;
    public static final int ANT2 = 9;
    public static final int ANT3 = 10;
    public static final int MOSQUITO = 11;
    public static final int LADYBUG = 12;
    public static final int PILLBUG = 13;
    
    //MACROS TYPES DE PIECES
    public static final int QUEEN_TYPE = 0;
    public static final int SPIDER_TYPE = 1;
    public static final int GRASSHOPPER_TYPE = 2;
    public static final int BEETLE_TYPE = 3;
    public static final int ANT_TYPE = 4;
    public static final int MOSQUITO_TYPE = 5;
    public static final int LADYBUG_TYPE = 6;
    public static final int PILLBUG_TYPE = 7;
    
    //MACROS NOMS PIECES
    public static final String ANT_NAME = "Ant";
    public static final String SPIDER_NAME = "Spider";
    public static final String QUEEN_NAME = "Queen";
    public static final String BEETLE_NAME = "Beetle";
    public static final String GRASSHOPPER_NAME = "Grasshopper";
    
    //MACROS COORDONNEES DESSIN PIECES
    public static final double SIDE_SIZE = 30;
    public static final double X_ORIGIN = 30;
    public static final double Y_ORIGIN = 0;
    
    //MACRO NOMBRE DE VOISINS
    public static final int SIX=6;
    
    //MACRO MODE DE DIFFICULTE IA ET PLAYER
    public static final int PVP = 0;
    public static final int PVAI = 1;
    
    //MACRO PLAYER
    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;
    public static final int AI_PLAYER = 1;
    
    //MACRO AI DIFFICULTY
    public static final int EASY = 0;
    public static final int MEDIUM = 1;    
    public static final int HARD = 2;

    //MACRO TEXTE MENU
    public static final String PVP_STRING = "Joueur contre joueur";
    public static final String PVIA_STRING = "Joueur contre ordinateur";
    
    //MACRO STATUS GAME
    public static final int INGAME = 0;
    public static final int WIN_TEAM1 = 1;
    public static final int WIN_TEAM2 = 2;
    public static final int NUL = 3;
    
    public static final String getNotation(int id){
    	switch (id){
	    	case 0 : return "Q";
	    	case 1 : return "S1";
	    	case 2 : return "S2";
	    	case 3 : return "G1";
	    	case 4 : return "G2";
	    	case 5 : return "G3";
	    	case 6 : return "B1";
	    	case 7 : return "B2";
	    	case 8 : return "A1";
	    	case 9 : return "A2";
	    	case 10 : return "A3";
	    	case 11 : return "M";
	    	case 12 : return "L";
	    	case 13 : return "P";
    	}
    	return null;
    }
    
    public static final int getId(String notation){
    	switch (notation){
	    	case "Q" : return 0;
	    	case "S1" : return 1;
	    	case "S2" : return 2;
	    	case "G1" : return 3;
	    	case "G2" : return 4;
	    	case "G3" : return 5;
	    	case "B1" : return 6;
	    	case "B2" : return 7;
	    	case "A1" : return 8;
	    	case "A2" : return 9;
	    	case "A3" : return 10;
	    	case "M" : return 11;
	    	case "L" : return 12;
	    	case "P" : return 13;
    	}
    	return -1;
    }
    
    public static final String getName(int id){
    	switch (id){
	    	case 0 : return "QUEEN";
	    	case 1 : return "SPIDER1";
	    	case 2 : return "SPIDER2";
	    	case 3 : return "GRASSHOPPER1";
	    	case 4 : return "GRASSHOPPER2";
	    	case 5 : return "GRASSHOPPER3";
	    	case 6 : return "BEETLE1";
	    	case 7 : return "BEETLE2";
	    	case 8 : return "ANT1";
	    	case 9 : return "ANT2";
	    	case 10 : return "ANT3";
	    	case 11 : return "MOSQUITO";
	    	case 12 : return "LADYBUG";
	    	case 13 : return "PILLBUG";
    	}
    	return null;
    }

}
