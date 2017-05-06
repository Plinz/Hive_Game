package main.java.ia;

import java.util.ArrayList;
import main.java.utils.Consts;
import main.java.utils.Coord;

/*
this class is used to run algorithms on in an optimal way
It has a double representation : 
    _-_-_Array -> Used for a O(1) access to a tile given the coords
                    each node contents : piece (type & team), coords (x,y,z) + some bools (stuck, visited ...)
    _-_-_stconf-> Used for a O(1) access to the coords of a given piece
                    same representation as in Storing config (one tricky int)

Advantage over StoringConfig -> more or less constant time search by coords
 */

 /*
********************         Constructor           *************************
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
        while ((node != null) && (node.getY() != y)) {
            node = node.getNext();
        }
        return node;
    }
    //search by type (eg get coords of white spider 2)
    // -> careful, there's no info about height

    public Coord getCoord(int piece) {
        return new Coord((int) stconf.getX(piece), (int) stconf.getY(piece));
    }

    /*
    ****************           Neighbors getters      **********************
     */
    ///One by one
    public LoopingConfigNode getNorthEast(LoopingConfigNode node) {
        return this.getNode((byte) (node.getX() + 1), (byte) (node.getY() - 1));
    }

    public LoopingConfigNode getNorthWest(LoopingConfigNode node) {
        return this.getNode((byte) (node.getX()), (byte) (node.getY() - 1));
    }

    public LoopingConfigNode getEast(LoopingConfigNode node) {
        return this.getNode((byte) (node.getX() + 1), (byte) (node.getY()));
    }

    public LoopingConfigNode getWest(LoopingConfigNode node) {
        return this.getNode((byte) (node.getX() - 1), (byte) (node.getY()));
    }

    public LoopingConfigNode getSouthEast(LoopingConfigNode node) {
        return this.getNode((byte) (node.getX()), (byte) (node.getY() + 1));
    }

    public LoopingConfigNode getSouthWest(LoopingConfigNode node) {
        return this.getNode((byte) (node.getX() - 1), (byte) (node.getY() + 1));
    }

    //All of them -> 2 implementations 
    //one returns array -> all of them (even null) in order
    //one returns arrayList -> no order, only non null neighbors
    //get all neighbors of a tile in an array
    public LoopingConfigNode[] getNeighborsInArray(LoopingConfigNode node) {
        LoopingConfigNode result[] = new LoopingConfigNode[6];
        result[0] = this.getEast(node);
        result[1] = this.getSouthEast(node);
        result[2] = this.getSouthWest(node);
        result[3] = this.getWest(node);
        result[4] = this.getNorthWest(node);
        result[5] = this.getNorthEast(node);
        return result;
    }

    //get all neighbors of a tile in an array list
    public ArrayList<LoopingConfigNode> getNeighborsInArrayList(LoopingConfigNode node) {
        ArrayList<LoopingConfigNode> result = new ArrayList<>();
        LoopingConfigNode current_neighbor;
        //East
        current_neighbor = this.getEast(node);
        if (current_neighbor != null) {
            result.add(current_neighbor);
        }
        //SouthEast
        current_neighbor = this.getSouthEast(node);
        if (current_neighbor != null) {
            result.add(current_neighbor);
        }
        //SouthWest
        current_neighbor = this.getSouthWest(node);
        if (current_neighbor != null) {
            result.add(current_neighbor);
        }
        //West
        current_neighbor = this.getWest(node);
        if (current_neighbor != null) {
            result.add(current_neighbor);
        }
        //NorthWest
        current_neighbor = this.getNorthWest(node);
        if (current_neighbor != null) {
            result.add(current_neighbor);
        }
        //NorthEast
        current_neighbor = this.getNorthEast(node);
        if (current_neighbor != null) {
            result.add(current_neighbor);
        }
        return result;
    }

    /**
     * ************ Deplacements ****************************
     */
    public ArrayList<StoringConfig> getPossibleDestinations(LoopingConfigNode node) throws CloneNotSupportedException {
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

    public ArrayList<StoringConfig> getPossibleAntDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<StoringConfig> getPossibleBeetleDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<StoringConfig> getPossibleGrassHopperDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<StoringConfig> getPossibleSpiderDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<StoringConfig> getPossibleQueenDestinations(LoopingConfigNode node) throws CloneNotSupportedException {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }

        LoopingConfigNode neighbors[] = this.getNeighborsInArray(node);
        ArrayList<StoringConfig> possibleDest = new ArrayList<>();
        int i;
        for (i = 0; i < neighbors.length; i++) {
            //testing gates : function to be implemented
            if ((neighbors[i].getPiece() == (byte) 0)
                    && ((neighbors[(i + 1) % neighbors.length].getPiece() == (byte) 0) && (neighbors[(i - 1) % neighbors.length].getPiece() != (byte) 0)
                    || (neighbors[(i + 1) % neighbors.length].getPiece() != (byte) 0) && (neighbors[(i - 1) % neighbors.length].getPiece() == (byte) 0))) {
                StoringConfig newConf = new StoringConfig(this.stconf.config.length);
                System.arraycopy(this.stconf.config, 0, newConf.config, 0, this.stconf.config.length);
                //actualizing the coordinates of the queen
                newConf.setX((int) node.getPiece(), neighbors[i].getX());
                newConf.setY((int) node.getPiece(), neighbors[i].getY());
                possibleDest.add(newConf);
            }
        }
        return possibleDest;
    }

    /*
    *****************         One hive rule     ******************************
     */
    ///walking along some kind of graph to check if the one_hive rule
    /// is still respected when the piece node is moving
    private boolean RespectsOneHive(LoopingConfigNode node) {
        // 1st check -> if there's only one neighbor -> no need to go further
        ArrayList<LoopingConfigNode> neighbors = this.getNeighborsInArrayList(node);
        if (neighbors.size() == 1) {
            return true;
        }
        //now we chose any neighbor of node
        LoopingConfigNode neighbor = neighbors.get(0), current_node;

        //marking both the moving node & the first neighbor we put in the set
        node.isVisited = true;
        neighbor.isVisited = true;
        ArrayList<LoopingConfigNode> nodeSet = new ArrayList<>();
        nodeSet.add(neighbor);
        while (!nodeSet.isEmpty()) {
            current_node = nodeSet.get(0);
            neighbors = this.getNeighborsInArrayList(current_node);
            for (LoopingConfigNode current_node_neighbor : neighbors) {
                if (!current_node_neighbor.isVisited) {
                    current_node_neighbor.isVisited = true;
                    nodeSet.add(current_node_neighbor);
                }
            }
            nodeSet.remove(current_node);
        }

        for (int x = 0; x < array.length; x++) {
            current_node = array[x];
            while (current_node != null) {
                if (!current_node.isVisited) {
                    return false;
                }
                current_node = current_node.next;
            }
        }
        return true;
    }

    //may not be necessary
    /*public boolean noGateFound(LoopingConfigNode source, LoopingConfigNode destination) throws Exception
    {
        //method to be used only on adjacent tiles
        //does not test the freeness of the destination tile
        //does not test the constant contact
        
        LoopingConfigNode neighbors[] = this.getNeighbors(source);
        int destIndex = 0;
        
        // /!\ CORRECTNESS OF THE SECOND CONDITION TO BE CHECKED
        while((destIndex < neighbors.length) && (neighbors[destIndex] != destination))
        {
            destIndex += 1;
        }
        if (destIndex> neighbors.length)
            throw new Exception("Source and destination nodes do not represent adjacent tiles !");
        
        if ((neighbors[(destIndex+1)%neighbors.length].getPiece() == (byte)0) || (neighbors[(destIndex-1)%neighbors.length].getPiece() == (byte)0))
            return true;
       
        return false;
           
    }*/
}
