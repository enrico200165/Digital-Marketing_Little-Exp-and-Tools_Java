

package com.enrico200165.emm.unica.interact.client;

/*
 # *******************************************************************************


 
 #  Licensed Materials - Property of IBM 
 #  Unica Interact
 #  (c) Copyright IBM Corporation 2001, 2011.
 #  US Government Users Restricted Rights - Use, duplication or disclosure
 #  restricted by GSA ADP Schedule Contract with IBM Corp. 
 # *******************************************************************************
 */

import java.rmi.RemoteException;
import java.util.*;

import org.apache.log4j.Logger;

import com.enrico200165.emm.unica.interact.client.RunConfigs.ConfigExperiment;
import com.enrico200165.emm.unica.interact.client.RunConfigs.Findomestic_Cookie;
import com.enrico200165.emm.unica.interact.client.RunConfigs.Findomestic_Profile;
import com.enrico200165.emm.unica.interact.client.RunConfigs.Findomestic_Referrer;
import com.enrico200165.emm.unica.interact.client.RunConfigs.Findomestic_Search;
import com.enrico200165.emm.unica.interact.client.RunConfigs.IRunConfig;
import com.enrico200165.emm.unica.interact.client.RunConfigs.RunConfigEUChurn;
import com.enrico200165.emm.unica.interact.client.RunConfigs.RunConfigRatePlan;
import com.enrico200165.emm.unica.interact.client.RunConfigs.TowneBankWebSite;
import com.unicacorp.interact.api.*;
import com.unicacorp.interact.api.jsoverhttp.InteractAPI;

public class InteractEnricoClient {
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		
		Response response = null;
		boolean relyOnExistingSession = false;
		boolean initialDebugFlag = true;
		IRunConfig conn = null;
		
//		conn = new RunConfigEUChurn();				
//		conn = new RunConfigEUSegs();
//		conn = new RunConfigRatePlan();				
		conn = new Findomestic_Search(); 
//		conn = new Findomestic_Referrer(); 
//		conn = new RunConfigInsurance();
//		conn = new Findomestic_Cookie(); 
//		conn = new Findomestic_Profile(); 

		
		
		processCMDLineArgs(args, conn);
		
		// non sembra piÃ¹ funzionare, capire se puÃ² servire ancora a qualcosa
		NameValuePairImpl[] initialAudienceId = new NameValuePairImpl[1];
		initialAudienceId[0] = new NameValuePairImpl();
		initialAudienceId[0].setName(conn.getAudienceId());
		initialAudienceId[0].setValueAsNumeric(conn.getIntAudIDs().get(0)+0.0);
		initialAudienceId[0].setValueDataType(NameValuePair.DATA_TYPE_NUMERIC);
		
		if (newPar == null) {
			log.warn("no parameter value provided, creating a dummy one");
			newPar = new NVPairStr("Dummy1", "dummy_par_value");
		}
		
		/*********************************************************************************
		 * Initialization
		 **********************************************************************************/
		
		NameValuePairImpl[] initialParameters = new NameValuePairImpl[1];
		initialParameters[0] = new NameValuePairImpl();
		initialParameters[0].setName("pippo");
		initialParameters[0].setValueAsString("1");
		newLines(3);
		
		api = InteractAPI.getInstance(conn.getUrl());
		
		newLines(2);
		
		/*********************************************************************************
		 * Method: getOffers
		 **********************************************************************************/
		
		log.info("offers for channel: "+conn.getChannel());
		int nrOffersRequested = 10;
		
		NameValuePairImpl[] noParameters = null;
		
