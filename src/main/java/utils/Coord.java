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
	
	public Coord getEast(){
		return new Coord(this.x+1, this.y);
	}
	public Coord getSouthEast(){
		return new Coord(this.x, this.y+1);
	}
	public Coord getSouthWest(){
		return new Coord(this.x-1, this.y+1);
	}
	public Coord getWest(){
		return new Coord(this.x-1, this.y);
	}
	public Coord getNorthWest(){
		return new Coord(this.x, this.y-1);
	}
	public Coord getNorthEast(){
		return new Coord(this.x+1, this.y-1);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().getCanonicalName() == "Coord"){
			Coord c = (Coord) obj;
			return (c.getX() == this.x && c.getY() == this.y);
		}
		return super.equals(obj);
	}

    @Override
    public String toString() {
        return "Coord{" + "x=" + x + ", y=" + y + '}' + '\n';
    }
        
        
	
}
