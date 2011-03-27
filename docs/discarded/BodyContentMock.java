package ikube.web.tag.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

public class BodyContentMock extends BodyContent {

	private StringBuilder builder;

	public BodyContentMock(JspWriter writer) {
		super(writer);
		builder = new StringBuilder();
	}

	@Override
	public Reader getReader() {
		return null;
	}

	@Override
	public String getString() {
		return builder.toString();
	}

	@Override
	public void writeOut(Writer out) throws IOException {
	}

	@Override
	public void clear() throws IOException {
	}

	@Override
	public void clearBuffer() throws IOException {
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public int getRemaining() {
		return 0;
	}

	@Override
	public void newLine() throws IOException {
	}

	@Override
	public void print(boolean b) throws IOException {
	}

	@Override
	public void print(char c) throws IOException {
	}

	@Override
	public void print(int i) throws IOException {
	}

	@Override
	public void print(long l) throws IOException {
	}

	@Override
	public void print(float f) throws IOException {
	}

	@Override
	public void print(double d) throws IOException {
	}

	@Override
	public void print(char[] s) throws IOException {
	}

	@Override
	public void print(String s) throws IOException {
	}

	@Override
	public void print(Object obj) throws IOException {
		builder.append(obj);
	}

	@Override
	public void println() throws IOException {
	}

	@Override
	public void println(boolean x) throws IOException {
	}

	@Override
	public void println(char x) throws IOException {
	}

	@Override
	public void println(int x) throws IOException {
	}

	@Override
	public void println(long x) throws IOException {
	}

	@Override
	public void println(float x) throws IOException {
	}

	@Override
	public void println(double x) throws IOException {
	}

	@Override
	public void println(char[] x) throws IOException {
	}

	@Override
	public void println(String x) throws IOException {
	}

	@Override
	public void println(Object x) throws IOException {
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	}

}