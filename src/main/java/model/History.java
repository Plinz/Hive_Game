package main.java.model;

import java.util.Stack;

public class History {

	Stack<State> previousState;
	Stack<State> nextState;
	
	public History(){
		previousState = new Stack<State>();
		nextState = new Stack<State>();
	}
	
	public void saveState(State currentState) {
		previousState.push(new State(currentState));
	}

}
