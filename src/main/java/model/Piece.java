package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.java.utils.CoordGene;

public abstract class Piece implements Cloneable, Serializable {

    private static final long serialVersionUID = 673440018064999695L;
    protected String name;
    protected int team;
    protected String description;
    protected List<CoordGene<Integer>> possibleMovement;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + this.team;
        hash = 29 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.team != other.team) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nPiece{" + "name=" + name + ", team=" + team + ", description=" + description + '}';
    }

    public abstract List<CoordGene<Integer>> getPossibleMovement(Tile tile, Board board);

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
    public Piece clone() {

        Piece clone = null;
        try {
            clone = (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        clone.description = new String(this.description);
        clone.name = new String(this.name);
        clone.team = this.team;
        clone.possibleMovement = new ArrayList<CoordGene<Integer>>();
        if (this.possibleMovement != null) {
            for (CoordGene<Integer> coord : this.possibleMovement) {
                clone.possibleMovement.add(new CoordGene<Integer>(coord.getX(), coord.getY()));
            }
        }

        return clone;
    }

    public void clear() {
        this.possibleMovement = null;
        //this.highlighted = false;
    }

}
