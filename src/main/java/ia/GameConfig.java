/*

 */
package main.java.ia;

import java.util.ArrayList;
import main.java.utils.Consts;
import main.java.utils.Coord;
import main.java.utils.Cube;

public class GameConfig {

    private final PieceNode[] pieces;
    private final PieceNode[][] board;
    private final StoringConfig storingConfig;
    Heuristics heuristics;
    ArrayList<StoringConfig> nextPossibleConfigs;
    int nbPiecesPerColor;
    int currentPlayer;
    int turn;
    int heuristicValue;

    /*
     *              CONSTRUCTOR
     */
    public GameConfig(StoringConfig storingConfig, int turn) {
        int totalPiecesNb = storingConfig.config.length;
        this.turn = turn;
        this.currentPlayer = storingConfig.currentPlayer;
        this.nbPiecesPerColor = totalPiecesNb / 2;
        this.storingConfig = storingConfig;
        this.pieces = new PieceNode[totalPiecesNb];
        this.board = new PieceNode[totalPiecesNb][totalPiecesNb];
        PieceNode pieceNode;
        int pieceID;
        //fillling up the pieces and grid arrays
        for (pieceID = 0; pieceID < totalPiecesNb; pieceID++) {
            pieceNode = new PieceNode(storingConfig, pieceID);
            pieces[pieceID] = pieceNode;
            if (pieceNode.isOnBoard() && pieceNode.getZ() == 0) {
                board[pieceNode.getX()][pieceNode.getY()] = pieceNode;
            }
        }
        //for pieces not on the floor -> we put them in the grid now
        //the piece below can link to the next directly
        for (pieceID = 0; pieceID < totalPiecesNb; pieceID++) {
            if (pieces[pieceID].isOnBoard() && pieces[pieceID].getZ() > 0) {
                for (int pieceBelow = 0; pieceBelow < totalPiecesNb; pieceBelow++) {
                    if ((pieces[pieceBelow].getX() == pieces[pieceID].getX())
                            && (pieces[pieceBelow].getY() == pieces[pieceID].getY())
                            && (pieces[pieceBelow].getZ() == pieces[pieceID].getZ() - 1)) {
                        pieces[pieceBelow].pieceAbove = pieces[pieceID];
                    }
                }
            }
        }
    }

    /*
     *              TESTS
     */
    public boolean isFreeCoord(Coord coord) {
        if (!coord.isValidCoord()) {
            return true;
        }
        return this.board[coord.getX()][coord.getY()] == null;
    }

    public boolean isFreeCoord(Cube<Integer> cube) {
        if ((cube.getX().intValue() < 0) || (cube.getY().intValue() < 0)) {
            return true;
        }
        PieceNode node = this.board[cube.getX()][cube.getY()];
        int z = cube.getZ();
        while ((z > 0) && (node != null)) {
            node = node.pieceAbove;
            --z;
        }
        return node == null;
    }

    public boolean isSameColor(PieceNode node1, PieceNode node2) {
        //one of both is null -> ret true
        if ((node1 == null) || (node2 == null)) {
            return true;
        }

        PieceNode visibleNode1 = node1;
        PieceNode visibleNode2 = node2;

        while (visibleNode1.pieceAbove != null) {
            visibleNode1 = visibleNode1.pieceAbove;
        }
        while (visibleNode2.pieceAbove != null) {
            visibleNode2 = visibleNode2.pieceAbove;
        }
        boolean isNode1White = visibleNode1.piece < nbPiecesPerColor;
        boolean isNode2White = visibleNode2.piece < nbPiecesPerColor;
        return ((isNode1White && isNode2White) || (!isNode1White && !isNode2White));
    }

    /*
     *              GETTERS
     */
    public PieceNode[] getPieces() {
        return pieces;
    }

    public PieceNode[][] getBoard() {
        return board;
    }

    public int getNbPiecesPerColor() {
        return nbPiecesPerColor;
    }

    public PieceNode getNode(Cube<Integer> cube) {
        int z = cube.getZ();
        PieceNode node = this.board[cube.getX()][cube.getY()];
        while ((node != null) && (z > 0)) {
            --z;
            node = node.pieceAbove;
        }
        return node;
    }

