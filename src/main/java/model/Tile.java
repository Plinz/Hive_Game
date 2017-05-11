package main.java.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import main.java.utils.CoordGene;
import main.java.view.BoardDrawer;

@XmlRootElement(name="tile")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tile {

    @XmlElement
	private Piece piece;
    @XmlElement(name="blocked")
	private boolean blocked;
    @XmlAttribute(name="x")
	private int x;
    @XmlAttribute(name="y")
	private int y;
    @XmlAttribute(name="z")
	private int z;

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
}
