package com.enrico200165.xtify.api_send;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.enrico200165.xtify.utils.BaseMobilePush;
import com.enrico200165.xtify.utils.Configurazione;
import com.enrico200165.xtify.utils.XtifyWrapper;

public class Main {
	
	// NORMAL USATA 
	private static final String	XTIFY_APP_KEY	= "0b7427f9-e2d9-4514-9402-63a0b6d84dcd";
	private static final String	XTIFY_API_KEY	= "f1becb16-6531-4904-8cbe-1cc44c8c7952";
	
	// private static final String	XTIFY_SECRET	= "360071e3-76a7-4713-82d3-74d4396f46be";
	// private static final String	SENDER_ID		= "1097835681706";
	
	public static void main(String args[]) throws IOException {
		
		String pushCfgFile = "";
		String keysFile = "";
		
		// contenuti notifica
		if (args.length >= 1) {
			pushCfgFile = args[0];
		}		
		// chiavi eventualmente nel secondo file
		if (args.length >= 2) {
			keysFile = args[1];
		}
							
		
		Configurazione cf = new Configurazione(null);
		cf.addToConfig(pushCfgFile);
		log.info("config:\n" + cf.toString());
		cf.addToConfig(keysFile);
		log.info("config:\n" + cf.toString());

		// build notification request
		BaseMobilePush push = new BaseMobilePush();
		String tstamp = "inviata alle: "
				+ new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
		push.setSubject(cf.getnSubject()).setMessage(tstamp+" "+cf.getnMessage());
		XtifyWrapper.postPushRequest(cf.getXtifyAppKey(),cf.getXtifyAPIKey(), push );
				
	}
	
	static Logger	log	= Logger.getLogger(Main.class.getName());
}