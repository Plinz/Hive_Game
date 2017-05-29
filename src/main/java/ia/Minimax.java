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
        this.moveFromParent = "root";
        this.moveToParent = "root";
        this.depth = 0;
    }

    public Minimax(Minimax parent, String moveFromParent, String moveToParent) {
        this.core = parent.core;
        this.depth = parent.depth + 1;
        HeuristicsFactory heuristicsFactory = new HeuristicsFactory();
        this.heuristics = heuristicsFactory.buildHeuristics(parent.heuristics.difficulty, core);
        this.heuristics.AIPlayer = parent.heuristics.AIPlayer;
        this.moveFromParent = moveFromParent;
        this.moveToParent = moveToParent;
    }

    public List<Minimax> getChildrenWithHeuristics() {
        heuristics.nbConfigsStudied = 0;

        List<Minimax> children = new ArrayList<>();
        List<String> allPossibleMovesAndUnmoves = getAllPossibleMovesAndUnmoves();

        for (String moveAndUnmove : allPossibleMovesAndUnmoves) {
            String[] splitted = moveAndUnmove.split(";");   //  move;unmove
            core.playEmulate(splitted[0], splitted[1]);
            Minimax child = new Minimax(this, splitted[0], splitted[1]);
            children.add(child);
            if (child.heuristics.maxdepth <= 1) {
                child.heuristicValue = child.heuristics.getHeuristicsValue();
            } else {
                child.getHeuristicsValueRecursively(heuristics.maxdepth, HeuristicConst.MOINS_INFINI, HeuristicConst.PLUS_INFINI);
            }
            core.previousState();
        }
        return children;
    }

    private double getHeuristicsValueRecursively(int maxdepth, double alpha, double beta) {
        double currentHeuristic;
        heuristics.resetValues();
        if (depth >= maxdepth) {
            double result = calculateHeuristics();
            heuristics.nbConfigsStudied++;
            heuristicValue = result;
            return result;
        }
        if (core.getStatus() == Consts.WIN_TEAM1 || core.getStatus() == Consts.WIN_TEAM2) {
            heuristics.nbConfigsStudied++;
            heuristicValue = calculateHeuristics();
            return heuristicValue;
        }
        List<String> nextMovesAndUnmoves = getAllPossibleMovesAndUnmoves();

        //Case WHITE TO PLAY -> we keep the highest heuristic (max node)
        if (core.getCurrentPlayer() == 0) {
            double bestHeuristicSoFar = HeuristicConst.MOINS_INFINI;

            for (String moveAndUnmove : nextMovesAndUnmoves) {
                String[] splitted = moveAndUnmove.split(";");
                core.playEmulate(splitted[0], splitted[1]);
                depth++;

                currentHeuristic = getHeuristicsValueRecursively(heuristics.maxdepth, alpha, beta);
                if (currentHeuristic > bestHeuristicSoFar) {
                    bestHeuristicSoFar = currentHeuristic;
                }
                //beta cut
                if (bestHeuristicSoFar >= beta) {
                    depth--;
                    core.previousState();
                    return bestHeuristicSoFar;
                }

                if (bestHeuristicSoFar > alpha) {
                    alpha = bestHeuristicSoFar;
                }
                depth--;
                core.previousState();
            }
            return bestHeuristicSoFar;

            //Case BLACK TO PLAY -> we keep the lowest heuristic (min node)
        } else {

            double lowestHeuristicSoFar = HeuristicConst.PLUS_INFINI;

            for (String moveAndUnmove : nextMovesAndUnmoves) {

                String[] splittedMove = moveAndUnmove.split(";");
                core.playEmulate(splittedMove[0], splittedMove[1]);
                depth++;

                currentHeuristic = getHeuristicsValueRecursively(heuristics.maxdepth, alpha, beta);
                if (currentHeuristic < lowestHeuristicSoFar) {
                    lowestHeuristicSoFar = currentHeuristic;
                }
                //alpha cut
                if (alpha >= lowestHeuristicSoFar) {
                    depth--;
                    core.previousState();
                    return lowestHeuristicSoFar;
                }

                if (beta < lowestHeuristicSoFar) {
                    beta = lowestHeuristicSoFar;
                }
                depth--;
                core.previousState();
            }
            return lowestHeuristicSoFar;
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

    public List<Minimax> getChildren() {
        heuristics.nbConfigsStudied = 0;

        List<Minimax> children = new ArrayList<>();
        double alpha = HeuristicConst.MOINS_INFINI;
        double beta = HeuristicConst.PLUS_INFINI;

        for (String moveAndUnmove : getAllPossibleMovesAndUnmoves()) {
            String[] splitted = moveAndUnmove.split(";");   //  move;unmove

            core.playEmulate(splitted[0], splitted[1]);

            Minimax child = new Minimax(this, splitted[0], splitted[1]);
            child.heuristicValue = child.getHeuristicsValueRecursively(heuristics.maxdepth, alpha, beta);

            if (AIPlayer == 0) { //max node case -> could modify alpha
                if (child.heuristicValue >= alpha) {
                    alpha = child.heuristicValue;
                }
            } else //min node case -> could modify beta
             if (beta >= child.heuristicValue) {
                    beta = child.heuristicValue;
                }

            children.add(child);
            core.previousState();
        }
        return children;
    }

    public double calculateHeuristics() {

        //heuristics for pieces in hand of current player
        List<CoordGene<Integer>> possibleAddCurrentPlayer = core.getPossibleAdd(core.getCurrentPlayer());
        int possibleAddSizeCurrentPlayer = possibleAddCurrentPlayer.size();
        for (Piece piece : core.getPlayers()[0].getInventory()) {
            heuristics.pieces[0][piece.getId()].getValuesInHand(possibleAddSizeCurrentPlayer);
        }

        //heuristics for pieces in hand of other player
        List<CoordGene<Integer>> possibleAddOtherPlayer = core.getPossibleAdd(1 - core.getCurrentPlayer());
        int possibleAddSizeOtherPlayer = possibleAddOtherPlayer.size();
        for (Piece piece : core.getPlayers()[1].getInventory()) {
            heuristics.pieces[1][piece.getId()].getValuesInHand(possibleAddSizeOtherPlayer);
        }

        //getting all possible moves + heuristics of both players and add possible moves for current player
        for (Column column : core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if (tile != null && tile.getPiece() != null) {
                        List<CoordGene<Integer>> PossibleDestinations = new ArrayList<>();
                        if (!tile.isBlocked()) {
                            PossibleDestinations = core.getPossibleMovement(tile.getCoord());
                        }
                        int nbNeighbors = core.getBoard().getNbNeighborsOnFloor(tile);
                        int possibleMoveSize = PossibleDestinations.size();

                        if (tile.getPiece().getTeam() == 0) {
                            heuristics.pieces[0][tile.getPiece().getId()].getValuesOnBoard(nbNeighbors, possibleMoveSize);
                        } else {
                            heuristics.pieces[1][tile.getPiece().getId()].getValuesOnBoard(nbNeighbors, possibleMoveSize);
                        }
                    }
                }
            }
        }
        //calculate heuristics
        double result = heuristics.getHeuristicsValue();
        return result;

    }

    public void printIndented(String string) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
        System.out.print(string + "\n");
    }
}
