package com.enrico200165.xtify.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class XtifyWrapper {
	
	static final String	PUSH20URL	= "http://api.xtify.com/2.0/push";
	
	static int pushIt(String urlString, String pushRequest, 
			String appKey, String APIKey, boolean simulateIt) throws IOException {
		int httpRetCode = -1;
		log.info("requesting push:\n" + pushRequest);
		
		if (simulateIt) {
			log.info("Non mandato veramente");
			return 0;
		}
		String result = null;
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		
		HttpURLConnection urlConn = null;
		OutputStream out = null;
		BufferedReader in = null;
		if (url != null) {
			try {
				urlConn = (HttpURLConnection) url.openConnection();
				// urlConn.addRequestProperty("Content-Type",
				// "application/xml");
				urlConn.addRequestProperty("Content-Type", "application/json");
				
				// urlConn.setRequestMethod("PUT");
				urlConn.setRequestMethod("POST");
				
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				urlConn.connect();
				
				// Write content data to server
				out = urlConn.getOutputStream();
				out.write(pushRequest.getBytes());
				out.flush();
				
				log.info("richiesta inviata");
				
				// Check response code
				httpRetCode = urlConn.getResponseCode();
				if (httpRetCode != HttpURLConnection.HTTP_OK) {
					
					in = new BufferedReader(new InputStreamReader(
							urlConn.getInputStream()), 8192);
					StringBuffer strBuff = new StringBuffer();
					String inputLine;
					
					while ((inputLine = in.readLine()) != null) {
						strBuff.append(inputLine);
					}
					result = strBuff.toString();
					
				} else {
					System.err.println("ERRORE");
				}
				System.out.println(result);
				System.out.println(httpRetCode);
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (urlConn != null) {
					urlConn.disconnect();
				}
			}
		}
		return httpRetCode;
	}
	
	public static int postPushRequest(String appKey, String APIKey, BaseMobilePush pushReq) throws IOException {
		int ret = XtifyWrapper.pushIt(PUSH20URL, pushReq.getJsonRequest(appKey, APIKey), 
				appKey, APIKey,false);
		return ret;
	}
	
	static Logger	log	= Logger.getLogger(XtifyWrapper.class.getName());
}