		int ses = 1000;
		for (Integer id : conn.getIntAudIDs()) {
		//for (Double id : conn.getDoubleAudIDs()) {
			conn.setSessionID("" + ses++);
			
			response = api.startSession(conn.getSessionID(), relyOnExistingSession,
					initialDebugFlag, conn.getChannel(), initialAudienceId,
					conn.getAudienceLevel(), initialParameters/*initialParameters*/);
			processStartSessionResponse(response);
			
			NameValuePairImpl currCustID = new NameValuePairImpl();
			currCustID.setName(conn.getAudienceId());
			currCustID.setValueDataType(NameValuePair.DATA_TYPE_NUMERIC);
			currCustID.setValueAsNumeric(id+0.0);
			NameValuePairImpl[] newAudienceId = { currCustID };
			response = api.setAudience(conn.getSessionID(), newAudienceId, conn.getAudienceLevel(),
					noParameters);
			if (response.getStatusCode() == Response.STATUS_SUCCESS) {
				String outFile = "c:\\out\\offerta"+id+".html";
				for (String ip : conn.getIPs()) {
					log.info("---------- getOffers for clID: " + id + " IP: " + ip + " --------------");
					Thread.sleep(300);
					response = api.getOffers(conn.getSessionID(), ip, nrOffersRequested);
					processGetOffersResponse(response, true);
					if (response.getStatusCode() == Response.STATUS_SUCCESS) {
						
						OfferList offerList = response.getOfferList();
						HTMLFileDisplay.writeOut(outFile, "" + id, offerList);
						
						Runtime rt = Runtime.getRuntime();
//						rt.exec(new String[] { "C:/Program Files (x86)/Mozilla Firefox/firefox.exe ", outFile });
						//Thread.sleep(5000);
					} else {
						log.error("get offers failed for clID: " + id);
						System.exit(1);
					}
				}
			} else {
				log.error("set audience failed");
				processSetAudienceResponse(response, false);
			}
			
			response = api.endSession(conn.getSessionID());
			processEndSessionResponse(response);
			// Thread.sleep(200);			
		}
		System.exit(0);
		newLines(3);
		
		/*********************************************************************************
		 * Method: getProfile
		 **********************************************************************************/
		Thread.sleep(500); // NB senza questo fallisce
		
		response = api.getProfile(conn.getSessionID());
		processGetProfileResponse(response, conn);
		newLines(3);
		
		/*********************************************************************************
		 * Method: postEvent
		 **********************************************************************************/
		
		// --- leggiamo attributo, non ricordo perchè  ---
		String attr = "name"; // attributo che tentiamo di leggere
		String value = getProfileAttribute(attr, conn.getSessionID());
		log.info(attr + ": " + value);
		// che faceva?  value = value.length() > 16 ? "a" : (value + "-");
		
		// Attributi 
		NameValuePairImpl[] postEventParameters = { newPar.getIT() };
		postEventParameters[0] = newPar.setVN(attr, "" + value).getIT();
		
		String evento = "Dummy1";
		response = api.postEvent(conn.getSessionID(), evento, postEventParameters);
		processPostEventResponse(response);
		log.info(attr + ": " + getProfileAttribute("action", conn.getSessionID()));
		// System.exit(1);
		newLines(3);
		
		/*********************************************************************************
		 * Method: getOffers
		 **********************************************************************************/
		for (String ip : conn.getIPs()) {
			log.info("---------- getOffers for IP: " + ip + " --------------");
			response = api.getOffers(conn.getSessionID(), ip, nrOffersRequested);
			processGetOffersResponse(response, true);
		}
		newLines(3);
		/*********************************************************************************
		 * Method: setAudience
		 **********************************************************************************/
		/**
		 * For this example, let's keep the same audience level, but change the
		 * id associated (a real life example would be the anonymous user logs
		 * in and becomes known)
		 */
		double newID = 1001920.0;
		log.info("---------- changing ID to " + newID + " ----------");
		NameValuePairImpl custId2 = new NameValuePairImpl();
		custId2.setName(conn.getAudienceId());
		custId2.setValueAsNumeric(newID);
		custId2.setValueDataType(NameValuePair.DATA_TYPE_NUMERIC);
		NameValuePairImpl[] newAudienceId = new NameValuePairImpl[1];
		newAudienceId[0] = custId2;
		
		// Similar to the startSession, parameters can be passed in as well.
		// For this example we could just pass in null;
		{
			noParameters = null;
			// make the call - reuse conn.getSessionID() and audienceLevel from above
			response = api.setAudience(conn.getSessionID(), newAudienceId, conn.getAudienceLevel(),
					noParameters);
			processSetAudienceResponse(response, false);
		}
		for (Double id : conn.getDoubleAudIDs()) {
			custId2.setValueAsNumeric(id);
			response = api.setAudience(conn.getSessionID(), newAudienceId, conn.getAudienceLevel(),
					noParameters);
			processSetAudienceResponse(response, false);
		}
		
