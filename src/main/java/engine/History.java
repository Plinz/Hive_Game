package main.java.engine;

import java.util.Stack;

public class History implements Cloneable{

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
        
        public Stack<String> getNextPlay(){
		return nextPlay;
	}
	
	public Stack<String> getNextUnplay(){
		return nextUnplay;
	}
	
        public void setNextPlay(Stack<String> nextPlay) {
            this.nextPlay = nextPlay;
        }

        public void setNextUnplay(Stack<String> nextUnplay) {
            this.nextUnplay = nextUnplay;
        }
        
	@SuppressWarnings("unchecked")
	@Override
	protected History clone() {
		History history = null;
        try {
        	history = (History) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        history.nextPlay = (Stack<String>) nextPlay.clone();
        history.nextUnplay = (Stack<String>) nextUnplay.clone();
        history.prevPlay = (Stack<String>) prevPlay.clone();
        history.prevUnplay = (Stack<String>) prevUnplay.clone();
        return history;
   }
}
