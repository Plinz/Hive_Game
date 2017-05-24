/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.java.engine.Core;
import main.java.engine.Notation;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;
import main.java.utils.Tuple;

public abstract class AI {

    Core core;
    Heuristics heuristics;
    int AIPlayer;
    int[] choiceDuringOpening;
    ArrayList<CoordGene<Integer>> foreseenMoves;
    Tile foreseenTileToMove;

    public AI(Core core) {
        this.core = core;
        this.foreseenMoves = new ArrayList<>();
        this.foreseenTileToMove = null;
    }

    public boolean isDuringOpening() {
        return (core.getTurn() <= 7);
    }

    public String getNextMove() {
        System.err.println("Erreur : AI abstraite");
        return null;
    }

    public Piece chooseAPiece(double[] proportions) {
        Piece piece = null;
        int counterAgainstInfiniteLoop = 0;
        do {
            Random random = new Random();
            float rand = random.nextFloat();
            int i = -1;
            while (rand > 0) {
                i++;
                rand -= proportions[i];
            }
            System.out.println("Piece choisie : type= " + i + "," + core.getTurn());
            for (Piece pieceFromInventory : core.getPlayers()[AIPlayer].getFirstPieceOfEachType()) {
                if (Consts.getType(pieceFromInventory.getId()) == i) {
                    piece = pieceFromInventory;
                    break;
                }
            }
            if (isDuringOpening()) {
                this.choiceDuringOpening[core.getTurn() / 2] = i;
            }
            counterAgainstInfiniteLoop++;
        } while (piece == null && counterAgainstInfiniteLoop < 100);
        if (counterAgainstInfiniteLoop == 100) {
            System.out.println("Antoine, ça bug");
        }

        return piece;
    }

    public String addPieceWherever(Piece piece) {
        List<CoordGene<Integer>> possibleAddCoords = core.getPossibleAdd(core.getCurrentPlayer());
        Random random = new Random();
        int rand = random.nextInt(possibleAddCoords.size());
        String move = Notation.getMoveNotation(core.getBoard(), piece, possibleAddCoords.get(rand));
        String unMove = Notation.getInverseMoveNotation(core.getBoard(), piece);
        return move + ";" + unMove;
    }

    public Tile chooseWhateverFreeTileOnBoard(int currentPlayer) {
        List<Tile> tilesOnBoard = core.getBoard().getFreePiecesOnBoard(core.getCurrentPlayer());
        Random random = new Random();
        int rand = random.nextInt(tilesOnBoard.size());
        return tilesOnBoard.get(rand);
    }

