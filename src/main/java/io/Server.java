package main.java.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

import main.java.engine.Core;

public class Server extends IO {

	private static final int PORT = 16518;

	private PrintWriter client;
	private String nextMove = null;

	public Server(Core core) {
		super(core);
		try {
			ServerSocket socket = new ServerSocket(PORT);
			new Client(socket.accept(), this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void sendMove(String notation) {
		synchronized (this) {
			if (client != null){
				client.println(notation);
				client.flush();
			}
		}
	}
	
	synchronized public void receiveMove(String notation) {
		if (nextMove == null){
			nextMove = notation;
		}
	}

	synchronized public void delClient() {
		client = null;
	}

	public void addClient(PrintWriter _out) {
		client = _out;
	}

	public String getNextMove() {
		synchronized (this){
			String s = nextMove;
			nextMove = "";
			return s;
		}
	}

	public void setNextMove(String nextMove) {
		synchronized (this) {
			this.nextMove = nextMove;
		}
	}

}
