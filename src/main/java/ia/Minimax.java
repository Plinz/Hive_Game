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
        heuristics.resetValues();
        if (depth >= maxdepth) {
            return heuristics.getHeuristicsValue();
        }
        List<String> allPossibleMovesAndUnmoves = getAllPossibleMovesAndUnmoves();

        if (AIPlayer == core.getCurrentPlayer()) {
            double bestHeuristic = Consts.MINIMUM_HEURISTICS;

            for (String moveAndUnmove : allPossibleMovesAndUnmoves) {
                String[] splitted = moveAndUnmove.split(";");
                core.playEmulate(splitted[0], splitted[1]);
                depth++;
                getHeuristicsValueRecursively(heuristics.maxdepth);
                if (heuristicValue > bestHeuristic) {
                    bestHeuristic = heuristicValue;
                }
                depth--;
                core.previousState();
            }
            return heuristics.getHeuristicsValue() + bestHeuristic / 2;
        } else {
            double worstHeuristic = Consts.BEST_HEURISTICS;

            for (String moveAndUnmove : allPossibleMovesAndUnmoves) {
                String[] splitted = moveAndUnmove.split(";");
                core.playEmulate(splitted[0], splitted[1]);
                depth++;
                getHeuristicsValueRecursively(heuristics.maxdepth);
                if (heuristicValue < worstHeuristic) {
                    worstHeuristic = heuristicValue;
                }
                depth--;
                core.previousState();
            }
            return heuristics.getHeuristicsValue() - worstHeuristic / 2;
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

        List<CoordGene<Integer>> possibleAdd = core.getPossibleAdd(AIPlayer);
        int possibleAddSize = possibleAdd.size();
        for (Piece piece : core.getCurrentPlayerObj().getFirstPieceOfEachType()) {
            for (CoordGene<Integer> destination : possibleAdd) {
                possibleMovements.add(Notation.getMoveNotation(core.getBoard(), piece, destination));
                possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), piece));
            }
        }

        for (int i = 0; i < 2; i++) {
            for (Piece piece : core.getPlayers()[i].getInventory()) {
                if (i == AIPlayer) {
                    heuristics.pieces[0][piece.getId()].getValuesInHand(possibleAddSize);
                } else {
                    heuristics.pieces[1][piece.getId()].getValuesInHand(possibleAddSize);
                }
            }
        }

        for (Column column : core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile != null && tile.getPiece() != null && !tile.isBlocked()) {

                        List<CoordGene<Integer>> PossibleDestinations = core.getPossibleMovement(tile.getCoord());

                        if (tile.getPiece().getTeam() == core.getCurrentPlayer()) {
                            for (CoordGene<Integer> destination : PossibleDestinations) {
                                possibleMovements.add(Notation.getMoveNotation(core.getBoard(), tile.getPiece(), destination));
                                possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece()));
                            }
                        }

                        int possibleMoveSize = PossibleDestinations.size();
                        int nbNeighbors = core.getBoard().getPieceNeighbors(tile).size();

                        if (tile.getPiece().getTeam() == AIPlayer) {
                            heuristics.pieces[0][tile.getPiece().getId()].getValuesOnBoard(nbNeighbors, possibleMoveSize);
                            System.out.println("White "+tile.getPiece().getId()+" : neigbors = "+nbNeighbors);
                        } else {
                            heuristics.pieces[1][tile.getPiece().getId()].getValuesOnBoard(nbNeighbors, possibleMoveSize);
                        }
                    }
                }
            }
        }
        
        heuristicValue = heuristics.getHeuristicsValue();
        System.out.println("move : "+this.moveFromParent);
        System.out.println("heuristique :"+this.heuristicValue);
        for (int i = 0; i < possibleMovements.size(); i++) {
            String toAdd = possibleMovements.get(i) + ";" + possibleUnplay.get(i);
            result.add(toAdd);
        }
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
            Minimax child = new Minimax(this, splitted[0], splitted[1]);
            child.getAllPossibleMovesWithHeuristics();
            children.add(child);
            core.previousState();
        }
        System.out.println("NbConfigStudied = " + children.size());
        return children;
    }
}