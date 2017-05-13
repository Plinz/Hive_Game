/*
this class is used to store a game configuration in an optimal way
each int(32 b) contains :
    _________________________
   |__x_|__y_|_z_|_b_|_b_|_b_| , x,y,z :1 byte each and up to 8 booleans
    
    (real booleans, 1 bit each, not java (16 bytes).

Booleans (from left to right) :1- isStuck ;2- isOnBoard 

The pieces are associated to int between 0 and 10 in Consts.
 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Column;
import main.java.model.State;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.Coord;

public class StoringConfig {

    public int config[];
    public int turn, currentPlayer;

    public StoringConfig(int nb_pieces) {
        config = new int[nb_pieces];
    }

    public StoringConfig(StoringConfig stconf) {
        this.config = new int[stconf.config.length];
        System.arraycopy(stconf.config, 0, this.config, 0, stconf.config.length);
    }

    /*
    This is the translator -> translates a state given by the 
    core into a Storing config.
    Advantage -> this is much lighter (about 44 bytes heavy for a whole
    game configuration.
     */
    public StoringConfig(State state) {
        this.turn = state.getTurn();
        this.currentPlayer = state.getCurrentPlayer();
        List<Column> board = state.getBoard().getBoard();
        Tile current;

        /// first we find out the total amount of pieces in the game
        //->pieces on the board
        int total_pieces_nb = state.getBoard().getNbPieceOnTheBoard();
        // + pieces in hands of p1 & p2
        total_pieces_nb += state.getPlayers()[0].getInventory().size();
        total_pieces_nb += state.getPlayers()[1].getInventory().size();

        //now it's possible to allocate the config tab
        config = new int[total_pieces_nb];

        //now filling up from the infos found in the board
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                for (int k = 0; k < board.get(i).get(j).size(); k++) ///dirty -> getting one short from lots of infos
                {
                    int index = 0;
                    current = board.get(i).get(j).get(k);

                    //check if tile exists & if there's a piece in it
                    if ((current != null) && (current.getPiece() != null)) { ///current != null ->> probably useless
                        // get the piece byte from the team and name
                        //first team & type
                        //team
                        if (current.getPiece().getTeam() == 1) {
                            index += total_pieces_nb / 2;
                        }
                        /*
                        //type
                        switch (current.getPiece().getName()) {
                            //Ants
                            case Consts.ANT_NAME:
                                if (config[index + Consts.ANT1] == 0) {
                                    index += Consts.ANT1;
                                } else if (config[index + Consts.ANT2] == 0) {
                                    index += Consts.ANT2;
                                } else if (config[index + Consts.ANT3] == 0) {
                                    index += Consts.ANT3;
                                } else {
                                    //means there are 4 Ants with same color in the field -> should never happen
                                    System.err.println("Erreur : 4 fourmis de la même couleur sur la board");
                                }
                                break;
                            //Beetles
                            case Consts.BEETLE_NAME:
                                if (config[index + Consts.BEETLE1] == 0) {
                                    index += Consts.BEETLE1;
                                } else if (config[index + Consts.BEETLE2] == 0) {
                                    index += Consts.BEETLE2;
                                } else {
                                    //means there are 3 beetles with same color in the field -> should never happen
                                    System.err.println("Erreur : 3 scarabées de la même couleur sur la board");
                                }
                                break;
                            //Queens
                            case Consts.QUEEN_NAME:
                                if (config[index + Consts.QUEEN] == 0) {
                                    index += Consts.QUEEN;
                                } else {
                                    //means there are 2 queens with same color in the field -> should never happen
                                    System.err.println("Erreur : 2 reines de la même couleur sur la board");
                                }
                                break;
                            case Consts.SPIDER_NAME:
                                if (config[index + Consts.SPIDER1] == 0) {
                                    index += Consts.SPIDER1;
                                } else if (config[index + Consts.SPIDER2] == 0) {
                                    index += Consts.SPIDER2;
                                } else {
                                    //means there are 3 spiders with same color in the field -> should never happen
                                    System.err.println("Erreur : 3 araignées de la même couleur sur la board");
                                }
                                break;
                            case Consts.GRASSHOPPER_NAME:
                                if (config[index + Consts.GRASSHOPPER1] == 0) {
                                    index += Consts.GRASSHOPPER1;
                                } else if (config[index + Consts.GRASSHOPPER2] == 0) {
                                    index += Consts.GRASSHOPPER2;
                                } else if (config[index + Consts.GRASSHOPPER3] == 0) {
                                    index += Consts.GRASSHOPPER3;
                                } else {
                                    //means there are 4 grasshoppers with same color in the field -> should never happen
                                    System.err.println("Erreur : 4 sauterelles de la même couleur sur la board");
                                }
                                break;
                            default:
                                System.err.println("Erreur : Nom de tuile inconnu présent dans la board");
                                break;
                        }*/
                        index += current.getPiece().getId();
                        this.setX(index, (byte) current.getX());
                        this.setY(index, (byte) current.getY());
                        this.setZ(index, (byte) current.getZ());
                        this.setIsStuck(index, current.isBlocked());
                        this.setIsOnBoard(index, true);
                    }

                }

            }
        }
    }

    /*
     *                  GETTERS
     */
    public byte getX(int index) {
        return (byte) (config[index] >> 24);
    }

    public byte getY(int index) {
        return (byte) ((config[index] & 0x00FF0000) >> 16);
    }

    public byte getZ(int index) {
        return (byte) ((config[index] & 0x0000FF00) >> 8);
    }

    public boolean isStuck(int index) { //tells if there's a bug atop that bug
        return ((config[index] & 0x00000080) >> 7) == 1;
    }

    public boolean isOnBoard(int index) { //tells if the piece is on the board or not
        return ((config[index] & 0x00000040) >> 6) == 1;
    }

    /*
     *                  SETTERS
     */
    public void setX(int index, byte newX) {
        config[index] &= 0x00FFFFFF;
        int toAdd = (int) (newX << 24);
        config[index] |= toAdd;
    }

    public void setY(int index, byte newY) {
        config[index] &= 0xFF00FFFF;
        int toAdd = (int) (newY << 16);
        config[index] |= toAdd;
    }

    public void setZ(int index, byte newZ) {
        config[index] &= 0xFFFF00FF;
        int toAdd = (int) (newZ << 8);
        config[index] |= toAdd;
    }

    public void setIsStuck(int index, boolean newIsStuck) {
        config[index] &= 0xFFFFFF7F;
        int toAdd;
        if (newIsStuck) {
            toAdd = 1;
        } else {
            toAdd = 0;
        }
        toAdd <<= 7;
        config[index] |= toAdd;
    }

    public void setIsOnBoard(int index, boolean newIsVisited) {
        config[index] &= 0xFFFFFFD;
        int toAdd;
        if (newIsVisited) {
            toAdd = 1;
        } else {
            toAdd = 0;
        }
        toAdd <<= 6;
        config[index] |= toAdd;
    }

    /*
     *                   NEXT MOVE
     */
    public ArrayList<StoringConfig> getNextPossibleMoves() {
        ArrayList<StoringConfig> temp, result = new ArrayList<>();
        System.err.println("getNextPossibleMove :\nturn =" + turn);

        GameConfig gameConfig = new GameConfig(this, turn);

        //if 1st turn
        if (turn == 0) {
            result = gameConfig.getFirstTurnMove();
            System.err.println("PossibleFirstTurn :");
            for (StoringConfig storingConfig : result) {
                System.err.println(storingConfig.toString());
            }
            return result;
            //if 2nd turn
        } else if (turn == 1) {
            result = gameConfig.getSecondTurnMove();
            System.err.println("PossibleSecondTurn :");
            for (StoringConfig storingConfig : result) {
                System.err.println(storingConfig.toString());
            }
            return result;
        }

        ArrayList<Coord> possibleNewPositions = gameConfig.getNewPossiblePositions();
        int start = gameConfig.currentPlayer * gameConfig.nbPiecesPerColor;
        int finish = start + gameConfig.nbPiecesPerColor;

        //7th & 8th turns -> if player did not play the queen he has to
        if ((gameConfig.turn == 7) || (gameConfig.turn == 8)) {
            if (!gameConfig.getNode(start).isOnBoard) {
                for (Coord coord : possibleNewPositions) {
                    StoringConfig newStoringConfig = new StoringConfig(this);
                    newStoringConfig.setX(start, (byte) coord.getX());
                    newStoringConfig.setY(start, (byte) coord.getY());
                    newStoringConfig.setIsOnBoard(start, true);
                    result.add(newStoringConfig);
                }
                System.err.println("Possible 7 & 8 Turn :");
                for (StoringConfig storingConfig : result) {
                    System.err.println(storingConfig.toString());
                }
                return result;
            }
        }

        //normal situation
        // check all pieces from player
        for (int i = start; i < finish; i++) {

            //piece on board -> check that queen is on board too
            if (gameConfig.getNode(i).isOnBoard) {
                if (gameConfig.getNode(start).isOnBoard) {
                    temp = gameConfig.getPossibleDestinations(gameConfig.getNode(i));
                    for (StoringConfig stconfig : temp) {
                        result.add(stconfig);
                    }
                }
            } else {
                //piece not on board -> add possible positions for it
                int j = i % gameConfig.nbPiecesPerColor;
                //stupid condition -> can be refactored
                if (((j == Consts.SPIDER2) && (!gameConfig.getNode(Consts.SPIDER1).isOnBoard))
                        || ((j == Consts.GRASSHOPPER2) && (!gameConfig.getNode(Consts.GRASSHOPPER1).isOnBoard))
                        || ((j == Consts.GRASSHOPPER3) && (!gameConfig.getNode(Consts.GRASSHOPPER2).isOnBoard))
                        || ((j == Consts.BEETLE2) && (!gameConfig.getNode(Consts.BEETLE1).isOnBoard))
                        || ((j == Consts.ANT2) && (!gameConfig.getNode(Consts.ANT1).isOnBoard))
                        || ((j == Consts.ANT3) && (!gameConfig.getNode(Consts.ANT2).isOnBoard))) {
                    //do nothing -> the same kind of piece was just added
                } else {
                    for (Coord coord : possibleNewPositions) {
                        StoringConfig newStoringConfig = new StoringConfig(this);
                        newStoringConfig.setX(i, (byte) coord.getX());
                        newStoringConfig.setY(i, (byte) coord.getY());
                        newStoringConfig.setIsOnBoard(i, true);
                        result.add(newStoringConfig);
                    }
                }
            }
        }
        /* printing all config for study
        System.err.println("PossibleNextTurn :");
        for (StoringConfig storingConfig : result){
            System.err.println(storingConfig.toString());
        }*/
        return result;
    }

    /*
     *                  UTILS
     */
    public String toString() {
        String result = "Storing Config :\n\tOn board :\n";
        for (int i = 0; i < config.length; i++) {
            if (this.isOnBoard(i)) {
                result += "\tpiece:" + i + "->x,y,z=" + this.getX(i) + "," + this.getY(i) + "," + this.getZ(i) + ".";
                if (this.isStuck(i)) {
                    result += "Stuck.\n";
                } else {
                    result += "\n";
                }
            }

        }
        return result;
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
            return true;
        
        if (object == null)
            return false;
        
        if (this.getClass() != object.getClass())
            return false;
        
        StoringConfig obj = (StoringConfig) object;
        if (this.config.length != obj.config.length)
            return false;
        
        for (int i = 0; i < this.config.length; i++)
            if (this.config[i] != obj.config[i])
                return false;
        return true;
    }
    
}
