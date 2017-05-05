package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.Coord;

public class Spider extends Piece {

	public Spider(int team) {
		super(Consts.SPIDER_NAME, team, "Spider description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		List<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Coord coord = tile.getCoord();
			int deep = 0;
			List<Coord> neighbors = coord.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				Coord curr = neighbors.get(i);
				Coord prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
				Coord next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);

				if (board.getTile(curr).getPiece() == null && board.getPieceNeighbors(curr).size() != 1
						&& ((board.getTile(prev).getPiece() != null & board.getTile(next).getPiece() == null)
								|| (board.getTile(prev).getPiece() == null & board.getTile(next).getPiece() != null))) {
					list.add(curr);
					while (deep++ < 2 && curr != null && !curr.equals(tile.getCoord())){
						curr = nextBox(curr, board, list, tile);
						System.err.println(curr);
					}
					deep = 0;
					list.add(curr);
				}
			}
		}
		this.possibleMovement = list;
		return list;

	}

	private Coord nextBox(Coord coord, Board board, List<Coord> list, Tile tile) {
		List<Coord> neighbors = coord.getNeighbors();
		for (int i = 0; i < neighbors.size(); i++) {
			Coord curr = neighbors.get(i);
			Coord prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
			Coord next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);
			if (!list.contains(curr) && board.getTile(curr) != null && board.getTile(curr).getPiece() == null
					&& board.getPieceNeighbors(curr).size() != 0
					&& (board.getTile(prev) == null || board.getTile(next) == null
							|| ((board.getTile(prev).getPiece() != null & board.getTile(next).getPiece() == null)
									|| (board.getTile(prev).getPiece() == null
											& board.getTile(next).getPiece() != null)))
					&& !curr.getNeighbors().contains(tile.getCoord()))
				return curr;
		}
		return null;
	}

	// List<Coord> list = new ArrayList<Coord>();
	// if (!tile.isBlocked()) {
	// Coord coord = tile.getCoord();
	// int deep = 0;
	// nextBox(coord, list, board, deep);
	// }
	// this.possibleMovement = list;
	// return list;
	//
	// }
	//
	// private void nextBox(Coord coord, List<Coord> list, Board board, int
	// deep) {
	// if (deep++ < 3) {
	// List<Coord> neighbors = coord.getNeighbors();
	// for (int i=0; i<neighbors.size(); i++){
	// Coord curr = neighbors.get(i);
	// Coord prev = i==0 ? neighbors.get(neighbors.size()-1) :
	// neighbors.get(i-1);
	// Coord next = i==neighbors.size()-1 ? neighbors.get(0) :
	// neighbors.get(i+1);
	// if (board.getTile(curr) != null && board.getTile(curr).getPiece() == null
	// && !list.contains(curr)
	// && board.getPieceNeighbors(curr).size() != 1
	// && (board.getTile(prev).getPiece() == null ||
	// board.getTile(next).getPiece() == null)){
	// list.add(curr);
	// nextBox(curr, list, board, deep);
	// }
	// }
	// }
	// }
}
