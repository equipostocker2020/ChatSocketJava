package chat.socket.seg.informatica;

import java.io.DataInputStream;

import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

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
				String encriptado = encriptar(mensaje, "encriptar");
	            String desencriptado = desencriptar(encriptado, "encriptar");
	             
	            System.out.println("Cadena Original: " + mensaje);
	            System.out.println("Escriptado     : " + encriptado);
	            System.out.println("Desencriptado  : " + desencriptado);
	            
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

    private SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] claveEncriptacion = clave.getBytes("UTF-8");
         
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
         
        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);
         
        SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");
 
        return secretKey;
    }
 
    public String encriptar(String datos, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = this.crearClave(claveSecreta);
         
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
 
        byte[] datosEncriptar = datos.getBytes("UTF-8");
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
        String encriptado = Base64.getEncoder().encodeToString(bytesEncriptados);
 
        return encriptado;
    }
 
    public String desencriptar(String datosEncriptados, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = this.crearClave(claveSecreta);
 
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
         
        byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
        String datos = new String(datosDesencriptados);
         
        return datos;
    }
}
