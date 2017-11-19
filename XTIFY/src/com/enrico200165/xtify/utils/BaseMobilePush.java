package com.enrico200165.xtify.utils;
public class BaseMobilePush {
	
	public BaseMobilePush() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getJsonRequest(String appKey, String APIKey) {
		String eol = "\n";
		String request = "{" + eol
		+ "\"apiKey\" : \"" + APIKey + "\"," + eol
		+ "\"appKey\" : \"" + appKey + "\"," + eol
		// +"\"xids\" : [\"544f50f81fde007c80821ce9\"]," + eol
		+ "\"sendAll\": true,"
		+ eol
		// +"\"hasTags\" : []," + eol
		// +"\"notTags\": [],"+ eol
		// +"\"sendAll\": true,"+ eol
		// +"\"inboxOnly\" : false,"+ eol
		+ "\"content\": {" + eol
		+ "    \"subject\":\"" + subject + "\"," + eol
		+ "    \"message\": \"" + message + "\"," + eol
		+ "    \"action\": {" + eol
		+ "            \"type\": \"DEFAULT\"" + eol
		+ "        }" + eol
		+ "    }" + eol
		// +"    \"payload\": \"{'key1': 'value1','key2':'value2'}\"," +
		// eol
		// +"    \"sound\": \"default.caf\"," + eol
		// +"    \"badge\": \"+1\"" + eol
		+ "  }" + eol + "}";
				return request;
	}
	
	
	public String getnSubject() {
		return subject;
	}

	public BaseMobilePush setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getnMessage() {
		return message;
	}

	public BaseMobilePush setMessage(String nMessage) {
		this.message = nMessage;
		return this;
	}


	String subject;
	String message;
}
