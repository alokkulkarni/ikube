package ikube.index.parse.text;

import ikube.IConstants;
import ikube.index.parse.IParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Michael Couck
 * @since 03.09.10
 * @version 01.00
 */
public class TextParser implements IParser {

	@Override
	public final OutputStream parse(final InputStream inputStream, final OutputStream outputStream) throws Exception {
		StringBuilder builder = new StringBuilder();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, IConstants.ENCODING);
		char[] chars = new char[1024];
		int read = inputStreamReader.read(chars);
		while (read > -1) {
			for (int i = 0; i < read; i++) {
				char c = chars[i];
				if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
					builder.append(c);
				}
			}
			read = inputStreamReader.read(chars);
		}
		outputStream.write(builder.toString().getBytes());
		return outputStream;
	}

}
