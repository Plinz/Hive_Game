package main.java.model;

import java.util.Stack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="history")
@XmlAccessorType(XmlAccessType.FIELD)
public class History {

    @XmlElement(name="previousState")
	Stack<State> previousState;
    @XmlElement(name="previousState")
	Stack<State> nextState;
	
	public History(){
		previousState = new Stack<State>();
		nextState = new Stack<State>();
	}
	
	public void saveState(State currentState) {
		previousState.push(new State(currentState));
		nextState.clear();
	}

	public boolean hasPreviousState(){
		return !this.previousState.isEmpty();
	}
	
	public boolean hasNextState(){
		return !this.nextState.isEmpty();
	}
	
	public State getPreviousState(){
		return this.nextState.push(this.previousState.pop());
	}
	
	public State getNextState(){
		return this.nextState.pop();
	}
		
}
