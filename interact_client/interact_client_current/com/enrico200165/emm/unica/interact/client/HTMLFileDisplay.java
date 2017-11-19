
package com.enrico200165.emm.unica.interact.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.unicacorp.interact.api.NameValuePair;
import com.unicacorp.interact.api.Offer;
import com.unicacorp.interact.api.OfferList;

public class HTMLFileDisplay {
	
	static String getHTML(String codCliente, OfferList offerlist) {
		String s = "";
		String spazioLinee = "<br><br>";
		String fontSize = "150%";
		
		s += "<html><head><meta content=\"text/html; charset=ISO-8859-1\"http-equiv=\"content-type\"><title>Energy&Utilities Model</title></head>";
		
		s += "<body style=\"font-size: "+fontSize+";\"><br><img style=\"width: 100px; height: 100px;\" alt=\"logo\" src=\"http://www.enel.it/common/img/logo-trans-it-IT.png\">";
		s += spazioLinee;
		
		s += "<big>Codice cliente: <span style='color:red'>" + codCliente + "</span></big>";
		
		// offerte
		if (offerlist != null &&  offerlist.getRecommendedOffers() != null) {
			Offer[] offers = offerlist.getRecommendedOffers();
			
			int i = 1;
			for (i = 0; i < offers.length; i++) {
				if (offers[i] == null) 
					log.error("");
				s += spazioLinee;
				s += "<big>Offerta " + (i+1) + ": <span style='color:blue'>" + offers[i].getOfferName() + "</span></big>";
				
				// --- cerchiamo di mostrare lo score ---				
                s += "<br><span>Score: "+offers[i].getScore()+"</span>";
				
                // --- fine attributo
			}
		} else {
			s += spazioLinee;
			s += "<big>Offerta " +  ": <span style='color:blue'>" + "no applicable offers" + "</span></big>";
			log.error("empty offer list for client id: " + codCliente);
		}
		
		// coda
		s += "<br>";
		s += "</body></html>";
		
		return s;
	}
	
	static public void writeOut(String fname, String ID, OfferList offerList) {
		String contents = getHTML(ID, offerList);
		
		FileOutputStream fos = null;
		OutputStreamWriter outwriter = null;
		try {
			fos = new FileOutputStream(fname);
			outwriter = new OutputStreamWriter(fos);
			
			outwriter.write(contents);
			
			outwriter.flush();
			outwriter.close();
			outwriter = null;
			fos.flush();
			fos.close();
			fos = null;
		} catch (IOException e) {
			System.err.print("Eccezione di IO\n" + e.toString());
		}
		
	}
	
	private static org.apache.log4j.Logger	log	= Logger.getLogger(HTMLFileDisplay.class);
}
