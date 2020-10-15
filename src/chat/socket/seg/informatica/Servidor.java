package chat.socket.seg.informatica;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Observable implements Runnable {
	
	private int puerto;
	
	public Servidor(int puerto) {
		this.puerto=puerto;
	}
	@Override
	public void run() {
		ServerSocket servidor = null;
		Socket sc = null;
		DataInputStream in;
		
		try {
			//creando socket al server.
			servidor = new ServerSocket(puerto);
			System.out.println("Servidor Iniciado");
			
			// siempre escucha las peticiones ...
			while (true) {
				//esperando al cliente ...
				sc = servidor.accept();
				System.out.println("Cliente conectado");
				in = new DataInputStream(sc.getInputStream());
				
				//leyendo el mensaje recibido
				String mensaje = in.readUTF();
				System.out.println(mensaje);
				
				this.setChanged();
				this.notifyObservers(mensaje);
				this.clearChanged();
				
				//cerrando socket
				sc.close();
				System.out.println("Cliente desconectado");
			}
		} catch (Exception ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
