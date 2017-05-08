package main.java.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import main.java.utils.Coord;

public abstract class Piece implements Cloneable{
	
	protected String name;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + this.team;
        hash = 83 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (this.team != other.team) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
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

	public List<Coord> getPossibleMovement() {
		return possibleMovement;
	}

	public void setPossibleMovement(List<Coord> possibleMovement) {
		this.possibleMovement = possibleMovement;
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

}
