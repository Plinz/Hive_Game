/*

 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;
import main.java.engine.Core;
import main.java.engine.Emulator;
import main.java.engine.Notation;
import main.java.model.Board;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Player;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.CoordGene;

public class Minimax {

    Core core;
    Board board;
    Player players[];
    Heuristics heuristics;
    String moveFromParent;
    Emulator emulator;
    int heuristicValue;
    int currentPlayer;
    int AIPlayer;
    int depth;

    public Minimax(Core core1) {
        this.core = new Core(core1);
        this.board = this.core.getBoard();
        this.players = this.core.getPlayers();
        this.depth = 0;
        this.currentPlayer = this.core.getCurrentPlayer();
        this.AIPlayer = this.currentPlayer;
        this.emulator = new Emulator(this.core, this.board, this.players);
        HeuristicsFactory heuristicsFactory = new HeuristicsFactory();
        this.heuristics = heuristicsFactory.buildHeuristics(this.core.getDifficulty(), this.core);
        this.moveFromParent = null;
        System.out.println("const minimax : core = "+this.core.toString());
    }

    public Minimax(Minimax parent, String moveFromParent) {
        this.core = parent.core;
        this.board = parent.board;
        this.currentPlayer = 1 - parent.currentPlayer;
        this.depth = parent.depth + 1;
        this.emulator = parent.emulator;
        this.heuristics = parent.heuristics;
        this.players = parent.players;
        this.moveFromParent = moveFromParent;
        for (int i = 0 ; i<this.depth ; i++)
            System.out.print("  ");
        System.out.println("depth = "+this.depth);
    }

    public List<Minimax> getChildrenWithHeuristics() {
        List<Minimax> children = new ArrayList<>();
        List<String> possibleMovements = new ArrayList<>();

        core.getPossibleAdd(this.currentPlayer).stream().forEach((PossibleAdd) -> {
            players[currentPlayer].getInventory().stream().forEach((piece) -> {
                possibleMovements.add(Notation.getMoveNotation(board, piece, PossibleAdd));
            });
        });

        board.getBoard().stream().forEach((Column column) -> {
            column.stream().forEach((Box box) -> {
                box.stream().filter((Tile tile) -> {
                    return tile != null && tile.getPiece() != null && tile.getPiece().getTeam() != currentPlayer;
                }).forEach((tile) -> {
                    List<CoordGene<Integer>> PossibleDestinations = tile.getPiece().getPossibleMovement(tile, board);
                    PossibleDestinations.stream().forEach((Destination) -> {
                        possibleMovements.add(Notation.getMoveNotation(board, tile.getPiece(), Destination));
                    });
                });
            });
        });
        for (String possibleMovement : possibleMovements) {
            emulator.play(possibleMovement);
            Minimax child = new Minimax(this, possibleMovement);
            children.add(child);
            child.getHeuristicsValueRecursively(heuristics.maxdepth);
            core.previousState();
        }
        return children;
    }

    private int getHeuristicsValueRecursively(int maxdepth) {
        if (depth >= maxdepth) {
            return heuristics.getHeuristicsValue();
        }

        List<String> possibleMovements = new ArrayList<>();

        core.getPossibleAdd(this.currentPlayer).stream().forEach((PossibleAdd) -> {
            players[currentPlayer].getInventory().stream().forEach((piece) -> {
                possibleMovements.add(Notation.getMoveNotation(board, piece, PossibleAdd));
            });
        });

        board.getBoard().stream().forEach((Column column) -> {
            column.stream().forEach((Box box) -> {
                box.stream().filter((Tile tile) -> {
                    return tile != null && tile.getPiece() != null && tile.getPiece().getTeam() != currentPlayer;
                }).forEach((tile) -> {
                    List<CoordGene<Integer>> PossibleDestinations = tile.getPiece().getPossibleMovement(tile, board);
                    PossibleDestinations.stream().forEach((Destination) -> {
                        possibleMovements.add(Notation.getMoveNotation(board, tile.getPiece(), Destination));
                    });
                });
            });
        });

        if (AIPlayer == currentPlayer) {
            int bestHeuristic = Consts.MINIMUM_HEURISTICS;
            for (String possibleMovement : possibleMovements) {
                emulator.play(possibleMovement);
                Minimax child = new Minimax(this, possibleMovement);
                child.heuristicValue = child.getHeuristicsValueRecursively(heuristics.maxdepth);
                if (child.heuristicValue > bestHeuristic) {
                    bestHeuristic = child.heuristicValue;
                }
                core.previousState();
            }
            return bestHeuristic;
        } else {
            int worstHeuristic = Consts.BEST_HEURISTICS;
            for (String possibleMovement : possibleMovements) {
                emulator.play(possibleMovement);
                Minimax child = new Minimax(this, moveFromParent);
                child.heuristicValue = child.getHeuristicsValueRecursively(heuristics.maxdepth);
                if (child.heuristicValue < worstHeuristic) {
                    worstHeuristic = child.heuristicValue;
                }
                core.previousState();
            }
            return worstHeuristic;
        }
    }
    
    public int getMobility(Player player)
    {
        int result = 0;
        for (Column column : board.getBoard()){
            for (Box box : column) {
                for (Tile tile : box){
                    if ((tile.getPiece() != null) && (tile.getPiece().getTeam() == player.getTeam())){
                        result++;
                    }
                }
            }
        }
        return result;
    }
        
    

}
