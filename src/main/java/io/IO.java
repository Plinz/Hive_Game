package main.java.io;

import main.java.engine.Core;

public abstract class IO {
	
	Core core;
	
	public IO(Core core){
		this.core = core;
	}
	
	public String getNextMove(){
        System.err.println("Erreur : IO abstraite");
        return null;
	}
	
	public abstract void sendMove(String move);

}
