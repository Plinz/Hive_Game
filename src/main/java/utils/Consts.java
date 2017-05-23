
package main.java.utils;

public class Consts {
    
    //MACRO RESOLUTION
    public static final String[] RESOLUTIONS= {"1280x720"};
    
    //MACRO NB PIECES TOTAL
    public static final int NB_PIECES_PER_PLAYER = 11;
    public static final int NB_PIECES_IN_GAME = 22;
    
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
    public static double SIDE_SIZE = 50;
    public static final double X_ORIGIN = 30;
    public static final double Y_ORIGIN = 0;
    
    //MACRO NOMBRE DE VOISINS
    public static final int SIX=6;
    
    //MACRO MODE DE JEU
    public static final int PVP = 0;
    public static final int PVAI = 1;
    public static final int AIVP = 2;
    public static final int PVEX = 3;
    public static final int EXVP = 4;
    
    //MACRO PLAYER
    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;
    
    //MACRO AI DIFFICULTY
    public static final int EASY = 0;
    public static final int MEDIUM = 1;    
    public static final int HARD = 2;
    
    //MACRO NAME IA
    public static final String[] IA_NAME = {"IA_FACILE","IA_NORMALE","IA_DIFFICILE"};

    //MACRO TEXTE MENU
    public static final String PVP_STRING = "Joueur contre joueur";
    public static final String PVIA_STRING = "Joueur contre ordinateur";
    
    //MACRO STATUS GAME
    public static final int INGAME = 0;
    public static final int WIN_TEAM1 = 1;
    public static final int WIN_TEAM2 = 2;
    public static final int NUL = 3;
    
    //MACRO HEURISTIQUE
    public static final int MINIMUM_HEURISTICS = -5000;
    public static final int BEST_HEURISTICS = 5000;
    
    public static final int DEPTH_EASY = 1;
    public static final int DEPTH_MEDIUM = 2;
    public static final int DEPTH_HARD = 3;
        //MACRO HEURISTIQUE -> OPENINGS   
        //  /!\ la somme des doubles DOIT faire 1 et la taille du tableau = 5
        //rappel ordre : queen - spider -grasshopper - beetle - ant  
    public static final double[] CHOOSE_QUEEN = {1, 0, 0, 0, 0};
    public static final double[] CHOOSE_WHATEVER = {0.2, 0.2, 0.2, 0.2, 0.2};

    public static final double[] EASY_ADD_TURN_1 = {0.2, 0.3, 0.3, 0.1, 0.1};
    public static final double[] EASY_ADD_TURN_2 = {0.2, 0.3, 0.3, 0.1, 0.1};
    public static final double[] EASY_ADD_TURN_3 = {0.2, 0.2, 0.2, 0.2, 0.2};
    public static final double[] EASY_ADD_TURN_4 = {0, 0.15, 0.15, 0.35, 0.35};
    
    public static final double EASY_TURN_4_CHOOSE_TO_ADD = 0.75;
    public static final double EASY_MID_GAME_CHOOSE_TO_ADD = 0.55;
    
    public static final double[] MEDIUM_ADD_TURN_1 = {0.1, 0.4, 0.4, 0.05, 0.05};
        public static final double[] MEDIUM_ADD_TURN_2_IF_SPIDER_ON_1 = {0.4, 0.1, 0.4, 0.05, 0.05};
        public static final double[] MEDIUM_ADD_TURN_2_IF_GRASSHOPPER_ON_1 = {0.4, 0.4, 0.1, 0.05, 0.05};
        public static final double[] MEDIUM_ADD_TURN_2_IF_QUEEN_ON_1 = {0, 0.45, 0.45, 0.05, 0.05};
        public static final double[] MEDIUM_ADD_TURN_2_IF_BETTLE_OR_ANT_ON_1 = {0.4, 0.3, 0.3, 0, 0};
    public static final double[] MEDIUM_ADD_TURN_3 = {0.4, 0.15, 0.15, 0.15, 0.15};
    public static final double[] MEDIUM_ADD_TURN_4 = {0, 0.1, 0.1, 0.4, 0.4};
    
    public static final double MEDIUM_TURN_4_CHOOSE_TO_ADD = 0.5;

    //METHODES
    public static final int getType (int id){
    	switch (id){
	    	case 0 : return 0;
	    	case 1 :
	    	case 2 : return 1;
	    	case 3 :
	    	case 4 :
	    	case 5 : return 2;
	    	case 6 :
	    	case 7 : return 3;
	    	case 8 :
	    	case 9 :
	    	case 10 : return 4;
	    	case 11 : return 5;
	    	case 12 : return 6;
	    	case 13 : return 7;
		}
		return -1;   	
    }
    
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
    
    public static final char getColor(int player){
    	return player == Consts.PLAYER1 ? 'w' : 'b';
    }
    
    public static final int getPlayer(char c){
    	return c == 'w' ? Consts.PLAYER1 : Consts.PLAYER2;
    }
    
    //MACRO REGLES DU JEU
    public static String GOAL = "Le but du jeu est d'encercler totalement la reine des abeilles adverse, à l'aide de vos pièces et de celles de votre adversaire.";
    public static String DURING_THE_GAME = "Le joueur blanc commence la partie. Ensuite les joueurs jouent tour à tour leurs pièces avec les restrictions suivantes : \n- Chaque joueur doit poser sa reine des abeilles dans les 4 premiers tours.\n- Dès lors, il est possible au joueur de déplacer les pièces présentes sur le plateau. \nLa partie se termine par une victoire d'un des deux joueurs lorsque l'une des reines est totalement encerclée. \n Si les deux reines sont encerclées, alors il s'agit d'un match nul.";
    public static String PIECE_POSITIONNING = "Au premier tour, le joueur blanc pose sa première pièce sur le seul emplacement disponible. Le joueur noir répond en placant sa première pièce autour de la pièce du joueur blanc.\n A partir des tours suivants, les joueurs sont obligés de poser leur pièce uniquement à côté des pièces de leur couleur, elles ne doivent pas toucher une pièce de leur adversaire. \n\n Le déplacement d'une pièce est impossible si la nouvelle ruche formée n'est pas connexe.";
    public static String QUEEN_MOVEMENT = "La reine des abeilles peut se déplacer d'une case.\nVous perdez la partie si votre reine est totalement encerclée.";
    public static String ANT_MOVEMENT = "La fourmi peut se déplacer autour du plateau de jeu.";
    public static String SPIDER_MOVEMENT = "L'araignée peut se déplacer de trois cases exactement.";
    public static String GRASSHOPPER_MOVEMENT = "La sauterelle peut sauter par dessus une rangée de cases, le nombre de cases qu'elle saute n'est pas restreint.";
    public static String BEETLE_MOVEMENT = "Le scarabée peut se déplacer d'une case.\nCependant il peut aussi monter sur les pièces adjacentes, les empêchant ainsi de bouger." ;

    //MACRO QUITTER MENU DE JEU
    public static final int GO_TO_MAIN = 0;
    public static final int GO_TO_GAME = 1;
    public static final int GO_TO_LOAD = 2;
    
    //MACRO ETAT NOUVELLE PARTIE
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int RANDOM = 2;
    
    //MACRO IO
	public static final int PORT = 16518;
        
    //MACRO STATE BOARD
        public static final int WAIT_FOR_INPUT = 0;
        public static final int PROCESSING = 1;
        public static final int READY_TO_CHANGE = 2;
        public static final int END_OF_THE_GAME = 3;
        public static final int ANIMATING = 4;
        
    //MACRO FRAME BETWEEN PLAYS
        public static final int FRAMES_TO_WAIT = 1;
    
}
