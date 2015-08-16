package application;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesSaver {
	Preferences prefs; 
	
	public PreferencesSaver() {
		// Retrieve the user preference node for the package com.mycompany
		this.prefs = Preferences.userNodeForPackage(application.PreferencesSaver.class);
	}
	
	public void setPerf(String name, int value){
		this.prefs.put(name, String.valueOf(value));
		System.out.println("saved Prefs " +value);
	}
	
	public void setPerfFloat(String name, float value){
		this.prefs.put(name, String.valueOf(value));
		System.out.println("saved Prefs "+ value);
	}
	
	public int getPref(String name, int defaultValue){
		System.out.println(this.prefs.getInt(name, defaultValue));
		return this.prefs.getInt(name, defaultValue);
	}
	public float getPrefFloat(String name, float defaultValue){
		//System.out.println();
		return this.prefs.getFloat(name, defaultValue);
	}
	/**
	 * Okaaay might be this is stupid but her we save the number of presets (saved settings) we have saved so far
	 */
	public void setAmountPrefsSaved(String key, int amount){
		this.prefs.put(key, String.valueOf(amount));
		System.out.println("new amount of saved settings = "+amount );
	}
	
	/**
	 * And her we save the individual preset with under its position in the list which we can ask for above method and iterate through it
	 * and its position retrieves the name where it is to find. 
	 */
	public void savePreset(int pos, String name){
		this.prefs.put(String.valueOf(pos), name );
		System.out.println("Die einstellung mit dem Namen " + name +"hatt die pos "+pos);
	}
	//TODO think of a intelligent way to handle the default value...
	/**
	 * 
	 * @param key the key to use is "key"
	 * @return
	 */
	public int getAmountPrefsSaved(String key){
		return this.prefs.getInt(key, 0);
	}
	//TODO think of a intelligent way to handle the default value...
	public String getSavedPreset(int pos){
		return this.prefs.get(String.valueOf(pos), "Failed To retrieve");
	}
	
	public void deletePreset(int pos){
		String path = SettingsSaver.settingsSavePath + getSavedPreset(pos) + ".txt";
		File f = new File(path);
		if(f.delete()){
			System.out.println("deleted");
			if(getAmountPrefsSaved("key")>0){
				for(int i = pos; i <= getAmountPrefsSaved("key"); i++){
					//do i have to do that. cant remember...
					int j = i;
					System.out.println("i"+i+"j"+j);
					System.out.println("vormspeichern"+getSavedPreset(j+1));
					savePreset(i, getSavedPreset(j+1));
					System.out.println( "vormspeichern"+ getSavedPreset(i));
					System.out.println(getSavedPreset(j+1));
				}
				setAmountPrefsSaved("key", getAmountPrefsSaved("key")-1);
			}
		}
		else{
			System.out.println("Hat nicht gekalppt");
		}
	}
	/**
	 * This is a temporary method to clean up shit so I dont get problems when testing
	 */
	public void clear(){
		try {
			this.prefs.clear();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
