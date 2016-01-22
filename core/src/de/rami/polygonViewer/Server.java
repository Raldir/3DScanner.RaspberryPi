package de.rami.polygonViewer;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

/**
 * Erzeugt Server
 * @author Rami und Anton
 *
 */
public class Server {

	ServerSocket serverSocket;
	Socket client;
	FileReceiveListener listener = null;
	ConnectionCloseListener clistener = null;
		
	
	public Server(int port) throws IOException{
		serverSocket = new ServerSocket(port);
		client = waitForClientLogin();
	}
	
	/**
	 * der Server wartet solange, bis der Client sich verbunden hat
	 * @return
	 * @throws IOException
	 */
	public Socket waitForClientLogin() throws IOException {
		Socket socket = serverSocket.accept();
		
		return socket;
	}

	/**
	 * Je nachdem welche Nachricht der Client dem Server liefert, wird entweder Das geschickte File ausgelesen und weiterverarbeitet, oder die Verbindung getrennt.
	 * @throws IOException
	 */
	public void listen() throws IOException {
		DataInputStream in = new DataInputStream(client.getInputStream());
		String s = in.readUTF();
		if (s.equals("sendFile")) {
			final File f = readFile();
			if (listener != null) {
				listener.fileReceived(f);
			}
			sendNotification("fileReceived");
			listen();
		} else if (s.equals("close")) {
			if (clistener != null) {
				clistener.connectionClosed();
			}
		}
	}

	public void setReceiveAction(FileReceiveListener listener) {
		this.listener = listener;
	}
	
	public void setCloseAction(ConnectionCloseListener clistener) {
		this.clistener = clistener;
	}

	/**
	 * Sorgt dafuer, dass die ueberlieferten Bytes als File abgespeichert werden.
	 * @return
	 */
	private File readFile() {
		File temp = null;
		try {
			temp = File.createTempFile(System.currentTimeMillis() + "   temp", ".an");
			FileOutputStream out = new FileOutputStream(temp);
			out.write(readBytes());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Gibt die Bytes zurueck, die vom Clienten Uebertragen wurden.
	 * @return
	 * @throws IOException
	 */
	private byte[] readBytes() throws IOException {
		DataInputStream in = new DataInputStream(client.getInputStream());
		int i = in.readInt();
		byte[] b = new byte[i];
		int total = 0;
		while (total < b.length) {
			total += in.read(b, total, b.length - total);
		}
		return b;
	}

	/**
	 * Schickt dem Clienten die Uebergebene Nachricht.
	 * @param s
	 * @throws IOException
	 */
	private void sendNotification(String s) throws IOException {
		DataOutputStream in = new DataOutputStream(client.getOutputStream());
		in.writeUTF(s);
		in.flush();
	}

}