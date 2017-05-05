package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Consts;
import main.java.utils.Coord;

public class Ant extends Piece {

	public Ant(int team) {
		super(Consts.ANT_NAME, team, "Fourmi description");
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;

		List<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Coord coord = tile.getCoord();

			List<Coord> neighbors = coord.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) {
				Coord curr = neighbors.get(i);
				Coord prev = i == 0 ? neighbors.get(neighbors.size() - 1) : neighbors.get(i - 1);
				Coord next = i == neighbors.size() - 1 ? neighbors.get(0) : neighbors.get(i + 1);
				if (board.getTile(curr).getPiece() == null && board.getPieceNeighbors(curr).size() != 1
						&& (board.getTile(prev).getPiece() == null || board.getTile(next).getPiece() == null)) {
					while (curr != null && !curr.equals(tile.getCoord())) {
						System.err.println("coord=" + curr);
						list.add(curr);
						curr = nextBox(curr, board, list, tile);
					}
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
							|| board.getTile(prev).getPiece() == null || board.getTile(next).getPiece() == null)
					&& !curr.getNeighbors().contains(tile.getCoord()))
				return curr;
		}
		return null;
	}
}
