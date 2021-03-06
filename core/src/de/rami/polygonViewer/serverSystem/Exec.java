package de.rami.polygonViewer.serverSystem;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.concurrent.TimeUnit;

import de.rami.polygonViewer.systemAndSettings.Settings;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;


/**
 * @author Rami und Anton
 * Erstellt eine Verbindung zu dem Raspberry Pi mit übergebener Bilderanzahl(IP muss noch angegeben werde, in Bearbeitung)	
 * @param fotosAnzahl
 * @throws IOException
 */
public class Exec {
    public static void connectfromPItoServer(int fotosAnzahl, int belichtungsDauer)
            throws IOException {
    	final String ipServer = Inet4Address.getLocalHost().getHostAddress();
//    	System.out.println(Inet4Address.getAllByName("raspberrypi"));
//    	System.out.println(fotosAnzahl);
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new NullHostKeyVerifier());
//        for(int i = 0; i < InetAddress.)
//        ssh.connect("192.168.2.100", 22);
        ssh.connect(Settings.raspberryIP, 22);
       
        try {
         ssh.authPassword("pi", "raspberry");
            final Session session = ssh.startSession();
            try {
            	System.out.println("BEGINNE MIt tray");
                final Command cmd = session.exec("java -jar Desktop/Driver/Java/client.jar " + fotosAnzahl + " " + ipServer + " " + belichtungsDauer);
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
                cmd.join(5, TimeUnit.SECONDS);
                System.out.println("\n** exit status: " + cmd.getExitStatus());
            } finally {
                session.close();
            }
        } finally {
            ssh.disconnect();
            ssh.close();
        }
    }
}