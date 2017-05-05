package main.java.ia;

import java.util.ArrayList;
import main.java.utils.Consts;
import main.java.utils.Coord;

/*
this class is used to run algorithms on in an optimal way
representation : it's an array of nodes
each node contents : piece (type & team), coords (x,y,z) + some bools (stuck, visited ...)
Advantage over StoringConfig -> more or less constant time search by coords
-> almost constant time translation from storingConfig to LoopingConfig & opposite
 */
public class LoopingConfig {

    final private StoringConfig stconf;
    LoopingConfigNode array[];
    byte nbPiecesPerColor;

    public LoopingConfig(StoringConfig stconf) {
        this.stconf = stconf;
        this.array = new LoopingConfigNode[stconf.config.length];
        this.nbPiecesPerColor = (byte) (stconf.config.length / 2);
        int i;
        LoopingConfigNode node;
        //head insertion
        for (i = 0; i < stconf.config.length; i++) {
            node = new LoopingConfigNode(stconf, i);
            node.next = this.array[stconf.getX(i)];
            this.array[stconf.getX(i)] = node;
        }
    }

    /**
     * **************************** getters ****************************
     */
    //search by coordinates
    public LoopingConfigNode getNode(byte x, byte y) {
        LoopingConfigNode node = array[x];
        while ((node != null) && (node.y != y)) {
            node = node.next;
        }
        return node;
    }
    //search by type (eg get coords of white spider 2)
    // -> careful, there's no info about 

    public Coord getCoord(int piece) {
        return new Coord((int) stconf.getX(piece), (int) stconf.getY(piece));
    }

    //Neighbors getters
    public LoopingConfigNode getNorthEast(LoopingConfigNode node) {
        return this.getNode((byte) (node.x + 1), (byte) (node.y - 1));
    }

    public LoopingConfigNode getNorthWest(LoopingConfigNode node) {
        return this.getNode((byte) (node.x), (byte) (node.y - 1));
    }

    public LoopingConfigNode getEast(LoopingConfigNode node) {
        return this.getNode((byte) (node.x + 1), (byte) (node.y));
    }

    public LoopingConfigNode getWest(LoopingConfigNode node) {
        return this.getNode((byte) (node.x - 1), (byte) (node.y));
    }

    public LoopingConfigNode getSouthEast(LoopingConfigNode node) {
        return this.getNode((byte) (node.x), (byte) (node.y + 1));
    }

    public LoopingConfigNode getSouthWest(LoopingConfigNode node) {
        return this.getNode((byte) (node.x - 1), (byte) (node.y + 1));
    }

    //get all neighbors of a tile in an array list
    public ArrayList<LoopingConfigNode> getNeighbors(LoopingConfigNode node) {
        ArrayList<LoopingConfigNode> result = new ArrayList<LoopingConfigNode>();
        result.add(this.getEast(node));
        result.add(this.getNorthEast(node));
        result.add(this.getNorthWest(node));
        result.add(this.getSouthEast(node));
        result.add(this.getSouthWest(node));
        result.add(this.getWest(node));
        return result;
    }

    /**
     * ************ Deplacements            ****************************
     */
    public ArrayList<Coord> getPossibleDestinations(LoopingConfigNode node) {
        byte piece_type = (byte) (node.piece % nbPiecesPerColor);
        if (piece_type <= Consts.QUEEN) {
            return getPossibleQueenDestinations(node);
        } else if (piece_type <= Consts.SPIDER2) {
            return getPossibleSpiderDestinations(node);
        } else if (piece_type <= Consts.GRASSHOPPER3) {
            return getPossibleGrassHopperDestinations(node);
        } else if (piece_type <= Consts.BEETLE2) {
            return getPossibleBeetleDestinations(node);
        } else if (piece_type <= Consts.ANT3) {
            return getPossibleAntDestinations(node);
        } else {
            System.err.println("Erreur : Impossible de reconnaitre le type de pièce");
            return null;
        }
    }

    private ArrayList<Coord> getPossibleAntDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private ArrayList<Coord> getPossibleBeetleDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Coord> getPossibleGrassHopperDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Coord> getPossibleSpiderDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Coord> getPossibleQueenDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean RespectsOneHive(byte piece) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
