package com.enrico200165.xtify.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configurazione {
	
	/*
	 * Legge i dati di run da properties
	 * per ora da un unico file
	 */
	
	public Configurazione(String filename) {
		if (null == filename || filename.length() <= 0) {
			filename = "mce.properties";
		}
		setDefaults();
		pr = new Properties();
	}
	
	public void addToConfig(String filename) {
		if (filename == null || filename.length() <= 0) {
			log.warn("config. filename null or empty, exiting");
			return;
		}
		try {
			FileInputStream in = new FileInputStream(filename);
			pr.load(in);
			
			xtifyAppKey = pr.getProperty("xtifyAppKey");
			xtifyAPIKey = pr.getProperty("xtifyAPIKey");
			nSubject = pr.getProperty("nSubject");
			nMessage = pr.getProperty("nMessage");
			
		} catch (IOException e) {
			log.error("non trovato push configuration file,path: \"" + filename + "\", esecuzione abortita");
			System.exit(1);
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		s += "\n" + this.getXtifyAppKey()
				+ "\n" + this.getXtifyAPIKey()
				+ "\n" + this.getnSubject()
				+ "\n" + this.getnMessage();
				
		return s;
	}
	
	public String getXtifyAppKey() {
		return xtifyAppKey;
	}
	
	public void setXtifyAppKey(String xtifyAppKey) {
		this.xtifyAppKey = xtifyAppKey;
	}
	
	public String getXtifyAPIKey() {
		return xtifyAPIKey;
	}
	
	public void setXtifyAPIKey(String xtifyAPIKey) {
		this.xtifyAPIKey = xtifyAPIKey;
	}
	
	public String getnSubject() {
		return nSubject;
	}
	
	public void setnSubject(String nSubject) {
		this.nSubject = nSubject;
	}
	
	public String getnMessage() {
		return nMessage;
	}
	
	public void setnMessage(String nMessage) {
		this.nMessage = nMessage;
	}
	
	public Properties getPr() {
		return pr;
	}
	
	public void setPr(Properties pr) {
		this.pr = pr;
	}
	
	void setDefaults() {
		xtifyAppKey = "0b7427f9-e2d9-4514-9402-63a0b6d84dcd";
		xtifyAPIKey = "f1becb16-6531-4904-8cbe-1cc44c8c7952";
		nSubject = "defFubject";
		nMessage = "defMessage";
	}
	
	String			xtifyAppKey;
	String			xtifyAPIKey;
	String			nSubject;
	String			nMessage;
					
	Properties		pr;
	static Logger	log	= Logger.getLogger(Configurazione.class.getName());
}
