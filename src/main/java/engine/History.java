package main.java.engine;

import java.util.Stack;

public class History {

	private Stack<String> prevPlay;
	private Stack<String> prevUnplay;
	private Stack<String> nextPlay;
	private Stack<String> nextUnplay;
	

	public History() {
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
	
	public Stack<String> getPrevPlay(){
		return prevPlay;
	}
	
	public Stack<String> getPrevUnplay(){
		return prevUnplay;
	}
	

}