		/*********************************************************************************
		 * Method: setDebug
		 **********************************************************************************/
		// performs the same debug toggle as the startSession call
		response = api.setDebug(conn.getSessionID(), false);
		processSetDebugResponse(response);
		response = api.setDebug(conn.getSessionID(), true);
		processSetDebugResponse(response);
		
		/*********************************************************************************
		 * Method: getVersion
		 **********************************************************************************/
		
		response = api.getVersion();
		processGetVersionResponse(response);
		
		/*********************************************************************************
		 * Method: endSession
		 **********************************************************************************/
		response = api.endSession(conn.getSessionID());
		processEndSessionResponse(response);
		
		/*********************************************************************************
		 * Method: executeBatch
		 **********************************************************************************/
		
		/**
		 * For this example, lets combine all the above calls in one by calling
		 * the executeBatch command. The advantage of this is so that we can
		 * minimize the number of trips to the server. To accomplish all the
		 * above calls under one executeBatch (and yes this scenario would be an
		 * unrealistic use case) we could do the following. Note that each
		 * parameter for the above methods have matching setters in the Command
		 * object - except for sessionId.
		 */
		
		/*
		 * build the startSession command
		 */
		Command startSessionCommand = new CommandImpl();
		startSessionCommand.setMethodIdentifier(Command.COMMAND_STARTSESSION);
		startSessionCommand.setInteractiveChannel(conn.getChannel());
		startSessionCommand.setAudienceID(initialAudienceId);
		startSessionCommand.setAudienceLevel(conn.getAudienceLevel());
		startSessionCommand.setEventParameters(initialParameters);
		startSessionCommand.setDebug(initialDebugFlag);
		startSessionCommand.setRelyOnExistingSession(relyOnExistingSession);
		
		/*
		 * build the getOffers command
		 */
		Command getOffersCommand = new CommandImpl();
		getOffersCommand.setMethodIdentifier(Command.COMMAND_GETOFFERS);
		getOffersCommand.setInteractionPoint(conn.getIPs().get(0));
		getOffersCommand.setNumberRequested(nrOffersRequested);
		
		/*
		 * build the postEvent command
		 */
		Command postEventCommand = new CommandImpl();
		postEventCommand.setMethodIdentifier(Command.COMMAND_POSTEVENT);
		postEventCommand.setEventParameters(postEventParameters);
		postEventCommand.setEvent(eventName);
		
		/*
		 * build the getProfile command
		 */
		Command getProfileCommand = new CommandImpl();
		getProfileCommand.setMethodIdentifier(Command.COMMAND_GETPROFILE);
		
		/*
		 * build the setAudience command
		 */
		Command setAudienceCommand = new CommandImpl();
		setAudienceCommand.setMethodIdentifier(Command.COMMAND_SETAUDIENCE);
		setAudienceCommand.setAudienceID(newAudienceId);
		setAudienceCommand.setAudienceLevel(conn.getAudienceLevel());
		
		/*
		 * build the setDebug command
		 */
		Command setDebugCommand = new CommandImpl();
		setDebugCommand.setMethodIdentifier(Command.COMMAND_SETDEBUG);
		setDebugCommand.setDebug(true);
		
		/*
		 * build the getVersion command
		 */
		Command getVersionCommand = new CommandImpl();
		getVersionCommand.setMethodIdentifier(Command.COMMAND_GETVERSION);
		
		// build the endSession command
		Command endSessionCommand = new CommandImpl();
		endSessionCommand.setMethodIdentifier(Command.COMMAND_ENDSESSION);
		
		// Build command array
		Command[] commands = { startSessionCommand, getOffersCommand,
				postEventCommand, getProfileCommand, setAudienceCommand,
				setDebugCommand, getVersionCommand, endSessionCommand };
		
