package de.rami.polygonViewer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.connection.channel.direct.Session.Shell;

public class Exec {

		
    public static void connectfromPItoServer(int fotosAnzahl)
            throws IOException {
    	final String ipServer = Inet4Address.getLocalHost().getHostAddress();
//    	System.out.println(ipServer);
//    	System.out.println(fotosAnzahl);
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new NullHostKeyVerifier());
       
        ssh.connect(InetAddress.getByName("raspberrypi").getHostAddress(), 22);
       
        try {
         ssh.authPassword("pi", "raspberry");
            final Session session = ssh.startSession();
            try {
            	System.out.println("BEGINNE MIt tray");
                final Command cmd = session.exec("java -jar Desktop/Driver/Java/client.jar " + fotosAnzahl + " " + ipServer);
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(5, TimeUnit.SECONDS);
                System.out.println("\n** exit status: " + cmd.getExitStatus());
            } finally {
                session.close();
            }
        } finally {
            ssh.disconnect();
        }
    }
    
    public static void main(String[] args){
    	try {
			connectfromPItoServer(16);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}