package main.java.model;

import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

public class Tile implements Cloneable{

	private Piece piece;
	private boolean blocked;
	private int x;
	private int y;
	private int z;

    public Tile(){
    	this.blocked = false;
    	this.x = -1;
    	this.y = -1;
    	this.z = -1;
    }
    
	public Tile(Piece piece, boolean blocked, int x, int y, int z) {
		this.piece = piece;
		this.blocked = blocked;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Tile(Tile t) {
		this.piece = t.piece != null ? t.getPiece().clone() : null;
		this.blocked = t.isBlocked();
		this.x = t.getX();
		this.y = t.getY();
		this.z = t.getZ();
	}

	public Tile(int i, int j, int k) {
		this.piece = null;
		this.blocked = false;
		this.x = i;
		this.y = j;
		this.z = k;
	}

	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public CoordGene<Integer> getCoord() {
		return new CoordGene<Integer>(this.x, this.y);
	}

	public boolean accept(BoardDrawer b) {
		b.visit(this);
		return false;
	}

	@Override
	public String toString() {
		return "Tile{" + "x=" + x + ", y=" + y + '}';
	}
	
	@Override
	protected Tile clone() {
		Tile tile = null;
        try {
        	tile = (Tile) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        tile.blocked = blocked;
        tile.piece = piece.clone();
        tile.x = x;
        tile.y = y;
        tile.z = z;
        return tile;
	}
}
