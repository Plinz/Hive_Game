package main.java.utils;

import java.util.ArrayList;
import java.util.List;

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

    public CoordGene<Integer> getEast() {
        return new CoordGene<Integer>((Integer)this.x + 1, (Integer)this.y);
    }

    public CoordGene<Integer> getSouthEast() {
        return new CoordGene<Integer>((Integer)this.x, (Integer)this.y + 1);
    }

    public CoordGene<Integer> getSouthWest() {
        return new CoordGene<Integer>((Integer)this.x - 1, (Integer)this.y + 1);
    }

    public CoordGene<Integer> getWest() {
        return new CoordGene<Integer>((Integer)this.x - 1, (Integer)this.y);
    }

    public CoordGene<Integer> getNorthWest() {
        return new CoordGene<Integer>((Integer)this.x, (Integer)this.y - 1);
    }

    public CoordGene<Integer> getNorthEast() {
        return new CoordGene<Integer>((Integer)this.x + 1, (Integer)this.y - 1);
    }

    public List<CoordGene<Integer>> getNeighbors() {
        List<CoordGene<Integer>> list = new ArrayList<CoordGene<Integer>>();
        list.add(getEast());
        list.add(getSouthEast());
        list.add(getSouthWest());
        list.add(getWest());
        list.add(getNorthWest());
        list.add(getNorthEast());
        return list;
    }
}
