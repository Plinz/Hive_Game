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
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;

		List<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Coord coord = tile.getCoord();
			int deep = 0;
			nextBox(coord, list, board, deep);
		}
		this.possibleMovement = list;
		return list;

	}

	private void nextBox(Coord coord, List<Coord> list, Board board, int deep) {
		if (deep++ < 3) {
			if (board.getTile(coord.getEast()).getPiece() == null && !list.contains(coord.getEast())
					&& board.getPieceNeighbors(coord.getEast()).size() != 1
					&& (board.getTile(coord.getSouthEast()).getPiece() == null || board.getTile(coord.getNorthEast()).getPiece() == null)) {
				list.add(coord.getEast());
				nextBox(coord.getEast(), list, board, deep);
			}
			if (board.getTile(coord.getSouthEast()).getPiece() == null && !list.contains(coord.getSouthEast())
					&& board.getPieceNeighbors(coord.getSouthEast()).size() != 1
					&& (board.getTile(coord.getSouthWest()).getPiece() == null || board.getTile(coord.getEast()).getPiece() == null)) {
				list.add(coord.getSouthEast());
				nextBox(coord.getSouthEast(), list, board, deep);
			}
			if (board.getTile(coord.getSouthWest()).getPiece() == null && !list.contains(coord.getSouthWest())
					&& board.getPieceNeighbors(coord.getSouthWest()).size() != 1
					&& (board.getTile(coord.getWest()).getPiece() == null || board.getTile(coord.getSouthEast()).getPiece() == null)) {
				list.add(coord.getSouthWest());
				nextBox(coord.getSouthWest(), list, board, deep);
			}
			if (board.getTile(coord.getWest()).getPiece() == null && !list.contains(coord.getWest())
					&& board.getPieceNeighbors(coord.getWest()).size() != 1
					&& (board.getTile(coord.getNorthWest()).getPiece() == null || board.getTile(coord.getSouthWest()).getPiece() == null)) {
				list.add(coord.getWest());
				nextBox(coord.getWest(), list, board, deep);
			}
			if (board.getTile(coord.getNorthWest()).getPiece() == null && !list.contains(coord.getNorthWest())
					&& board.getPieceNeighbors(coord.getNorthWest()).size() != 1
					&& (board.getTile(coord.getNorthEast()).getPiece() == null || board.getTile(coord.getWest()).getPiece() == null)) {
				list.add(coord.getNorthWest());
				nextBox(coord.getNorthWest(), list, board, deep);
			}
			if (board.getTile(coord.getNorthEast()).getPiece() == null && !list.contains(coord.getNorthEast())
					&& board.getPieceNeighbors(coord.getNorthEast()).size() != 1
					&& (board.getTile(coord.getEast()).getPiece() == null || board.getTile(coord.getNorthWest()).getPiece() == null)) {
				list.add(coord.getNorthEast());
				nextBox(coord.getNorthEast(), list, board, deep);
			}
		}
	}
}
