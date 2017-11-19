package com.enrico200165.emm.unica.interact.client.RunConfigs;

import java.util.ArrayList;

public class TowneBankWebSite implements IRunConfig {
	
	public TowneBankWebSite() {
		super();
		
		this.urlPath = "/interact/servlet/InteractJSService";
		this.host = "localhost";
		this.port = "7001";
		this.channel = "TowneBankWebSite";
		this.audienceLevel = "Individual";
		this.sessionID = null;
		this.audienceId = "Indiv_ID";
		
		// set the client IDs
		intAudIDs = new ArrayList<Integer>();
		
		for (int i = 1; i <= 5; i++) {			
			intAudIDs.add(i);
		}
		
		// add here Interaction points there will be a getOffers for each IP in this list
		this.IPs = new ArrayList<String>();
		IPs.add("AccountMsg");
		
	}
	
	public ArrayList<Double> getDoubleAudIDs() {
		return doubleAudIDs;
	}
	
	public void setDoubleAudIDs(ArrayList<Double> doubleAudIDs) {
		this.doubleAudIDs = doubleAudIDs;
	}
	
	public ArrayList<Integer> getIntAudIDs() {
		return intAudIDs;
	}
	
	public void setIntAudIDs(ArrayList<Integer> intAudIDs) {
		this.intAudIDs = intAudIDs;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getUrlPath()
	 */
	@Override
	public String getUrlPath() {
		return urlPath;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setUrlPath(java.lang.String)
	 */
	@Override
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getHost()
	 */
	@Override
	public String getHost() {
		return host;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setHost(java.lang.String)
	 */
	@Override
	public IRunConfig setHost(String host) {
		this.host = host;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getPort()
	 */
	@Override
	public String getPort() {
		return port;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setPort(java.lang.String)
	 */
	@Override
	public IRunConfig setPort(String port) {
		this.port = port;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getChannel()
	 */
	@Override
	public String getChannel() {
		return channel;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setChannel(java.lang.String)
	 */
	@Override
	public IRunConfig setChannel(String channel) {
		this.channel = channel;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getUrl()
	 */
	@Override
	public String getUrl() {
		return "http://" + this.host + ":" + this.port + this.urlPath;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getAudienceLevel()
	 */
	@Override
	public String getAudienceLevel() {
		return audienceLevel;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setAudienceLevel(java.lang.String)
	 */
	@Override
	public IRunConfig setAudienceLevel(String audienceId) {
		this.audienceLevel = audienceId;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getSessionID()
	 */
	@Override
	public String getSessionID() {
		return sessionID;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setSessionID(java.lang.String)
	 */
	@Override
	public IRunConfig setSessionID(String sessionID) {
		this.sessionID = sessionID;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#getAudienceId()
	 */
	@Override
	public String getAudienceId() {
		return audienceId;
	}
	
	/* (non-Javadoc)
	 * @see IRunConfig#setAudienceId(java.lang.String)
	 */
	@Override
	public IRunConfig setAudienceId(String audienceId) {
		this.audienceId = audienceId;
		return this;
	}
	
	public ArrayList<String> getIPs() {
		return IPs;
	}
	
	public void setIPs(ArrayList<String> iPs) {
		IPs = iPs;
	}
	
	ArrayList<Double>	doubleAudIDs;
	ArrayList<Integer>	intAudIDs;
	
	ArrayList<String>	IPs;
	
	String				host;
	String				port;
	String				channel;
	String				audienceLevel;
	String				sessionID;
	String				audienceId;
	String				urlPath;
}
