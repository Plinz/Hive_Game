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
			if (board.getTile(coord.getEast()) == null && !list.contains(coord.getEast())
					&& board.getNeighbors(coord.getEast()).size() != 1
					&& (board.getTile(coord.getSouthEast()) == null || board.getTile(coord.getNorthEast()) == null)) {
				list.add(coord.getEast());
				nextBox(coord.getEast(), list, board, deep);
			}
			if (board.getTile(coord.getSouthEast()) == null && !list.contains(coord.getSouthEast())
					&& board.getNeighbors(coord.getSouthEast()).size() != 1
					&& (board.getTile(coord.getSouthWest()) == null || board.getTile(coord.getEast()) == null)) {
				list.add(coord.getSouthEast());
				nextBox(coord.getSouthEast(), list, board, deep);
			}
			if (board.getTile(coord.getSouthWest()) == null && !list.contains(coord.getSouthWest())
					&& board.getNeighbors(coord.getSouthWest()).size() != 1
					&& (board.getTile(coord.getWest()) == null || board.getTile(coord.getSouthEast()) == null)) {
				list.add(coord.getSouthWest());
				nextBox(coord.getSouthWest(), list, board, deep);
			}
			if (board.getTile(coord.getWest()) == null && !list.contains(coord.getWest())
					&& board.getNeighbors(coord.getWest()).size() != 1
					&& (board.getTile(coord.getNorthWest()) == null || board.getTile(coord.getSouthWest()) == null)) {
				list.add(coord.getWest());
				nextBox(coord.getWest(), list, board, deep);
			}
			if (board.getTile(coord.getNorthWest()) == null && !list.contains(coord.getNorthWest())
					&& board.getNeighbors(coord.getNorthWest()).size() != 1
					&& (board.getTile(coord.getNorthEast()) == null || board.getTile(coord.getWest()) == null)) {
				list.add(coord.getNorthWest());
				nextBox(coord.getNorthWest(), list, board, deep);
			}
			if (board.getTile(coord.getNorthEast()) == null && !list.contains(coord.getNorthEast())
					&& board.getNeighbors(coord.getNorthEast()).size() != 1
					&& (board.getTile(coord.getEast()) == null || board.getTile(coord.getNorthWest()) == null)) {
				list.add(coord.getNorthEast());
				nextBox(coord.getNorthEast(), list, board, deep);
			}
		}
	}
}
