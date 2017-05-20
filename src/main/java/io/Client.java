package main.java.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import main.java.engine.Core;
import main.java.utils.Consts;

public class Client extends IO {
	private PrintWriter server = null;
	private String notation = null;

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

	public class ClientThread implements Runnable {
		private Socket connexion = null;
		private BufferedInputStream reader = null;
		private String[] listCommands = { "INFO", "DATE", "HOUR", "NONE" };

		public ClientThread(Socket socket) {
			this.connexion = socket;
		}

		public void run() {
			try {
				server = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());
				// On envoie la commande au serveur
				String commande = getCommand();
				server.write(commande);
				// TOUJOURS UTILISER flush() POUR ENVOYER RÉELLEMENT DES INFOS
				// AU SERVEUR
				server.flush();
				System.out.println("Commande " + commande + " envoyée au serveur");
				// On attend la réponse
				String response = read();
				System.out.println("\t * " + notation + " : Réponse reçue " + response);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			server.write("CLOSE");
			server.flush();
			server.close();
		}

		// Méthode qui permet d'envoyer des commandeS de façon aléatoire
		private String getCommand() {
			Random rand = new Random();
			return listCommands[rand.nextInt(listCommands.length)];
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
