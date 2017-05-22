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
	private String otherName = null;

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
		synchronized (this) {
			return notation;
		}
	}

	@Override
	public boolean updateInfo() {
		synchronized (this) {
			if (otherName != null){
				(core.getMode()==Consts.PVEX?core.getPlayers()[Consts.PLAYER2]:core.getPlayers()[Consts.PLAYER1]).setName(otherName);
				core.setState(core.getMode()==Consts.PVEX?Consts.WAIT_FOR_INPUT:Consts.PROCESSING);
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean sendInfo(String playerName) {
		synchronized (this) {
			if (client != null){
				client.println("MODE"+core.getMode());
				client.println("NAME"+playerName);
				client.flush();
				return true;
			}
			return false;
		}
	}
	
	@Override
	public void sendMove(String move) {
		synchronized (this) {
			if (client != null){
				client.println("MOVE"+move);
				client.flush();
			}
		}
	}
	
	@Override
	public void sendMessage(String message) {
		synchronized (this) {
			if (client != null){
				client.println("MESG"+message);
				client.flush();
			}
		}
	}

	@Override
	public void processReceive(String response) {
		synchronized (this) {
			if (response.startsWith("NAME")){
				otherName = response.substring(4);
			} else if (response.startsWith("MESG")) {
				core.newMessage(response.substring(4));
			} else if (response.startsWith("MOVE")) {
				String[] tokens = response.substring(4).split(";");
				core.playExtern(tokens[0], tokens[1]);
			}
		}
	}

	public class ServerThread implements Runnable {
	
		private Socket socket;
		private BufferedReader in;
		private ServerSocket serverSocket;
	
		ServerThread(ServerSocket ss) {
			serverSocket = ss;
		}
	
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
						processReceive(message);
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
					e.printStackTrace(System.out);
				}
			}
		}
	}
}
