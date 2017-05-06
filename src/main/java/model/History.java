package main.java.model;

import java.io.Serializable;
import java.util.Stack;

public class History implements Serializable{

	private static final long serialVersionUID = -45267853243056059L;
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