    public PieceNode getNode(int pieceID) {
        return pieces[pieceID];
    }

    //returns the lowest piece present on (x,y) if there are several
    // and null if there is nothing
    public PieceNode getNode(Coord coord) {
        return board[coord.getX()][coord.getY()];
    }

    public Coord getCoord(PieceNode node) {
        return new Coord(node.getX(), node.getY());
    }

    public Coord getCoord(int pieceId) {
        return getCoord(pieces[pieceId]);
    }

    public Cube<Integer> getCube(PieceNode node) {
        return new Cube<>(node.getX(), node.getY(), node.getZ());
    }

    public PieceNode getHighestNode(Coord coord) {
        //case of invalid coord
        if (!coord.isValidCoord()) {
            return null;
        }

        PieceNode node = board[coord.getX()][coord.getY()];
        //case of empty coord
        if (node == null) {
            return null;
        }
        while (node.pieceAbove != null) {
            node = node.pieceAbove;
        }
        return node;
    }

    public int getHeight(Coord coord) {
        int result = 0;
        PieceNode currentNode = getNode(coord);
        while (currentNode != null) {
            result++;
            currentNode = currentNode.pieceAbove;
        }
        return result;
    }

    public ArrayList<PieceNode> getNeighborsInArrayList(PieceNode node) {
        ArrayList<PieceNode> result = new ArrayList<>();
        Coord[] neighborsCoords = getCoord(node).getNeighborsInArray();
        for (int i = 0; i < 6; i++) {
            PieceNode neighbor = getNode(neighborsCoords[i]);
            if (neighbor != null) {
                result.add(neighbor);
            }
        }
        return result;
    }

    /*
     *              CALCUL NEXT MOVES & HEURISTICS
     */
    public void calculateAll() {
        int start = nbPiecesPerColor * currentPlayer;
        int finish = start + nbPiecesPerColor;

        nextPossibleConfigs = new ArrayList<>();
        //different cases depending on the turn
        switch (turn) {
            case 0:
                nextPossibleConfigs = getFirstTurnMove();
                break;
            case 1:
                nextPossibleConfigs = getSecondTurnMove();
                break;
            default:
                ArrayList<Coord> possibleNewPositions = getNewPossiblePositions();
                //if queen is not yet on board during turn 7 or 8
                if ((!getNode(start).isOnBoard) && (turn >= 7)) {
                    for (Coord coord : possibleNewPositions) {
                        StoringConfig newConfig = new StoringConfig(storingConfig);
                        newConfig.setX(start, (byte) coord.getX());
                        newConfig.setY(start, (byte) coord.getY());
                        newConfig.setIsOnBoard(start, true);
                        nextPossibleConfigs.add(newConfig);
                    }
                } else { //queen is already on board or turn is different than 7 or 8
                    for (int i = start; i < finish; i++) {
                        //piece is on board -> move it
                        if (getNode(i).isOnBoard) {
                            ArrayList<StoringConfig> temp = getPossibleDestinations(getNode(i));
                            for (StoringConfig config : temp) {
                                if (!possibleNewPositions.contains(config)) {
                                    nextPossibleConfigs.add(config);
                                }
                            }
                        } else {
                            //piece not on board -> add possible positions for it
                            int j = i % nbPiecesPerColor;
                            //stupid condition -> can be refactored
                            if (((j == Consts.SPIDER2) && (!getNode(Consts.SPIDER1).isOnBoard))
                                    || ((j == Consts.GRASSHOPPER2) && (!getNode(Consts.GRASSHOPPER1).isOnBoard))
                                    || ((j == Consts.GRASSHOPPER3) && (!getNode(Consts.GRASSHOPPER2).isOnBoard))
                                    || ((j == Consts.BEETLE2) && (!getNode(Consts.BEETLE1).isOnBoard))
                                    || ((j == Consts.ANT2) && (!getNode(Consts.ANT1).isOnBoard))
                                    || ((j == Consts.ANT3) && (!getNode(Consts.ANT2).isOnBoard))) {
                                //do nothing -> the same kind of piece was just added
                            } else {
                                for (Coord coord : possibleNewPositions) {
                                    StoringConfig newStoringConfig = new StoringConfig(storingConfig);
                                    newStoringConfig.setX(i, (byte) coord.getX());
                                    newStoringConfig.setY(i, (byte) coord.getY());
                                    newStoringConfig.setIsOnBoard(i, true);
                                    nextPossibleConfigs.add(newStoringConfig);
                                }
                            }
                        }
                    }
                }
                break;
        }

        heuristicValue = heuristics.getHeuristicsValue(this);

    }

