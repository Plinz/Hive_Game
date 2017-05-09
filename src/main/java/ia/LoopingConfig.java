package main.java.ia;

import java.util.ArrayList;
import main.java.utils.Consts;
import main.java.utils.Coord;

/*
this class is used to run movements & heuristics algorithms on in an optimal way
It has a double representation : 
    _-_-_Array -> Used for a O(1) access to a tile given the coords
                    each node contents : piece (type & team), coords (x,y,z) + some bools (stuck, visited ...)
    _-_-_stconf-> Used for a O(1) access to the coords of a given piece
                    same representation as in Storing config (one tricky int)

Advantage over StoringConfig -> more or less constant time search by coords
 */
public class LoopingConfig {

    final private StoringConfig stconf;
    LoopingConfigNode array[];
    int nbPiecesPerColor;
    int player, turn;

    /*
     ********************         Constructor           *************************
     */
    public LoopingConfig(StoringConfig stconf) {
        this.stconf = stconf;
        this.array = new LoopingConfigNode[stconf.config.length];
        this.nbPiecesPerColor = (stconf.config.length / 2);
        int i;
        LoopingConfigNode node;
        //head insertion
        for (i = 0; i < stconf.config.length; i++) {
            node = new LoopingConfigNode(stconf, i);
            node.next = this.array[stconf.getX(i)];
            this.array[stconf.getX(i)] = node;
        }
    }


    /*
    ****************************     Testers   *************************
     */
    public boolean isFreeNode(LoopingConfigNode node) {
        return (node.piece == 0);
    }

    public boolean isFreeCoord(Coord coord) {
        return (this.getNode(coord.getX(), coord.getY()).piece == 0);
    }

    //check if 2 tiles have same color -> takes in account the fact beetle can cover
    //a tile.
    public boolean isSameColor(LoopingConfigNode node1, LoopingConfigNode node2) {
        LoopingConfigNode node1Visible = array[node1.getX()];
        if (node1.stuck) {
            while ((node1Visible != null) && ((node1Visible.getY() != node1.getY()) || (node1Visible.stuck))) {
                node1Visible = node1Visible.next;
            }
        } else {
            node1Visible = node1;
        }

        LoopingConfigNode node2Visible = array[node2.getX()];
        if (node2.stuck) {
            while ((node2Visible != null) && ((node2Visible.getY() != node2.getY()) || (node2Visible.stuck))) {
                node2Visible = node2Visible.next;
            }
        } else {
            node2Visible = node1;
        }

        return (((node1Visible.getPiece() < nbPiecesPerColor) && (node2Visible.getPiece() < nbPiecesPerColor))
                || ((node1Visible.getPiece() >= nbPiecesPerColor) && (node2Visible.getPiece() >= nbPiecesPerColor)));

    }

    /**
     * **************************** getters ****************************
     */
    //search by coordinates
    public LoopingConfigNode getNode(int x, int y) {
        LoopingConfigNode node = array[x];
        while ((node != null) && (node.getY() != y)) {
            node = node.getNext();
        }
        return node;
    }

    public LoopingConfigNode getNode(Coord coord) {
        return this.getNode(coord.getX(), coord.getY());
    }

    public LoopingConfigNode getNode(int piece) {
        return this.getNode(this.getCoord(piece));
    }

    //search by type (eg get coords of white spider 2)
    // -> careful, there's no info about height
    public Coord getCoord(int piece) {
        return new Coord((int) stconf.getX(piece), (int) stconf.getY(piece));
    }

    public Coord getCoord(LoopingConfigNode node) {
        return this.getCoord(node.piece);
    }

    /*
    ****************           Neighbors getters      **********************
     */
    ///One by one
    public LoopingConfigNode getNorthEast(LoopingConfigNode node) {
        return this.getNode((node.getX() + 1), (node.getY() - 1));
    }

    public LoopingConfigNode getNorthWest(LoopingConfigNode node) {
        return this.getNode((node.getX()), (node.getY() - 1));
    }

    public LoopingConfigNode getEast(LoopingConfigNode node) {
        return this.getNode((node.getX() + 1), (node.getY()));
    }

    public LoopingConfigNode getWest(LoopingConfigNode node) {
        return this.getNode((node.getX() - 1), (node.getY()));
    }

    public LoopingConfigNode getSouthEast(LoopingConfigNode node) {
        return this.getNode((node.getX()), (node.getY() + 1));
    }

