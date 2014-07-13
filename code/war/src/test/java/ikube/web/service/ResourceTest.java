package ikube.web.service;

import ikube.AbstractTest;
import ikube.IConstants;
import ikube.model.Analysis;
import ikube.model.Search;
import ikube.toolkit.ObjectToolkit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Michael Couck
 * @version 01.00
 * @since 18-11-2012
 */
public class ResourceTest extends AbstractTest {

    private Resource resource;

    @Before
    public void before() {
        resource = new SearcherJson();
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void buildResponse() {
        String russian = "Россия   русский язык  ";
        String german = "Produktivität";
        String french = "Qu'est ce qui détermine la productivité, et comment est-il mesuré?";
        String somthingElseAlToGether = "Soleymān Khāţer";
        String[] result = {russian, german, french, somthingElseAlToGether};
        Response response = resource.buildJsonResponse(result);
        Object entity = response.getEntity();
        logger.info("Entity : " + entity);
        logger.info("Entity : " + Arrays.deepToString(result));
        assertTrue("Must have the weird characters : ", entity.toString().contains(somthingElseAlToGether));
        assertTrue("Must have the weird characters : ", entity.toString().contains("ä"));

        Analysis<String, String> analysis = ObjectToolkit.populateFields(new Analysis(), Boolean.TRUE, 100, "exception");
        response = resource.buildJsonResponse(analysis);
        logger.info("Response : " + response.getEntity());
    }

    @Test
    public void unmarshall() throws Exception {
        Analysis analysis = ObjectToolkit.populateFields(new Analysis(), Boolean.TRUE, 10);
        System.out.println(IConstants.GSON.toJson(analysis));

        Search search = ObjectToolkit.populateFields(new Search(), Boolean.TRUE, 10, "id", "exception");
        final String json = IConstants.GSON.toJson(search);
        final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(json.getBytes());

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        ServletInputStream servletInputStream = mock(ServletInputStream.class);

        when(httpServletRequest.getInputStream()).thenReturn(servletInputStream);
        when(servletInputStream.read(any(byte[].class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(final InvocationOnMock invocation) throws Throwable {
                byte[] bytes = (byte[]) invocation.getArguments()[0];
                return arrayInputStream.read(bytes);
            }
        });

        resource.unmarshall(Analysis.class, httpServletRequest);
    }

    @Test
    public void unmarshallJson() throws Exception {
        final String json = "{\"analyzer\":\"\",\"clazz\":\"\",\"input\":{},\"output\":{},\"algorithmOutput\":\"\"," +
                "\"duration\":0.1230345670741767,\"algorithm\":false,\"correlation\":true,\"distribution\":true,\"classesAndClusters\":false," +
                "\"sizesForClassesAndClusters\":true,\"aggregated\":true,\"distributed\":false,\"id\":6967546706258352350," +
                "\"timestamp\":\"Jul 12, 196654457 8:49:06 AM\"}";
        /*final String json = "{\"analyzer\":\"context-smo\",\"clazz\":null,\"input\":\"Hello world\",\"output\":null," +
                "\"algorithmOutput\":true,\"correlation\":false,\"distribution\":false,\"classesAndClusters\":false," +
                "\"sizesForClassesAndClusters\":false,\"exception\":null,\"correlationCoefficients\":false,\"distributionForInstances\":false}";*/
        final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(json.getBytes());

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        ServletInputStream servletInputStream = mock(ServletInputStream.class);

        when(httpServletRequest.getInputStream()).thenReturn(servletInputStream);
        when(servletInputStream.read(any(byte[].class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(final InvocationOnMock invocation) throws Throwable {
                byte[] bytes = (byte[]) invocation.getArguments()[0];
                return arrayInputStream.read(bytes);
            }
        });

        resource.unmarshall(Analysis.class, httpServletRequest);
    }

    @Test
    public void split() {
        String searchString = "hello, world | there you are";
        String[] result = resource.split(searchString);
        assertEquals("hello, world ", result[0]);
        assertEquals(" there you are", result[1]);
    }

}