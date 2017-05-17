package main.java.utils;

import java.util.ArrayList;
import java.util.List;

@Deprecated
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

    public Coord getEast() {
        return new Coord(this.x + 1, this.y);
    }

    public Coord getSouthEast() {
        return new Coord(this.x, this.y + 1);
    }

    public Coord getSouthWest() {
        return new Coord(this.x - 1, this.y + 1);
    }

    public Coord getWest() {
        return new Coord(this.x - 1, this.y);
    }

    public Coord getNorthWest() {
        return new Coord(this.x, this.y - 1);
    }

    public Coord getNorthEast() {
        return new Coord(this.x + 1, this.y - 1);
    }

    public List<Coord> getNeighbors() {
        List<Coord> list = new ArrayList<Coord>();
        list.add(getEast());
        list.add(getSouthEast());
        list.add(getSouthWest());
        list.add(getWest());
        list.add(getNorthWest());
        list.add(getNorthEast());
        return list;
    }

    public Coord[] getNeighborsInArray() {
        Coord result[] = new Coord[6];
        result[0] = this.getEast();
        result[1] = this.getSouthEast();
        result[2] = this.getSouthWest();
        result[3] = this.getWest();
        result[4] = this.getNorthWest();
        result[5] = this.getNorthEast();
        return result;
    }

    public boolean isValidCoord(){
        return ((x>=0)&&(y>=0));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coord) {
            return (this.x == ((Coord) obj).getX() && this.y == ((Coord) obj).getY());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Coord{" + "x=" + x + ", y=" + y + '}' + '\n';
    }

}