    public LoopingConfigNode getSouthWest(LoopingConfigNode node) {
        return this.getNode((node.getX() - 1), (node.getY() + 1));
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
    public ArrayList<StoringConfig> getPossibleDestinations(LoopingConfigNode node) {
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
            return new ArrayList<>();
        }
    }

    //Get coords of positions where a new tile can be placed by player
    //called with player as param so no redundant call for each piece in player's hand
    public ArrayList<Coord> getNewPossiblePositions(int player) {
        int start = player * nbPiecesPerColor;
        int finish = start + nbPiecesPerColor;
        ArrayList<Coord> result = new ArrayList<>();
        LoopingConfigNode currentNode, currentNeighbor;
        for (int i = start; i < finish; i++) {
            //find all pieces from player already on board
            if (this.stconf.isOnBoard(i)) {
                currentNode = this.getNode(i);
                //get the neighbors of piece on board
                Coord[] neighbors = this.getCoord(i).getNeighborsInArray();
                for (int j = 0; j < 6; j++) {
                    //if neighbor is free -> check its neighbor to see if no other player piece
                    if (this.getNode(neighbors[j]) == null) {
                        Coord[] neighborsOfNeighbors = neighbors[j].getNeighborsInArray();
                        boolean canBeAdded = true;
                        for (int k = 0; k < 6; k++) {
                            currentNeighbor = this.getNode(neighborsOfNeighbors[k]);
                            if ((currentNeighbor != null) && (!this.isSameColor(currentNode, currentNeighbor))) {
                                canBeAdded = false;
                            }
                        }
                        //it can be added -> add the coord to resultif not redundant
                        if ((canBeAdded) && (!result.contains(neighbors[j]))) {
                            result.add(neighbors[j]);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * ***************************** Sliding Bugs ******************
     */
    ///Getting the available neighbors when sliding -> method used to calculate
    ///displacements of the ant, spider & queen.
    /// it's a Coord->Coord method so for spider & ant move, we can use result to call method on it
    /// this method does check both freedom_to_move & permanent_contact rules, but not one_hive
    public ArrayList<Coord> getPossibleSlidingDestinations(Coord coord) {
        Coord neighbors[] = coord.getNeighborsInArray();
        ArrayList<Coord> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            // tricky condition -> if neighbor[i] is free 
            if (this.isFreeCoord(neighbors[i])) {
                // and if one & only one of both (i-1,i+1) neighbors are free 
                if ((this.isFreeCoord(neighbors[(i - 1) % 6])) && (!this.isFreeCoord(neighbors[(i + 1) % 6]))
                        || (!this.isFreeCoord(neighbors[(i - 1) % 6])) && (this.isFreeCoord(neighbors[(i + 1) % 6]))) {
                    result.add(neighbors[i]);
                }
            }
        }
        return result;
    }

    public ArrayList<StoringConfig> getPossibleSpiderDestinations(LoopingConfigNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }
        //now we 'remove' the spider tile from the board, we 'll put it back in place
        //before leaving the method
        int originalX = node.getX(), originalY = node.getY();
        node.x = -1;
        node.y = -1;
        //getting possible coords after one move
        ArrayList<Coord> CoordsAfterFirstMove = getPossibleSlidingDestinations(new Coord(originalX, originalY));

        //getting possible coords after 2 moves
        ArrayList<Coord> temp, CoordsAfterSecondMove = new ArrayList<>();
        for (Coord coord : CoordsAfterFirstMove) {
            temp = getPossibleSlidingDestinations(coord);
            for (Coord newCoord : temp) {
                if (!CoordsAfterSecondMove.contains(newCoord)) {
                    CoordsAfterSecondMove.add(newCoord);
                }
            }
        }

        //getting possible coords after the 3rd move
        ArrayList<Coord> CoordsAfterThirdMove = new ArrayList<>();
        for (Coord coord : CoordsAfterSecondMove) {
            temp = getPossibleSlidingDestinations(coord);
            for (Coord newCoord : temp) {
                if (!CoordsAfterThirdMove.contains(newCoord)) {
                    CoordsAfterThirdMove.add(newCoord);
                }
            }
        }

        //creating all the storing configs from the coords we obtained
        ArrayList<StoringConfig> result = new ArrayList<>();
        for (Coord coord : CoordsAfterThirdMove) {
            StoringConfig newStConfig = new StoringConfig(stconf);
            newStConfig.setX(node.piece, (byte) coord.getX());
            newStConfig.setY(node.piece, (byte) coord.getY());
            result.add(newStConfig);
        }
        //reset the coords from the spider back to original ones
        node.x = originalX;
        node.y = originalY;

        return result;
    }

    public ArrayList<StoringConfig> getPossibleQueenDestinations(LoopingConfigNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }

        LoopingConfigNode neighbors[] = this.getNeighborsInArray(node);
        ArrayList<StoringConfig> possibleDest = new ArrayList<>();
        int i;
        for (i = 0; i < neighbors.length; i++) {
            //testing gates : function to be implemented
            if ((neighbors[i].getPiece() == 0)
                    && ((neighbors[(i + 1) % neighbors.length].getPiece() == 0) && (neighbors[(i - 1) % neighbors.length].getPiece() != 0)
                    || (neighbors[(i + 1) % neighbors.length].getPiece() != 0) && (neighbors[(i - 1) % neighbors.length].getPiece() == 0))) {
                StoringConfig newConf = new StoringConfig(this.stconf.config.length);
                System.arraycopy(this.stconf.config, 0, newConf.config, 0, this.stconf.config.length);
                //actualizing the coordinates of the queen
                newConf.setX((int) node.getPiece(), (byte) neighbors[i].getX());
                newConf.setY((int) node.getPiece(), (byte) neighbors[i].getY());
                possibleDest.add(newConf);
            }
        }
        return possibleDest;
    }

    public ArrayList<StoringConfig> getPossibleAntDestinations(LoopingConfigNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }

        //now we 'remove' the ant tile from the board, we 'll put it back in place
        //before leaving the method
        int originalX = node.getX(), originalY = node.getY();
        node.x = -1;
        node.y = -1;

        ArrayList<Coord> possibleDestinations = new ArrayList<>(), temp;
        ArrayList<Coord> newlyAdded = getPossibleSlidingDestinations(new Coord(originalX, originalY));
        boolean newCoordWasAdded = true;
        while (newCoordWasAdded) {
            ArrayList<Coord> newlyAddedtemp = new ArrayList<>();
            newCoordWasAdded = false;
            for (Coord coord : newlyAdded) {
                temp = getPossibleSlidingDestinations(coord);
                for (Coord newCoord : temp) {
                    if (!possibleDestinations.contains(newCoord)) {
                        possibleDestinations.add(coord);
                        newCoordWasAdded = true;
                        newlyAddedtemp.add(newCoord);
                    }
                }
            }
            newlyAdded = newlyAddedtemp;
        }
        ArrayList<StoringConfig> result = new ArrayList<>();
        for (Coord coord : possibleDestinations) {
            StoringConfig newStConfig = new StoringConfig(stconf);
            newStConfig.setX(node.piece, (byte) coord.getX());
            newStConfig.setY(node.piece, (byte) coord.getY());
            result.add(newStConfig);
        }
        //reset the coords from the spider back to original ones
        node.x = originalX;
        node.y = originalY;

        return result;

    }

    /**
     * ******************************** Other Bugs ****************************
     */
    public ArrayList<StoringConfig> getPossibleBeetleDestinations(LoopingConfigNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<StoringConfig> getPossibleGrassHopperDestinations(LoopingConfigNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }
        ArrayList<StoringConfig> result = new ArrayList<>();
        StoringConfig newStoringConfig;
        Coord currentCoord;
        //East
        currentCoord = this.getCoord(node).getEast();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getEast();
            }
            newStoringConfig = new StoringConfig(stconf);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
        }
        //NorthEast
        currentCoord = this.getCoord(node).getNorthEast();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getNorthEast();
            }
            newStoringConfig = new StoringConfig(stconf);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
        }
        //East
        currentCoord = this.getCoord(node).getSouthEast();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getSouthEast();
            }
            newStoringConfig = new StoringConfig(stconf);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
        }
        //East
        currentCoord = this.getCoord(node).getWest();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getWest();
            }
            newStoringConfig = new StoringConfig(stconf);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
        }
        //East
        currentCoord = this.getCoord(node).getNorthWest();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getNorthWest();
            }
            newStoringConfig = new StoringConfig(stconf);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
        }
        //East
        currentCoord = this.getCoord(node).getSouthWest();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getSouthWest();
            }
            newStoringConfig = new StoringConfig(stconf);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
        }
        return result;
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

    /*
     **************************      ToString *********************************
     */
    public String toString() {
        String result = "Looping Config :\n";
        result += "player : " + player + ",turn =" + turn + ",pieces par joueur=" + nbPiecesPerColor + "\narray :\n";
        for (int i = 0; i < array.length; i++) {
            result += array[i].toString();
        }
        result += "stconf :\n" + stconf.toString();
        return result;
    }
}
