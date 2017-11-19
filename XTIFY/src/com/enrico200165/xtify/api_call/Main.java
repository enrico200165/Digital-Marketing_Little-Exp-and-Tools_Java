package com.enrico200165.xtify.api_call;

import com.enrico200165.xtify.utils.*;

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

public class Main {
	
	// NORMAL USATA 
	private static final String	XTIFY_APP_KEY	= "0b7427f9-e2d9-4514-9402-63a0b6d84dcd";
	private static final String	XTIFY_API_KEY	= "f1becb16-6531-4904-8cbe-1cc44c8c7952";
	
	
	public static void main(String args[]) throws IOException {
		
		String tstamp = "inviata alle: "
				+ new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
		
		// build notification request
		com.enrico200165.xtify.utils.BaseMobilePush push = new com.enrico200165.xtify.utils.BaseMobilePush();
		push.setSubject("from API").setMessage(tstamp+" "+"sent including a jar in your app");		
		
		com.enrico200165.xtify.utils.XtifyWrapper.postPushRequest("0b7427f9-e2d9-4514-9402-63a0b6d84dcd",
				"f1becb16-6531-4904-8cbe-1cc44c8c7952", push );
				
	}
	
	static Logger	log	= Logger.getLogger(Main.class.getName());
}