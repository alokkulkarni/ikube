package ikube.action.index.handler.strategy;

import ikube.IConstants;
import ikube.action.index.IndexManager;
import ikube.action.index.handler.IStrategy;
import ikube.model.IndexContext;
import ikube.model.Indexable;
import ikube.toolkit.FileUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.springframework.beans.factory.annotation.Value;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;

/**
 * @author Michael Couck
 * @since 19.06.13
 * @version 01.00
 */
public final class MultiLanguageClassifierSentimentAnalysisStrategy extends AStrategy {

	@Value("${multi.language.ngram}")
	private int nGram = 8;
	private AtomicInteger atomicInteger;
	private Map<String, DynamicLMClassifier<NGramProcessLM>> languageClassifiers;

	public MultiLanguageClassifierSentimentAnalysisStrategy() {
		this(null);
	}

	public MultiLanguageClassifierSentimentAnalysisStrategy(final IStrategy nextStrategy) {
		super(nextStrategy);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean aroundProcess(final IndexContext<?> indexContext, final Indexable<?> indexable, final Document document, final Object resource)
			throws Exception {
		// TODO Perhaps detect the subject and the object. Separate the constructs of the sentence for further processing
		String language = document.get(IConstants.LANGUAGE);
		String content = indexable.getContent() != null ? indexable.getContent().toString() : resource != null ? resource.toString() : null;
		if (language != null && content != null) {
			// If this data is already classified by another strategy then train the language
			// classifiers on the data. We can then also classify the data and correlate the results
			String sentiment = document.get(IConstants.SENTIMENT);
			String languageSentiment = detectSentiment(language, content);
			if (StringUtils.isEmpty(sentiment)) {
				// Not analyzed so add the sentiment that we get
				IndexManager.addStringField(IConstants.SENTIMENT, languageSentiment, document, Store.YES, Index.ANALYZED, TermVector.NO);
			} else {
				// Retrain on the previous strategy sentiment
				train(content, sentiment, language);
				if (!sentiment.contains(languageSentiment)) {
					// We don't change the original analysis do we?
					IndexManager.addStringField(IConstants.SENTIMENT_CONFLICT, languageSentiment, document, Store.YES, Index.ANALYZED, TermVector.NO);
				}
			}
		}
		if (atomicInteger.getAndIncrement() % 10000 == 0) {
			logger.info("Document : " + document + ", " + document.hashCode());
			persistClassifiers();
		}
		return super.aroundProcess(indexContext, indexable, document, resource);
	}

	public String detectSentiment(final String language, final String content) {
		DynamicLMClassifier<NGramProcessLM> dynamicLMClassifier = getDynamicLMClassifier(language);
		Classification classification = dynamicLMClassifier.classify(content);
		return classification.bestCategory();
	}

	private void persistClassifiers() {
		// Persist the classifiers from time to time
		File classifiersDirectory = getClassifiersDirectory();
		for (final Map.Entry<String, DynamicLMClassifier<NGramProcessLM>> mapEntry : languageClassifiers.entrySet()) {
			OutputStream outputStream = null;
			try {
				DynamicLMClassifier<NGramProcessLM> dynamicLMClassifier = mapEntry.getValue();
				File classifierFile = FileUtilities.getOrCreateFile(new File(classifiersDirectory, mapEntry.getKey() + "." + IConstants.CLASSIFIER));
				outputStream = new FileOutputStream(classifierFile);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				dynamicLMClassifier.compileTo(objectOutputStream);
			} catch (Exception e) {
				logger.error("Exception persisting the classifier : " + mapEntry.getKey() + ", " + mapEntry.getValue(), e);
			} finally {
				IOUtils.closeQuietly(outputStream);
			}
		}
	}

	private File getClassifiersDirectory() {
		File classifiersDirectory = FileUtilities.findDirectoryRecursively(new File("."), IConstants.CLASSIFIERS);
		if (classifiersDirectory == null) {
			classifiersDirectory = new File("./ikube/common/sentiment", IConstants.CLASSIFIERS);
		}
		return FileUtilities.getOrCreateDirectory(classifiersDirectory);
	}

	private void train(final String content, final String sentiment, final String language) throws Exception {
		DynamicLMClassifier<NGramProcessLM> dynamicLMClassifier = getDynamicLMClassifier(language);
		Classification classification = new Classification(sentiment);
		Classified<CharSequence> classified = new Classified<CharSequence>(content, classification);
		dynamicLMClassifier.handle(classified);
	}

	private DynamicLMClassifier<NGramProcessLM> getDynamicLMClassifier(final String language) {
		DynamicLMClassifier<NGramProcessLM> dynamicLMClassifier = languageClassifiers.get(language);
		if (dynamicLMClassifier == null) {
			dynamicLMClassifier = DynamicLMClassifier.createNGramProcess(IConstants.SENTIMENT_CATEGORIES, nGram);
			languageClassifiers.put(language, dynamicLMClassifier);
		}
		return dynamicLMClassifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void initialize() {
		atomicInteger = new AtomicInteger(0);
		languageClassifiers = new HashMap<String, DynamicLMClassifier<NGramProcessLM>>();
		File classifiersDirectory = getClassifiersDirectory();
		File[] classifierFiles = classifiersDirectory.listFiles();
		if (classifierFiles != null) {
			for (final File classifierFile : classifierFiles) {
				String language = FilenameUtils.getBaseName(classifierFile.getName());
				InputStream inputStream = null;
				ObjectInputStream objectInputStream = null;
				try {
					inputStream = new FileInputStream(classifierFile);
					objectInputStream = new ObjectInputStream(inputStream);
					DynamicLMClassifier<NGramProcessLM> dynamicLMClassifier = (DynamicLMClassifier<NGramProcessLM>) objectInputStream.readObject();
					languageClassifiers.put(language, dynamicLMClassifier);
				} catch (Exception e) {
					logger.error("Exception deserializing classifier : " + classifierFile, e);
				} finally {
					IOUtils.closeQuietly(objectInputStream);
					IOUtils.closeQuietly(inputStream);
				}
			}
		}
	}

}