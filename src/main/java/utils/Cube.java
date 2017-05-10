package main.java.utils;

import java.util.ArrayList;
import java.util.List;

public class Cube<T>{
	
    private T x;
    private T y;
    private T z;

    public Cube(T x, T y, T z) {
            this.x = x;
            this.y = y;
            this.z = z;
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
    public void setZ(T z) {
            this.z = z;
    }
    public T getZ() {
            return z;
    }
    public Cube getEast() {
        return new Cube((Integer)this.x + 1, this.y, this.z);
    }

    public Cube getSouthEast() {
        return new Cube(this.x, (Integer)this.y + 1, this.z);
    }

    public Cube getSouthWest() {
        return new Cube((Integer)this.x - 1, (Integer)this.y + 1, this.z);
    }

    public Cube getWest() {
        return new Cube((Integer)this.x - 1, this.y, this.z);
    }

    public Cube getNorthWest() {
        return new Cube(this.x, (Integer)this.y - 1, this.z);
    }

    public Cube getNorthEast() {
        return new Cube((Integer)this.x + 1, (Integer)this.y - 1, this.z);
    }

    public List<Cube> getNeighbors() {
        List<Cube> list = new ArrayList<Cube>();
        list.add(getEast());
        list.add(getSouthEast());
        list.add(getSouthWest());
        list.add(getWest());
        list.add(getNorthWest());
        list.add(getNorthEast());
        return list;
    }
       
    //to be replaced with calls to the function above
    public Cube[] getNeighborsInArray() {
        Cube result[] = new Cube[6];
        result[0] = this.getEast();
        result[1] = this.getSouthEast();
        result[2] = this.getSouthWest();
        result[3] = this.getWest();
        result[4] = this.getNorthWest();
        result[5] = this.getNorthEast();
        return result;
    }
        
}
