package com.enrico200165.emm.unica.interact.client.RunConfigs;
import java.util.ArrayList;

public interface IRunConfig {

	
	// -- list of interaction points
	public abstract ArrayList<String> getIPs();

	// list of client IDs if they are double, int etc
	public abstract ArrayList<Double> getDoubleAudIDs();
	public abstract ArrayList<Integer> getIntAudIDs();

	
	
	public abstract String getUrlPath();
	
	public abstract void setUrlPath(String urlPath);
	
	public abstract String getHost();
	
	public abstract IRunConfig setHost(String host);
	
	public abstract String getPort();
	
	public abstract IRunConfig setPort(String port);
	
	public abstract String getChannel();
	
	public abstract IRunConfig setChannel(String channel);
	
	public abstract String getUrl();
	
	public abstract String getAudienceLevel();
	
	public abstract IRunConfig setAudienceLevel(String audienceId);
	
	public abstract String getSessionID();
	
	public abstract IRunConfig setSessionID(String sessionID);
	
	public abstract String getAudienceId();
	
	public abstract IRunConfig setAudienceId(String audienceId);
	
}