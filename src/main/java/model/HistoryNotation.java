package main.java.model;

import java.util.Stack;

public class HistoryNotation {

	Stack<String> prevPlay;
	Stack<String> prevUnplay;
	Stack<String> nextPlay;
	Stack<String> nextUnplay;
	

	public HistoryNotation() {
		prevPlay = new Stack<String>();
		prevUnplay = new Stack<String>();
		nextPlay = new Stack<String>();
		nextUnplay = new Stack<String>();
	}

	public void save(String notation, String unplay) {
		this.prevPlay.push(notation);
		this.prevUnplay.push(unplay);
		this.nextPlay.clear();
		this.nextUnplay.clear();
	}

	public boolean hasPrevious() {
		return !this.prevUnplay.isEmpty();
	}

	public boolean hasNext() {
		return !this.nextPlay.isEmpty();
	}

	public String getPrevious() {
		nextPlay.push(prevPlay.pop());
		return nextUnplay.push(prevUnplay.pop());
	}

	public String getNext() {
		prevUnplay.push(nextUnplay.pop());
		return prevPlay.push(nextPlay.pop());
	}
	

}
