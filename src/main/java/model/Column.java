package main.java.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="column")
@XmlAccessorType(XmlAccessType.FIELD)
public class Column implements List<Box>{
    @XmlElementWrapper(name="column")
	private List<Box> boxs;

	@Override
	public boolean add(Box e) {
		return boxs.add(e);
	}

	@Override
	public void add(int index, Box element) {
		boxs.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Box> c) {
		return boxs.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Box> c) {
		return boxs.addAll(index, c);
	}

	@Override
	public void clear() {
		boxs.clear();
	}

	@Override
	public boolean contains(Object o) {
		return boxs.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return boxs.containsAll(c);
	}

	@Override
	public Box get(int index) {
		return boxs.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return boxs.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return boxs.isEmpty();
	}

	@Override
	public Iterator<Box> iterator() {
		return boxs.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return boxs.lastIndexOf(o);
	}

	@Override
	public ListIterator<Box> listIterator() {
		return boxs.listIterator();
	}

	@Override
	public ListIterator<Box> listIterator(int index) {
		return boxs.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return boxs.remove(o);
	}

	@Override
	public Box remove(int index) {
		return boxs.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return boxs.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return boxs.retainAll(c);
	}

	@Override
	public Box set(int index, Box element) {
		return boxs.set(index, element);
	}

	@Override
	public int size() {
		return boxs.size();
	}

	@Override
	public List<Box> subList(int fromIndex, int toIndex) {
		return boxs.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return boxs.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return boxs.toArray(a);
	}
}
