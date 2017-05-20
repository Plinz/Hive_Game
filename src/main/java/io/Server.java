package main.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import main.java.engine.Core;
import main.java.utils.Consts;

public class Server extends IO {

	private PrintWriter client;
	private String notation = null;

	public Server(Core core) {
		super(core);
	}
	
	@Override
	public void connect(String host) {
		try {
			new Thread(new ServerThread(new ServerSocket(Consts.PORT))).start();;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getMove() {
		return notation;
	}

	@Override
	public void updateInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendInfo() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendMove(String move) {
		synchronized (this) {
			if (client != null){
				client.println(move);
				client.flush();
			}
		}
	}

//	synchronized public void receiveMove(String notation) {
//		if (nextMove == null){
//			nextMove = notation;
//		}
//	}
//
//	synchronized public void delClient() {
//		client = null;
//	}
//
//	public void addClient(PrintWriter _out) {
//		client = _out;
//	}
//
//	public String getNextMove() {
//		synchronized (this){
//			String s = nextMove;
//			nextMove = "";
//			return s;
//		}
//	}
//
//	public void setNextMove(String nextMove) {
//		synchronized (this) {
//			this.nextMove = nextMove;
//		}
//	}

	public class ServerThread implements Runnable {
	
		private Socket socket;
		private BufferedReader in;
		private ServerSocket serverSocket;
	
		ServerThread(ServerSocket ss) {
			serverSocket = ss;
		}
	
		// ** Methode : exécutée au lancement du thread par t.start() **
		// ** Elle attend les messages en provenance du serveur et les redirige **
		// cette méthode doit obligatoirement être implémentée à cause de
		// l'interface Runnable
		public void run() {
			try {
				socket = serverSocket.accept();
				client = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
			String message = "";
			try {
				char charCur[] = new char[1];
				while (in.read(charCur, 0, 1) != -1) {
					if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
						message += charCur[0];
					else if (!message.equalsIgnoreCase("")) {
						notation = message;
						message = "";
					}
				}
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				try {
					System.out.println("Le client s'est deconnecte");
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
