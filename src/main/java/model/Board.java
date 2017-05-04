package main.java.model;

import java.util.ArrayList;
import java.util.List;

import main.java.utils.Coord;
import main.java.view.BoardDrawer;

public class Board {
	
	private List<List<List<Tile>>> board;
	
	public Board(){
		this.board = new ArrayList<List<List<Tile>>>();
		Tile first = new Tile(0,0,0);
		List<Tile> box = new ArrayList<Tile>();
		box.add(first);
		List<List<Tile>> column = new ArrayList<List<Tile>>();
		column.add(box);
		this.board.add(column);
	}
      
	public Board(Board b) {
		this.board = new ArrayList<List<List<Tile>>>();
		for (List<List<Tile>> column : b.getBoard()){
			List<List<Tile>> newColumn = new ArrayList<List<Tile>>();
			for (List<Tile> box : column){
				List<Tile> newBox = new ArrayList<Tile>();
				for (Tile tile : box)
					newBox.add(new Tile(tile));
				newColumn.add(newBox);
			}
			this.board.add(newColumn);
		}
	}
        

	public List<List<List<Tile>>> getBoard() {
		return board;
	}

	public void setBoard(List<List<List<Tile>>> board) {
		this.board = board;
	}

    public boolean accept(BoardDrawer b){
        b.visit(this);
        return false;
    }
	
	public Tile getTile(Coord coord){
		Tile ret = null;
		List<List<Tile>> column;
		if((column = this.board.get(coord.getX())).size() != 0){
			List<Tile> box = new ArrayList<Tile>();
			if (column.get(coord.getY()).size() != 0){
				for (Tile t: box){
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
				}
			}
		}
		return ret;
	}
	
	public Tile getTile(Coord coord, int floor){
		Tile ret = null;
		List<List<Tile>> column;
		if((column = this.board.get(coord.getX())).size() != 0){
			List<Tile> box = new ArrayList<Tile>();
			if (column.get(coord.getY()).size() >= floor+1)
				ret = box.get(floor);
		}
		return ret;
	}
	
	public void resize(int xOffset, int yOffset){
		for(List<List<Tile>> column : this.board){
			for(List<Tile> box : column){
				for(Tile tile : box){
					tile.setX(tile.getX()+xOffset);
					tile.setY(tile.getY()+yOffset);
				}
			}
		}
	}
	
	public void addPiece(Piece piece, Coord coord){
		Tile tile = new Tile(piece, false, coord.getX(), coord.getY(), 0);
		List<Tile> box = this.board.get(coord.getX()).get(coord.getY());
		box.add(tile);

		boolean resize = false;
		int x = 0;
		int y = 0;
		if (coord.getX() == 0){
			resize = true;
			x = 1;
			List<List<Tile>> column = new ArrayList<List<Tile>>();
			this.board.add(0, column);
		} if (coord.getX()+1 == this.board.size()){
			List<List<Tile>> column = new ArrayList<List<Tile>>();
			this.board.add(column);
		} if (coord.getY() == 0){
			resize = true;
			y = 1;
			for (List<List<Tile>> column : this.board)
				column.add(0, new ArrayList<Tile>());
		}
		if (resize){
			this.resize(x, y);
			while(this.board.get(tile.getX()+1).size() < tile.getY()+1)
				this.board.get(tile.getX()+1).add(new ArrayList<Tile>());
			while(this.board.get(tile.getX()-1).size() < tile.getY()+2)
				this.board.get(tile.getX()-1).add(new ArrayList<Tile>());
			if(this.board.get(tile.getX()).size() < tile.getY()+2)
				this.board.get(tile.getX()+1).add(new ArrayList<Tile>());
			if(this.board.get(tile.getX()+1).get(tile.getY()).size() == 0)
				this.board.get(tile.getX()+1).get(tile.getY()).add(new Tile(tile.getX()+1, tile.getY(), 0));
			if(this.board.get(tile.getX()).get(tile.getY()+1).size() == 0)
				this.board.get(tile.getX()).get(tile.getY()+1).add(new Tile(tile.getX(), tile.getY()+1, 0));
			if(this.board.get(tile.getX()-1).get(tile.getY()+1).size() == 0)
				this.board.get(tile.getX()-1).get(tile.getY()+1).add(new Tile(tile.getX()-1, tile.getY()+1, 0));
			if(this.board.get(tile.getX()-1).get(tile.getY()).size() == 0)
				this.board.get(tile.getX()-1).get(tile.getY()).add(new Tile(tile.getX()-1, tile.getY(), 0));
			if(this.board.get(tile.getX()).get(tile.getY()-1).size() == 0)
				this.board.get(tile.getX()).get(tile.getY()-1).add(new Tile(tile.getX(), tile.getY()-1, 0));
			if(this.board.get(tile.getX()+1).get(tile.getY()-1).size() == 0)
				this.board.get(tile.getX()+1).get(tile.getY()-1).add(new Tile(tile.getX()+1, tile.getY()-1, 0));
		}
	}
	
	public Piece removePiece(Coord coord){
		Piece piece = null;
		List<Tile> box = this.board.get(coord.getX()).get(coord.getY());
		Tile tile = null;
		for (Tile tmp : box)
			if (tile == null || tmp.getZ() > tile.getZ())
				tile = tmp;
		
		if (tile != null){
			piece = tile.getPiece();
			if (box.size() == 1){
				tile.setPiece(null);
				this.updateNeighbors(tile);
				checkBoardSize(tile.getCoord());
			}
			else
				box.remove(tile);
		}
		
		return piece;
			
	}
	
	public void movePiece(Coord coordSource, Coord coordTarget){
		Piece piece = this.removePiece(coordSource);
		this.addPiece(piece, coordTarget);
	}
	
