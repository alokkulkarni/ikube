package ikube.cluster.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import ikube.BaseTest;
import ikube.model.Url;
import ikube.toolkit.ApplicationContextManager;
import ikube.toolkit.PerformanceTester;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CacheTest extends BaseTest {

	private Url url;
	private ICache cache;

	@Before
	public void before() {
		cache = ApplicationContextManager.getBean(ICache.class);
		url = new Url();
		url.setUrl("http://localhost");
		url.setHash(System.nanoTime());
	}

	@After
	public void after() {
		cache.clear(Url.class);
	}

	@Test
	public void setUrl() {
		cache.set(Url.class, url.getHash(), url);
		Url url = cache.get(Url.class, this.url.getHash());
		assertNotNull(url);
		cache.clear(Url.class);
	}

	@Test
	public void getUrlBatch() throws Exception {
		int size = 100;
		for (int i = 0; i < size; i++) {
			Url url = new Url();
			url.setHash(System.nanoTime());
			url.setUrl(Integer.toHexString(i));
			cache.set(Url.class, url.getHash(), url);
			Thread.sleep(1);
		}
		List<Url> batchUrls = cache.get(Url.class, null, null, 100);
		assertEquals(size, batchUrls.size());
		cache.clear(Url.class);
	}

	@Test
	public void performance() throws Exception {
		int iterations = 10;
		double executionsPerSecond = PerformanceTester.execute(new PerformanceTester.APerform() {
			public void execute() throws Exception {
				long nanoTime = System.nanoTime();
				Url url = new Url();
				url.setHash(nanoTime);
				url.setUrl(Long.toHexString(nanoTime));
				cache.set(Url.class, url.getHash(), url);
				Thread.sleep(1);
			}
		}, "Cache set : ", iterations);
		assertTrue(executionsPerSecond > 10);
	}

}