		// make the call - reuse above sessionId
		BatchResponse batchResponse = api.executeBatch(conn.getSessionID(), commands);
		processExecuteBatchResponse(batchResponse);
		
	}
	
	/**
	 * Handle the response of a startSession call
	 * 
	 * @param response
	 */
	public static void processStartSessionResponse(Response response) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("startSession call processed with a warning");
		} else {
			log.info("startSession call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) printDetailMessageOfWarningOrError("StartSession",
				response.getAdvisoryMessages());
		// All responses will return the sessionId that was passed into the
		// calling method (except
		// for getVersion, which does not require a sessionId
		log.info("sessionId: " + response.getSessionID());
	}
	
	static String dumpOffer(Offer offer, String header) {
		String s = header;
		
		// print offer
		s += "\nName: " + offer.getOfferName();
		s += "\nDesc:" + offer.getDescription();
		s += "\nScore:" + offer.getScore();
		s += "\ntreatmentcode:" + offer.getTreatmentCode();
		
		s += "\nattributes:";
		// Let's iterate through the offerAttributes
		for (NameValuePair offerAttribute : offer.getAdditionalAttributes()) {
			s += "\n          " + offerAttribute.getName() + "="
					+ offerAttribute.getValueAsString();
		}
		s += "}";
		
		return s;
	}
	
	/**
	 * Handle the response of a getOffers call
	 * 
	 * @param response
	 */
	public static void processGetOffersResponse(Response response, boolean dump) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
			if (!dump) return;
			OfferList offerList = response.getOfferList();
			if (offerList.getRecommendedOffers() != null) {
				int i = 1;
				for (Offer offer : offerList.getRecommendedOffers()) {
					String s = dumpOffer(offer, "\n------ offerta[" + i + "] ------");
					log.info(s + "\n --- end offer ---");
					i++;
				}
			} else { // count on the default Offer String
				log.info("lista offerte vuota");
			}
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.warn("getOffers call processed with a warning");
		} else {
			log.error("getOffers call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) printDetailMessageOfWarningOrError("getOffers",
				response.getAdvisoryMessages());
	}
	
	/**
	 * Handle the response of a postEvent call
	 * 
	 * @param response
	 */
	public static void processPostEventResponse(Response response) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("postEvent call processed with a warning");
		} else {
			log.info("postEvent call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) {
			printDetailMessageOfWarningOrError("postEvent",
					response.getAdvisoryMessages());
		}
	}
	
	/**
	 * Handle the response of a getProfile call
	 * 
	 * @param response
	 */
	public static void processGetProfileResponse(Response response, IRunConfig conn) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
			log.info("getProfile no warnings or errors");
			
			log.info("-- the attributes of the profile ---");
			
			Map<String, String> unsortMap = new HashMap<String, String>();
			for (NameValuePair nvp : response.getProfileRecord()) {
				
				String name = nvp.getName();
				
				String value = "";
				log.debug(nvp.getName() + "=");
				if (nvp.getValueDataType().equals(
						NameValuePair.DATA_TYPE_DATETIME)) {
					value = "" + nvp.getValueAsDate();
					log.debug("" + nvp.getValueAsDate());
				} else if (nvp.getValueDataType().equals(
						NameValuePair.DATA_TYPE_NUMERIC)) {
					value = "" + nvp.getValueAsNumeric();
					log.debug("" + nvp.getValueAsNumeric());
				} else if (nvp.getValueDataType().equals(
						NameValuePair.DATA_TYPE_STRING)) {
					value = nvp.getValueAsString();
					log.debug("" + nvp.getValueAsString());
				} else {
					log.error("Unknown value type for: " + name + " existing");
					System.exit(-1);
				}
				unsortMap.put(name, value);
			}
			String s = "--- Session: " + unsortMap.get("uacisessionid")
					+ " --- User Id: " + unsortMap.get(conn.getAudienceId().toLowerCase()) + " ---";
			Map<String, String> treeMap = new TreeMap<String, String>(unsortMap);
			for (Map.Entry<String, String> entry : treeMap.entrySet()) {
				s += "\n" + padRight(entry.getKey() + ":", 32)
						+ entry.getValue();
			}
			log.info(s);
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("getProfile call processed with a warning");
		} else {
			log.info("getProfile call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) printDetailMessageOfWarningOrError("getProfile",
				response.getAdvisoryMessages());
		
	}
	
	public static String getProfileAttribute(String attribute, String sessID) {
		
		Response response = null;
		
		try {
			response = InteractEnricoClient.api.getProfile(sessID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
			
			Map<String, String> unsortMap = new HashMap<String, String>();
			for (NameValuePair nvp : response.getProfileRecord()) {
				String name = nvp.getName();
				String value = "";
				if (nvp.getValueDataType().equals(
						NameValuePair.DATA_TYPE_DATETIME)) {
					value = "" + nvp.getValueAsDate();
					log.debug("" + nvp.getValueAsDate());
				} else if (nvp.getValueDataType().equals(
						NameValuePair.DATA_TYPE_NUMERIC)) {
					value = "" + nvp.getValueAsNumeric();
					log.debug("" + nvp.getValueAsNumeric());
				} else if (nvp.getValueDataType().equals(
						NameValuePair.DATA_TYPE_STRING)) {
					value = nvp.getValueAsString();
					log.debug("" + nvp.getValueAsString());
				} else {
					log.error("Unknown value type for: " + name + " existing");
					System.exit(-1);
				}
				unsortMap.put(name, value);
			}
			return "" + unsortMap.get(attribute);
		} else {
			return "";
		}
	}
	
	/**
	 * Handle the response of a setAudience call
	 * 
	 * @param response
	 */
	public static boolean processSetAudienceResponse(Response response, boolean noDump) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
			return true;
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("setAudience call processed with a warning");
		} else {
			log.info("setAudience call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) {
			printDetailMessageOfWarningOrError("setAudience",
					response.getAdvisoryMessages());
		}
		return false;
	}
	
	/**
	 * Handle the response of a setDebug call
	 * 
	 * @param response
	 */
	public static void processSetDebugResponse(Response response) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("setDebug call processed with a warning");
		} else {
			log.info("setDebug call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) printDetailMessageOfWarningOrError("setDebug",
				response.getAdvisoryMessages());
	}
	
	/**
	 * Handle the response of a getVersion call
	 * 
	 * @param response
	 */
	public static void processGetVersionResponse(Response response) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("getVersion call processed with a warning");
		} else {
			log.info("getVersion call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) printDetailMessageOfWarningOrError("getVersion",
				response.getAdvisoryMessages());
	}
	
	/**
	 * Handle the response of a endSession call
	 * 
	 * @param response
	 */
	public static void processEndSessionResponse(Response response) {
		// check if response is successful or not
		if (response.getStatusCode() == Response.STATUS_SUCCESS) {
		} else if (response.getStatusCode() == Response.STATUS_WARNING) {
			log.info("endSession call processed with a warning");
		} else {
			log.info("endSession call processed with an error");
		}
		
		// For any non-successes, there should be advisory messages explaining
		// why
		if (response.getStatusCode() != Response.STATUS_SUCCESS) printDetailMessageOfWarningOrError("endSession",
				response.getAdvisoryMessages());
	}
	
	/**
	 * Handle the response of a startSession call
	 * 
	 * @param response
	 */
	public static void processExecuteBatchResponse(BatchResponse batchResponse) {
		// Top level status code is a short cut to determine if there are any
		// Non successes in
		// the array of Response objects
		if (batchResponse.getBatchStatusCode() == Response.STATUS_SUCCESS) {
			log.info("ExecuteBatch ran perfectly!");
		} else if (batchResponse.getBatchStatusCode() == Response.STATUS_WARNING) {
			System.out
					.println("ExecuteBatch call processed with at least one warning");
		} else {
			System.out
					.println("ExecuteBatch call processed with at least one error");
		}
		
		// Iterate through the array, and print out the message for any
		// non-successes
		for (Response response : batchResponse.getResponses()) {
			if (response.getStatusCode() != Response.STATUS_SUCCESS) {
				printDetailMessageOfWarningOrError("executeBatchCommand",
						response.getAdvisoryMessages());
			}
		}
	}
	
	/**
	 * convenience method to print out the advisory message. In production
	 * systems this info should go to a monitoring or logging service. For this
	 * example, we will just print to standard out.
	 */
	public static void printDetailMessageOfWarningOrError(String command,
			AdvisoryMessage[] messages) {
		log.info("Called " + command);
		for (AdvisoryMessage msg : messages) {
			log.info(msg.getMessage());
			// Some advisory messages may have additional detail:
			log.info(msg.getDetailMessage());
			
			// All advisory messages have a code that will allow the client to
			// implement different
			// behavior based on the type of warning/error
			
			switch (msg.getMessageCode()) {
			
			case AdvisoryMessageCodes.INVALID_INTERACTIVE_CHANNEL: {
				log.info("IC passed in is not valid!!");
				break;
			}
			case AdvisoryMessageCodes.INVALID_INTERACTION_POINT: {
				log.info("IP name passed in is not valid!!");
				break;
			}
			case AdvisoryMessageCodes.INVALID_EVENT_NAME: {
				log.info("Invalid Event Name");
				break;
			}
			default: {
				log.info("Method call failed! code: " + msg.getMessageCode());
			}
			}
			
		}
	}
	
	/**
	 * convenience method to print out a NameValuePair object
	 * 
	 * @param nvp
	 */
	public static void printNameValuePair(NameValuePair nvp) {
		// print out the name:
		log.info(nvp.getName() + "=");
		
		// based on the datatype, call the appropriate method to get the value
		if (nvp.getValueDataType() == NameValuePair.DATA_TYPE_DATETIME) log.info("(DateValue):" + nvp.getValueAsDate());
		else if (nvp.getValueDataType() == NameValuePair.DATA_TYPE_NUMERIC) log.info("(NumericValue):"
				+ nvp.getValueAsNumeric());
		else log.info(")StringValue):" + nvp.getValueAsString());
		log.info("");
	}

	
	static void processCMDLineArgs(String args[], IRunConfig conn) {
		boolean processed = false;
		for (String arg : args) {
			if (arg.matches("^canale=.*")) {
				String s = arg.split("=")[1];
				conn.setChannel(s);
				processed = true;
				continue;
			}
			if (arg.matches("^sessione=.*")) {
				String s = arg.split("=")[1];
				conn.setSessionID(s);
				processed = true;
				continue;
			}
			if (arg.matches("^utente=.*")) {
				double v = Double.valueOf(arg.split("=")[1].trim())
						.doubleValue();
				initialAudienceId[0] = (new NVPairNum("Indiv_ID", v)).getIT();
				processed = true;
				continue;
			}
			if (arg.matches("evento=.*")) {
				eventName = arg.split("=")[1];
				processed = true;
				continue;
			}
			if (arg.matches(".*=.*")) {
				newPar = new NVPairStr(arg.split("=")[0], arg.split("=")[1]);
				processed = true;
				continue;
			}
			System.err.println("parametro non riconosciuto: " + arg + " esco");
			System.exit(1);
		}
		if (!processed) {
			log.info(usageDescription());
		}
	}
	
	public static String dumpResponse(Response response) {
		String s = "";
		return s;
	}
	
	public static void newLines(int nr) {
		for (int i = 0; i < nr; i++) {
			System.out.println("");
		}
	}
	
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
	
	static String usageDescription() {
		
		// andrebbero aggiiunti host e porta, , già presenti nell'oggetto configurazione
		
		String s = "you can(Should) provide parameters on the command line\n";
		s += "with the syntax argument=value";
		s += "\narguments are";
		s += "\ncanale";
		s += "\nsessione";
		s += "\nutente";
		s += "\nevento";
		return s;
	}
	

	
	
	static InteractAPI						api					= null;
	
	static String							interactionPoint	= "UnicaSearchResultsLeftSideOffers ";
	static String							eventName			= "trigreseg";
	static NameValuePairImpl[]				initialAudienceId	= new NameValuePairImpl[1];
	static NVPairStr						newPar				= null;
	
	private static org.apache.log4j.Logger	log					= Logger
																		.getLogger(InteractEnricoClient.class);
}
