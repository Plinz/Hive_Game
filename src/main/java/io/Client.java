package main.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private Thread _t;
	private Socket _s;
	private PrintWriter _out;
	private BufferedReader _in;
	private Server _blablaServ;

	Client(Socket s, Server blablaServ) {
		_blablaServ = blablaServ;
		_s = s;
		try {
			_out = new PrintWriter(_s.getOutputStream());
			_in = new BufferedReader(new InputStreamReader(_s.getInputStream()));
			blablaServ.addClient(_out);
		} catch (IOException e) {
		}

		_t = new Thread(this);
		_t.start();
	}

	// ** Methode : exécutée au lancement du thread par t.start() **
	// ** Elle attend les messages en provenance du serveur et les redirige **
	// cette méthode doit obligatoirement être implémentée à cause de
	// l'interface Runnable
	public void run() {
		String message = "";
		try {
			char charCur[] = new char[1];
			while (_in.read(charCur, 0, 1) != -1) {
				if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
					message += charCur[0];
				else if (!message.equalsIgnoreCase("")) {
					_blablaServ.receiveMove(message);
					message = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				System.out.println("Le client s'est deconnecte");
				_blablaServ.delClient();
				_s.close();
			} catch (IOException e) {
			}
		}
	}
}
