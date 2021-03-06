package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

/**
 * 
 * @author Rami und Anton
 *
 */
public class SettingsSaver implements java.io.Serializable{
	/**
	 * I have no idea what that does. If it causes conflicts get rid of it. it works without it, too
	 */
	protected static String settingsSavePath;
	
	private static final long serialVersionUID = -3705430483055238924L;
	int obererSchwellenWert, polygonAnzahl;
	float bildskalierung, grunddicke, skalierungswertX, skalierungswertY;
	
	public SettingsSaver(int obererSchwellenWert, float skalierungswertX, float skalierungswertY, int polygonAnzahl, float bildskalierung, float grunddicke) {
		settingsSavePath = System.getProperty("user.home");
		this.grunddicke = grunddicke;
		this.obererSchwellenWert = obererSchwellenWert;
		this.skalierungswertX = skalierungswertX; 
		this.skalierungswertY = skalierungswertY;
		this.polygonAnzahl = polygonAnzahl;
		this. bildskalierung = bildskalierung;
		System.out.println(grunddicke + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
	}
	
	public SettingsSaver(){}
	
	public void systout(){
		System.out.println("sysout mthe "+grunddicke + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
	}
	
	public String calculatePath(){
		String findDirectory = null;
		try {
			findDirectory = new File("").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(findDirectory.charAt(findDirectory.length() - 8));
		if(sb.charAt(0) == findDirectory.charAt(findDirectory.length() - 8)){
			sb.append(sb.charAt(0));
		}
		return (findDirectory + sb + ".." + sb + "savedSettings" + sb);
	}
	
	public void saveSettings(String nameOfSett){
		 try{
			 	FileOutputStream fout = new FileOutputStream(settingsSavePath + "//" + nameOfSett + ".pref");
				ObjectOutputStream oos = new ObjectOutputStream(fout); 
				System.out.println("saveStetting" +grunddicke + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
				System.out.println(this);
				oos.writeObject(this);
				
				fout.close();
				oos.close();
				
				System.out.println("Done");
				   
			   }catch(Exception ex){
				   ex.printStackTrace();
			   }
	}
	
	public SettingsSaver loadSettings(String nameOfSett){
		 try{
			   FileInputStream fin = new FileInputStream(settingsSavePath + "//" + nameOfSett + ".pref");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   SettingsSaver savedSetts = (SettingsSaver) ois.readObject();
			   
			   fin.close();
			   ois.close();
			   //useless because it must be null!!! gets the values from previously ceated object not savedSetts
			   System.out.println("loadsett "+grunddicke + " "+ obererSchwellenWert + " "+ skalierungswertX + " "+ skalierungswertY + " "+ polygonAnzahl + " "+ bildskalierung);
			   systout();
			   return savedSetts;
			   
		   }catch(Exception ex){
			   ex.printStackTrace();
			   return null;
		   } 
	}

	public float getgrunddicke() {
		return grunddicke;
	}

	public int getObererSchwellenWert() {
		return obererSchwellenWert;
	}

	public float getSkalierungswertX() {
		return skalierungswertX;
	}

	public float getSkalierungswertY() {
		return skalierungswertY;
	}

	public int getPolygonAnzahl() {
		return polygonAnzahl;
	}

	public float getBildskalierung() {
		return bildskalierung;
	}


	public void setgrunddicke(float grunddicke) {
		this.grunddicke = grunddicke;
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


