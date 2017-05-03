package main.java.utils;

public class Coord {
	
	private int x;
	private int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
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

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().getCanonicalName() == "Coord"){
			Coord c = (Coord) obj;
			return (c.getX() == this.x && c.getY() == this.y);
		}
		return super.equals(obj);
	}
	
}
