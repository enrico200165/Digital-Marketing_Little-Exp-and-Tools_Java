package com.enrico20165.unica.social;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.enrico_viali.utils.IRenderableAsTextLine;
import com.enrico_viali.utils.TextFileLine;




public class UpdateOutputTxt {
	/** Requires two arguments - the file name, and the encoding to use. */
	public static void main(String... aArgs) throws IOException {
		String fileName = // aArgs[0];
		"C:/Affinium/Campaign/partitions/partition1/Demo/output.txt";
		String encoding = // aArgs[1];
		"";

		FileHelper fh = new FileHelper("output.txt",
				"C:/Affinium/Campaign/partitions/partition1/Demo/output1.txt",
				null);
		ArrayList<IRenderableAsTextLine> buffer = new ArrayList<IRenderableAsTextLine>();
		System.out.println("size " + buffer.size());
		fh.readAll(true, buffer);
		System.out.println("size " + buffer.size());
		String sep = "\t";

		int i = 0;
		for (IRenderableAsTextLine ltemp : buffer) {
			i++;
			TextFileLine l = (TextFileLine)ltemp;
			String[] splitLine = l.get_line().split(sep);			
			String msg = splitLine[9];
			if (!msg.equals("OfferMessage")) {
				splitLine[9] = tstamp(splitLine[9], i);
				String newLine = "";
				for (int j = 0; j < splitLine.length; j++) {
					newLine += splitLine[j] + sep;
				}
				l.set_line(newLine);
			}
			l.set_line(l.get_line()+"\n");			
		}
		
		for (IRenderableAsTextLine l : buffer) {
			System.out.print(l.getLine());
		}
		System.out.println("entriea" + buffer.size());
		fh.write("C:/Affinium/Campaign/partitions/partition1/Demo/output.txt", "", null,  buffer, "", "", null, new LineRenderer(), -1, -1, 100, true);
		
		// test.write();
	}

	/** Constructor. */
	UpdateOutputTxt(String aFileName, String aEncoding) {
		fEncoding = aEncoding;
		fFileName = aFileName;
	}

	static String tstamp(String msg, int row) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		String tstamp = dateFormat.format(date);
		
		
		Long msecs = date.getTime();
		String[][] parole1 = {{ "house", "tablet", "smartphone", "laptop","car","headphone","monitor"},
				{"place", "restaurant", "girl", "walk","tennis","head","racket"}};
		String[][] parole2 = {
				{ "saluti", "ciao", "arrivederci", "buonasera","buonanotte","a presto","addio"},
				{"salut", "hallo", "aurevoir", "hallo","bonjour","bonjour","bonne nuit"}};

		long randomWord1 = msecs % parole1[row%2].length;
		long randomWord2 = msecs % parole2[row%2].length;
		
		return parole2[row%2][(int)randomWord2] + " " + tstamp+","+msecs + " " + parole1[row%2][(int)randomWord1];
	}

	// PRIVATE
	private final String fFileName;
	private final String fEncoding;

	private void log(String aMessage) {
		System.out.println(aMessage);
	}
}
