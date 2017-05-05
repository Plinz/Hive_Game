package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Queen extends Piece {

	public Queen(int team) {
		super("Queen", team, "Queen description");

		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;

		List<Coord> list = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Coord coord = tile.getCoord();
			if (board.getTile(coord.getEast()).getPiece() == null && board.getNeighbors(coord.getEast()).size() != 1
					&& (board.getTile(coord.getSouthEast()).getPiece() == null || board.getTile(coord.getNorthEast()).getPiece() == null))
				list.add(coord.getEast());
			if (board.getTile(coord.getSouthEast()).getPiece() == null && board.getNeighbors(coord.getSouthEast()).size() != 1
					&& (board.getTile(coord.getSouthWest()).getPiece() == null || board.getTile(coord.getEast()).getPiece() == null))
				list.add(coord.getSouthEast());
			if (board.getTile(coord.getSouthWest()).getPiece() == null && board.getNeighbors(coord.getSouthWest()).size() != 1
					&& (board.getTile(coord.getWest()).getPiece() == null || board.getTile(coord.getSouthEast()).getPiece() == null))
				list.add(coord.getSouthWest());
			if (board.getTile(coord.getWest()).getPiece() == null && board.getNeighbors(coord.getWest()).size() != 1
					&& (board.getTile(coord.getNorthWest()).getPiece() == null || board.getTile(coord.getSouthWest()).getPiece() == null))
				list.add(coord.getWest());
			if (board.getTile(coord.getNorthWest()).getPiece() == null && board.getNeighbors(coord.getNorthWest()).size() != 1
					&& (board.getTile(coord.getNorthEast()).getPiece() == null || board.getTile(coord.getWest()).getPiece() == null))
				list.add(coord.getNorthWest());
			if (board.getTile(coord.getNorthEast()).getPiece() == null && board.getNeighbors(coord.getNorthEast()).size() != 1
					&& (board.getTile(coord.getEast()).getPiece() == null || board.getTile(coord.getNorthWest()).getPiece() == null))
				list.add(coord.getNorthEast());
		}
		this.possibleMovement = list;
		return list;
	}

}
