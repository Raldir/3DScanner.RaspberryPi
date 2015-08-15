package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsSaver implements java.io.Serializable{
	int glaettungsfaktor, obererSchwellenWert, skalierungswertX, skalierungswertY, polygonAnzahl;
	float bildskalierung;

	public SettingsSaver(int glaettungsfaktor, int obererSchwellenWert, int skalierungswertX, int skalierungswertY, int polygonAnzahl, float bildskalierung) {
		this.glaettungsfaktor = glaettungsfaktor;
		this.obererSchwellenWert = obererSchwellenWert;
		this.skalierungswertX = skalierungswertX; 
		this.skalierungswertY = skalierungswertY;
		this.polygonAnzahl = polygonAnzahl;
		this. bildskalierung = bildskalierung;
		System.out.println(glaettungsfaktor + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
	}
	
	public SettingsSaver(){}
	
	public void systout(){
		System.out.println("sysout mthe "+glaettungsfaktor + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
	}
	
	public void saveSettings(String nameOfSett){
		 try{
				FileOutputStream fout = new FileOutputStream("/Users/Ich/Desktop/"+nameOfSett);
				ObjectOutputStream oos = new ObjectOutputStream(fout); 
				System.out.println("saveStetting" +glaettungsfaktor + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
				System.out.println(this);
				oos.writeObject(this);
				oos.close();
				System.out.println("Done");
				   
			   }catch(Exception ex){
				   ex.printStackTrace();
			   }
	}
	
	public SettingsSaver loadSettings(String nameOfSett){
		 try{
			   FileInputStream fin = new FileInputStream("/Users/Ich/Desktop/"+nameOfSett);
			   System.out.println("/Users/Ich/Desktop/"+nameOfSett);
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   SettingsSaver savedSetts = (SettingsSaver) ois.readObject();
			   ois.close();
			   //useless because it must be null!!! gets the values from previously ceated object not savedSetts
			   System.out.println("loadsett "+glaettungsfaktor + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
			   systout();
			   return savedSetts;
			   
		   }catch(Exception ex){
			   ex.printStackTrace();
			   return null;
		   } 
	}

	public int getGlaettungsfaktor() {
		return glaettungsfaktor;
	}

	public int getObererSchwellenWert() {
		return obererSchwellenWert;
	}

	public int getSkalierungswertX() {
		return skalierungswertX;
	}

	public int getSkalierungswertY() {
		return skalierungswertY;
	}

	public int getPolygonAnzahl() {
		return polygonAnzahl;
	}

	public float getBildskalierung() {
		return bildskalierung;
	}


	public void setGlaettungsfaktor(int glaettungsfaktor) {
		this.glaettungsfaktor = glaettungsfaktor;
	}

	public void setObererSchwellenWert(int obererSchwellenWert) {
		this.obererSchwellenWert = obererSchwellenWert;
	}

	public void setSkalierungswertX(int skalierungswertX) {
		this.skalierungswertX = skalierungswertX;
	}

	public void setSkalierungswertY(int skalierungswertY) {
		this.skalierungswertY = skalierungswertY;
	}

	public void setPolygonAnzahl(int polygonAnzahl) {
		this.polygonAnzahl = polygonAnzahl;
	}

	public void setBildskalierung(float bildskalierung) {
		this.bildskalierung = bildskalierung;
	}

}

