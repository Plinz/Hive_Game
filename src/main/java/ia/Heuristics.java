/*
Heuristic Data : heuristicData[player][insect][typeHeuristique]
    avec player 0 -> IA
         player 1 -> opponent
                insect -> 0 queen, 1 spider, 2 grasshopper, 3 beetle, 4 ant, 5 general
                        et type ->  0 valid move (deplacement et placement)
                                    1 valid placement
                                    2 valid movement (deplacement)
                                    3 voisins (encerclement)
                                    4 en main
                                    5 en jeu
                                    6 pinned

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.engine.Core;
import main.java.model.Board;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Heuristics {

    int maxdepth;
    Core core;
    int AIPlayer;
    int nbConfigsStudied;
    int difficulty;
    int nbPieceInHandAI, nbPieceInHandOpponent, nbPieceOnBoardAI, nbPieceOnBoardOpponent;
    int nbMovesAI, nbMovesOpponent, nbPlacementAI, nbPlacementOpponent;
    int nbPinnedAI, nbPinnedOpponent;
    boolean isVictory;
    double[][][] heuristicData;
    HeuristicPiece[][] pieces;

    /*
     *                  CONSTUCTOR
     */
    public Heuristics(Core core) {
        this.core = core;
        heuristicData = HeuristicConst.getHeuristicDataFromConsts();
        pieces = new HeuristicPiece[2][Consts.NB_PIECES_PER_PLAYER];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < Consts.NB_PIECES_PER_PLAYER; j++) {
                pieces[i][j] = new HeuristicPiece(j);
            }
        }
        this.AIPlayer = core.getCurrentPlayer();
    }

    /*
     *                  METHODE OVERIDE IN CHILD HEURISTICS
     */
    public double getHeuristicsValue() {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    public int getValuePieceInHand(int pieceId, int player) {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    public int getValuePieceOnBoard(int pieceId, int player) {
        System.err.println("Erreur : getHeuristics value ne doit as être appelée depuis la classe mère");
        return 0;
    }

    /*
     *                  GETTERS
     */
    public void getGeneralValues() {
        resetGeneralValues();
        for (int pieceId = 0; pieceId < Consts.NB_PIECES_PER_PLAYER; pieceId++) {
            nbMovesAI += pieces[0][pieceId].nbMoves * pieces[0][pieceId].isInGame;
            nbPlacementAI += pieces[0][pieceId].nbMoves * (1 - pieces[0][pieceId].isInGame);
            nbPieceInHandAI += 1 - pieces[0][pieceId].isInGame;
            nbPieceOnBoardAI += pieces[0][pieceId].isInGame;
            nbPinnedAI += pieces[0][pieceId].isPinned;

            nbMovesOpponent += pieces[1][pieceId].nbMoves * pieces[1][pieceId].isInGame;
            nbPlacementOpponent += pieces[1][pieceId].nbMoves * (1 - pieces[1][pieceId].isInGame);
            nbPieceInHandOpponent += 1 - pieces[1][pieceId].isInGame;
            nbPieceOnBoardOpponent += pieces[1][pieceId].isInGame;
            nbPinnedOpponent += pieces[1][pieceId].isPinned;
        }
    }

    public int getNbPiecesAroundQueen(int player) {
        if (core.getPlayers()[player].getInventory().stream().noneMatch(piece -> piece.getId() == Consts.QUEEN)) {
            return core.getBoard().getPieceNeighbors(getTile(Consts.QUEEN, player)).size();
        } else {
            return 0;
        }
    }

    public boolean isVictory() {
        return isVictory;
    }

    public void resetVictory() {
        isVictory = false;
    }

    public Tile getTile(int pieceId, int player) {
        Board board = core.getBoard();
        for (Column column : board.getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile.getPiece() != null && tile.getPiece().getId() == pieceId && tile.getPiece().getTeam() == player) {
                        return tile;
                    }
                }
            }
        }
        return null;
    }

    public void resetValues() {
        resetGeneralValues();
        for (int player = 0; player < 2; player++) {
            for (int pieceId = 0; pieceId < Consts.NB_PIECES_PER_PLAYER; pieceId++) {
                pieces[player][pieceId].resetValues();
            }
        }
    }

    public void resetGeneralValues() {
        nbPieceInHandAI = 0;
        nbPieceInHandOpponent = 0;
        nbPieceOnBoardAI = 0;
        nbPieceOnBoardOpponent = 0;
        nbMovesAI = 0;
        nbMovesOpponent = 0;
        nbPlacementAI = 0;
        nbPlacementOpponent = 0;
        nbPinnedAI = 0;
        nbPinnedOpponent = 0;
    }

    public boolean isOpponentsQueenNeighbor(int player, Tile tile) {
        for (Tile t : core.getBoard().getNeighbors(getTile(Consts.QUEEN, 1 - player))) {
            if (tile.getCoord() == t.getCoord()) {
                return true;
            }
        }
        return false;
    }

    public int distanceToOpposingQueen(Tile tile) {
        if (tile == null || tile.getPiece() == null
                || (tile.getPiece().getId() != Consts.BEETLE1 && tile.getPiece().getId() != Consts.BEETLE2)) {
            return -1;
        }
        Board board = core.getBoard();
        int distance = 0;
        List<Tile> ab = board.getAboveAndBelow(tile);
        for (Tile t : ab) {
            if (t.getPiece() != null && t.getPiece().getId() == Consts.QUEEN && t.getPiece().getTeam() != tile.getPiece().getTeam()) {
                return distance;
            }
        }

        HashSet<CoordGene<Integer>> visited = new HashSet<>();
        List<CoordGene<Integer>> possibleDest = new ArrayList<>();
        Set<CoordGene<Integer>> tmp = new HashSet<>();
        possibleDest.addAll(beetleMov(tile, tile));
        visited.add(tile.getCoord());
        while (!possibleDest.isEmpty()) {

            for (CoordGene<Integer> coord : possibleDest) {
                ab = board.getAboveAndBelow(board.getTile(coord));
                ab.add(board.getTile(coord));
                for (Tile t : ab) {
                    if (t.getPiece() != null && t.getPiece().getId() == Consts.QUEEN && t.getPiece().getTeam() != tile.getPiece().getTeam()) {
                        return distance;
                    }
                }
            }

            visited.addAll(possibleDest);
            for (CoordGene<Integer> c : possibleDest) {
                List<CoordGene<Integer>> list = beetleMov(board.getTile(c), tile);
                for (CoordGene<Integer> nextC : list) {
                    if (!visited.contains(nextC)) {
                        tmp.add(nextC);
                    }
                }
            }
            possibleDest.clear();
            possibleDest.addAll(tmp);
            tmp.clear();
            distance++;
        }
        return distance;
    }

    public List<CoordGene<Integer>> beetleMov(Tile from, Tile exception) {
        List<CoordGene<Integer>> list = new ArrayList<>();
        List<CoordGene<Integer>> neighbors = from.getCoord().getNeighbors();
        for (int i = 0; i < neighbors.size(); i++) {
            Tile target = core.getBoard().getTile(neighbors.get(i));
            CoordGene<Integer> left = neighbors.get(Math.floorMod(i - 1, 6));
            CoordGene<Integer> right = neighbors.get(Math.floorMod(i + 1, 6));
            int floor;
            if (target != null) {
                List<Tile> nei = core.getBoard().getPieceNeighbors(target);
                if (nei.size() != 1 || nei.get(0).getCoord().equals(exception.getCoord())) {
                    if (target.getZ() == 0 && from.getZ() == 0) {
                        floor = (target.getPiece() == null) ? 0 : 1;
                    } else {
                        floor = Math.max(target.getZ(), from.getZ());
                    }
                    if (core.getBoard().freedomToMove(floor, left, right, from.getCoord())
                            && core.getBoard().permanentContact(floor, left, right, from.getCoord())
                            && !target.equals(exception.getCoord()) && !left.equals(exception.getCoord()) && !right.equals(exception.getCoord())) {
                        list.add(target.getCoord());
                    }
                }
            }
        }
        return list;
    }

}

//    public int grassHopperToOpponentsQueensFreeNeighbor(int player, Tile tile)
//    {
//        int turn = 0;
//        HashSet<Tile> tileSet = new HashSet();
//        HashSet<Tile> markedTiles = new HashSet();
//        boolean destFound = false;
//        tileSet.add(tile);
//        markedTiles.add(tile);
//        do
//        {
//            turn++;
//            for (Tile tileElem : tileSet)
//            {
//                for (CoordGene coords : tileElem.getPiece().getPossibleMovement(tileElem, this.core.getBoard()))
//                {
//                    if (!(markedTiles.contains(this.core.getBoard().getTile(coords))))
//                    {
//                        if (this.isOpponentsQueenNeighbor(player, this.core.getBoard().getTile(coords)))
//                        {
//                            destFound = true;
//                            break;
//                        }
//                        tileSet.add(this.core.getBoard().getTile(coords));
//                        markedTiles.add(this.core.getBoard().getTile(coords));
//                    }
//                }
//                if (destFound)
//                    break;
//                
//                tileSet.remove(tileElem);
//            }
//        }
//        while((!(tileSet.isEmpty())) && (!(destFound)));
//        
//        if (destFound)
//            return turn;
//        return -1;
//    }
