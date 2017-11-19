
package com.enrico200165.emm.unica.interact.client;

import com.unicacorp.interact.api.NameValuePair;
import com.unicacorp.interact.api.NameValuePairImpl;

public class NVPairStr {

	public NVPairStr(String name, String value) {
		nvpi = new NameValuePairImpl();
		nvpi.setName(name);
		nvpi.setValueAsString(value);
		nvpi.setValueDataType(NameValuePair.DATA_TYPE_STRING);
	}

	NVPairStr setName(String n) {
		nvpi.setName(n);
		return this;
	}
	
	public NVPairStr set(String value) {
		nvpi.setValueAsString(value);
		nvpi.setValueDataType(NameValuePair.DATA_TYPE_STRING);
		return this;
	}

	NVPairStr setVN(String n,String v ) {
		setName(n);		
		set(v);
		return this;
	}

	
	public NameValuePairImpl getIT() {

		return nvpi;
	}

	NameValuePairImpl nvpi;
}
