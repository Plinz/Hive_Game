package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.model.Tile;
import main.java.utils.Coord;

public class Grasshopper extends Piece{

	public Grasshopper(int team) {
		super("Grasshopper", team, "Grasshopper description");

	}

	@Override
	public List<Coord> getPossibleMovement(Tile tile, Board board) {
		ArrayList<Coord> pos = new ArrayList<Coord>();
		if (tile.isBlocked())
			return pos;
		
		Tile tmp;
		Coord coord = tile.getCoord();
		while ((tmp = board.getTile(coord.getEast())) != null)
			coord = tmp.getCoord();
		addCoord(coord, tile, pos);
		
		coord = tile.getCoord();
		while ((tmp = board.getTile(coord.getSouthEast())) != null)
			coord = tmp.getCoord();
		addCoord(coord, tile, pos);
		
		coord = tile.getCoord();
		while ((tmp = board.getTile(coord.getSouthWest())) != null)
			coord = tmp.getCoord();
		addCoord(coord, tile, pos);
		
		coord = tile.getCoord();
		while ((tmp = board.getTile(coord.getWest())) != null)
			coord = tmp.getCoord();
		addCoord(coord, tile, pos);
		
		coord = tile.getCoord();
		while ((tmp = board.getTile(coord.getNorthWest())) != null)
			coord = tmp.getCoord();
		addCoord(coord, tile, pos);
		
		coord = tile.getCoord();
		while ((tmp = board.getTile(coord.getNorthEast())) != null)
			coord = tmp.getCoord();
		addCoord(coord, tile, pos);
		
		return pos;
	}
	
	private void addCoord(Coord coord, Tile tile, List<Coord> list){
		if (! coord.equals(tile))
			list.add(new Coord(coord.getX(), coord.getY()));
	
	}

}
