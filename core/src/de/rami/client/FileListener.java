package de.rami.client;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
 

public class FileListener {
 
	
	Client cl = null;
	
	/**
	 *Der FileListener "schaut" permanent nach neuen Files in dem angegebenen Pfad und schickt diese an den Server.
	 * @param path
	 * @param ip
	 */
    public FileListener(String path, String ip){
        try {
            cl = new Client(ip, 1234);
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(path);
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
            System.out.println("Watch Service registered for dir: " + dir.getFileName());
             
            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return;
                }
                
                
                /**
                 * Falls eines der WatchEvents eingetreten ist(Create, Delete) wird das erkannte File mit einem 1 Sekunden Delay zum Server geschickt.
                 */
                for (WatchEvent<?> event : key.pollEvents()) {
                    
                	WatchEvent.Kind<?> kind = event.kind();
                     
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    File f = new File(path + "/" + fileName);
                    if(f.exists()){
	             		try {
							cl.sendFile(f);
	             			System.out.println("hello");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                    System.out.println("thirdPrint" + kind.name() + ": " + fileName);
                    System.out.println(fileName.getFileName().toString());
                    System.out.println(""+ (Bilderstellung.fotosAnzahl-1) + ".jpg");
                    if(fileName.getFileName().toString().equals((""+ (Bilderstellung.fotosAnzahl-1) + ".jpg"))){
                    	System.out.println("knapp vor close");
                    	this.close();
                    	//return;	
                    }
                }       
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
             
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    /**
     * Sorgt dafür, dass der Client dem Server bescheid gibt, dass alle Bilder gesendet wurden. Daraufhin wird der FileListener Prozess beendet.
     * @throws IOException
     */
    public void close() throws IOException{
    	System.out.println("davor close");
    	cl.close();
    	System.out.println("danach close");
    	System.exit(-1);
    }
    
//    public static void main(String[] args){
//    	FileListener l = new FileListener("C:\\Users\\Ramor\\Desktop\\1", "192.168.1.14");
//    }
}