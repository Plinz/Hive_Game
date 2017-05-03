package main.java.model.piece;

import java.util.ArrayList;
import java.util.List;

import main.java.model.Board;
import main.java.model.Piece;
import main.java.utils.Coord;

public class Grasshopper extends Piece{

	public Grasshopper(String name, String color) {
		super(name, color);
	}

	@Override
	public List<Coord> getPossibleMovement(Coord coord, int floor, Board board) {
		ArrayList<Coord> pos = new ArrayList<Coord>();
		if (board.getTile(coord, floor).isBlocked())
			return pos;
		
		int i = coord.getX();
		int j = coord.getY();
		
		while (board.getTile(new Coord(++i, j)) != null);
		if (i != coord.getX()+1)
			pos.add(new Coord(i, j));
		
		i = coord.getX();
		while (board.getTile(new Coord(i, ++j)) != null);
		if (j != coord.getY()+1)
			pos.add(new Coord(i, j));
		
		j = coord.getY();
		while (board.getTile(new Coord(--i, ++j)) != null);
		if (i != coord.getX()+1 && j != coord.getY()+1)
			pos.add(new Coord(i, j));
		
		i = coord.getX();
		j = coord.getY();
		while (board.getTile(new Coord(--i, j)) != null);
		if (i != coord.getX()+1)
			pos.add(new Coord(i, j));
		
		i = coord.getX();
		while (board.getTile(new Coord(i, --j)) != null);
		if (j != coord.getY()+1)
			pos.add(new Coord(i, j));
		
		j = coord.getY();
		while (board.getTile(new Coord(++i, --j)) != null);
		if (i != coord.getX()+1 && j != coord.getY()+1)
			pos.add(new Coord(i, j));
		
		return pos;
	}

}
