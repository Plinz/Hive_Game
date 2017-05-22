/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import main.java.engine.Core;
import main.java.engine.Notation;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Minimax {

    Core core;
    Heuristics heuristics;
    String moveFromParent;
    String moveToParent;
    double heuristicValue;
    int AIPlayer;
    int depth;

    public Minimax(Core core1, Heuristics heuristics1) {
        this.core = core1;
        this.AIPlayer = core1.getCurrentPlayer();

        this.heuristics = heuristics1;

        this.heuristics.AIPlayer = this.AIPlayer;
        this.moveFromParent = "root";
        this.moveToParent = "root";
        this.depth = 0;
    }

    public Minimax(Minimax parent, String moveFromParent, String moveToParent) {
        this.core = parent.core;
        this.depth = parent.depth + 1;
        HeuristicsFactory heuristicsFactory = new HeuristicsFactory();
        this.heuristics = heuristicsFactory.buildHeuristics(parent.heuristics.difficulty, core);
        this.moveFromParent = moveFromParent;
        this.moveToParent = moveToParent;
    }

    public List<Minimax> getChildrenWithHeuristics() {
        heuristics.nbConfigsStudied = 0;

        List<Minimax> children = new ArrayList<>();
        List<String> allPossibleMovesAndUnmoves = getAllPossibleMovesAndUnmoves();

        //System.out.println("Minimax : possible moves = " + possibleMovements.toString());
        //System.out.println("size :" + possibleMovements.size());
        for (String moveAndUnmove : allPossibleMovesAndUnmoves) {
            String[] splitted = moveAndUnmove.split(";");   //  move;unmove
            core.playEmulate(splitted[0], splitted[1]);
            Minimax child = new Minimax(this, splitted[0], splitted[1]);
            children.add(child);
            if (child.heuristics.maxdepth <= 1) {
                child.heuristicValue = child.heuristics.getHeuristicsValue();
            } else {
                child.getHeuristicsValueRecursively(heuristics.maxdepth);
            }
            core.previousState();

        }
        System.out.println("NbConfigStudied = " + heuristics.nbConfigsStudied);
        return children;
    }

    private double getHeuristicsValueRecursively(int maxdepth) {
        System.out.println("/*/*/*/*/*/*/GetHeurRec Move =" + moveFromParent + " and depth :" + depth + " and currentpl :" + core.getCurrentPlayer());
        heuristics.resetValues();
        if (depth >= maxdepth) {
            getAllPossibleMovesWithHeuristics();
            return heuristicValue;
        }

        //Case Ai just played -> we keep the best heuristic
        if (AIPlayer != core.getCurrentPlayer()) {
            double bestHeuristic = Consts.MINIMUM_HEURISTICS;

            for (String moveAndUnmove : getAllPossibleMovesWithHeuristics()) {
                String[] splitted = moveAndUnmove.split(";");
                core.playEmulate(splitted[0], splitted[1]);
                System.out.println("play " + splitted[0]);
                depth++;
                getHeuristicsValueRecursively(heuristics.maxdepth);
                if (heuristicValue > bestHeuristic) {
                    bestHeuristic = heuristicValue;
                }
                depth--;
                System.out.println("unplay " + splitted[1]);
                core.previousState();
            }
            return bestHeuristic;
        } else {
            double worstHeuristic = Consts.BEST_HEURISTICS;

            for (String moveAndUnmove : getAllPossibleMovesWithHeuristics()) {
                String[] splitted = moveAndUnmove.split(";");
                core.playEmulate(splitted[0], splitted[1]);
                System.out.println("play " + splitted[0]);
                depth++;
                getHeuristicsValueRecursively(heuristics.maxdepth);
                if (heuristicValue < worstHeuristic) {
                    worstHeuristic = heuristicValue;
                }
                depth--;
                System.out.println("unplay " + splitted[1]);
                core.previousState();
            }
            return worstHeuristic;
        }
    }

    private List<String> getAllPossibleMovesAndUnmoves() {
        ArrayList<String> result = new ArrayList<>();
        List<String> possibleMovements = new ArrayList<>();
        List<String> possibleUnplay = new ArrayList<>();

        core.getPossibleAdd(core.getCurrentPlayer()).stream().forEach((possibleAdd) -> {
            core.getCurrentPlayerObj().getFirstPieceOfEachType().stream().forEach((piece) -> {
                possibleMovements.add(Notation.getMoveNotation(core.getBoard(), piece, possibleAdd));
                possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), piece));
            });
        });

        for (Column column : core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile != null && tile.getPiece() != null && tile.getPiece().getTeam() == core.getCurrentPlayer() && !tile.isBlocked()) {
                        List<CoordGene<Integer>> PossibleDestinations = core.getPossibleMovement(tile.getCoord());
                        for (CoordGene<Integer> destination : PossibleDestinations) {
                            possibleMovements.add(Notation.getMoveNotation(core.getBoard(), tile.getPiece(), destination));
                            possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece()));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < possibleMovements.size(); i++) {
            String toAdd = possibleMovements.get(i) + ";" + possibleUnplay.get(i);
            result.add(toAdd);
        }
        return result;
    }

    public List<String> getAllPossibleMovesWithHeuristics() {
        ArrayList<String> result = new ArrayList<>();
        List<String> possibleMovements = new ArrayList<>();
        List<String> possibleUnplay = new ArrayList<>();

        //heuristics for pieces in hand of current player
        List<CoordGene<Integer>> possibleAddCurrentPlayer = core.getPossibleAdd(core.getCurrentPlayer());
        int possibleAddSizeCurrentPlayer = possibleAddCurrentPlayer.size();
        for (Piece piece : core.getCurrentPlayerObj().getInventory()) {
            heuristics.pieces[0][piece.getId()].getValuesInHand(possibleAddSizeCurrentPlayer);
        }

        //heuristics for pieces in hand of other player
        List<CoordGene<Integer>> possibleAddOtherPlayer = core.getPossibleAdd(1 - core.getCurrentPlayer());
        int possibleAddSizeOtherPlayer = possibleAddOtherPlayer.size();
        for (Piece piece : core.getPlayers()[1 - core.getCurrentPlayer()].getInventory()) {
            heuristics.pieces[0][piece.getId()].getValuesInHand(possibleAddSizeOtherPlayer);
        }

        //getting all possible adds of current player
        for (Piece piece : core.getCurrentPlayerObj().getFirstPieceOfEachType()) {
            for (CoordGene<Integer> destination : possibleAddCurrentPlayer) {
                possibleMovements.add(Notation.getMoveNotation(core.getBoard(), piece, destination));
                possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), piece));
            }
        }

        //getting all possible moves + heuristics of both players and add possible moves for current player
        for (Column column : core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile != null && tile.getPiece() != null && !tile.isBlocked()) {

                        List<CoordGene<Integer>> PossibleDestinations = core.getPossibleMovement(tile.getCoord());
                        int nbNeighbors = core.getBoard().getPieceNeighbors(tile).size();
                        int possibleMoveSize = PossibleDestinations.size();

                        if (tile.getPiece().getTeam() == core.getCurrentPlayer()) {
                            //heuristics
                            heuristics.pieces[0][tile.getPiece().getId()].getValuesOnBoard(nbNeighbors, possibleMoveSize);
                            //list of possible moves
                            for (CoordGene<Integer> destination : PossibleDestinations) {
                                possibleMovements.add(Notation.getMoveNotation(core.getBoard(), tile.getPiece(), destination));
                                possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece()));
                            }
                        } else {
                            heuristics.pieces[1][tile.getPiece().getId()].getValuesOnBoard(nbNeighbors, possibleMoveSize);
                        }
                    }
                }
            }
        }
        //calculate heuristics
        System.out.println("move : " + this.moveFromParent + ",currentPlayer" + core.getCurrentPlayer());
        heuristicValue = heuristics.getHeuristicsValue();
        System.out.println("heuristique :" + this.heuristicValue);
        //concatenate strings for result
        for (int i = 0; i < possibleMovements.size(); i++) {
            String toAdd = possibleMovements.get(i) + ";" + possibleUnplay.get(i);
            result.add(toAdd);
        }
        System.out.println("Possible move " + result.toString());
        return result;
    }

    public List<Minimax> getChildren() {
        heuristics.nbConfigsStudied = 0;

        List<Minimax> children = new ArrayList<>();

        //System.out.println("Minimax : possible moves = " + possibleMovements.toString());
        //System.out.println("size :" + possibleMovements.size());
        for (String moveAndUnmove : getAllPossibleMovesWithHeuristics()) {
            String[] splitted = moveAndUnmove.split(";");   //  move;unmove
            core.playEmulate(splitted[0], splitted[1]);
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$-----Move :" + splitted[0] + " profondeur " + depth);
            Minimax child = new Minimax(this, splitted[0], splitted[1]);
            child.getHeuristicsValueRecursively(heuristics.maxdepth);
            children.add(child);
            //System.out.println("Move" + splitted[1]);
            core.previousState();
        }
        System.out.println("NbConfigStudied = " + children.size());
        return children;
    }
}
