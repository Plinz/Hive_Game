package main.java.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

public class Board{

	private List<Column> columns;
	private int nbPieceOnTheBoard;

	public Board() {
		init();
		nbPieceOnTheBoard = 0;
	}

	public Board(Board b) {
		columns = new ArrayList<Column>();
		for (Column column : b.getBoard()) {
			Column newColumn = new Column();
			for (Box box : column) {
				Box newBox = new Box();
				for (Tile tile : box)
					newBox.add(new Tile(tile));
				newColumn.add(newBox);
			}
			columns.add(newColumn);
		}
		nbPieceOnTheBoard = b.getNbPieceOnTheBoard();
	}

	public List<Column> getBoard() {
		return columns;
	}

	public int getNbPieceOnTheBoard() {
		return nbPieceOnTheBoard;
	}
	
	private void init() {
		columns = new ArrayList<Column>();
		Tile first = new Tile(0, 0, 0);
		Box box = new Box();
		box.add(first);
		Column column = new Column();
		column.add(box);
		columns.add(column);
	}

	public boolean accept(BoardDrawer b) {
		b.visit(this);
		for (int i = 0; i < columns.size(); i++) {
			for (int j = 0; j < columns.get(i).size(); j++) {
				int taille = columns.get(i).get(j).size();
				if (taille != 0) {
					columns.get(i).get(j).get(taille - 1).accept(b);
				}
			}
		}
		return false;
	}

	public Tile getTile(CoordGene<Integer> coord) {
		Box box = null;
		if (coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < columns.size()
				&& coord.getY() < columns.get(coord.getX()).size()
				&& !(box = columns.get(coord.getX()).get(coord.getY())).isEmpty())
			return box.get(box.size() - 1);
		return null;
	}

	public Tile getTile(CoordGene<Integer> coord, int floor) {
		if (coord.getX() >= 0 && coord.getY() >= 0 && coord.getX() < columns.size()
				&& coord.getY() < columns.get(coord.getX()).size())
			return columns.get(coord.getX()).get(coord.getY()).get(floor);
		return null;
	}

	public void resize(int xOffset, int yOffset) {
		for (Column column : columns)
			for (Box box : column)
				for (Tile tile : box) {
					tile.setX(tile.getX() + xOffset);
					tile.setY(tile.getY() + yOffset);
				}
	}

	public void addPiece(Piece piece, CoordGene<Integer> coord) {
		this.nbPieceOnTheBoard++;
		Tile added = null;
		Box box = columns.get(coord.getX()).get(coord.getY());
		if (box.size() == 1 && (added = box.get(0)).getPiece() == null)
			added.setPiece(piece);
		else {
			box.add(added = (new Tile(piece, false, coord.getX(), coord.getY(), box.size())));
			box.get(box.size() - 2).setBlocked(true);
		}

		if (coord.getY() == 0) {
			for (Column column : columns)
				column.add(0, new Box());
			resize(0, 1);
                        //Ici trouver un moyen de notifier le TraducteurBoard
		}
		if (coord.getX() == 0) {
			Column column = new Column();
			columns.add(0, column);
			resize(1, 0);
                        //Ici trouver un moyen de notifier le TraducteurBoard
		}
		if (added.getY() + 1 == columns.get(added.getX()).size())
			columns.get(added.getX()).add(new Box());
		if (added.getX() + 1 == columns.size())
			columns.add(new Column());
		while (columns.get(added.getX() + 1).size() < added.getY() + 1)
			columns.get(added.getX() + 1).add(new Box());
		while (columns.get(added.getX() - 1).size() < added.getY() + 2)
			columns.get(added.getX() - 1).add(new Box());
		if (columns.get(added.getX() + 1).get(added.getY()).size() == 0)
			columns.get(added.getX() + 1).get(added.getY()).add(new Tile(added.getX() + 1, added.getY(), 0));
		if (columns.get(added.getX()).get(added.getY() + 1).size() == 0)
			columns.get(added.getX()).get(added.getY() + 1).add(new Tile(added.getX(), added.getY() + 1, 0));
		if (columns.get(added.getX() - 1).get(added.getY() + 1).size() == 0)
			columns.get(added.getX() - 1).get(added.getY() + 1).add(new Tile(added.getX() - 1, added.getY() + 1, 0));
		if (columns.get(added.getX() - 1).get(added.getY()).size() == 0)
			columns.get(added.getX() - 1).get(added.getY()).add(new Tile(added.getX() - 1, added.getY(), 0));
		if (columns.get(added.getX()).get(added.getY() - 1).size() == 0)
			columns.get(added.getX()).get(added.getY() - 1).add(new Tile(added.getX(), added.getY() - 1, 0));
		if (columns.get(added.getX() + 1).get(added.getY() - 1).size() == 0)
			columns.get(added.getX() + 1).get(added.getY() - 1).add(new Tile(added.getX() + 1, added.getY() - 1, 0));
	}

	public Piece removePiece(CoordGene<Integer> coord) {
		this.nbPieceOnTheBoard--;
		Piece piece = null;
		Box box = columns.get(coord.getX()).get(coord.getY());
		Tile tile = getTile(coord);
		if (tile != null) {
			piece = tile.getPiece();
			if (box.size() == 1) {
				tile.setPiece(null);
				this.updateNeighbors(tile);
				checkBoardSize(tile.getCoord());
			} else {
				box.remove(tile);
				box.get(box.size() - 1).setBlocked(false);
			}
		}
		return piece;

	}

	public void movePiece(CoordGene<Integer> coordSource, CoordGene<Integer> coordTarget) {
		Tile source = this.getTile(coordSource);
		this.addPiece(source.getPiece(), coordTarget);
		this.removePiece(source.getCoord());
	}

