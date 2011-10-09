package ikube.model;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
@Entity()
@SuppressWarnings("serial")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class IndexableInternet extends Indexable<IndexableInternet> {

	@Transient
	private transient String		currentUrl;
	@Transient
	private transient InputStream	currentInputStream;
	@Transient
	private transient Pattern		pattern;
	@Transient
	private transient URI			uri;
	@Transient
	private transient String		baseUrl;

	private String					url;
	private int						internetBatchSize;
	private String					excludedPattern;
	private int						timeout;
	@Field()
	private String					titleFieldName;
	@Field()
	private String					idFieldName;
	@Field()
	private String					contentFieldName;

	public URI getUri() {
		if (uri == null && getUrl() != null) {
			try {
				uri = new URI(getUrl());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return uri;
	}

	public void setUri(final URI uri) {
		this.uri = uri;
	}

	public String getUrl() {
		return url;
	}

	public String getBaseUrl() {
		if (baseUrl == null) {
			int lastDotIndex = getUri().getPath().lastIndexOf('.');
			if (lastDotIndex < 0) {
				baseUrl = getUrl();
			} else {
				lastDotIndex = getUri().toString().lastIndexOf('/');
				baseUrl = getUri().toString().substring(0, lastDotIndex);
			}
		}
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getTitleFieldName() {
		return titleFieldName;
	}

	public void setTitleFieldName(final String titleFieldName) {
		this.titleFieldName = titleFieldName;
	}

	public String getIdFieldName() {
		return idFieldName;
	}

	public void setIdFieldName(final String idFieldName) {
		this.idFieldName = idFieldName;
	}

	public String getContentFieldName() {
		return contentFieldName;
	}

	public void setContentFieldName(final String contentFieldName) {
		this.contentFieldName = contentFieldName;
	}

	public int getInternetBatchSize() {
		return internetBatchSize;
	}

	public void setInternetBatchSize(int interrnetBatchSize) {
		this.internetBatchSize = interrnetBatchSize;
	}

	public String getExcludedPattern() {
		return excludedPattern;
	}

	public void setExcludedPattern(final String excludedPatterns) {
		this.excludedPattern = excludedPatterns;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	public boolean isExcluded(final String string) {
		if (pattern == null) {
			if (getExcludedPattern() != null) {
				pattern = Pattern.compile(getExcludedPattern());
			} else {
				return Boolean.FALSE;
			}
		}
		return pattern.matcher(string).matches();
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(final String currentUrl) {
		this.currentUrl = currentUrl;
	}

	public InputStream getCurrentInputStream() {
		return currentInputStream;
	}

	public void setCurrentInputStream(final InputStream currentInputStream) {
		this.currentInputStream = currentInputStream;
	}

}