    /*
     *              DEPLACEMENTS
     */
    public ArrayList<StoringConfig> getPossibleDestinations(PieceNode node) {
        int piece_type = IdToType(node.piece % nbPiecesPerColor);
        switch (piece_type) {
            case Consts.QUEEN_TYPE:
                return getPossibleQueenDestinations(node);
            case Consts.SPIDER_TYPE:
                return getPossibleSpiderDestinations(node);
            case Consts.ANT_TYPE:
                return getPossibleAntDestinations(node);
            case Consts.GRASSHOPPER_TYPE:
                return getPossibleGrassHopperDestinations(node);
            case Consts.BEETLE_TYPE:
                return getPossibleBeetleDestinations(node);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * ******************* Slidind bugs (Ant, spider & queen) **************
     */
    ///Getting the available neighbors when sliding -> method used to calculate
    ///displacements of the ant, spider & queen.
    /// it's a Coord->Coord method so for spider & ant move, we can use result to call method on it
    /// this method does check both freedom_to_move & permanent_contact rules, but not one_hive
    public ArrayList<Coord> getPossibleSlidingDestinations(Coord coord) {
        Coord neighbors[] = coord.getNeighborsInArray();
        ArrayList<Coord> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (isFreeCoord(neighbors[i])) {
                if ((isFreeCoord(neighbors[(i + 5) % 6]) && !isFreeCoord(neighbors[(i + 1) % 6]))
                        || (!isFreeCoord(neighbors[(i + 5) % 6]) && isFreeCoord(neighbors[(i + 1) % 6]))) {
                    result.add(neighbors[i]);
                }
            }
        }
        return result;
    }

    public ArrayList<StoringConfig> getPossibleQueenDestinations(PieceNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }
        ArrayList<StoringConfig> result = new ArrayList<>();
        ArrayList<Coord> possibleCoords = getPossibleSlidingDestinations(getCoord(node));

        //save the possible dests in the PieceNode for later heuristics calculations
        node.possibleDestinations = possibleCoords;

        for (Coord possibleCoord : possibleCoords) {
            StoringConfig newStConfig = new StoringConfig(storingConfig);
            newStConfig.setX(node.getPiece(), (byte) possibleCoord.getX());
            newStConfig.setY(node.getPiece(), (byte) possibleCoord.getY());
            result.add(newStConfig);
        }
        return result;
    }

