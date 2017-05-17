package main.java.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Box implements List<Tile>, Cloneable{
	private ArrayList<Tile> tiles;
    
    public Box(){
    	this.tiles = new ArrayList<Tile>();
    }

	@Override
	public boolean add(Tile e) {
		return tiles.add(e);
	}

	@Override
	public void add(int index, Tile element) {
		tiles.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Tile> c) {
		return tiles.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Tile> c) {
		return tiles.addAll(index, c);
	}

	@Override
	public void clear() {
		tiles.clear();
	}

	@Override
	public boolean contains(Object o) {
		return tiles.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return tiles.containsAll(c);
	}

	@Override
	public Tile get(int index) {
		return tiles.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return tiles.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	@Override
	public Iterator<Tile> iterator() {
		return tiles.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return tiles.lastIndexOf(o);
	}

	@Override
	public ListIterator<Tile> listIterator() {
		return tiles.listIterator();
	}

	@Override
	public ListIterator<Tile> listIterator(int index) {
		return tiles.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return tiles.remove(o);
	}

	@Override
	public Tile remove(int index) {
		return tiles.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return tiles.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return tiles.retainAll(c);
	}

	@Override
	public Tile set(int index, Tile element) {
		return tiles.set(index, element);
	}

	@Override
	public int size() {
		return tiles.size();
	}

	@Override
	public List<Tile> subList(int fromIndex, int toIndex) {
		return tiles.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return tiles.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return tiles.toArray(a);
	}
	
	@Override
	protected Box clone() {
		Box box = null;
        try {
        	box = (Box) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        box.tiles = (ArrayList<Tile>) tiles.clone();
        return box;
	}
}
