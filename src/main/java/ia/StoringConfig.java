/*
this class is used to store a game configuration in an optimal way
the short(16b) contains :
    ________________________
   |  x |  y |  z | b | b| b| , x,y :5 bits each ; z : 3 bits ; and 3 booleans

The pieces are associated to int between 0 and 10 in Consts.
 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import main.java.model.State;
import main.java.model.Tile;
import main.java.utils.Consts;

public class StoringConfig {

    public short config[];

    public StoringConfig(int nb_pieces) {
        config = new short[nb_pieces];
    }

    public StoringConfig(State state) {

        List<List<List<Tile>>> board = state.getBoard().getBoard();

        /// first we find out the total amount of pieces in the game
        //->pieces on the board
        int total_pieces_nb = 0;
        for (int i = 0; i < board.size(); i++) {
            total_pieces_nb += board.get(i).size();
        }
        // + pieces in hands of p1 & p2
        total_pieces_nb += state.getPlayer1().getInventory().size();
        total_pieces_nb += state.getPlayer2().getInventory().size();

        //now it's possible to allocate the config tab
        config = new short[total_pieces_nb];

        //now filling up from the infos found in the board
        Tile current;
        int index = 0;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(i).size(); j++) {
                for (int k = 0; k < board.get(i).get(j).size(); k++) ///dirty -> getting one short from lots of infos
                {
                    current = board.get(i).get(j).get(k);

                    if ((current != null) && (current.getPiece() != null) && (current.getPiece().getTeam() == 1)) {
                        index += total_pieces_nb / 2;
                    }
                    // get the piece byte from the team and name
                    switch (current.getPiece().getName()) {
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
                        case Consts.BEETLE_NAME:

                            break;
                        case Consts.QUEEN_NAME:

                            break;
                        case Consts.SPIDER_NAME:

                            break;
                    }
                }

            }
        }
    }
    //////     getters

    public byte getX(int index) {
        return (byte) ((config[index] & 0xF800) >> 11);
    }

    public byte getY(int index) {
        return (byte) ((config[index] & 0x07C0) >> 6);
    }

    public byte getZ(int index) {
        return (byte) ((config[index] & 0x0038) >> 3);
    }

    public byte getStuck(int index) {
        return (byte) ((config[index] & 0x0004) >> 2);
    }

    public byte getIsVisited(int index) {
        return (byte) ((config[index] & 0x0002) >> 1);
    }

    public byte getBool3(int index) {
        return (byte) (config[index] & 1);
    }

    //////    Setters
    public void setX(int index, byte newX) {
        config[index] &= 0x07FF;
        short toAdd = (short) (newX << 11);
        config[index] |= toAdd;
    }

    public void setY(int index, byte newY) {
        config[index] &= 0xF83F;
        short toAdd = (short) (newY << 6);
        config[index] |= toAdd;
    }

    public void setZ(int index, byte newZ) {
        config[index] &= 0xFFC7;
        short toAdd = (short) (newZ << 3);
        config[index] |= toAdd;
    }

    public void setStuck(int index, byte newStuck) {
        config[index] &= 0xFFFB;
        short toAdd = (short) (newStuck << 2);
        config[index] |= toAdd;
    }

    public void setIsVisited(int index, byte newIsVisited) {
        config[index] &= 0xFFFD;
        short toAdd = (short) (newIsVisited << 1);
        config[index] |= toAdd;
    }

    public void setBool3(int index, byte newBool3) {
        config[index] &= 0xFFFE;
        config[index] |= newBool3;
    }
/*
    // Tile to short -> Used to create the config from the state
    public short TileToShort(Tile tile) {

        short result = 0;
        if (tile.getPiece().getTeam() == 1) {
            result = 
        }
        // get the piece byte from the team and name
        switch (tile.getPiece().getName()) {
            case Consts.ANT_NAME:

                break;
            case Consts.BEETLE_NAME:

                break;
            case Consts.QUEEN_NAME:

                break;
            case Consts.SPIDER_NAME:

                break;
        }
    }
*/
}
