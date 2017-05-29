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
import main.java.utils.Quartet;
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

    public String getNextMove() {
        System.err.println("Erreur : AI abstraite");
        return null;
    }

//////////////              EARLY GAME METHODS
    public boolean isEarlyGame() {
        return (core.getTurn() <= 7);
    }

    public Piece chooseAPiece(double[] proportions) {
        Piece piece = null;
        do {
            Random random = new Random();
            float rand = random.nextFloat();
            int i = -1;
            while (rand > 0) {
                i++;
                rand -= proportions[i];
            }
            for (Piece pieceFromInventory : core.getPlayers()[AIPlayer].getFirstPieceOfEachType()) {
                if (Consts.getType(pieceFromInventory.getId()) == i) {
                    piece = pieceFromInventory;
                    break;
                }
            }
            if (isEarlyGame()) {
                this.choiceDuringOpening[core.getTurn() / 2] = i;
            }
        } while (piece == null);

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
                return addPieceWherever(chooseAPiece(HeuristicConst.CHOOSE_WHATEVER));
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
//////////////             LATE GAME METHODS

    public boolean isNeighborOfOpponentQueen(Tile tile) {
        if (tile.getZ() != 0) {
            return false;
        }

        List<CoordGene<Integer>> neighbors = tile.getCoord().getNeighbors();
        for (CoordGene<Integer> neighbor : neighbors) {
            if (core.getBoard().getTile(neighbor) != null) {
                Tile neighborTile = core.getBoard().getTile(neighbor, 0);
                if (neighborTile != null && neighborTile.getPiece() != null) {
                }
                if (neighborTile != null && neighborTile.getPiece() != null
                        && neighborTile.getPiece().getTeam() != AIPlayer
                        && neighborTile.getPiece().getId() == Consts.QUEEN) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTimeToFinishOpponent() {
        return (heuristics.getNbPiecesAroundQueen(1 - AIPlayer) == 5);
    }

    public String tryGetFinishMove() {
        List<Piece> availablePieces = getAvailablePiecesForAttack();
        int nbMovesMin = 500;
        Piece chosenOne = null;
        String chosenFirstMove = null;
        for (Piece piece : availablePieces) {
            Tuple<Integer, String> temp = getMoveAndDistanceToOpponentQueen(piece);
            int nbMoves = temp.getX();
            if (nbMoves > 0 && nbMoves < nbMovesMin) {
                nbMovesMin = nbMoves;
                chosenOne = piece;
                chosenFirstMove = temp.getY();
            }
        }

        //here we try adding one piece
        List<CoordGene<Integer>> possibleAdds = core.getPossibleAdd(AIPlayer);
        Tuple<Integer, String> temp;
        for (Piece piece : core.getPlayers()[AIPlayer].getFirstPieceOfEachType()) {
            for (CoordGene<Integer> addCoord : possibleAdds) {
                String move = Notation.getMoveNotation(core.getBoard(), piece, addCoord);
                String unmove = Notation.getInverseMoveNotation(core.getBoard(), piece);

                core.playEmulate(move, unmove);
                temp = getMoveAndDistanceToOpponentQueen(piece);
                int nbMoves = temp.getX();
                if (nbMoves > 0 && nbMoves < nbMovesMin) {
                    nbMovesMin = nbMoves;
                    chosenOne = piece;
                    chosenFirstMove = move + ";" + unmove;
                }

                core.previousState();
            }
        }

        if (chosenFirstMove != null) {
            return chosenFirstMove;
        }
        return null;
    }

    public List<Piece> getAvailablePiecesForAttack() {
        ArrayList<Piece> result = new ArrayList<>();

        core.getBoard().getBoard().stream().forEach((column) -> {
            column.stream().forEach((box) -> {
                box.stream().filter((tile) -> (tile.getPiece() != null
                        && tile.getPiece().getTeam() == core.getCurrentPlayer()
                        && !isNeighborOfOpponentQueen(tile)
                        && !tile.getPiece().getPossibleMovement(tile, core.getBoard()).isEmpty())).forEach((tile) -> {
                    result.add(tile.getPiece());
                });
            });
        });
        return result;
    }

    public Tuple<Integer, CoordGene<Integer>> getNbMovesTillDest(Piece piece, CoordGene<Integer> destination) {
        int bestDistanceSoFar = 500;
        Tuple<Integer, CoordGene<Integer>> temp;
        if (isOnBoard(piece)) {
            Tile tile = null;
            for (Column column : core.getBoard().getBoard()) {
                for (Box box : column) {
                    for (Tile currentTile : box) {
                        if (currentTile.getPiece() == piece) {
                            tile = currentTile;
                        }
                    }
                }
            }
            if (tile == null) {
                return null;//should never happen
            }

            switch (Consts.getType(piece.getId())) {
                case Consts.ANT_TYPE:
                    if (piece.getPossibleMovement(tile, core.getBoard()).contains(destination)) {
                        return new Tuple<>(1, destination); //should not be useful -> victory always in one turn
                    }
                    break;
                case Consts.BEETLE_TYPE:
                    //temp = getMoveAndDistance(tile, destination);
                    break;
                case Consts.SPIDER_TYPE:

                    break;
                case Consts.GRASSHOPPER_TYPE:
                    break;
            }
        } else {
            List<CoordGene<Integer>> possibleAddCoords = core.getPossibleAdd(AIPlayer);
            switch (Consts.getType(piece.getId())) {
                case Consts.ANT_TYPE:

                    break;
                case Consts.BEETLE_TYPE:
                    break;
                case Consts.SPIDER_TYPE:
                    break;
                case Consts.GRASSHOPPER_TYPE:
                    break;
            }
        }

        return null;
    }

    public boolean isOnBoard(Piece piece) {
        for (Piece pieceInInventory : core.getPlayers()[piece.getTeam()].getInventory()) {
            if (pieceInInventory == piece) {
                return false;
            }
        }
        return true;
    }

    public Tuple<Integer, String> getMoveAndDistanceToOpponentQueen(Piece piece) {
        //Using Quartet (distance, move, unmove, firstMove)
        //Use only for pieces already on board

        //if piece is already a neighbor
        if (!isOnBoard(piece)) {
            return new Tuple<>(-1, null);
        }

        if (isNeighborOfOpponentQueen(getTile(piece))) {
            return new Tuple<Integer, String>(0, null);
        }
        Tile tile = getTile(piece);
        ArrayList<Quartet<Integer, String, String, String>> movesAndDistance = new ArrayList<>();

        String root = Notation.getMoveNotation(core.getBoard(), piece, tile.getCoord());
        String unroot = Notation.getInverseMoveNotation(core.getBoard(), piece);

        Quartet<Integer, String, String, String> origin = new Quartet<>(0, root, unroot, root);
        movesAndDistance.add(origin);

        ArrayList<String> MovesDone = new ArrayList<>();
        MovesDone.add(root);

        int actualSize;
        int actualDistance = 0;
        do {
            ArrayList<Quartet<Integer, String, String, String>> newlyAdded = new ArrayList<>();
            actualSize = movesAndDistance.size();
            for (Quartet<Integer, String, String, String> move : movesAndDistance) {
                if (move.getA().equals(actualDistance)) {
                    core.playEmulate(move.getB(), move.getC());
                    //terminaison case -> the piece arrived near opponent queen
                    Tile currentTile = getTile(piece);
                    if (isNeighborOfOpponentQueen(currentTile)) {
                        core.previousState();
                        return new Tuple<>(move.getA(), move.getD() + ";" + move.getC());
                    }

                    List<CoordGene<Integer>> temp = piece.getPossibleMovement(currentTile, core.getBoard());
                    for (CoordGene<Integer> newCoord : temp) {

                        String currentMove = Notation.getMoveNotation(core.getBoard(), piece, newCoord);
                        if (!MovesDone.contains(currentMove)) {
                            MovesDone.add(currentMove);
                            Quartet<Integer, String, String, String> toAdd;
                            if (actualDistance == 0) {
                                toAdd = new Quartet<>(1, currentMove, unroot, currentMove);
                            } else {
                                toAdd = new Quartet<>(actualDistance + 1, currentMove, unroot, move.getD());
                            }
                            newlyAdded.add(toAdd);
                        }
                    }

                    core.previousState();
                }
            }
            if (!newlyAdded.isEmpty()) {
                for (Quartet<Integer, String, String, String> newElement : newlyAdded) {
                    movesAndDistance.add(newElement);
                }
            }
            actualDistance++;
        } while (movesAndDistance.size() != actualSize);
        return new Tuple<>(-1, null);
    }

    
    public boolean isASuicideMove(Minimax chosenOne) {
        boolean result;
        if (AIPlayer == 0){
            core.playEmulate(chosenOne.moveFromParent, chosenOne.moveToParent);
            result = core.getStatus() == Consts.WIN_TEAM1;
            core.previousState();
        } else {
            core.playEmulate(chosenOne.moveFromParent, chosenOne.moveToParent);
            result = core.getStatus() == Consts.WIN_TEAM2;
            core.previousState();
        }
        return result;
    }

    public Minimax chooseDecentMove(List<Minimax> possibleMovements) {
        Minimax defaultMove = possibleMovements.get(0);
        Minimax bestOption;
        int sign;
        if (AIPlayer == 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        
        
        do {
            bestOption = possibleMovements.get(0);
            for (Minimax option : possibleMovements){
                if (option.heuristicValue * sign > bestOption.heuristicValue * sign){
                    bestOption = option;
                }
            }
            if (isASuicideMove(bestOption)){
                possibleMovements.remove(bestOption);
                bestOption = null;
            }
        } while (bestOption == null && !possibleMovements.isEmpty());
        if (bestOption != null){
            return bestOption;
        } else {
            return defaultMove;
        }
        
    }
    
    public Tile getTile(Piece piece) {
        for (Piece pieceInventory : core.getPlayers()[piece.getTeam()].getInventory()) {
            if (piece == pieceInventory) {
                return null;
            }
        }
        for (Column column : core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile.getPiece() == piece) {
                        return tile;
                    }
                }
            }
        }
        return null;
    }
}
