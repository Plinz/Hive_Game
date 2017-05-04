package main.java.model;

import java.util.List;

import main.java.utils.Coord;

public abstract class Piece implements Cloneable{
	
	protected String name;
	protected int team;
	protected String description;
	
	public abstract List<Coord> getPossibleMovement(Tile tile, Board board);

	public Piece(String name, int team, String description) {
		this.name = name;
		this.team = team;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Piece clone(){
		
		Piece clone = null;
	    try {
			clone = (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	    clone.description = new String(this.description);
	    clone.name = new String(this.name);
	    clone.team = this.team;
	    
	    return clone;
	}


//	//TODO
//	public boolean accepte(Visitor v){
//		v.visit(this);
//	}
//	
}
