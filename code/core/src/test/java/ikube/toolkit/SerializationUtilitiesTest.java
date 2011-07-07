package ikube.toolkit;

import static org.junit.Assert.assertTrue;
import ikube.ATest;
import ikube.IConstants;
import ikube.model.IndexContext;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * This test is just to see that the serializer is correctly encoding the languages.
 * 
 * @author Michael Couck
 * @since 21.05.2011
 * @version 01.00
 */
public class SerializationUtilitiesTest extends ATest {

	public SerializationUtilitiesTest() {
		super(SerializationUtilitiesTest.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void serializeAndDeserialize() {
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		Map<String, String> result = new HashMap<String, String>();
		String russian = "Что определяет производительность";
		String arabic = "تيات تبحث عن ابن الحلالللصداقة و الزوا";
		result.put(IConstants.FRAGMENT, russian + arabic);
		results.add(result);
		String xml = SerializationUtilities.serialize(results);
		assertTrue("The serialized string should contain the Russian characters : ", xml.contains(russian));

		results = (List<Map<String, String>>) SerializationUtilities.deserialize(xml);
		String fragment = results.get(0).get(IConstants.FRAGMENT);
		assertTrue("The de-serialized fragment should contain the Russian characters : ", fragment.contains(russian));
	}

	@Test
	public void setTransientFields() throws Exception {
		SerializationUtilities.setTransientFields(IndexContext.class, new ArrayList<Class<?>>());
		BeanInfo info = Introspector.getBeanInfo(IndexContext.class);
		PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
		boolean containsIndex = Boolean.FALSE;
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String name = propertyDescriptor.getName();
			if (name != null && name.equals("index")) {
				containsIndex = Boolean.TRUE;
			}
		}
		assertTrue("The index field should be set to transient : ", containsIndex);
	}

}