package main.java.io;

import main.java.engine.Core;

public abstract class IO {
	
	Core core;
	
	public IO(Core core){
		this.core = core;
	}

	public abstract void connect(String host);
	public abstract String getMove();
	public abstract void updateInfo();
	public abstract void sendMove(String move);
	public abstract void sendInfo();
	public abstract void processReceive(String response);
	
}
