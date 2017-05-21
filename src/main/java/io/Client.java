package main.java.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import main.java.engine.Core;
import main.java.utils.Consts;

public class Client extends IO {
	private PrintWriter server = null;
	private String notation = null;
	private boolean run = true;

	public Client(Core core) {
		super(core);
	}

	@Override
	public void connect(String host) {
		try {
			new Thread(new ClientThread(new Socket(host, Consts.PORT))).start();
			;
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
		// TODO
	}

	@Override
	public void sendMove(String move) {
		if (server != null) {
			server.println(move);
			server.flush();
		}
	}

	@Override
	public void sendInfo() {
		if (server != null) {
			server.println("INFO");
			server.println();
			server.flush();
		}
	}
	
	@Override
	public void processReceive(String response){
		synchronized (this) {
			String[] tokens = response.split(";");
			core.playEmulate(tokens[0], tokens[1]);
		}
	}

	public class ClientThread implements Runnable {
		private Socket connexion = null;
		private BufferedInputStream reader = null;

		public ClientThread(Socket socket) {
			this.connexion = socket;
		}

		public void run() {
			try {
				server = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());
				while(run){
					String response = read();
					if (response != null && !response.isEmpty()){
						processReceive(response);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			server.write("CLOSE");
			server.flush();
			server.close();
		}

		// Méthode pour lire les réponses du serveur
		private String read() throws IOException {
			String response = "";
			int stream;
			byte[] b = new byte[4096];
			stream = reader.read(b);
			response = new String(b, 0, stream);
			return response;
		}
	}
}
