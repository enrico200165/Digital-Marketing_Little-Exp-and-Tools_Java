package com.enrico20165.unica.social;
import com.enrico_viali.utils.IRenderableAsTextLine;
import com.enrico_viali.utils.ITexLineRenderer;
import com.enrico_viali.utils.TextFileLine;


public class LineRenderer implements ITexLineRenderer {

	public String render(IRenderableAsTextLine e, long scanned, long included,
			int level) {

		TextFileLine tfl = (TextFileLine) e;
		
		
		return tfl.get_line();
	}

}
