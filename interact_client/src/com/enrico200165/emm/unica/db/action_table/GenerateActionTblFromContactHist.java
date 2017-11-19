
package com.enrico200165.emm.unica.db.action_table;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.enrico_viali.libs.rdb_jdbc.JDBCEVTable;
import com.enrico_viali.libs.rdb_jdbc.RDBManagerMicrosoftSQLSrv;
import com.enrico_viali.libs.rdb_jdbc.StatementEV;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.enrico_viali.libs.rdb_jdbc.JDBCEVTable;
import com.enrico_viali.libs.rdb_jdbc.RDBManagerMicrosoftSQLSrv;

public class GenerateActionTblFromContactHist {

	/* Se ricostruisco bene le mie intenzioni dopo mesi lo scopo era
	 * Creare una action table su file basandomi sulla contact table o sulla response table
	 * (no ricordo quale delle due
	 *  
	 * Uso come esempio  <db sistema>.UA_ActionCustomer 
	 * tento un matching,
	 * non so se dovrei usare la contact ID con la contact history UA_IndivContactHistory: 
	 * [CustomerID] :      Indiv_ID, 1 
	 * [ActionDateTime] :  ContactDateTime 3 
	 * [ResponseChannel] : Contact_Channel 
	 * [CampaignCode] :    Campaign code 
	 * [OfferCode] :       Offer_Code
	 * [CellCode] :        Cell_Name  (controllare se effettivamente uguali
	 * [TreatmentCode] :   TreatmentInstID 
	 * [ProductID] :
	 * [ResponseTypeCode] : 
	 * 
	 * Campi disponibili nella join delle contact tables
	 * 
	 * Indiv_ID CellID PackageID ContactDateTime UpdateDateTime ContactStatusID
	 * DateID TimeID UserDefinedFields ValueBefore UsageBefore Campaign_Code
	 * Offer_Code Cell_Name Contact_Channel DirectResponse Product_Promo ---
	 * presumibilmente dalla tabella di dettaglio Indiv_ID TreatmentInstID
	 * ContactStatusID ContactDateTime UpdateDateTime DateID TimeID
	 * UserDefinedFields ValueBefore UsageBefore Campaign_Code Offer_Code
	 * Cell_Name Contact_Channel DirectResponse Product_Promo RTSelectionMethod
	 */

	public static void main(String[] s) {
		GenerateActionTblFromContactHist tfm = new GenerateActionTblFromContactHist();
		tfm.perform();
	}

	public GenerateActionTblFromContactHist() {
		super();
		contactTFields = new ArrayList<String>();
		buildFieldList();
	}

	/**
	 * gestisce i nomi delle colonne da escludere dal result set
	 */
	void buildFieldList() {
		contactTFields.add(contactTable + "." + "Indiv_ID");
		contactTFields.add(contactTable + "." + "CellID");
		contactTFields.add(contactTable + "." + "ContactDateTime");
		contactTFields.add(contactTable + "." + "ContactStatusID");
		contactTFields.add(contactTable + "." + "Campaign_Code");
		contactTFields.add(contactTable + "." + "Offer_Code");
		contactTFields.add(contactTable + "." + "Contact_Channel");
		contactTFields.add(contactTable + "." + "DirectResponse");
	}

	String fields() {
		String s = "";
		for (int i = 0; i < contactTFields.size(); i++) {
			s += contactTFields.get(i);
			if (i < contactTFields.size() - 1)
				s += " ,";
		}
		return s;
	}

