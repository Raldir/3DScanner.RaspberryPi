package application;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesSaver {
	Preferences prefs; 
	
	public PreferencesSaver() {
		// Retrieve the user preference node for the package com.mycompany
		this.prefs = Preferences.userNodeForPackage(application.PreferencesSaver.class);
/*
		// Preference key name
		final String PREF_NAME = "name_of_preference";

		// Set the value of the preference
		String newValue = "a string";
		prefs.put(PREF_NAME, newValue);

		// Get the value of the preference;
		// default value is returned if the preference does not exist
		String defaultValue = "default string";
		String propertyValue = prefs.get(PREF_NAME, defaultValue); // "a string"
*/	}
	
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
	public int getAmountPrefsSaved(String key){
		return this.prefs.getInt(key, 0);
	}
	//TODO think of a intelligent way to handle the default value...
	public String getSavedPreset(int pos){
		return this.prefs.get(String.valueOf(pos), "Failed To retrieve");
	}
	
	public void clear(){
		try {
			this.prefs.clear();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
