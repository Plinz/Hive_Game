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
		if (board.getTile(coord.getEast()) == null && board.getNeighbors(coord.getEast()).size() != 1
				&& (board.getTile(coord.getSouthEast()) == null || board.getTile(coord.getNorthEast()) == null))
			return coord.getEast();
		if (board.getTile(coord.getSouthEast()) == null && board.getNeighbors(coord.getSouthEast()).size() != 1
				&& (board.getTile(coord.getSouthWest()) == null || board.getTile(coord.getEast()) == null))
			return coord.getSouthEast();
		if (board.getTile(coord.getSouthWest()) == null && board.getNeighbors(coord.getSouthWest()).size() != 1
				&& (board.getTile(coord.getWest()) == null || board.getTile(coord.getSouthEast()) == null))
			return coord.getSouthWest();
		if (board.getTile(coord.getWest()) == null && board.getNeighbors(coord.getWest()).size() != 1
				&& (board.getTile(coord.getNorthWest()) == null || board.getTile(coord.getSouthWest()) == null))
			return coord.getWest();
		if (board.getTile(coord.getNorthWest()) == null && board.getNeighbors(coord.getNorthWest()).size() != 1
				&& (board.getTile(coord.getNorthEast()) == null || board.getTile(coord.getWest()) == null))
			return coord.getNorthWest();
		if (board.getTile(coord.getNorthEast()) == null && board.getNeighbors(coord.getNorthEast()).size() != 1
				&& (board.getTile(coord.getEast()) == null || board.getTile(coord.getNorthWest()) == null))
			return coord.getNorthEast();
		return coord;
	}
}