    public String movePieceNearOpponent(Tile tile) {
        List<CoordGene<Integer>> possibleDestinations = tile.getPiece().getPossibleMovement(tile, core.getBoard());
        List<CoordGene<Integer>> possibleDestinationsNearOpponent = new ArrayList<>();
        for (CoordGene<Integer> destination : possibleDestinations) {
            for (Tile destinationNeighbor : core.getBoard().getPieceNeighbors(destination)) {
                if (destinationNeighbor.getPiece().getTeam() == core.getCurrentPlayer()) {
                    possibleDestinationsNearOpponent.add(destination);
                    break;
                }
            }
        }
        if (!possibleDestinationsNearOpponent.isEmpty()) {
            Random random = new Random();
            int rand = random.nextInt(possibleDestinationsNearOpponent.size());
            String move = Notation.getMoveNotation(core.getBoard(), tile.getPiece(), possibleDestinationsNearOpponent.get(rand));
            String unMove = Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece());
            return move + ";" + unMove;
        } else {
            return movePieceWherever(chooseWhateverFreeTileOnBoard(core.getCurrentPlayer()));
        }
    }

    public String movePieceWherever(Tile tileToMove) {
        List<CoordGene<Integer>> possibleDestinations = tileToMove.getPiece().getPossibleMovement(tileToMove, core.getBoard());
        if (possibleDestinations.isEmpty()) {
            tileToMove = null;
            //check if there is a possible move
            for (Tile tile : core.getBoard().getFreePiecesOnBoard(AIPlayer)) {
                if (!tile.getPiece().getPossibleMovement(tile, core.getBoard()).isEmpty()) {
                    tileToMove = tile;
                }
            }
            if (tileToMove == null) {
                return addPieceWherever(chooseAPiece(Consts.CHOOSE_WHATEVER));
            }
        }
        possibleDestinations = tileToMove.getPiece().getPossibleMovement(tileToMove, core.getBoard());
        Random random = new Random();
        int rand = random.nextInt(possibleDestinations.size());
        String move = Notation.getMoveNotation(core.getBoard(), tileToMove.getPiece(), possibleDestinations.get(rand));
        String unMove = Notation.getInverseMoveNotation(core.getBoard(), tileToMove.getPiece());
        return move + ";" + unMove;
    }

    public boolean whiteCanMoveAnt() {
        int nbAntInInventory = 0;
        for (Piece piece : core.getPlayers()[0].getInventory()) {
            if (Consts.getType(piece.getId()) == Consts.ANT_TYPE) {
                nbAntInInventory++;
            }
        }
        return (nbAntInInventory < 3);
    }

    public String moveForGates() {
        //if no list of moves ready to be played
        if ((this.foreseenMoves.isEmpty()) || (!(this.core.getPossibleMovement(this.foreseenTileToMove.getCoord()).contains(this.foreseenMoves.get(0))))) {
            //check if there is an "inactive" grassHopper that could help to circle the queen
            for (Column col : this.core.getBoard().getBoard()) {
                for (Box b : col) {
                    for (Tile t : b) {
                        if (Consts.getType(t.getPiece().getId()) == Consts.GRASSHOPPER_TYPE) {
                            if (!(this.core.getBoard().getNeighbors(heuristics.getTile(Consts.QUEEN, 1 - AIPlayer)).contains(t))
                                    && (heuristics.grassHopperToOpponentsQueensFreeNeighbor(AIPlayer, t) != null)) {
                                this.foreseenMoves = heuristics.grassHopperToOpponentsQueensFreeNeighbor(AIPlayer, t);
                                this.foreseenTileToMove = t;
                                return Notation.getMoveNotation(this.core.getBoard(), t.getPiece(), this.foreseenMoves.remove(0))
                                        + ";" + Notation.getInverseMoveNotation(this.core.getBoard(), t.getPiece());
                            }
                        }
                    }
                }
            }
            //if no inactive grassHopper can circle the opponent's queen, check inventory to find some others
            Piece grassHopperFound = null;
            for (Piece p : this.core.getPlayers()[AIPlayer].getInventory()) {
                if (Consts.getType(p.getId()) == Consts.GRASSHOPPER_TYPE) {
                    grassHopperFound = p;
                    break;
                }
            }

            //if IA has grasshoppers left to be played
            if (grassHopperFound != null) {
                //search for the tile where a new piece can be placed and from which a queen's neighbor is the most rapidly reached
                for (CoordGene<Integer> c : this.core.getPossibleAdd(AIPlayer)) {
                    ArrayList tileToQueen = heuristics.grassHopperToOpponentsQueensFreeNeighbor(AIPlayer, this.core.getBoard().getTile(c));
                    if (tileToQueen != null) {
                        if ((this.foreseenMoves.isEmpty()) || (tileToQueen.size() < this.foreseenMoves.size())) {
                            this.foreseenMoves = tileToQueen;
                            this.foreseenTileToMove = new Tile(grassHopperFound, false, c.getX(), c.getY(), 0);
                            return Notation.getMoveNotation(this.core.getBoard(), this.foreseenTileToMove.getPiece(), this.foreseenMoves.remove(0))
                                    + ";" + Notation.getInverseMoveNotation(this.core.getBoard(), this.foreseenTileToMove.getPiece());
                        }
                    }
                }
                // no tile where a piece can be added allows to reached to queen
                return null;
            } else {
                return null;
            }
        } else //play the next foreseen move
        {
            return Notation.getMoveNotation(this.core.getBoard(), this.foreseenTileToMove.getPiece(), this.foreseenMoves.remove(0))
                    + ";" + Notation.getInverseMoveNotation(this.core.getBoard(), this.foreseenTileToMove.getPiece());
        }

    }

    public boolean isNeighborOfOpponentQueen(Tile tile) {
        List<CoordGene<Integer>> neighbors = tile.getCoord().getNeighbors();
        for (CoordGene<Integer> neighbor : neighbors) {
            Tile neighborTile = core.getBoard().getTile(neighbor, 0);
            if (neighborTile != null && neighborTile.getPiece() != null
                    && neighborTile.getPiece().getTeam() != AIPlayer
                    && neighborTile.getPiece().getId() == Consts.QUEEN) {
                return true;
            }
        }
        return false;
    }

    public boolean isTimeToFinishOpponent() {
        return (heuristics.getNbPiecesAroundQueen(1 - AIPlayer) == 5);
    }

    public String tryGetFinishMove() {
        List<Piece> availablePieces = getAvailablepiecesForAttack();
        int nbMovesMin = 500;
        Piece chosenOne = null;
        CoordGene<Integer> chosenFirstMove = null;
        CoordGene<Integer> destination = getFreeSpaceAroundEnemyQueen();
        for (Piece piece : availablePieces){
            Tuple <Integer, CoordGene<Integer>> temp = getNbMovesTillDest(piece, destination);
            int nbMoves = temp.getX();
            if (nbMoves > 0 && nbMoves < nbMovesMin){
                nbMovesMin = nbMoves;
                chosenOne = piece;
                chosenFirstMove = temp.getY();
            }
        }
        
        if (chosenOne != null){
            String move = Notation.getMoveNotation(core.getBoard(), chosenOne, chosenFirstMove);
            String unMove = Notation.getInverseMoveNotation(core.getBoard(), chosenOne);
            return move+";"+unMove;
        }
        return null;    
    }

    public List<Piece> getAvailablepiecesForAttack() {
        ArrayList<Piece> result = new ArrayList<>();
        core.getCurrentPlayerObj().getInventory().stream().forEach((piece) -> {
            result.add(piece);
        });

        core.getBoard().getBoard().stream().forEach((column) -> {
            column.stream().forEach((box) -> {
                box.stream().filter((tile) -> (tile.getPiece().getTeam() == core.getCurrentPlayer()
                        && !isNeighborOfOpponentQueen(tile)
                        && !tile.getPiece().getPossibleMovement(tile, core.getBoard()).isEmpty())).forEach((tile) -> {
                    result.add(tile.getPiece());
                });
            });
        });
        return result;
    }

    public CoordGene<Integer> getFreeSpaceAroundEnemyQueen() {
        CoordGene<Integer> queenCoord = null;

        for (Column column : core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile != null
                            && tile.getPiece() != null
                            && tile.getPiece().getId() == Consts.QUEEN
                            && tile.getPiece().getTeam() == 1 - AIPlayer) {
                        queenCoord = tile.getCoord();
                    }
                }
            }
        }
        if (queenCoord == null)
            return null;
        
        for (Tile queenNeighbor : core.getBoard().getNeighbors(queenCoord)){
            if (queenNeighbor.getPiece() == null){
                return queenNeighbor.getCoord();
            }
        }
        return null;
    }

    public Tuple<Integer, CoordGene<Integer>> getNbMovesTillDest(Piece piece, CoordGene<Integer> destination) {
        if (isOnBoard(piece)){
            
        } else {
            
        }
        
        
        switch (Consts.getType(piece.getId())){
            case Consts.ANT_TYPE:
                if (piece.getPossibleMovement(foreseenTileToMove, core.getBoard()).contains(destination)){
                    
                }
                break;
            case Consts.BEETLE_TYPE:
                break;
            case Consts.SPIDER_TYPE:
                break;
            case Consts.GRASSHOPPER_TYPE:
                break;
        }
        return null;
    }
    
    public boolean isOnBoard(Piece piece){
        for (Piece pieceInInventory : core.getPlayers()[piece.getTeam()].getInventory()){
            if (pieceInInventory == piece)
                return false;
        }
        return true;
    }
}
