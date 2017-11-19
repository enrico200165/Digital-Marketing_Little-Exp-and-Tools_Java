package com.enrico200165.emm.unica.interact.client.RunConfigs;
import java.util.ArrayList;

public class RunConfigEUSegs implements IRunConfig {
	
	public RunConfigEUSegs() {
		super();
		
		this.urlPath = "/interact/servlet/InteractJSService";
		// this.host = "159.122.67.140";
		this.host = "127.0.0.1";
		this.port = "7001";
		this.channel = "00_EU_PredModelsSegments";
		this.audienceLevel = "Customer";
		this.sessionID = null;
		this.audienceId = "CUSTOMERID";

		
		
		
		// set the client IDs
		intAudIDs = new ArrayList<Integer>();

		intAudIDs.add(4106037);
		intAudIDs.add(4109808);
		intAudIDs.add(4144454);
		intAudIDs.add(4174750);
		intAudIDs.add(4220208);
		intAudIDs.add(4240425);
		intAudIDs.add(4288155);
		intAudIDs.add(4327311);
		intAudIDs.add(4337067);
		intAudIDs.add(4368181);
		intAudIDs.add(4417131);
		intAudIDs.add(4532357);
		intAudIDs.add(4567913);
		intAudIDs.add(4588096);
		intAudIDs.add(4603973);
		intAudIDs.add(4644641);
		intAudIDs.add(4722294);
		intAudIDs.add(4732897);
		intAudIDs.add(4742864);
		intAudIDs.add(4746134);
		intAudIDs.add(4788275);
		intAudIDs.add(4789862);
		intAudIDs.add(4837367);
		intAudIDs.add(4891772);
		intAudIDs.add(4892434);
		intAudIDs.add(4983076);
		intAudIDs.add(5028979);
		intAudIDs.add(5050202);
		intAudIDs.add(5079000);
		intAudIDs.add(5079769);
		/*
		*/
		// add here Interaction points there will be a getOffers for each IP in this list
		this.IPs = new ArrayList<String>();
		IPs.add("IP1");
		
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

	ArrayList<String> IPs;
	
	String				host;
	String				port;
	String				channel;
	String				audienceLevel;
	String				sessionID;
	String				audienceId;
	String				urlPath;
}
