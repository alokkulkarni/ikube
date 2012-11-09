package ikube.index.analyzer;

import ikube.IConstants;
import ikube.toolkit.ObjectToolkit;

import java.io.Reader;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;

/**
 * This analyzer will stem the words for plural and so on. All the stop words in all the languages that are available are also added to the
 * analyzer, slowing the performance down, but adding value to the results.
 * 
 * @author Michael Couck
 * @since 29.10.12
 * @version 01.00
 */
public final class StemmingAnalyzer extends Analyzer {

	/**
	 * This method will produce a lower case token stream, with stem words and none of the general stop words for some of the important
	 * languages.
	 */
	@Override
	public final TokenStream tokenStream(String fieldName, Reader reader) {
		LowerCaseTokenizer lowerCaseTokenizer = new LowerCaseTokenizer(IConstants.VERSION, reader);
		PorterStemFilter porterStemFilter = new PorterStemFilter(lowerCaseTokenizer);
		return new StopFilter(IConstants.VERSION, porterStemFilter, getStopWords());
	}

	@SuppressWarnings("unchecked")
	private final Set<String> getStopWords() {
		Set<String> stopWords = new TreeSet<String>();
		stopWords.addAll((Collection<? extends String>) GreekAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) CzechAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) CzechAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) DutchAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) FrenchAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) GermanAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) BrazilianAnalyzer.getDefaultStopSet());
		stopWords.addAll((Collection<? extends String>) ObjectToolkit.getFieldValue(new RussianAnalyzer(IConstants.VERSION), "stopSet"));
		return stopWords;
	}

}