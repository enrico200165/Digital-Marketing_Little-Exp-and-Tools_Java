package com.enrico20165.unica.interact.logsfilter;

import java.util.regex.Pattern;

public class InclRegexes {
	public InclRegexes(String inclRegex, String fetchRegex) {
		super();
		this.inclRegex = inclRegex;
		this.fetchRegex = fetchRegex;
		incl = Pattern.compile(inclRegex);		
		if (fetchRegex != null && fetchRegex.length() > 0) {			
			fetch = Pattern.compile(fetchRegex);
		}
	}

	String inclRegex;
	String fetchRegex;
	public Pattern incl;
	public Pattern fetch;
}
