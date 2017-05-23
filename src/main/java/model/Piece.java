package main.java.model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

import main.java.utils.CoordGene;

public abstract class Piece implements Cloneable{

    protected String name;
    protected int id;
    protected int team;
    protected Image image;
    protected String description;
    protected List<CoordGene<Integer>> possibleMovement;

    public abstract List<CoordGene<Integer>> updatePossibleMovement(Tile tile, Board board);

    public Piece(){
    	this.id = -1;
    	this.team = -1;
    }
    
    public Piece(String name, int id, int team, String description) {
        this.name = name;
        this.id = id;
        this.team = team;
        this.description = description;
        this.possibleMovement = null;
        this.image = new Image(getClass().getClassLoader().getResource("main/resources/img/tile/" + name + team + ".png").toString());
    }

    public String getName() {
        return name;
    }
    public int getId() {
		return id;
	}
	public int getTeam() {
        return team;
    }
    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    } 
    
    public List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board){
		if (this.possibleMovement == null)
			updatePossibleMovement(tile, board);
		return this.possibleMovement;
    }
    
    public void clear() {
        this.possibleMovement = null;
    }
    
    @Override
    public Piece clone() {

        Piece clone = null;
        try {
            clone = (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        clone.description = description;
        clone.name = name;
        clone.id = id;
        clone.team = team;
        clone.possibleMovement = new ArrayList<CoordGene<Integer>>();
        if (this.possibleMovement != null)
            for (CoordGene<Integer> coord : this.possibleMovement)
                clone.possibleMovement.add(new CoordGene<Integer>(coord.getX(), coord.getY()));
        return clone;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((possibleMovement == null) ? 0 : possibleMovement.hashCode());
		result = prime * result + team;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (possibleMovement == null) {
			if (other.possibleMovement != null)
				return false;
		} else if (!possibleMovement.equals(other.possibleMovement))
			return false;
		if (team != other.team)
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "\nPiece{" + "name=" + name + ", team=" + team + ", description=" + description + '}';
    }
}
