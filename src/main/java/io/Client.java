package main.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import main.java.engine.Core;
import main.java.utils.Consts;

public class Client extends IO {
	private PrintWriter server = null;
	private String notation = null;
	private String otherName = null;
	private Socket socket;
	private int mode = -1;
	private Thread t;

	public Client(Core core) {
		super(core);
	}

	@Override
	public void connect(String host) {
		try {
			socket = new Socket(host, Consts.PORT);
			t = new Thread(new ClientThread(socket));
			t.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void disconnect() {
		server.println("DECO");
		server.flush();
		t.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		core.setState(Consts.DISCONNECTED);
	}	
	
	@Override
	public String getMove() {
		return notation;
	}

	@Override
	public boolean updateInfo() {
		synchronized (this) {
			if (otherName != null && mode != -1){
				core.setMode(mode);
				(mode==Consts.PVEX?core.getPlayers()[Consts.PLAYER2]:core.getPlayers()[Consts.PLAYER1]).setName(otherName);
				core.setState(core.getMode()==Consts.PVEX?Consts.WAIT_FOR_INPUT:Consts.PROCESSING);
				return true;
			}
			return false;
		}
	}

	@Override
	public void sendMove(String move) {
		if (server != null) {
			server.println("MOVE"+move);
			server.flush();
		}
	}
	
	@Override
	public void sendMessage(String message) {
		synchronized (this) {
			if (server != null){
				server.println("MESG"+message);
				server.flush();
			}
		}
	}

	@Override
	public boolean sendInfo(String playerName) {
		synchronized (this) {
			if (server != null){
				server.println("NAME"+playerName);
				server.flush();
				return true;
			}
			return false;
		}
	}
	
	@Override
	public void processReceive(String response){
		synchronized (this) {
			if (response.startsWith("NAME")){
				otherName = response.substring(4);
			} else if(response.startsWith("MODE")){
				int m = Integer.parseInt(response.substring(4));
				mode = m==Consts.PVEX?Consts.EXVP:Consts.PVEX;
			} else if(response.startsWith("MESG")){
				core.newMessage(response.substring(4));
			} else if(response.startsWith("MOVE")){
				String[] tokens = response.substring(4).split(";");
				core.playExtern(tokens[0], tokens[1]);
			} else if (response.startsWith("RECO")) {
				String[] tokens = response.substring(4).split(";");
				core.playEmulate(tokens[0], tokens[1]);
				core.setState(core.getMode()==Consts.PVEX?core.getCurrentPlayer()==Consts.PLAYER1?Consts.WAIT_FOR_INPUT:Consts.PROCESSING:core.getCurrentPlayer()==Consts.PLAYER1?Consts.PROCESSING:Consts.WAIT_FOR_INPUT);
			} else if (response.startsWith("DECO")) {
				t.interrupt();
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				core.setState(Consts.DISCONNECTED);
			}
		}
	}

	public class ClientThread implements Runnable {
		private Socket connexion = null;
		private BufferedReader reader = null;

		public ClientThread(Socket socket) {
			this.connexion = socket;
		}

		public void run() {
			try {
				server = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
			String message = "";
			try {
				char charCur[] = new char[1];
				while (reader.read(charCur, 0, 1) != -1) {
					if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
						message += charCur[0];
					else if (!message.equalsIgnoreCase("")) {
						processReceive(message);
						message = "";
					}
				}
			} catch (Exception e) {
			} finally {
				disconnect();
			}
		}
	}
}
