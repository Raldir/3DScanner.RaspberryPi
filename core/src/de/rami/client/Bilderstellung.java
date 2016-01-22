package de.rami.client;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * main-Klasse des raspberry
 * @author Rami und Anton
 *
 */
public class Bilderstellung {
	
	
	public static String ip; 
	public static int fotosAnzahl;
	FileListener fl = null;
	
	/**
	 * Startet mit den eingegeben Werten des Nutzers den FileListener, und somit den Server in einem seperaten Thread. Danach wird das Python script ausgeführt
	 */
	public Bilderstellung(){
		//abstandFotos = sc.nextInt();	
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				//fl = new FileListener(Paths.get("/home/pi/Desktop/Driver/Java"), "10.0.4.52");
				System.out.println(ip + "     " + fotosAnzahl);
				fl = new FileListener("/home/pi/Desktop/Driver/Kamera", ip);
//					fl = new FileListener(Paths.get("C:\\Users\\Ramor\\Desktop\\Shoot002"), "127.0.0.1");
				System.out.println("hi1");
			}
		});	
		t2.start();
		try{	
			//Executes the python script
			Process p = Runtime.getRuntime().exec("sudo python /home/pi/Desktop/Driver/Java/SchrittUndPhoto2.py" + " " + fotosAnzahl);
			System.out.println("hi2");
			//p.waitFor();
//			executePython();
			//fl.close();
			
		}
		catch (Exception e){
			String cause = e.getMessage();
			if (cause.equals("python: not found"))
				System.out.println("No python interpreter found.");
		}
	}	
	public static void main(String[] args){
		fotosAnzahl = Integer.parseInt(args[0]);
		ip = args[1];
		System.out.println("hi0");
		Bilderstellung br = new Bilderstellung();
	}
}
