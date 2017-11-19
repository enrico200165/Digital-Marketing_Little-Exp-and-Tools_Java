package com.enrico20165.unica.interact.logsfilter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {

	public Main() {
		regexesToCatch = new ArrayList<InclRegexes>();

		tstampRemove = Pattern.compile("^[\\d]{4}-[\\d]{2}-[\\d]{1}");

		// =====================================

		regexesToCatch.add(new InclRegexes(
				".*",
				null));

		
		regexesToCatch.add(new InclRegexes(
				".*ClientAPIThread:postEvent.*com.unicacorp.interact.api.InteractServiceImpl.*InputParameters.*",
				null));

		regexesToCatch.add(new InclRegexes(
				".*ClientAPIThread:startSession.*com.unicacorp.interact.api.InteractServiceImpl.*InputParameters.*",
				null));
		regexesToCatch.add(new InclRegexes(
				".*FlowchartEngine: Scheduling flowchart id.*", null));
		regexesToCatch.add(new InclRegexes(".*Result of expression.*=1\\.0.*", null));
		regexesToCatch.add(new InclRegexes(".*FlowchartEngine: Completed flowchart id.*",
				null));
		// incl.add(".*Starting execution of process box.*");
		regexesToCatch.add(new InclRegexes(".*Assigning AudienceId.*to Segment.*", null));
		regexesToCatch.add(new InclRegexes(".*XXX.*", ""));

	}

	void perform() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("C:/Unica/Interact/logs/Interact.log"
							//"C:/IBM/WebSphere_v8/AppServer/profiles/AppSrv01/Interact.log"
							)));
			read(br);
		} catch (Exception e) {
			System.err.print("\neccezione!!!" + e);
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.perform();
	}

	String mustBeIncluded(String line, ArrayList<InclRegexes> expressions) {
		boolean exclude = false;

		for (InclRegexes exp : expressions) {
			if (line.matches(exp.inclRegex)) {
				if (exp.fetchRegex != null && exp.fetchRegex.length() > 0) {
					String dummy;
					String ret = "";
					Pattern genericMarking = Pattern.compile(exp.fetchRegex);
					Matcher genMatcher = genericMarking.matcher(line);
					while (genMatcher.matches()) {
						dummy = "" + genMatcher.start() + "  "
								+ genMatcher.end();
						String grp = genMatcher.group(1);
						ret += grp + " ";
					}
					return ret;
				} else {
					return line;
				}

			}
		}
		return null;
	}

	void read(BufferedReader br) {
		String line1 = "";
		String line2 = "";
		int nrPrinted = 0;
		int nrTot = 0;
		try {
			line1 = br.readLine();
			while (line1 != null && line2 != null) {
				nrTot++;
				line2 = br.readLine();
				if (line2 == null)
					break;
				
				if (!line2.matches("^201[\\d].*")) {
					line1 += line2;
					continue;
				}
				
				if (mustBeIncluded(line1, regexesToCatch) != null) {
					nrPrinted++;
					System.out.println(cleanUpLine(line1));
				}
				line1 = line2;
			}
			if (line2 != null)
				System.out.println(line2);

			System.out.print("\nline stampate nr: " + nrPrinted + "/" + nrTot);
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}

	}

	String cleanUpLine(String l) {
		String ret = l;
		Matcher m = tstampRemove.matcher(l);
		// if (m.find())
		ret = m.replaceAll("");
		if (ret.matches(".*\\[[^\\]]*\\[.*")) {
			ret = ret.replaceAll("\\[[^]^\\[]*\\]", ""); // parentesi interne
		}
		// ret = ret.replaceAll("\\[[^]^\\[]*\\]", ""); // parentesi interne
		ret = ret.replaceAll("CFS DEBUG", "");
		ret = ret.replaceAll("com\\.[\\w.]*", "");
		return ret;
	}

	Pattern tstampRemove;
	Matcher tstampMatcher;
	ArrayList<String> excl;
	ArrayList<InclRegexes> regexesToCatch;
}
