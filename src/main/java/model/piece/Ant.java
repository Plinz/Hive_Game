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
			coord = nextBox(coord, board);
			while (coord != null && !coord.equals(tile.getCoord())) {
				list.add(coord);
				coord = nextBox(coord, board);
			}
		}
		this.possibleMovement = list;
		return list;

	}

	private Coord nextBox(Coord coord, Board board) {
		if (board.getTile(coord.getEast()) == null && board.getPieceNeighbors(coord.getEast()).size() != 1
				&& (board.getTile(coord.getSouthEast()).getPiece() == null || board.getTile(coord.getNorthEast()).getPiece() == null))
			return coord.getEast();
		if (board.getTile(coord.getSouthEast()) == null && board.getPieceNeighbors(coord.getSouthEast()).size() != 1
				&& (board.getTile(coord.getSouthWest()).getPiece() == null || board.getTile(coord.getEast()).getPiece() == null))
			return coord.getSouthEast();
		if (board.getTile(coord.getSouthWest()) == null && board.getPieceNeighbors(coord.getSouthWest()).size() != 1
				&& (board.getTile(coord.getWest()).getPiece() == null || board.getTile(coord.getSouthEast()).getPiece() == null))
			return coord.getSouthWest();
		if (board.getTile(coord.getWest()) == null && board.getPieceNeighbors(coord.getWest()).size() != 1
				&& (board.getTile(coord.getNorthWest()).getPiece() == null || board.getTile(coord.getSouthWest()).getPiece() == null))
			return coord.getWest();
		if (board.getTile(coord.getNorthWest()) == null && board.getPieceNeighbors(coord.getNorthWest()).size() != 1
				&& (board.getTile(coord.getNorthEast()).getPiece() == null || board.getTile(coord.getWest()).getPiece() == null))
			return coord.getNorthWest();
		if (board.getTile(coord.getNorthEast()) == null && board.getPieceNeighbors(coord.getNorthEast()).size() != 1
				&& (board.getTile(coord.getEast()).getPiece() == null || board.getTile(coord.getNorthWest()).getPiece() == null))
			return coord.getNorthEast();
		return coord;
	}
}
