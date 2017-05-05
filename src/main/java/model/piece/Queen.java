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
			if (board.getTile(coord.getEast()) == null && board.getNeighbors(coord.getEast()).size() != 1
					&& (board.getTile(coord.getSouthEast()) == null || board.getTile(coord.getNorthEast()) == null))
				list.add(coord.getEast());
			if (board.getTile(coord.getSouthEast()) == null && board.getNeighbors(coord.getSouthEast()).size() != 1
					&& (board.getTile(coord.getSouthWest()) == null || board.getTile(coord.getEast()) == null))
				list.add(coord.getSouthEast());
			if (board.getTile(coord.getSouthWest()) == null && board.getNeighbors(coord.getSouthWest()).size() != 1
					&& (board.getTile(coord.getWest()) == null || board.getTile(coord.getSouthEast()) == null))
				list.add(coord.getSouthWest());
			if (board.getTile(coord.getWest()) == null && board.getNeighbors(coord.getWest()).size() != 1
					&& (board.getTile(coord.getNorthWest()) == null || board.getTile(coord.getSouthWest()) == null))
				list.add(coord.getWest());
			if (board.getTile(coord.getNorthWest()) == null && board.getNeighbors(coord.getNorthWest()).size() != 1
					&& (board.getTile(coord.getNorthEast()) == null || board.getTile(coord.getWest()) == null))
				list.add(coord.getNorthWest());
			if (board.getTile(coord.getNorthEast()) == null && board.getNeighbors(coord.getNorthEast()).size() != 1
					&& (board.getTile(coord.getEast()) == null || board.getTile(coord.getNorthWest()) == null))
				list.add(coord.getNorthEast());
		}
		this.possibleMovement = list;
		return list;
	}

}
