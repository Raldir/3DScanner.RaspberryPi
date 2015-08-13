package application;

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
	
}
