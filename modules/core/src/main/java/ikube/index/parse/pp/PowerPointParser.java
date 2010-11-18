package ikube.index.parse.pp;

import ikube.index.parse.IParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;

/**
 * Parser and extractor for the PowerPoint format.
 *
 * @author Michael Couck
 * @since 12.05.04
 * @version 01.00
 */
public class PowerPointParser implements IParser, POIFSReaderListener {

	/** Logger for the parser class. */
	private Logger LOGGER = Logger.getLogger(PowerPointParser.class);
	/** The output stream for the parsed data. */
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String parse(String string) throws Exception {
		POIFSReader reader = new POIFSReader();
		reader.registerListener(this);
		ByteArrayInputStream bis = new ByteArrayInputStream(string.getBytes());
		reader.read(bis);
		return bos.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void processPOIFSReaderEvent(POIFSReaderEvent event) {
		if (!event.getName().equalsIgnoreCase("PowerPoint Document"))
			return;
		try {
			DocumentInputStream input = event.getStream();
			byte buffer[] = new byte[input.available()];
			input.read(buffer, 0, input.available());
			for (int i = 0; i < buffer.length - 20; i++) {
				long type = LittleEndian.getUShort(buffer, i + 2);
				long size = LittleEndian.getUInt(buffer, i + 4);
				if (type == 4008L) {
					bos.write(32);
					bos.write(buffer, i + 4 + 4, (int) size);
					i = (i + 4 + 4 + (int) size) - 1;
				}
			}
		} catch (Exception t) {
			LOGGER.error("Exception thrown during parse", t);
		}
	}
}
