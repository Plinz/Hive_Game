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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import main.java.engine.Core;
import main.java.model.Board;
import main.java.model.Box;
import main.java.model.Column;
import main.java.model.Piece;
import main.java.model.Player;
import main.java.model.Tile;
import main.java.model.piece.Ant;
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
        for (int i = 0 ; i<2 ; i++){
            for (int j=0 ; j < Consts.NB_PIECES_PER_PLAYER ; j++){
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
    
    public void getGeneralValues(){
        resetGeneralValues();
        for (int pieceId = 0 ; pieceId < Consts.NB_PIECES_PER_PLAYER ; pieceId++){
            nbMovesAI += pieces[0][pieceId].nbMoves * pieces[0][pieceId].isInGame;
            nbPlacementAI +=  pieces[0][pieceId].nbMoves * (1-pieces[0][pieceId].isInGame);
            nbPieceInHandAI += 1-pieces[0][pieceId].isInGame;
            nbPieceOnBoardAI += pieces[0][pieceId].isInGame;
            nbPinnedAI += pieces[0][pieceId].isPinned;
            
            nbMovesOpponent += pieces[1][pieceId].nbMoves * pieces[1][pieceId].isInGame;
            nbPlacementOpponent +=  pieces[1][pieceId].nbMoves * (1-pieces[1][pieceId].isInGame);
            nbPieceInHandOpponent += 1-pieces[1][pieceId].isInGame;
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
    
    public boolean isVictory(){
        return isVictory;
    }
    
    public void resetVictory(){
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

    public int getMobility(Player player) {
        int result = 0;
        for (Column column : this.core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if ((tile.getPiece() != null) && (tile.getPiece().getTeam() == player.getTeam())) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public int getValue(int pieceId, int player) {
        int value = 0;
        if (isInHand(pieceId, player)) {
            value += getValuePieceInHand(pieceId, player);
        } else {
            value += getValuePieceOnBoard(pieceId, player);
        }
        return value;
    }

    public boolean isPinned(int PieceId) {

        //bug on the ground (ie not beetle or mosquito)
        for (Column column : this.core.getBoard().getBoard()) {
            for (Box box : column) {
                for (Tile tile : box) {
                    if ((tile.getPiece() != null) && (tile.getPiece().getId() == PieceId)) {
                        if (tile.getPiece().getPossibleMovement(tile, this.core.getBoard()).isEmpty()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInHand(int pieceId, int player) {
        for (Piece piece : core.getPlayers()[player].getInventory()) {
            if (piece.getId() == pieceId) {
                return true;
            }
        }
        return false;
    }

    public void resetValues(){
        resetGeneralValues();
        for (int player = 0; player < 2 ; player ++){
            for (int pieceId = 0 ; pieceId < Consts.NB_PIECES_PER_PLAYER ; pieceId++){
                pieces[player][pieceId].resetValues();
            }
        }
    }
    
    public void resetGeneralValues(){
    nbPieceInHandAI = 0;
    nbPieceInHandOpponent = 0;
    nbPieceOnBoardAI = 0;
    nbPieceOnBoardOpponent =0;
    nbMovesAI = 0;
    nbMovesOpponent = 0;
    nbPlacementAI = 0;
    nbPlacementOpponent = 0;
    nbPinnedAI = 0;
    nbPinnedOpponent = 0;
    }
        
    public boolean OpponentsQueenSurroundedExceptGates(int player)
    {
        if (this.core.getBoard().getNbPieceOnTheBoard() < 0)
            return false;
            
        //finding northest piece of the hive
        Tile northest = new Tile();
        for (Column col : this.core.getBoard().getBoard())
        {
            for (Box box : col)
            {
                for (Tile tile : box)
                {
                    if (tile.getPiece() != null)
                    {
                        northest = tile;
                        break;
                    }
                }
                if (northest.getPiece() != null)
                    break;
            }
            if (northest.getPiece() != null)
                break;
        }

        
        //adding an extra ant at the north of the hive
        Piece extraAnt = new Ant(50, this.core.getPlayers()[player].getTeam());
        this.core.getBoard().addPiece(extraAnt, northest.getCoord().getNorthWest());
        
        List<CoordGene<Integer>> possibleDest = extraAnt.getPossibleMovement(this.core.getBoard().getTile(northest.getCoord().getNorthWest()), this.core.getBoard());
        for (CoordGene<Integer> coords : getTile(Consts.QUEEN, 1 - player).getCoord().getNeighbors())
        {
            Tile neighbor = this.core.getBoard().getTile(coords);
            //if the extra ant can reach a neighbor of the queen 
            if ((neighbor.getPiece() == null) && (possibleDest.contains(neighbor.getCoord())))
                return false;
        }
        //removing extra piece from the board
        this.core.getBoard().removePiece(northest.getCoord().getNorthWest());
        
        return true;
    }
    
    
    public boolean isOpponentsQueenNeighbor(int player, Tile tile)
    {
        for (Tile t : core.getBoard().getNeighbors(getTile(Consts.QUEEN, 1 - player)))
        {
            if (tile.getCoord() == t.getCoord())
                return true;
        }
        return false;
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
    
    public ArrayList<CoordGene<Integer>> grassHopperToOpponentsQueensFreeNeighbor(int player, Tile tile)
    {
        ArrayList<ArrayList<CoordGene<Integer>>> mainList = new ArrayList<>();
        mainList.add(new ArrayList<>(Arrays.asList(tile.getCoord())));
        HashSet<CoordGene<Integer>> markedCoords = new HashSet<>(Arrays.asList(tile.getCoord()));
        do
        {
            for (ArrayList<CoordGene<Integer>> subList : mainList)
            {
                List<CoordGene<Integer>> newDests = new ArrayList<>();
                for (CoordGene<Integer> coord : this.core.getBoard().getTile(subList.get(subList.size()-1)).getPiece().getPossibleMovement(this.core.getBoard().getTile(subList.get(subList.size()-1)), this.core.getBoard()))
                {
                    if (!(markedCoords.contains(coord)))
                    {
                        newDests.add(coord);
                        markedCoords.add(coord);
                    }
                }
                if (newDests.isEmpty())
                    mainList.remove(subList);
                else
                {
                    for (int i = 0; i < newDests.size()-1; i++)
                    {
                        @SuppressWarnings("unchecked")
						ArrayList<CoordGene<Integer>> newList = (ArrayList<CoordGene<Integer>>)subList.clone();
                        newList.add(newDests.get(i));
                        mainList.add(newList);
                        if (isOpponentsQueenNeighbor(player, this.core.getBoard().getTile(newDests.get(i))))
                            return newList;
                    }
                    subList.add(newDests.get(newDests.size()-1));
                    if (isOpponentsQueenNeighbor(player, this.core.getBoard().getTile(subList.get(subList.size()-1))))
                        return subList;
                }  
            }
        }
        while(!(mainList.isEmpty()));   
        //reached only when all possible paths have been visited unsuccessfully
        return null;
    }

	public int distanceToOpposingQueen(Tile tile) {
		if (tile == null || tile.getPiece() == null
				|| (tile.getPiece().getId() != Consts.BEETLE1 && tile.getPiece().getId() != Consts.BEETLE2))
			return -1;
		Board board = core.getBoard();
		int distance = 0;
		List<Tile> neighbors = board.getAboveAndBelow(tile);
		List<Tile> neighbTmp = new ArrayList<>();
		HashSet<CoordGene<Integer>> visited = new HashSet<>();
		List<CoordGene<Integer>> possibleDest = new ArrayList<>();
		List<CoordGene<Integer>> posDestTmp = new ArrayList<>();

		possibleDest.add(tile.getCoord());
		visited.add(tile.getCoord());
		while (neighbors.stream().noneMatch(t -> t != null && t.getPiece() != null
				&& t.getPiece().getId() == Consts.QUEEN && t.getPiece().getTeam() != tile.getPiece().getTeam())) {
			neighbors.clear();
			possibleDest.forEach(c -> {
				posDestTmp.addAll(tile.getPiece().updatePossibleMovement(board.getTile(c), board).stream()
						.filter(co -> !visited.contains(co)).collect(Collectors.toList()));
				neighbors.addAll(board.getPieceNeighbors(c));
			});
			neighbors.forEach(t -> neighbTmp.addAll(board.getAboveAndBelow(t)));
			neighbors.addAll(neighbTmp);
			possibleDest = posDestTmp;
			posDestTmp.clear();
			visited.addAll(possibleDest);
			distance++;
		}
		return distance;
	}
}
