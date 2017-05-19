/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
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
        this.moveFromParent = null;
        this.moveToParent = null;
        this.depth = 0;
    }

    public Minimax(Minimax parent, String moveFromParent, String moveToParent) {
        this.core = parent.core;
        this.depth = parent.depth + 1;
        this.heuristics = parent.heuristics;
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
                child.heuristicValue = heuristics.getHeuristicsValue();
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
            return heuristics.getHeuristicsValue() + bestHeuristic/2;
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
            return heuristics.getHeuristicsValue() - worstHeuristic/2;
        }
    }

    private List<String> getAllPossibleMovesAndUnmoves() { //and calculate some heuristics along
        ArrayList<String> result = new ArrayList<>();
        List<String> possibleMovements = new ArrayList<>();
        List<String> possibleUnplay = new ArrayList<>();

        if ((core.getTurn() == 6 || core.getTurn() == 7) && !core.isQueenOnBoard(core.getCurrentPlayer())) {
            Piece queen = null;
            for (Piece piece : core.getCurrentPlayerObj().getInventory()) {
                if (piece.getId() == Consts.QUEEN) {
                    queen = piece;
                    break;
                }
            }
            for (CoordGene<Integer> possibleAdd : core.getPossibleAdd(AIPlayer)) {
                possibleMovements.add(Notation.getMoveNotation(core.getBoard(), queen, possibleAdd));
                possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), queen));
            }

        } else { //normal situation
            core.getPossibleAdd(core.getCurrentPlayer()).stream().forEach((possibleAdd) -> {
                core.getCurrentPlayerObj().getFirstPieceOfEachType().stream().forEach((piece) -> {
                    possibleMovements.add(Notation.getMoveNotation(core.getBoard(), piece, possibleAdd));
                    possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), piece));
                });
            });

            heuristics.possiblePlacements = possibleMovements.size();
            /*ancienne version avec des streams -> pas compatible avec les apppels de methode 
            core.getBoard().getBoard().stream().forEach((Column column) -> {
                column.stream().forEach((Box box) -> {
                    box.stream().filter(tile
                            -> tile != null && tile.getPiece() != null && !tile.isBlocked() && tile.getPiece().getTeam() == core.getCurrentPlayer()
                    ).forEach((tile) -> {
                        List<CoordGene<Integer>> PossibleDestinations = core.getPossibleMovement(tile.getCoord());
                        PossibleDestinations.stream().forEach((Destination) -> {
                            heuristics.incrementMobility(core.getCurrentPlayer());
                            possibleMovements.add(Notation.getMoveNotation(core.getBoard(), tile.getPiece(), Destination));
                            possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece()));
                        });
                    });
                });
            });*/

            for (Column column : core.getBoard().getBoard()) {
                for (Box box : column) {
                    for (Tile tile : box) {
                        if (tile != null && tile.getPiece() != null && tile.getPiece().getTeam() == core.getCurrentPlayer()) {
                            List<CoordGene<Integer>> PossibleDestinations = core.getPossibleMovement(tile.getCoord());
                            if (!PossibleDestinations.isEmpty()) {
                                for (CoordGene<Integer> destination : PossibleDestinations) {
                                    possibleMovements.add(Notation.getMoveNotation(core.getBoard(), tile.getPiece(), destination));
                                    possibleUnplay.add(Notation.getInverseMoveNotation(core.getBoard(), tile.getPiece()));
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0;
                    i < possibleMovements.size();
                    i++) {
                String toAdd = possibleMovements.get(i) + ";" + possibleUnplay.get(i);
                result.add(toAdd);
            }
        }
        return result;
    }
}