	private void checkBoardSize(CoordGene<Integer> coord) {
		if (nbPieceOnTheBoard == 0){
			init();
		} else{
			boolean isEmpty = true;
			boolean resize = false;
			int x = 0;
			int y = 0;
			int i = 0;
	
			if (coord.getX() == 1) {
				while (isEmpty && i < columns.size()) {
					for (Box box : columns.get(i)) {
						if (box.size() != 0) {
							isEmpty = false;
							break;
						}
					}
					if (columns.get(i).size() == 0 || isEmpty) {
						resize = true;
						x -= 1;
						columns.remove(i);
					}
					i++;
				}
			}
			isEmpty = true;
	
			if (coord.getX() == columns.size() - 2) {
				i = columns.size() - 1;
				while (isEmpty && i >= 0) {
					for (Box box : columns.get(i)) {
						if (box.size() != 0) {
							isEmpty = false;
							break;
						}
					}
					if (columns.get(i).size() == 0 || isEmpty)
						columns.remove(i);
					i--;
				}
			}
	
			if (coord.getY() == 1) {
				for (Column column : columns) {
					if (!column.isEmpty() && column.get(0).size() != 0) {
						isEmpty = false;
						break;
					}
				}
				if (isEmpty) {
					resize = true;
					y = -1;
					for (Column column : columns)
						column.remove(0);
				}
			}
			if (coord.getY() + 2 == columns.get(coord.getX()).size()) {
				for (Column column : columns)
					if (column.get(column.size() - 1).size() == 0)
						column.remove(column.size() - 1);
	
			}
			if (resize)
				this.resize(x, y);
		}
	}

	private void updateNeighbors(Tile tile) {
		List<Tile> neighbors = this.getNeighbors(tile);
		for (Tile neighbor : neighbors) {
			if (neighbor.getPiece() == null) {
				List<Tile> subNeighbors = this.getNeighbors(neighbor);
				boolean hasPieceAround = false;
				for (Tile subNeighbor : subNeighbors) {
					if (subNeighbor != null && subNeighbor.getPiece() != null) {
						hasPieceAround = true;
						break;
					}
				}
				if (!hasPieceAround) {
					columns.get(neighbor.getX()).get(neighbor.getY()).clear();
				}
			}

		}
	}

	public List<Tile> getNeighbors(CoordGene<Integer> coord) {
		List<Tile> list = new ArrayList<Tile>();
		list.add(this.getTile(coord.getEast()));
		list.add(this.getTile(coord.getSouthEast()));
		list.add(this.getTile(coord.getSouthWest()));
		list.add(this.getTile(coord.getWest()));
		list.add(this.getTile(coord.getNorthWest()));
		list.add(this.getTile(coord.getNorthEast()));
		return list;
	}

	public List<Tile> getNeighbors(Tile tile) {
		List<Tile> list = new ArrayList<Tile>();
		CoordGene<Integer> coord = tile.getCoord();
		list.addAll(getNeighbors(coord));
		list.addAll(this.getAboveAndBelow(tile));
		return list;
	}

	public List<Tile> getPieceNeighbors(CoordGene<Integer> coord) {
		List<Tile> list = new ArrayList<Tile>();
		for (Tile t : getNeighbors(coord))
			if (t != null && t.getPiece() != null)
				list.add(t);
		return list;
	}

	public List<Tile> getPieceNeighbors(Tile tile) {
		List<Tile> list = new ArrayList<Tile>();
		for (Tile t : getNeighbors(tile))
			if (t != null && t.getPiece() != null)
				list.add(t);
		return list;
	}

	public List<Tile> getAboveAndBelow(Tile tile) {
		Box box = columns.get(tile.getX()).get(tile.getY());
		List<Tile> list = new ArrayList<Tile>();
		for (Tile t : box)
			if (tile.getZ() != t.getZ())
				list.add(t);
		return list;
	}

	public void clearPossibleMovement() {
		for (Column column : columns)
			for (Box box : column)
				for (Tile tile : box)
					if (tile != null && tile.getPiece() != null)
						tile.getPiece().clear();
	}
	
	public boolean oneHive(Tile toMove) {
		List<Tile> list = getPieceNeighbors(toMove);

		if (toMove.getZ() != 0 || list.isEmpty())
			return true;

		HashSet<Tile> visited = new HashSet<Tile>();
		Stack<Tile> nextTiles = new Stack<Tile>();

		visited.add(toMove);
		nextTiles.add(list.get(0));
		Tile current;
		while (!nextTiles.isEmpty()) {
			if (!visited.contains(current = nextTiles.pop())) {
				nextTiles.addAll(getPieceNeighbors(current));
				visited.add(current);
			}
		}
		return getNbPieceOnTheBoard() == visited.size();
	}

	public boolean freedomToMove(int floor, CoordGene<Integer> left, CoordGene<Integer> right,
			CoordGene<Integer> exception) {
		Tile leftTile = getTile(left);
		Tile rightTile = getTile(right);
		return (leftTile == null || leftTile.getPiece() == null || leftTile.getZ() < floor || rightTile == null
				|| rightTile.getPiece() == null || rightTile.getZ() < floor || left.equals(exception)
				|| right.equals(exception));
	}

	public boolean permanentContact(int floor, CoordGene<Integer> left, CoordGene<Integer> right,
			CoordGene<Integer> exception) {
		if (floor > 0)
			return true;
		Tile leftTile = getTile(left);
		Tile rightTile = getTile(right);
		return (leftTile != null && leftTile.getPiece() != null && !left.equals(exception))
				|| (rightTile != null && rightTile.getPiece() != null && !right.equals(exception));
	}
}
