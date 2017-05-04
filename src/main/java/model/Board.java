package main.java.model;

import java.util.ArrayList;
import java.util.List;

import main.java.utils.Coord;

public class Board {
	
	private List<ArrayList<Tile>> board;
	
	public Board(){
		this.board = new ArrayList<ArrayList<Tile>>(24);
	}
	
	public Board(Board b) {
		this.board = new ArrayList<ArrayList<Tile>>(24);
		for (ArrayList<Tile> list : b.getBoard()){
			if (list != null){
				ArrayList<Tile> tmp = new ArrayList<Tile>();
				for(Tile t : list){
					tmp.add(new Tile(t));
				}
				this.board.add(tmp);
			}
		}
	}

	public List<ArrayList<Tile>> getBoard() {
		return board;
	}

	public void setBoard(List<ArrayList<Tile>> board) {
		this.board = board;
	}
	
	public List<Tile> getNeighbors(Tile tile){
		List<Tile> list = new ArrayList<Tile>();
		list.add(this.getEast(tile));
		list.add(this.getNorthEast(tile));
		list.add(this.getNorthWest(tile));
		list.add(this.getSouthEast(tile));
		list.add(this.getSouthWest(tile));
		list.add(this.getWest(tile));
		list.addAll(this.getAboveAndBelow(tile));
		return list;
	}
	
	public List<Tile> getAboveAndBelow(Tile tile){
		List<Tile> column = this.board.get(tile.getX());
		List<Tile> list = new ArrayList<Tile>();
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY() && tile.getZ() != t.getZ()){
					list.add(t);
				}
			}
		}
		return list;
	}
	
	public Tile getEast(Tile tile){
		List<Tile> column = this.board.get(tile.getX()+1);
		Tile ret = null;
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY() && (ret == null || ret.getZ() < t.getZ())){
					ret = t;
				}
			}
		}
		return ret;
	}
	
	public Tile getSouthEast(Tile tile){
		List<Tile> column = this.board.get(tile.getX());
		Tile ret = null;
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY()+1 && (ret == null || ret.getZ() < t.getZ())){
					ret = t;
				}
			}
		}
		return ret;
	}
	
	public Tile getSouthWest(Tile tile){
		List<Tile> column = this.board.get(tile.getX()-1);
		Tile ret = null;
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY()+1 && (ret == null || ret.getZ() < t.getZ())){
					ret = t;
				}
			}
		}
		return ret;
	}
	
	public Tile getWest(Tile tile){
		List<Tile> column = this.board.get(tile.getX()-1);
		Tile ret = null;
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY() && (ret == null || ret.getZ() < t.getZ())){
					ret = t;
				}
			}
		}
		return ret;
	}
	
	public Tile getNorthWest(Tile tile){
		List<Tile> column = this.board.get(tile.getX());
		Tile ret = null;
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY()-1 && (ret == null || ret.getZ() < t.getZ())){
					ret = t;
				}
			}
		}
		return ret;
	}
	
	public Tile getNorthEast(Tile tile){
		List<Tile> column = this.board.get(tile.getX()+1);
		Tile ret = null;
		if (column != null){
			for(Tile t : column){
				if (t.getY() == tile.getY()-1 && (ret == null || ret.getZ() < t.getZ())){
					ret = t;
				}
			}
		}
		return ret;
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
			this.board.add(0, null);
		} else if (coord.getX() == 23){
			resize = true;
			x = -1;
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
	
	public Piece removePiece(Coord coord){
		Piece piece = null;
		if(this.board.size() > coord.getX()){
			List<Tile> list = this.board.get(coord.getX());
			Tile t = null;
			for(Tile tile : list){
				if(tile.getY() == coord.getY()){
					if (t == null || t.getZ()< tile.getZ())
						t = tile;
				}
			}
			if (t != null)
				piece = t.getPiece();
			list.remove(t);
		}
		return piece;
			
	}
	
	public void movePiece(Coord coordSource, Coord coordTarget){
		Piece piece = this.removePiece(coordSource);
		this.addPiece(piece, coordTarget);
	}
	
//	//TODO
//	public boolean accepte(Visitor v){
//		v.visit(this);
//	}

}
