package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.utils.Coord;
import main.java.view.BoardDrawer;

public class Board implements Serializable{
	
	private static final long serialVersionUID = -9006505176631946800L;
	private List<List<List<Tile>>> board;
	private int nbPieceOnTheBoard;
	
	public Board(){
		this.board = new ArrayList<List<List<Tile>>>();
		Tile first = new Tile(0,0,0);
		List<Tile> box = new ArrayList<Tile>();
		box.add(first);
		List<List<Tile>> column = new ArrayList<List<Tile>>();
		column.add(box);
		this.board.add(column);
		this.nbPieceOnTheBoard = 0;
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
		this.nbPieceOnTheBoard = b.getNbPieceOnTheBoard();
	}
        

	public List<List<List<Tile>>> getBoard() {
		return board;
	}

	public void setBoard(List<List<List<Tile>>> board) {
		this.board = board;
	}

    public int getNbPieceOnTheBoard() {
		return nbPieceOnTheBoard;
	}

	public void setNbPieceOnTheBoard(int nbPieceOnTheBoard) {
		this.nbPieceOnTheBoard = nbPieceOnTheBoard;
	}

	public boolean accept(BoardDrawer b){
         b.visit(this);
        for(int i = 0;i<this.board.size();i++){
            for(int j = 0;j<this.board.get(i).size();j++){
                int taille = this.board.get(i).get(j).size();
                if(  taille != 0){
                    this.board.get(i).get(j).get(taille-1).accept(b);
                    
                }
            }
       }
         return false;
     }
	
	public Tile getTile(Coord coord){
		Tile ret = null;
		if(coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < this.board.size() && coord.getY() < this.board.get(coord.getX()).size())
			for(Tile t : this.board.get(coord.getX()).get(coord.getY()))
				if (ret == null || ret.getZ() < t.getZ())
					ret = t;
		return ret;
	}
	
	public Tile getTile(Coord coord, int floor){
		if(coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < this.board.size() && coord.getY() < this.board.get(coord.getX()).size())
			for(Tile t : this.board.get(coord.getX()).get(coord.getY()))
				if (t.getZ() == floor)
					return t;
		return null;
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
		Tile added = null;
		List<Tile> box = this.board.get(coord.getX()).get(coord.getY());
		if (box.size() == 1 && (added = box.get(0)).getPiece() == null)
			added.setPiece(piece);
		else
			box.add(added = (new Tile(piece, false, coord.getX(), coord.getY(), box.size())));

        if (coord.getY() == 0){
			for (List<List<Tile>> column : this.board)
				column.add(0, new ArrayList<Tile>());
			resize(0,1);
		} if (coord.getX() == 0){
			List<List<Tile>> column = new ArrayList<List<Tile>>();
			this.board.add(0, column);
			resize(1,0);
		}
        if (added.getY()+1 == this.board.get(added.getX()).size())
			this.board.get(added.getX()).add(new ArrayList<Tile>());
        if (added.getX()+1 == this.board.size())
			this.board.add(new ArrayList<List<Tile>>());
		while(this.board.get(added.getX()+1).size() < added.getY()+1)
			this.board.get(added.getX()+1).add(new ArrayList<Tile>());
		while(this.board.get(added.getX()-1).size() < added.getY()+2)
			this.board.get(added.getX()-1).add(new ArrayList<Tile>());
		if(this.board.get(added.getX()+1).get(added.getY()).size() == 0)
			this.board.get(added.getX()+1).get(added.getY()).add(new Tile(added.getX()+1, added.getY(), 0));
		if(this.board.get(added.getX()).get(added.getY()+1).size() == 0)
			this.board.get(added.getX()).get(added.getY()+1).add(new Tile(added.getX(), added.getY()+1, 0));
		if(this.board.get(added.getX()-1).get(added.getY()+1).size() == 0)
			this.board.get(added.getX()-1).get(added.getY()+1).add(new Tile(added.getX()-1, added.getY()+1, 0));
		if(this.board.get(added.getX()-1).get(added.getY()).size() == 0)
			this.board.get(added.getX()-1).get(added.getY()).add(new Tile(added.getX()-1, added.getY(), 0));
		if(this.board.get(added.getX()).get(added.getY()-1).size() == 0)
			this.board.get(added.getX()).get(added.getY()-1).add(new Tile(added.getX(), added.getY()-1, 0));
		if(this.board.get(added.getX()+1).get(added.getY()-1).size() == 0)
			this.board.get(added.getX()+1).get(added.getY()-1).add(new Tile(added.getX()+1, added.getY()-1, 0));
		
		this.nbPieceOnTheBoard++;
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
		this.nbPieceOnTheBoard--;
		return piece;
			
	}
	
	public void movePiece(Coord coordSource, Coord coordTarget){
		Tile source = this.getTile(coordSource);
		//Tile target = this.getTile(coordTarget);
		this.addPiece(source.getPiece(), coordTarget);
		this.removePiece(source.getCoord());
		//this.addPiece(piece, coordTarget);
	}
	
	private void checkBoardSize(Coord coord){
		boolean isEmpty = true;
		boolean resize = false;
		int x = 0;
		int y = 0;
		int i = 0;
		
		if (coord.getX() == 1){
			while (isEmpty && i<this.board.size()){
				for (List<Tile> box : this.board.get(i)){
					if(box.size() != 0){
						isEmpty = false;
						break;
					}
				}
				if (this.board.get(i).size() == 0 || isEmpty){
					resize = true;
					x -= 1;
					this.board.remove(i);
				}
				i++;
			}
		}
		isEmpty = true;
		
		if (coord.getX() == this.board.size()-2){
			i = this.board.size()-1;
			while (isEmpty && i>=0){
				for (List<Tile> box : this.board.get(i)){
					if(box.size() != 0){
						isEmpty = false;
						break;
					}
				}
				if (this.board.get(i).size() == 0 || isEmpty)
					this.board.remove(i);
				i--;
			}
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
					if (subNeighbor != null && subNeighbor.getPiece() != null){
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
		list.add(this.getTile(coord.getEast()));
		list.add(this.getTile(coord.getSouthEast()));
		list.add(this.getTile(coord.getSouthWest()));
		list.add(this.getTile(coord.getWest()));
		list.add(this.getTile(coord.getNorthWest()));
		list.add(this.getTile(coord.getNorthEast()));
		return list;
	}
	
	public List<Tile> getNeighbors(Tile tile){
		List<Tile> list = new ArrayList<Tile>();
		Coord coord = tile.getCoord();
		list.addAll(getNeighbors(coord));
		list.addAll(this.getAboveAndBelow(tile));
		return list;
	}
	
	public List<Tile> getPieceNeighbors(Coord coord){
		List<Tile> list = new ArrayList<Tile>();
		for (Tile t : getNeighbors(coord))
			if (t != null && t.getPiece() != null)
				list.add(t);
		return list;
	}
	
	public List<Tile> getPieceNeighbors(Tile tile){
		List<Tile> list = new ArrayList<Tile>();
		for (Tile t : getNeighbors(tile))
			if (t != null && t.getPiece() != null)
				list.add(t);
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

	public void clearPossibleMovement() {
		for(List<List<Tile>> column : this.board){
			for(List<Tile> box : column){
				for(Tile tile : box){
					if(tile != null && tile.getPiece() != null)
						tile.getPiece().clear();
				}
			}
		}
	}

}
