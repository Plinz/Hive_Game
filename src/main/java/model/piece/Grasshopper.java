package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Grasshopper extends Piece {

	public Grasshopper(int team) {
		super("Grasshopper", team, "Grasshopper description");

	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		if (this.possibleMovement != null)
			return this.possibleMovement;
		ArrayList<Coord> pos = new ArrayList<Coord>();
		if (!tile.isBlocked()) {
			Tile tmp;
			Coord coord = tile.getCoord();
			List<Coord> neighbors = coord.getNeighbors();
			while ((tmp = board.getTile(coord.getEast())).getPiece() != null)
				coord = tmp.getCoord();
			if (!neighbors.contains(coord))
				addCoord(coord, tile, pos);

			coord = tile.getCoord();
			while ((tmp = board.getTile(coord.getSouthEast())).getPiece() != null)
				coord = tmp.getCoord();
			if (!neighbors.contains(coord))
				addCoord(coord, tile, pos);

			coord = tile.getCoord();
			while ((tmp = board.getTile(coord.getSouthWest())).getPiece()!= null)
				coord = tmp.getCoord();
			if (!neighbors.contains(coord))
				addCoord(coord, tile, pos);

			coord = tile.getCoord();
			while ((tmp = board.getTile(coord.getWest())).getPiece() != null)
				coord = tmp.getCoord();
			if (!neighbors.contains(coord))
				addCoord(coord, tile, pos);

			coord = tile.getCoord();
			while ((tmp = board.getTile(coord.getNorthWest())).getPiece() != null)
				coord = tmp.getCoord();
			if (!neighbors.contains(coord))
				addCoord(coord, tile, pos);

			coord = tile.getCoord();
			while ((tmp = board.getTile(coord.getNorthEast())).getPiece() != null)
				coord = tmp.getCoord();
			if (!neighbors.contains(coord))
				addCoord(coord, tile, pos);
		}
		this.possibleMovement = pos;
		return pos;
	}

	private void addCoord(Coord coord, Tile tile, List<Coord> list) {
		if (!coord.equals(tile))
			list.add(new Coord(coord.getX(), coord.getY()));

	}

}
