package ikube;

import java.io.Reader;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * Constants for Ikube.
 * 
 * @author Michael Couck
 * @since 21.11.10
 * @version 01.00
 */
public interface IConstants {

	/** Application name. */
	String IKUBE = "ikube";
	/** The property for the configuration location. */
	String IKUBE_CONFIGURATION = IKUBE + ".configuration";
	/** The name of the log file. */
	String IKUBE_LOG = IKUBE + ".log";
	/** The file separator for the system. */
	String SEP = "/";
	String BCK_SEP = "\\";
	String META_INF = SEP + "META-INF";
	/** The name of the spring file. */
	String SPRING_XML = "spring.xml";
	/** Where the whole application is wired together. */
	String SPRING_CONFIGURATION_FILE = SEP + "conf" + SEP + SPRING_XML;
	/** The persistence units' names. */
	String PERSISTENCE_UNIT_H2 = "IkubePersistenceUnitH2";
	String PERSISTENCE_UNIT_DB2 = "IkubePersistenceUnitDb2";
	String PERSISTENCE_UNIT_ORACLE = "IkubePersistenceUnitOracle";
	String PERSISTENCE_UNIT_POSTGRES = "IkubePersistenceUnitPostgres";

	/** The default logging properties. */
	String LOG_4_J_PROPERTIES = META_INF + SEP + "log4j.properties";

	/** Maps mime types to file extensions.. */
	String MIME_TYPES = "mime-types.xml";
	/** Maps parsers to mime types. */
	String MIME_MAPPING = "mime-mapping.xml";

	Version VERSION = Version.LUCENE_30;
	Analyzer ANALYZER = new StandardAnalyzer(VERSION) {
		public final TokenStream tokenStream(String fieldName, Reader reader) {
			return new PorterStemFilter(new LowerCaseTokenizer(reader));
		}
	};
	long MAX_READ_LENGTH = 1000000;
	int MAX_RESULT_FIELD_LENGTH = 100;

	String READER_FILE_SUFFIX = ".ikube";
	int MAX_FRAGMENTS = 3;
	String FRAGMENT_SEPERATOR = "...";
	int RESET_DELETE_BATCH_SIZE = 1000;

	String ID = "id";
	String INDEX = "index";
	String SCORE = "score";
	String CONTENT = "content";
	String CONTENTS = "contents";
	String FRAGMENT = "fragment";
	String TOTAL = "total";
	String DURATION = "duration";
	String TITLE = "title";
	String NAME = "name";
	String URL = "url";
	String URL_DONE = "urlDone";
	String URL_ID = "urlId";
	String INDEXED = "indexed";
	String HASH = "hash";
	String START = "start";
	String END = "end";
	String RESULTS = "results";
	String RESULTS_ROUTED = "resultsRouted";
	String ADDRESS = "address";
	String SERVER = "server";
	String SERVERS = "servers";
	String ACTION = "action";
	String ACTIONS = "actions";
	String WEB_SERVICE_URLS = "webServiceUrls";
	String INDEX_CONTEXT = "indexContext";
	String INDEX_CONTEXTS = "indexContexts";
	String INDEX_NAMES = "indexNames";
	String INDEX_FIELD_NAMES_AND_VALUES = "indexFieldNamesAndValues";
	String GEOSPATIAL = "geospatial";
	String DEFAULT = "default";
	String FEATURECLASS = "featureclass";
	String FEATURECODE = "featurecode";
	String COUNTRYCODE = "countrycode";
	String TIMEZONE = "timezone";
	String ASCIINAME = "asciiname";
	String CITY = "city";
	String COUNTRY = "country";
	String CORRECTIONS = "corrections";
	String ACTION_NAME = "actionName";

	String ENCODING = "UTF-8";

	/** Url response codes */
	Integer HTTP_200 = Integer.valueOf(200); // OK
	Integer HTTP_301 = Integer.valueOf(301); // Move permanently
	Integer HTTP_400 = Integer.valueOf(400); // Bad request
	Integer HTTP_401 = Integer.valueOf(401); // Unauthorized
	Integer HTTP_403 = Integer.valueOf(403); // Forbidden
	Integer HTTP_404 = Integer.valueOf(404); // Not found
	Integer HTTP_418 = Integer.valueOf(418); // I"m a tea pot
	Integer HTTP_500 = Integer.valueOf(500); // Internal server error
	Integer HTTP_503 = Integer.valueOf(503); // Service unavailable

	String INDEX_NAME = "indexName";
	String MAX_RESULTS = "maxResults";

	String SEARCH_FIELDS = "searchFields";
	String SEARCH_STRINGS = "searchStrings";
	String TARGET_SEARCH_URL = "targetSearchUrl";
	String SORT_FIELDS = "sortFields";
	String FIRST_RESULT = "firstResult";

	/** The tags in the response from the Geo Location API. */
	String LAT = "lat";
	String LNG = "lng";
	String LOCATION = "location";

	String DISTANCE = "distance";

	String LATITUDE = "latitude";
	String LONGITUDE = "longitude";

	/** The maximum age that the server can get to before it is deleted from the cluster. */
	int MAX_AGE = 1800000;

	String STRIP_CHARACTERS = ",[]{};";

	long MAX_ACTIONS = 10000;
	long MAX_SNAPSHOTS = 1000;
	String TIER = "tier";

	String SEARCH = "search";

	String TMP_UNZIPPED_FOLDER = "/tmp/unzipped";
	String STRING_PATTERN = ".*(\\.zip\\Z).*|.*(\\.jar\\Z).*|.*(\\.war\\Z).*|.*(\\.ear\\Z).*|.*(\\.gz\\Z).*|.*(\\.sar\\Z).*|.*(\\.tar\\Z).*";
	Pattern ZIP_JAR_WAR_EAR_PATTERN = Pattern.compile(STRING_PATTERN);

	String LUCENE_CONJUNCTIONS_PATTERN_STRING = "and|or|between|not";
	Pattern LUCENE_CONJUNCTIONS_PATTERN = Pattern.compile(LUCENE_CONJUNCTIONS_PATTERN_STRING);

	int THREAD_POOL_SIZE = 100;

	String EXCEPTION = "exception";
	String EXCEPTION_STACK = "exceptionStack";
	String EXCEPTION_MESSAGE = "exceptionMessage";

	String SEMI_COLON = ";";

	String ENTITIES = "entities";
	String FIELD_NAMES = "fieldNames";

	String PROPERTIES = "properties";

	String TOTAL_SIZE = "totalSize";
	String TOTAL_DOCS = "totalDocs";

	String TOPIC = "topic";
	
	int MAX_RETRY_CLUSTER_REMOVE = 10;

}