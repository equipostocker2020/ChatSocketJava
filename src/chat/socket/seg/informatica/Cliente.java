package chat.socket.seg.informatica;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente implements Runnable {

	private int puerto;
	private String mensaje;

	public Cliente(int puerto, String mensaje) {
		this.puerto = puerto;
		this.mensaje = mensaje;
	}
	
	@Override
	public void run() {
		// host server
		final String HOST = "127.0.0.1";
		// port server
		DataOutputStream out;
		try {
			Socket sc = new Socket(HOST, puerto);
			out = new DataOutputStream(sc.getOutputStream());
			// aca se envia mensaje al cliente
			out.writeUTF(mensaje);
			
		} catch (IOException ex) {
			// en caso de exception se logea clase y se maneja lvl del log.
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
