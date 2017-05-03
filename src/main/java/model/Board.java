package main.java.model;

import java.util.ArrayList;
import java.util.List;

import main.java.utils.Coord;

public class Board {
	
	private List<ArrayList<Tile>> board;
	
	public Board(){
		this.board = new ArrayList<ArrayList<Tile>>(24);
	}
	
	public Board(List<ArrayList<Tile>> board) {
		this.board = board;
	}

	public List<ArrayList<Tile>> getBoard() {
		return board;
	}

	public void setBoard(List<ArrayList<Tile>> board) {
		this.board = board;
	}
	
	public Tile getTile(Coord coord){
		Tile ret = null;
		if(this.board.size() > coord.getX()){
			List<Tile> list = this.board.get(coord.getX());
			for(Tile tile : list){
				if(tile.getY() == coord.getY()){
					if (ret == null || ret.getZ()< tile.getZ())
						ret = tile;
				}
			}
		}
		return ret;
	}
	
	public Tile getTile(Coord coord, int floor){
		Tile ret = null;
		if(this.board.size() > coord.getX()){
			List<Tile> list = this.board.get(coord.getX());
			for(Tile tile : list){
				if(tile.getY() == coord.getY() && tile.getZ() == floor){
					ret = tile;
				}
			}
		}
		return ret;
	}
	
	public void resize(int xOffset, int yOffset){
		for(List<Tile> list : this.board){
			for(Tile tile : list){
				tile.setX(tile.getX()+xOffset);
				tile.setY(tile.getY()+yOffset);
			}
		}
	}
	
	public void addPiece(Piece piece, Coord coord){
		Tile tile = new Tile(piece, false, coord.getX(), coord.getY(), 0);
		List<Tile> column = this.board.get(coord.getX());
		if (column == null)
			column = new ArrayList<Tile>();
		column.add(tile);

		boolean resize = false;
		int x = 0;
		int y = 0;
		if (coord.getX() == 0){
			resize = true;
			x = 1;
			this.board.remove(23);
			this.board.add(0, null);
		} else if (coord.getX() == 23){
			resize = true;
			x = -1;
			this.board.remove(0);
			this.board.add(null);
		}
		if (coord.getY() == 0){
			resize = true;
			y = 1;
		} else if (coord.getY() == 23){
			resize = true;
			y = -1;
		}
		if (resize)
			this.resize(x, y);
	}
	
	//TODO
	public Piece removePiece(Coord coord){
		return null;
	}
	
	//TODO
	public void movePiece(Coord coordSource, Coord coordTarget){
		
	}
	
//	//TODO
//	public boolean accepte(Visitor v){
//		v.visit(this);
//	}

}
