package com.enrico200165.emm.unica.interact.client;

import com.unicacorp.interact.api.NameValuePair;
import com.unicacorp.interact.api.NameValuePairImpl;

public class NVPairNum {

	public NVPairNum(String name, double value) {
		nvpi = new NameValuePairImpl();
		nvpi.setName(name);
		nvpi.setValueAsNumeric(value);
		nvpi.setValueDataType(NameValuePair.DATA_TYPE_NUMERIC);
	}

	NVPairNum setName(String n) {
		nvpi.setName(n);
		return this;
	}
	
	NVPairNum set(double value) {
		nvpi.setValueAsNumeric(value);
		nvpi.setValueDataType(NameValuePair.DATA_TYPE_NUMERIC);	
		return this;
	}
	
	NVPairNum setVN(String n,double v ) {
		setName(n);
		set(v);
		return this;
	}
	
	public NameValuePairImpl getIT() {
		return nvpi;
	}

	NameValuePairImpl nvpi;
}