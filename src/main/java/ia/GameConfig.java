package main.java.ia;

import java.util.ArrayList;
import main.java.utils.Consts;
import main.java.utils.Coord;
import main.java.utils.Cube;
import static java.lang.Math.max;

/*
this class is used to run movements & heuristics algorithms on in an optimal way
It has a double representation : 
    _-_-_Array -> Used for a O(1) access to a tile given the coords
                    each node contents : piece (type & team), coords (x,y,z) + some bools (stuck, visited ...)
    _-_-_stconf-> Used for a O(1) access to the coords of a given piece
                    same representation as in Storing config (one tricky int)

Advantage over StoringConfig -> more or less constant time search by coords
 */
public class GameConfig {

    final private StoringConfig stconf;
    PieceNode array[];
    int nbPiecesPerColor;
    int player, turn;

    /*
     ********************         Constructor           *************************
     */
    public GameConfig(StoringConfig stconf, int turn) {
        this.turn = turn;
        this.player = (turn + 1) % 2;
        this.stconf = stconf;
        this.array = new PieceNode[stconf.config.length];
        this.nbPiecesPerColor = (stconf.config.length / 2);
        int i;
        PieceNode node;
        //head insertion
        for (i = 0; i < stconf.config.length; i++) {
            node = new PieceNode(stconf, i);
            node.next = this.array[stconf.getX(i)];
            this.array[stconf.getX(i)] = node;
        }
    }


    /*
    ****************************     Testers   *************************
     */
    public boolean isFreeNode(PieceNode node) {
        return (node.getPiece() == 0);
    }

    public boolean isFreeCoord(Coord coord) {
        return (this.getNode(coord.getX(), coord.getY()).getPiece() == 0);
    }

    public boolean isFreeCoord(Cube<Integer> coord) {
        return (this.getNode(coord.getX(), coord.getY(), coord.getZ()).getPiece() == 0);
    }

