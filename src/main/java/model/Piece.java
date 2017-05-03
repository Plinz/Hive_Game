package main.java.model;

import java.util.List;

import main.java.utils.Coord;

public abstract class Piece {
	
	protected String name;
	protected String color;
	protected String description;
	
	public abstract List<Coord> getPossibleMovement(Coord coord, int floor, Board board);

	public Piece(String name, String color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

//	//TODO
//	public boolean accepte(Visitor v){
//		v.visit(this);
//	}
//	
}