    public ArrayList<StoringConfig> getPossibleSpiderDestinations(PieceNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }
        ArrayList<Coord> resultCoords = new ArrayList<>();
        //now we 'remove' the spider tile from the board, we 'll put it back in place
        //before leaving the method
        int originalX = node.getX(), originalY = node.getY();
        node.setX(-1);
        node.setY(-1);
        board[originalX][originalY] = null;
        System.err.println("CoordOrigin : " + getCoord(node).toString());
        //get coords after one slide move
        ArrayList<Coord> CoordsAfterFirstMove = getPossibleSlidingDestinations(new Coord(originalX, originalY));
        for (Coord coordAfter1Move : CoordsAfterFirstMove) {
            System.err.println("\tCoordAfter1Move : " + coordAfter1Move.toString());
            ArrayList<Coord> CoordsAfterSecondMove = getPossibleSlidingDestinations(coordAfter1Move);
            for (Coord coordAfter2Move : CoordsAfterSecondMove) {
                System.err.println("\t\tCoordAfter2Move : " + coordAfter2Move.toString());
                //check the spider does not try to come back
                if (!coordAfter2Move.equals(new Coord(originalX, originalY))) {
                    ArrayList<Coord> CoordsAfterThirdMove = getPossibleSlidingDestinations(coordAfter2Move);
                    for (Coord coordAfter3Move : CoordsAfterThirdMove) {
                        System.err.println("\t\t\tCoordAfter3Move : " + coordAfter3Move.toString());
                        //check the spider does not try to come back && add only if not present in result
                        if ((!coordAfter3Move.equals(coordAfter1Move)) && (!resultCoords.contains(coordAfter3Move))) {
                            resultCoords.add(coordAfter3Move);
                        }
                    }
                }
            }
        }
        //now from an arraylist of coords we get arraylist of stconf
        ArrayList<StoringConfig> result = new ArrayList<>();
        for (Coord coord : resultCoords) {
            StoringConfig newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) coord.getX());
            newStoringConfig.setY(node.piece, (byte) coord.getY());
            result.add(newStoringConfig);
        }

        //put back the spider where it was
        board[originalX][originalY] = node;
        node.setX(originalX);
        node.setY(originalY);

        //save the possible dests in the PieceNode for later heuristics calculations
        node.possibleDestinations = resultCoords;

        return result;
    }

    public ArrayList<StoringConfig> getPossibleAntDestinations(PieceNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }
        ArrayList<Coord> resultCoords = new ArrayList<>();
        //now we 'remove' the ant tile from the board, we 'll put it back in place
        //before leaving the method
        int originalX = node.getX(), originalY = node.getY();
        node.setX(-1);
        node.setY(-1);
        board[originalX][originalY] = null;

        ArrayList<Coord> possibleDestinations = new ArrayList<>(), temp;
        ArrayList<Coord> newlyAdded = getPossibleSlidingDestinations(new Coord(originalX, originalY));

        boolean newCoordWasAdded = true;
        while (newCoordWasAdded) {
            ArrayList<Coord> newlyAddedtemp = new ArrayList<>();
            newCoordWasAdded = false;
            for (Coord newlyAddedCoord : newlyAdded) {
                temp = getPossibleSlidingDestinations(newlyAddedCoord);
                for (Coord newCoord : temp) {
                    if (!possibleDestinations.contains(newCoord)) {
                        possibleDestinations.add(newlyAddedCoord);
                        newCoordWasAdded = true;
                        newlyAddedtemp.add(newCoord);
                    }
                }
            }
            newlyAdded = newlyAddedtemp;
        }

        //now from an arraylist of coords we get arraylist of stconf
        ArrayList<StoringConfig> result = new ArrayList<>();
        for (Coord coord : possibleDestinations) {
            StoringConfig newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) coord.getX());
            newStoringConfig.setY(node.piece, (byte) coord.getY());
            result.add(newStoringConfig);
        }

        //put back the ant where it was
        board[originalX][originalY] = node;
        node.setX(originalX);
        node.setY(originalY);

        //save the possible dests in the PieceNode for later heuristics calculations
        node.possibleDestinations = resultCoords;
        System.err.println("PossibleAntDest :result size =" + result.size());
        return result;
    }

    /**
     * ******************* Other bugs (Beetle & grasshopper) **************
     */
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
            newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
            node.possibleDestinations.add(currentCoord);
        }

        //SouthEast
        currentCoord = this.getCoord(node).getSouthEast();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getSouthEast();
            }
            newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
            node.possibleDestinations.add(currentCoord);
        }

        //SouthWest
        currentCoord = this.getCoord(node).getSouthWest();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getSouthWest();
            }
            newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
            node.possibleDestinations.add(currentCoord);
        }

        //West
        currentCoord = this.getCoord(node).getWest();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getWest();
            }
            newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
            node.possibleDestinations.add(currentCoord);
        }

        //NorthWest
        currentCoord = this.getCoord(node).getNorthWest();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getNorthWest();
            }
            newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
            node.possibleDestinations.add(currentCoord);
        }

        //NorthEast
        currentCoord = this.getCoord(node).getNorthEast();
        if (!this.isFreeCoord(currentCoord)) {
            while (!this.isFreeCoord(currentCoord)) {
                currentCoord = currentCoord.getNorthEast();
            }
            newStoringConfig = new StoringConfig(storingConfig);
            newStoringConfig.setX(node.piece, (byte) currentCoord.getX());
            newStoringConfig.setY(node.piece, (byte) currentCoord.getY());
            result.add(newStoringConfig);
            node.possibleDestinations.add(currentCoord);
        }

        return result;
    }

    public ArrayList<StoringConfig> getPossibleBeetleDestinations(PieceNode node) {
        if ((!this.RespectsOneHive(node)) || node.isStuck()) {
            return new ArrayList<>();
        }

        ArrayList<StoringConfig> result = new ArrayList<>();

        Coord[] neighborsCoords = getCoord(node).getNeighborsInArray();
        int maxHeight; // represents max between source height & destination height
        for (int i = 0; i < 6; i++) {
            //setting maxHeight
            int destinationHeight = getHeight(neighborsCoords[i]) + 1;
            maxHeight = (node.getZ() > destinationHeight ? node.getZ() : destinationHeight);

            //check that the move respects freedom to move rule
            if (((getHeight(neighborsCoords[(i + 1) % 6]) < maxHeight)
                    || (getHeight(neighborsCoords[(i + 5) % 6]) < maxHeight))
                    && ((getNode(neighborsCoords[(i + 1) % 6]) != null)
                    || (getNode(neighborsCoords[(i + 5) % 6]) != null))) {
                StoringConfig newStoringConfig = new StoringConfig(storingConfig);

                //Unstuck the piece which was below the beetle
                for (int j = 0; j < newStoringConfig.config.length; j++) {
                    if (newStoringConfig.getX(j) == node.getX()
                            && (newStoringConfig.getY(j) == node.getY())
                            && (newStoringConfig.getZ(j) == node.getZ() - 1)) {
                        newStoringConfig.setIsStuck(j, false);
                        break;
                    }
                }
                //move the beetle to new position in newStoringConfig
                newStoringConfig.setX(node.piece, (byte) neighborsCoords[i].getX());
                newStoringConfig.setY(node.piece, (byte) neighborsCoords[i].getY());
                newStoringConfig.setZ(node.piece, (byte) destinationHeight);
                //if there's a piece underneath, set stuck to true
                PieceNode nodeToStuck = getHighestNode(neighborsCoords[i]);
                if (nodeToStuck != null) {
                    newStoringConfig.setIsStuck(nodeToStuck.piece, true);
                }
                //add the coord to the beetle PossibleDest
                node.possibleCubeDestinations.add(new Cube<>(neighborsCoords[i].getX(), neighborsCoords[i].getY(), destinationHeight));
                //and now add the storingConfig to result
                result.add(newStoringConfig);
            }
        }
        return result;
    }

    public boolean RespectsOneHive(PieceNode node) {
        // 1st check -> if there's only one neighbor -> no need to go further
        ArrayList<PieceNode> neighbors = this.getNeighborsInArrayList(node);
        if (neighbors.size() == 1) {
            return true;
        }

        //Setting all booleans 'isVisited' to false
        for (PieceNode[] boardX : board) {
            for (PieceNode boardXY : boardX) {
                if (boardXY != null) {
                    boardXY.setIsVisited(false);
                }
            }
        }

        //now we chose any neighbor of node
        PieceNode neighbor = neighbors.get(0);

        //marking both the moving node & the first neighbor we put in the set
        node.setIsVisited(true);
        this.board[node.getX()][node.getY()] = node;
        neighbor.setIsVisited(true);

        PieceNode current_node;
        ArrayList<PieceNode> nodeSet = new ArrayList<>();
        nodeSet.add(neighbor);
        while (!nodeSet.isEmpty()) {
            current_node = nodeSet.get(0);
            neighbors = this.getNeighborsInArrayList(current_node);
            for (PieceNode current_node_neighbor : neighbors) {
                if (!current_node_neighbor.isVisited) {
                    current_node_neighbor.setIsVisited(true);
                    nodeSet.add(current_node_neighbor);
                }
            }
            nodeSet.remove(current_node);
        }

        //CAN BE OPTIMIZED : TEST ONLY OTHER NEIGHBORS OF THE ORIGINAL NODE
        for (PieceNode[] boardX : board) {
            for (PieceNode boardXY : boardX) {
                if (boardXY != null && !boardXY.isVisited) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     *              NEW POSITIONS
     */
    public ArrayList<Coord> getNewPossiblePositions() {
        int start = currentPlayer * nbPiecesPerColor;
        int finish = start + nbPiecesPerColor;
        ArrayList<Coord> result = new ArrayList<>();
        Coord currentNodeCoord, currentNeighborCoord;

        //costy triple loop, but looks like no choice about it 
        //-> maximum 11*6*6 = around 400
        for (int pieceId = start; pieceId < finish; pieceId++) {
            //find pieces from currentPlayer already on board
            if (pieces[pieceId].isOnBoard) {
                currentNodeCoord = getCoord(pieces[pieceId]);
                Coord[] neighborsCoords = currentNodeCoord.getNeighborsInArray();
                for (int i = 0; i < 6; i++) {
                    if (isFreeCoord(neighborsCoords[i])) {
                        Coord[] neighborsOfNeighbor = neighborsCoords[i].getNeighborsInArray();
                        boolean canBeAdded = true;
                        for (int j = 0; j < 6; j++) {
                            currentNeighborCoord = neighborsOfNeighbor[j];
                            //System.out.println("LOLLOLL " + currentNeighborCoord);
                            if ((currentNeighborCoord.isValidCoord()) && (!isSameColor(getNode(currentNodeCoord), getNode(currentNeighborCoord)))) {
                                canBeAdded = false;
                            }
                        }
                        if (canBeAdded) {
                            result.add(neighborsCoords[i]);
                        }
                    }
                }
            }
        }
        return result;
    }

    /*
     *              FIRST & SECOND TURN
     */
    ArrayList<StoringConfig> getFirstTurnMove() {
        //put whatever type of piece on board in position (0,0)
        int lastType = -1;
        ArrayList<StoringConfig> result = new ArrayList<>();
        for (int i = 0; i < nbPiecesPerColor; i++) {
            //check to try only one typeof piece
            if (IdToType(i) != lastType) {
                lastType = IdToType(i);
                StoringConfig newStoringConfig = new StoringConfig(storingConfig);
                newStoringConfig.setX(i, (byte) 0);
                newStoringConfig.setY(i, (byte) 0);
                newStoringConfig.setZ(i, (byte) 0);
                newStoringConfig.setIsOnBoard(i, true);
                result.add(newStoringConfig);
            }
        }
        return result;
    }

    //same method as getFirstTurnMove,, but we put the new tile east of opponents tile
    ArrayList<StoringConfig> getSecondTurnMove() {
        int start = currentPlayer * nbPiecesPerColor;
        int finish = start + nbPiecesPerColor;
        int lastType = -1;
        ArrayList<StoringConfig> result = new ArrayList<>();
        for (int i = start; i < finish; i++) {
            //check to try only one typeof piece
            if (IdToType(i) != lastType) {
                lastType = IdToType(i);
                StoringConfig newStoringConfig = new StoringConfig(storingConfig);
                newStoringConfig.setX(i, (byte) 0);
                newStoringConfig.setY(i, (byte) 1);
                newStoringConfig.setZ(i, (byte) 0);
                newStoringConfig.setIsOnBoard(i, true);
                result.add(newStoringConfig);
            }
        }

        return result;
    }

    /*
     *              UTILS
     */
    //PIECE_ID into PIECE_TYPE
    public int IdToType(int id) {
        if (id >= nbPiecesPerColor) {
            id -= nbPiecesPerColor;
        }

        if (id <= Consts.QUEEN) {
            return Consts.QUEEN_TYPE;
        } else if (id <= Consts.SPIDER2) {
            return Consts.SPIDER_TYPE;
        } else if (id <= Consts.GRASSHOPPER3) {
            return Consts.GRASSHOPPER_TYPE;
        } else if (id <= Consts.BEETLE2) {
            return Consts.BEETLE_TYPE;
        } else if (id <= Consts.ANT3) {
            return Consts.ANT_TYPE;
        } else if (id <= Consts.MOSQUITO) {
            return Consts.MOSQUITO_TYPE;
        } else if (id <= Consts.LADYBUG) {
            return Consts.LADYBUG_TYPE;
        } else if (id <= Consts.PILLBUG) {
            return Consts.PILLBUG_TYPE;
        } else {
            System.err.println("Erreur : Impossible de reconnaitre le type de pièce");
            return 0;
        }
    }

}