	public void perform() {

		String campSysDB = "emm_camp";
		RDBManagerMicrosoftSQLSrv mgr = null;

		try {
			mgr = new RDBManagerMicrosoftSQLSrv(
					"com.microsoft.sqlserver.jdbc.SQLServerDriver",
					"jdbc:sqlserver" /* DBMSURLPar */, "192.168.140.10",
					campSysDB,// dbName,
					"integratedSecurity=true;" /* var URL */, "sa",
					"unica*03", false, true);

			if (!mgr.open(false)) {
				log.error("fallita apertura connessione, esco");
				System.exit(1);
			}

			JDBCEVTable contactH = new JDBCEVTable("UA_IndivContactHistory",mgr); // non necessaria
			contactH.loadFromDB();
			contactH.readMetadata();
			log.info(contactH.toString());

			JDBCEVTable contactHDet = new JDBCEVTable("UA_IndivDtlContactHist",mgr); // non necessaria
			contactHDet.loadFromDB();
			contactHDet.readMetadata();
			log.info(contactHDet.toString());

			String query = "Select " + fields();
			query += " from " + contactTable; /*
											 * + ", "+ contactHDet.getName();
											 * query += " where " + contactTable
											 * + ".Indiv_ID = " +
											 * contactHDet.getName() +
											 * ".Indiv_ID;";
											 */
			String sep = "\t";
			StatementEV stmt = new StatementEV(mgr.getConnection(false).createStatement());
			ResultSet rs = stmt.executeQuery(query, true);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();

			// GESTIONE COLONNE DA ESCLUDERE (NON RICORDO PERCHè FACCIO QUESTO
			// !!!
			// metto i nomi di eventuali colonne da escludere
			Set<String> nomiColsToExclude = new HashSet<String>();
			// qui dovrei inserire i nomi da escludere, ne metto uno dummy
			nomiColsToExclude.add("colonnaPippo");
			Set<Integer> nrColsToExclude = new HashSet<Integer>();
			// popoliamo i nr di colonne da escludere
			
			// avaloro l'header
			String header = "";
			for (int i = 1; i <= numberOfColumns; i++) {
				String colName = rsmd.getColumnName(i);
				if (!nomiColsToExclude.contains(colName)) {
					header += colName + sep;
				} else {
					nrColsToExclude.add(i);
					log.info("ignored column: " + colName);
				}
			}

			// ======== scriviamo il file
			FileOutputStream fos = null;
			OutputStreamWriter outwriter = null;
			String line = "";
			int pathNr = 1;
			int rowNum = 0;
			try {
				fos = new FileOutputStream(fileActTablePathName);
				outwriter = new OutputStreamWriter(new FileOutputStream(
						fileActTablePathName), "UTF-8");

				outwriter.write(header + "\n");

				int limitRows = 1000; // per non scrivere troppo in testing
				while (rs.next() && (rowNum < limitRows)) {
					rowNum++;
					line = "";
					for (int i = 1; i <= numberOfColumns; i++) {
						if (nrColsToExclude.contains(i)) {
							System.out.println("saltata colonna: " + i
									+ " nome: " + rsmd.getColumnName(i));
							// salto la colonna
						} else {
							line += rs.getString(i) + sep;
							// QUI DOVREI AGGIUNGERE I VALORI DI AZIONE
						}
					}
					line += "\n";
					log.info("writing: " + line);
					outwriter.write(line);
				}
				outwriter.flush();
				outwriter.close();
				outwriter = null;
				fos.flush();
				fos.close();
				fos = null;
			} catch (IOException e) {
				log.error("Eccezione di IO", e);
			}

			log.info("scritto: " + fileActTablePathName + " scritti: "
					+ (rowNum));

			ProcessBuilder pb = new ProcessBuilder(
					"C:/Program Files (x86)/Notepad++/notepad++.exe",
					fileActTablePathName);
			pb.start();
		} catch (Exception e) {
			log.error("Eccezione di IO", e);
		}
	}

	/* per generare dentro VM
	String UnicaHome = "C:\\Unica\\Campaign\\";
	String fileActTablePathName = UnicaHome
			+ "partitions\\partition1\\invidual_action_table.csv";
*/
	String fileActTablePathName = "invidual_action_table.csv";
	static final String contactTable = "UA_IndivContactHistory";
	ArrayList<String> contactTFields;

	private static org.apache.log4j.Logger log = Logger
			.getLogger(GenerateActionTblFromContactHist.class);
}
