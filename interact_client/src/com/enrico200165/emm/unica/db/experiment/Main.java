
package com.enrico200165.emm.unica.db.experiment;


import org.apache.log4j.Logger;






public class Main {

	public static void main(String[] s) {
		TableFileMgr tfm  = new TableFileMgr();
		log.info("creato il manager");
		tfm.perform();
	}


	private static org.apache.log4j.Logger log = Logger.getLogger(Main.class);
}
