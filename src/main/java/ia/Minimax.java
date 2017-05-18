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
    String moveToParent;
    Emulator emulator;
    int heuristicValue;
    int currentPlayer;
    int AIPlayer;
    int depth;

    public Minimax(Core core1) {
        this.core  = core1;
        this.board = this.core.getBoard();
        this.players = this.core.getPlayers();
        
        this.currentPlayer = this.core.getCurrentPlayer();
        
        this.AIPlayer = this.currentPlayer;
        HeuristicsFactory heuristicsFactory = new HeuristicsFactory();
        this.heuristics = heuristicsFactory.buildHeuristics(this.core.getDifficulty(), this.core);
        this.heuristics.AIPlayer = this.AIPlayer;
        this.moveFromParent = null;
        this.moveToParent = null;
        this.depth = 0;
    }

    public Minimax(Minimax parent, String moveFromParent, String moveToParent) {
        this.core = parent.core;
        this.board = parent.board;
        this.currentPlayer = 1- parent.currentPlayer;
        this.depth = parent.depth +1;
        this.emulator = parent.emulator;
        this.heuristics = parent.heuristics;
        this.players = parent.players;
        this.moveFromParent = moveFromParent;
        this.moveToParent = moveToParent;
    }

    public Minimax (Minimax parent){ //in the case one player is blocked
        this.core = parent.core;
        this.board = parent.board;
        this.currentPlayer = 1- parent.currentPlayer;
        this.depth = parent.depth ;
        this.emulator = parent.emulator;
        this.heuristics = parent.heuristics;
        this.players = parent.players;
    }
    
    public List<Minimax> getChildrenWithHeuristics() {
        //if (core.getTurn() == 6 || core.getTurn() == 7);
        List<Minimax> children = new ArrayList<>();
        List<String> possibleMovements = new ArrayList<>();
        List<String> possibleUnplay = new ArrayList<>();
        core.getPossibleAdd(this.currentPlayer).stream().forEach((PossibleAdd) -> {
            players[currentPlayer].getFirstPieceOfEachType().stream().forEach((piece) -> {
                possibleMovements.add(Notation.getMoveNotation(board, piece, PossibleAdd));
                possibleUnplay.add(Notation.getInverseMoveNotation(board, piece));
            });
        });

        board.getBoard().stream().forEach((Column column) -> {
            column.stream().forEach((Box box) -> {
                box.stream().filter((Tile tile) -> {
                    return tile != null && tile.getPiece() != null && tile.getPiece().getTeam() == currentPlayer;
                }).forEach((tile) -> {
                    List<CoordGene<Integer>> PossibleDestinations = tile.getPiece().getPossibleMovement(tile, board);
                    PossibleDestinations.stream().forEach((Destination) -> {
                        possibleMovements.add(Notation.getMoveNotation(board, tile.getPiece(), Destination));
                        possibleUnplay.add(Notation.getInverseMoveNotation(board, tile.getPiece()));
                    });
                });
            });
        });
        System.out.println("Minimax : possible moves = " + possibleMovements.toString());
        System.out.println("size :"+possibleMovements.size());
        for (int i = 0; i < possibleMovements.size(); i++) {
            System.out.println("i="+i);
            String move = possibleMovements.get(i);
            String unplay = possibleUnplay.get(i);
            core.playEmulate(move, unplay);
            Minimax child = new Minimax(this, move, unplay);
            children.add(child);
            if (child.heuristics.maxdepth <= 1){
                child.heuristicValue = heuristics.getHeuristicsValue();
            } else {
            child.getHeuristicsValueRecursively(heuristics.maxdepth);
            }
            core.previousState();

        }
        return children;
    }

    private int getHeuristicsValueRecursively(int maxdepth) {
        if (depth >= maxdepth) {
            return heuristics.getHeuristicsValue();
        }

        List<String> possibleMovements = new ArrayList<>();
        List<String> possibleUnplay = new ArrayList<>();

        core.getPossibleAdd(this.currentPlayer).stream().forEach((PossibleAdd) -> {
            players[currentPlayer].getFirstPieceOfEachType().stream().forEach((piece) -> {
                possibleMovements.add(Notation.getMoveNotation(board, piece, PossibleAdd));
                possibleUnplay.add(Notation.getInverseMoveNotation(board, piece));

            });
        });

        board.getBoard().stream().forEach((Column column) -> {
            column.stream().forEach((Box box) -> {
                box.stream().filter((Tile tile)
                        -> tile != null && tile.getPiece() != null && tile.getPiece().getTeam() == currentPlayer
                ).forEach((tile) -> {
                    List<CoordGene<Integer>> PossibleDestinations = tile.getPiece().getPossibleMovement(tile, board);
                    PossibleDestinations.stream().forEach((Destination) -> {
                        possibleMovements.add(Notation.getMoveNotation(board, tile.getPiece(), Destination));
                        possibleUnplay.add(Notation.getInverseMoveNotation(board, tile.getPiece()));
                    });
                });
            });
        });
        
        if (possibleMovements.isEmpty()){
            System.out.println("Player "+currentPlayer+" is blocked");
            Minimax child = new Minimax(this);
            return getHeuristicsValueRecursively(maxdepth);
        }
        
        if (AIPlayer == currentPlayer) {
            int bestHeuristic = Consts.MINIMUM_HEURISTICS;
            for (int i = 0; i <possibleMovements.size() ; i++) {
                String move = possibleMovements.get(i);            
                String unplay = possibleUnplay.get(i);
                System.out.println("Depth ="+depth+",move :"+move);
                core.playEmulate(move, unplay);
                Minimax child = new Minimax(this, move, unplay);
                child.heuristicValue = child.getHeuristicsValueRecursively(heuristics.maxdepth);
                if (child.heuristicValue > bestHeuristic) {
                    bestHeuristic = child.heuristicValue;
                }
                System.out.println("unplay :"+unplay);
                core.previousState();
            }
            return bestHeuristic;
        } else {
            int worstHeuristic = Consts.BEST_HEURISTICS;
            for (int i = 0; i <possibleMovements.size() ; i++) {
                String move = possibleMovements.get(i);            
                String unplay = possibleUnplay.get(i);
                System.out.println("Depth ="+depth+"current player = "+currentPlayer+",move :"+move);
                core.playEmulate(move, unplay);
                Minimax child = new Minimax(this, move, unplay);
                child.heuristicValue = child.getHeuristicsValueRecursively(heuristics.maxdepth);
                if (child.heuristicValue < worstHeuristic) {
                    worstHeuristic = child.heuristicValue;
                }
                System.out.println("unplay :"+unplay);
                core.previousState();
            }
            return worstHeuristic;
        }
    }
}
