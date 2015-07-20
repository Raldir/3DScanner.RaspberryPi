package de.rami.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

public class Client {

	Socket socket;
	
	/**
	 * Konstruktur, der einen neuen Socket mit übergebener Ip und Port erstellt.
	 * @param ip
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(String ip, int port) throws UnknownHostException, IOException{
		socket = new Socket(ip, port);
	}
	/**
	 * Schickt die übergebenen Bytes zum Server
	 * @param b
	 * @throws IOException
	 */
	public void sendBytes(byte[] b) throws IOException {
		DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
		writer.writeInt(b.length);
		for (int i = 0; i < b.length; i += 512) {
			writer.write(b, i, Math.min(512, b.length - i));
			writer.flush();
		}
	}

	/**
	 * Schickt das File, welches in die Bytes zerlegt wurde zum Server. Um sicherzustellen, dass der Server das vorherige Bild erhalten hat, schickt der Server dem Clienten eine
	 * Nachricht, welche der Client überprüft.
	 * @param 
	 * @throws Exception
	 */
	public void sendFile(File f) throws Exception {
		sendNotification("sendFile");
		byte[] b = Files.readAllBytes(f.toPath());
		sendBytes(b);
		if (!readNotification().equals("fileReceived")) {
			throw new Exception("no Files received");
		}

	}

	public void close() throws IOException {
		sendNotification("close");
	}
	
	/**
	 * Gibt die Nachricht zurück, die vom InputStream ausgelesen wird.
	 * @return
	 * @throws IOException
	 */
	private String readNotification() throws IOException {
		DataInputStream in = new DataInputStream(socket.getInputStream());
		return in.readUTF();
	}

	/**
	 * Erstellt einen Outputstream und schickt die übergeben Nachricht zum Server.
	 * @param s
	 * @throws IOException
	 */
	private void sendNotification(String s) throws IOException {
		DataOutputStream in = new DataOutputStream(socket.getOutputStream());
		in.writeUTF(s);
		in.flush();
	}
}