    //check if 2 tiles have same color -> takes in account the fact beetle can cover
    //a tile.
    public boolean isSameColor(PieceNode node1, PieceNode node2) {
        PieceNode node1Visible = array[node1.getX()];
        if (node1.stuck) {
            while ((node1Visible != null) && ((node1Visible.getY() != node1.getY()) || (node1Visible.isStuck()))) {
                node1Visible = node1Visible.getNext();
            }
        } else {
            node1Visible = node1;
        }

        PieceNode node2Visible = array[node2.getX()];
        if (node2.stuck) {
            while ((node2Visible != null) && ((node2Visible.getY() != node2.getY()) || (node2Visible.isStuck()))) {
                node2Visible = node2Visible.getNext();
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
    public PieceNode getNode(int x, int y) {
        PieceNode node = array[x];
        while ((node != null) && (node.getY() != y)) {
            node = node.getNext();
        }
        return node;
    }

    //search by coordinates for cubes
    public PieceNode getNode(int x, int y, int z) {
        PieceNode node = array[x];
        while ((node != null) && ((node.getY() != y)) || (node.getZ() != z)) {
            node = node.getNext();
        }
        return node;
    }

    public PieceNode getNode(Coord coord) {
        return this.getNode(coord.getX(), coord.getY());
    }

    public PieceNode getNode(int piece) {
        return this.getNode(this.getCoord(piece));
    }

    //search by type (eg get coords of white spider 2)
    // -> careful, there's no info about height
    public Coord getCoord(int piece) {
        return new Coord((int) stconf.getX(piece), (int) stconf.getY(piece));
    }

    public Coord getCoord(PieceNode node) {
        return this.getCoord(node.piece);
    }

    /*
    ****************           Neighbors getters      **********************
     */
    ///One by one
    public PieceNode getNorthEast(PieceNode node) {
        return this.getNode((node.getX() + 1), (node.getY() - 1));
    }

    public PieceNode getNorthWest(PieceNode node) {
        return this.getNode((node.getX()), (node.getY() - 1));
    }

    public PieceNode getEast(PieceNode node) {
        return this.getNode((node.getX() + 1), (node.getY()));
    }

    public PieceNode getWest(PieceNode node) {
        return this.getNode((node.getX() - 1), (node.getY()));
    }

    public PieceNode getSouthEast(PieceNode node) {
        return this.getNode((node.getX()), (node.getY() + 1));
    }

    public PieceNode getSouthWest(PieceNode node) {
        return this.getNode((node.getX() - 1), (node.getY() + 1));
    }

    //get all neighbors of a tile in an array list
    public ArrayList<PieceNode> getNeighborsInArrayList(PieceNode node) {
        ArrayList<PieceNode> result = new ArrayList<>();
        PieceNode current_neighbor;
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

    public PieceNode getHighestNode(Coord coords) {
        PieceNode current = this.array[coords.getX()];
        if (current == null) {
            return null;
        }
        PieceNode highestNode = null;
        while (current != null) {
            if (current.getY() == coords.getY()) {
                if (highestNode == null) {
                    highestNode = current;
                } else if (current.getZ() > highestNode.getZ()) {
                    highestNode = current;
                }
            }
            current = current.getNext();
        }
        return highestNode;
    }

    /**
     * ************ Deplacements ****************************
     */
    public ArrayList<StoringConfig> getPossibleDestinations(PieceNode node) {
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
    public ArrayList<Coord> getNewPossiblePositions() {
        int start = player * nbPiecesPerColor;
        int finish = start + nbPiecesPerColor;
        ArrayList<Coord> result = new ArrayList<>();
        PieceNode currentNode;
        PieceNode currentNeighbor;
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

    /*public ArrayList<Cube> getPossibleSlidingDestinations(Cube coord) {
        Cube neighbors[] = coord.getNeighborsInArray();
        ArrayList<Cube> result = new ArrayList<>();
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
    }*/
    public ArrayList<StoringConfig> getPossibleSpiderDestinations(PieceNode node) {
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
                if ((!CoordsAfterSecondMove.contains(newCoord)) && (newCoord.equals(this.getCoord(node)))) {
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

    public ArrayList<StoringConfig> getPossibleQueenDestinations(PieceNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }

        //creating array from arraylist
        PieceNode neighbors[] = new PieceNode[this.getNeighborsInArrayList(node).size()];
        neighbors = this.getNeighborsInArrayList(node).toArray(neighbors);
        ArrayList<StoringConfig> possibleDest = new ArrayList<>();
        int i;
        for (i = 0; i < neighbors.length; i++) {
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

    public ArrayList<StoringConfig> getPossibleAntDestinations(PieceNode node) {
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
    public ArrayList<StoringConfig> getPossibleBeetleDestinations(PieceNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }

        Coord nodeCoords = new Coord(node.getX(), node.getY());
        Coord neighbors[] = new Coord[nodeCoords.getNeighbors().size()];
        neighbors = nodeCoords.getNeighbors().toArray(neighbors);

        ArrayList<StoringConfig> possibleDest = new ArrayList<>();
        int i, maxHeight;

        for (i = 0; i < neighbors.length; i++) {
            maxHeight = max(node.getZ(), this.getHighestNode(neighbors[i]).getZ());
            if (((this.getHighestNode(neighbors[i + 1])).getZ() < maxHeight)
                    || ((this.getHighestNode(neighbors[i - 1])).getZ() < maxHeight)) {
                StoringConfig newConf = new StoringConfig(this.stconf.config.length);
                System.arraycopy(this.stconf.config, 0, newConf.config, 0, this.stconf.config.length);

                //freeing the former stuck piece
                if (node.getZ() > 0) {
                    this.getNode(node.getX(), node.getY(), node.getZ() - 1).setStuck(false);
                }

                //paralizing the piece underneath the beetle and actualizing Z coordinate of the beetle
                if (!(this.isFreeNode(this.getNode(neighbors[i])))) {
                    newConf.setIsStuck(this.getHighestNode(neighbors[i]).getPiece(), true);
                    newConf.setZ((int) node.getPiece(), (byte) (node.getZ() - (node.getZ() - 1 - (this.getHighestNode(neighbors[i]).getZ()))));
                } else {
                    newConf.setZ((int) node.getPiece(), (byte) 1);
                }

                //actualizing X and Y coordinates of the beetle
                newConf.setX((int) node.getPiece(), (byte) neighbors[i].getX());
                newConf.setY((int) node.getPiece(), (byte) neighbors[i].getY());

                possibleDest.add(newConf);
            }
        }

        return possibleDest;
    }

    public ArrayList<StoringConfig> getPossibleGrassHopperDestinations(PieceNode node) {
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
        //SouthEast
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
        //West
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
        //NorthWest
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
        //SouthWest
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
    private boolean RespectsOneHive(PieceNode node) {
        // 1st check -> if there's only one neighbor -> no need to go further
        ArrayList<PieceNode> neighbors = this.getNeighborsInArrayList(node);
        if (neighbors.size() == 1) {
            return true;
        }
        //now we chose any neighbor of node
        PieceNode neighbor = neighbors.get(0);
        PieceNode current_node;

        //marking both the moving node & the first neighbor we put in the set
        node.isVisited = true;
        neighbor.isVisited = true;
        ArrayList<PieceNode> nodeSet = new ArrayList<>();
        nodeSet.add(neighbor);
        while (!nodeSet.isEmpty()) {
            current_node = nodeSet.get(0);
            neighbors = this.getNeighborsInArrayList(current_node);
            for (PieceNode current_node_neighbor : neighbors) {
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
