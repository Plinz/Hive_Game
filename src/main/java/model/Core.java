package main.java.model;

public class Core {
	
	private History history;
	private State currentState;
	private int mode;
	
	public Core(int mode) {
		this.history = new History();;
		this.currentState = new State();
		this.mode = mode;
	}
	
	public Core(History history, State currentState, int mode) {
		this.history = history;
		this.currentState = currentState;
		this.mode = mode;
	}
		
	public History getHistory() {
		return history;
	}
	public void setHistory(History history) {
		this.history = history;
	}
	public State getCurrentState() {
		return currentState;
	}
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	

}
