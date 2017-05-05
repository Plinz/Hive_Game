package main.java.utils;

public class CoordGene<T> {
	
	private T x;
	private T y;
	
	public CoordGene(T x, T y) {
		this.x = x;
		this.y = y;
	}
	public T getX() {
		return x;
	}
	public void setX(T x) {
		this.x = x;
	}
	public T getY() {
		return y;
	}
	public void setY(T y) {
		this.y = y;
	}

	
}