	private void checkBoardSize(Coord coord){
		boolean isEmpty = true;
		boolean resize = false;
		int x = 0;
		int y = 0;
		
		if (coord.getX() == 1){
			for (List<Tile> box : this.board.get(0)){
				if(box.size() != 0){
					isEmpty = false;
					break;
				}
			}
			if (this.board.get(0).size() == 0 || isEmpty){
				resize = true;
				x = -1;
				this.board.remove(0);
			} else
				isEmpty = true;
		}
		if (coord.getX() == this.board.size()-2){		
			for (List<Tile> box : this.board.get(this.board.size()-1)){
				if(box.size() != 0){
					isEmpty = false;
					break;
				}
			}
			if (this.board.get(this.board.size()-1).size() == 0 || isEmpty)
				this.board.remove(this.board.size()-1);
			else
				isEmpty = true;
		}
		if (coord.getY() == 1){
			for(List<List<Tile>> column : this.board){
				if(!column.isEmpty() && column.get(0).size() != 0){
					isEmpty = false;
					break;
				}
			}
			if (isEmpty){
				resize = true;
				y = -1;
				for(List<List<Tile>> column : this.board)
					column.remove(0);
			}
		}
		if (coord.getY()+2 == this.board.get(coord.getX()).size()){
			for(List<List<Tile>> column : this.board)
				if(column.get(column.size()-1).size() == 0)
					column.remove(column.size()-1);
				
		}
		if (resize)
			this.resize(x, y);
	}
	
	private void updateNeighbors(Tile tile){
		List<Tile> neighbors = this.getNeighbors(tile);
		for (Tile neighbor : neighbors){
			if (neighbor.getPiece() == null){
				List<Tile> subNeighbors = this.getNeighbors(neighbor);
				boolean hasPieceAround = false;
				for (Tile subNeighbor : subNeighbors){
					if (subNeighbor.getPiece() != null){
						hasPieceAround = true;
						break;
					}
				}
				if (!hasPieceAround){
					this.board.get(neighbor.getX()).get(neighbor.getY()).clear();
				}
			}
			
		}
	}
	
	public List<Tile> getNeighbors(Coord coord){
		List<Tile> list = new ArrayList<Tile>();
		list.add(this.getEast(coord));
		list.add(this.getNorthEast(coord));
		list.add(this.getNorthWest(coord));
		list.add(this.getSouthEast(coord));
		list.add(this.getSouthWest(coord));
		list.add(this.getWest(coord));
		return list;
	}
	
	public List<Tile> getNeighbors(Tile tile){
		List<Tile> list = new ArrayList<Tile>();
		list.add(this.getEast(tile.getCoord()));
		list.add(this.getNorthEast(tile.getCoord()));
		list.add(this.getNorthWest(tile.getCoord()));
		list.add(this.getSouthEast(tile.getCoord()));
		list.add(this.getSouthWest(tile.getCoord()));
		list.add(this.getWest(tile.getCoord()));
		list.addAll(this.getAboveAndBelow(tile));
		return list;
	}
	
	public List<Tile> getAboveAndBelow(Tile tile){
		List<Tile> box = this.board.get(tile.getX()).get(tile.getY());
		List<Tile> list = new ArrayList<Tile>();
		for(Tile t : box)
			if (tile.getZ() != t.getZ())
				list.add(t);
		return list;
	}
	
	public Tile getEast(Coord coord){
		Tile ret = null;
		if(coord.getX()+1 > 0 && coord.getX()+1 < this.board.size())
			if (coord.getY() > 0 && coord.getY() < this.board.get(coord.getX()+1).size())
				for(Tile t : this.board.get(coord.getX()+1).get(coord.getY()))
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
		return ret;
	}
	
	public Tile getSouthEast(Coord coord){
		Tile ret = null;
		if(coord.getX() > 0 && coord.getX() < this.board.size())
			if (coord.getY()+1 > 0 && coord.getY()+1 < this.board.get(coord.getX()).size())
				for(Tile t : this.board.get(coord.getX()).get(coord.getY()+1))
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
		return ret;
	}
	
	public Tile getSouthWest(Coord coord){
		Tile ret = null;
		if(coord.getX()-1 > 0 && coord.getX()-1 < this.board.size())
			if (coord.getY()+1 > 0 && coord.getY()+1 < this.board.get(coord.getX()-1).size())
				for(Tile t : this.board.get(coord.getX()-1).get(coord.getY()+1))
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
		return ret;
	}
	
	public Tile getWest(Coord coord){
		Tile ret = null;
		if(coord.getX()-1 > 0 && coord.getX()-1 < this.board.size())
			if (coord.getY() > 0 && coord.getY() < this.board.get(coord.getX()-1).size())
				for(Tile t : this.board.get(coord.getX()-1).get(coord.getY()))
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
		return ret;
	}
	
	public Tile getNorthWest(Coord coord){
		Tile ret = null;
		if(coord.getX() > 0 && coord.getX() < this.board.size())
			if (coord.getY()-1 > 0 && coord.getY()-1 < this.board.get(coord.getX()).size())
				for(Tile t : this.board.get(coord.getX()).get(coord.getY()-1))
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
		return ret;
	}
	
	public Tile getNorthEast(Coord coord){
		Tile ret = null;
		if(coord.getX()+1 > 0 && coord.getX()+1 < this.board.size())
			if (coord.getY()-1 > 0 && coord.getY()-1 < this.board.get(coord.getX()+1).size())
				for(Tile t : this.board.get(coord.getX()+1).get(coord.getY()-1))
					if (ret == null || ret.getZ() < t.getZ())
						ret = t;
		return ret;
	}

}
