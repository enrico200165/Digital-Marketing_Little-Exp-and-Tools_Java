package com.enrico20165.unica.social;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.enrico_viali.utils.IEVRenderableFilter;
import com.enrico_viali.utils.IRenderableAsTextLine;
import com.enrico_viali.utils.ITexLineRenderer;
import com.enrico_viali.utils.StringUtils;
import com.enrico_viali.utils.TextFileLine;
import com.enrico_viali.utils.Utl;

import com.enrico_viali.utils.*;

/**
 * @author it068498 Should contain helper functions for file management
 *         curretly: - reading a file into an array of lines - writing a file,
 *         using functionoids passed as parameters currently rather messy
 */
public class FileHelper {
	
	public FileHelper(String rootPath, String pathName, String encoding) {
		super();
		this.rootPath = StringUtils.adjustPath(rootPath);
		_pathName = StringUtils.adjustPath(pathName);
		_lines = new ArrayList<TextFileLine>();
		_file = new File(pathName);
		
		if (encoding == null || encoding.equals("")) {
			log.info("encoding not provided, using default");
		} else _encoding = encoding;
		
		extBufferPar = null;
		intoInterBuff = false;
	}
	
	public boolean performOnFileOrDir(File f) {
		return readAll(intoInterBuff, extBufferPar);
	}
	
	static public boolean write(String rootPathName, String ext,
			String encoding, Collection<IRenderableAsTextLine> entries,
			String header, String footer, IEVRenderableFilter filter,
			ITexLineRenderer renderer, int first, int last, int maxPerFile,
			boolean overwriteFile) {
		
		String pathName = rootPathName + ext;
		
		pathName = StringUtils.adjustPath(pathName);
		if (entries.size() == 0) {
			log.error("attempt to write to file when empty");
			return false;
		}
		if (!overwriteFile && (new File(pathName)).exists()) {
			log.warn("file exists, will not overwrite it, file: " + pathName);
			return false;
		}
		
		FileOutputStream fos = null;
		OutputStreamWriter outwriter = null;
		String line = "";
		int pathNr = 1;
		try {
			fos = new FileOutputStream(StringUtils.adjustPath(pathName));
			if (encoding != null && encoding.length() > 0) {
				outwriter = new OutputStreamWriter(fos, encoding);
			} else {
				outwriter = new OutputStreamWriter(fos);
			}
			outwriter.write(header);
			
			long scanned = 0;
			int written = 0;
			int inFile = 0;
			for (IRenderableAsTextLine e : entries) {
				scanned++;
				if (first != Utl.NOT_INITIALIZED_INT && scanned < first) continue;
				if (last != Utl.NOT_INITIALIZED_INT && scanned > last) break;
				if (filter != null && !filter.includeIt(e)) continue;
				
				// eventually new file
				if (maxPerFile != Utl.NOT_INITIALIZED_INT
						&& (written % maxPerFile) == 0 && written != 0) {
					log.info("closing file, written: total, in file): "
							+ written + ", " + inFile);
					outwriter.write(footer);
					pathNr++;
					outwriter.flush();
					outwriter.close();
					outwriter = null;
					fos.flush();
					fos.close();
					fos = null;
					fos = new FileOutputStream(pathName + "_" + pathNr + "_"
							+ ext);
					outwriter = new OutputStreamWriter(fos, encoding);
					outwriter.write(header);
					inFile = 0;
				}
				line = renderer.render(e, scanned, written, 0);
				log.debug("writing #: " + scanned + " " + line);
				outwriter.write(line);
				++written;
				++inFile;
			}
			outwriter.write(footer);
			outwriter.flush();
			outwriter.close();
			outwriter = null;
			fos.flush();
			fos.close();
			fos = null;
			log.info("scritto: " + pathName + " \n# esaminati: " + (scanned)
					+ " scritti: " + (written));
			// Runtime rt = Runtime.getRuntime();
			// rt.exec("firefox " + pathName);
			return true;
		} catch (IOException e) {
			log.error("Eccezione di IO", e);
		} finally {
			System.gc();
		}
		return false;
	}
	
	/**
	 * Reads lines in the member "buffer" if not null Appends lines to the
	 * "buffer" passed as parameter if not null
	 * 
	 * @return
	 */
	public boolean readAll(boolean intoInternBuffer,
			ArrayList<IRenderableAsTextLine> appendGloBuffer) {
		boolean rc = true;
		InputStreamReader isr;
		BufferedReader bufferedReader;
		
		if (!(new File(_pathName)).exists()) {
			log.error("read non existing file: " + _pathName);
			return false;
		}
		
		try {
			log.trace("Dir di lavoro: " + System.getProperty("user.dir")
					+ "\nopening: " + _pathName);
			if (_encoding != null && _encoding.length() > 0) isr = new InputStreamReader(
					new FileInputStream(_pathName),
					_encoding);
			else isr = new InputStreamReader(new FileInputStream(_pathName));
			
			bufferedReader = new BufferedReader(isr);
			log.debug("Encoding: in lettura " + isr.getEncoding());
			
			String line = "enrico";
			int lineNr;
			String relPath = _pathName.substring(rootPath.length(),
					_pathName.length());
			for (lineNr = 1; (line = bufferedReader.readLine()) != null; lineNr++) {
				if (intoInternBuffer) _lines.add(new TextFileLine(relPath, lineNr, line));
				if (appendGloBuffer != null) {
					log.error("riattiva e sistema la linea qui sotto, ESCO");
//					appendGloBuffer.add(new TextFileLine(relPath, lineNr, line));
					System.exit(1);
				}
			}
			log.debug("elaborate nr linee input: " + lineNr);
		} catch (IOException e) {
			log.error("", e);
			return false;
		} catch (Exception e) {
			log.error("Exception", e);
			return false;
		}
		return rc;
		
	}
	
	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		_encoding = encoding;
	}
	
	/**
	 * @return the pathName
	 */
	public String getPathName() {
		return _pathName;
	}
	
	/**
	 * @param extBufferPar
	 *            the extBufferPar to set
	 */
	public void setExtBufferPar(ArrayList<IRenderableAsTextLine> extBufferPar) {
		this.extBufferPar = extBufferPar;
	}
	
	/**
	 * @return the lines
	 */
	public ArrayList<String> getLines() {
		ArrayList<String> locBuffer = new ArrayList<String>();
		for (TextFileLine tfl : _lines)
			locBuffer.add(tfl.get_line());
		return locBuffer;
	}
	
	File									_file;
	String									rootPath;
	String									_pathName;
	String									_encoding;
	ArrayList<TextFileLine>					_lines;
	
	ArrayList<IRenderableAsTextLine>		extBufferPar;								// holds parameter for functionoid
	// perform()
	boolean									intoInterBuff;								// holds parameter for functionoid perform()
																						
	private static org.apache.log4j.Logger	log	= Logger
														.getLogger(FileHelper.class);
}
