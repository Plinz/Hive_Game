package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.utils.Coord;

public abstract class Piece implements Cloneable, Serializable{
	
	private static final long serialVersionUID = 673440018064999695L;
	protected String name;
	protected int team;
	protected String description;
	protected List<Coord> possibleMovement;

    @Override
    public String toString() {
        return "\nPiece{" + "name=" + name + ", team=" + team + ", description=" + description + '}';
    }
	
	public abstract List<Coord> getPossibleMovement(Tile tile, Board board);

	public Piece(String name, int team, String description) {
		this.name = name;
		this.team = team;
		this.description = description;
		this.possibleMovement = null;
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
	    clone.possibleMovement = new ArrayList<Coord>();
	    if (this.possibleMovement != null){
		    for (Coord coord : this.possibleMovement)
		    	clone.possibleMovement.add(new Coord(coord.getX(), coord.getY()));
	    }
	    
	    return clone;
	}

	public void clear() {
		this.possibleMovement = null;
		//this.highlighted = false;
	}

}
