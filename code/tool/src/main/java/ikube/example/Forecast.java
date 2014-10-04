package ikube.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This example of prediction is for stock market time series prediction, using the Weka framework. The market data
 * is taken from Yahoo! and at the time of writing was accurate. The results can be validated against the values that
 * the market had in the following days.
 * <p/>
 * The process is as follows:
 * <p/>
 * <pre>
 *     * Create a context with the name, the analyzer type, in this
 *       case the WekaForecastClassifier, and the training data in csv format
 *     * Convert the context to Json format using the Gson library from Google
 *     * Execute the create method on the rest service
 *     * Create an analysis object with the name of the analyzer that has just been created, and
 *       the options for the prediction, in this case is the forecast field, i.e. the price, the timestamp
 *       field for the training data, min and max lag for the time periods and forecasts for how many days
 *       to forecast
 *     * Execute the analyze method on the rest service, the result will be the output field in the
 *       response object, which is an array of the predicted stock prices
 * </pre>
 * <p/>
 * These methods can of course be executed in a browser using the Json generated from the context
 * and analysis object, and as such can be used from any platform and language that can access the
 * internet.
 *
 * @author Michael Couck
 * @version 01.00
 * @since 29-09-2014
 */
public class Forecast {

    /**
     * Ths class to hold the information for the analyzer to be able to create it.
     */
    public static class Context {
        /* The name of your specific analyzer */
        String name;
        /* The class of analyzer to use, perhaps WekaForecastClassifier, fully qualified name */
        String analyzer;
        /* The training data, in this case a couple of years data, closing price of the stock,
        and some other features, please have a look at the forecast.csv for exactly the contents */
        String[] trainingDatas;
    }

    /**
     * This class just holds the name of the analyzer that is just created, and the input
     * options for the forecaster.
     */
    public static class Analysis {
        /* The analyzer name, forecast-classifier */
        String context;
        /* The input options, either string or array,
         * '-fieldsToForecast,6,-timeStampField,0,-minLag,1,-maxLag,1,-forecasts,5' */
        String input;
    }

    public static void main(final String[] args) throws IOException {
        // The Jersey client for accessing the rest service
        Client client = Client.create();
        // For converting from Java to Json
        Gson gson = new GsonBuilder().create();

        // Get the training data from the file
        File trainingDataFile = new File("code/tool/src/main/resources/anal/example/forecast.csv");
        // Create the context for the creation
        Context context = new Context();
        context.name = "forecast-classifier";
        context.analyzer = "ikube.analytics.weka.WekaForecastClassifier";
        context.trainingDatas = new String[]{IOUtils.toString(new FileInputStream(trainingDataFile))};

        // Create the analyzer, and initialize with the training data for the stock market prediction
        String body = gson.toJson(context);
        System.out.println("Create body : " + body);
        WebResource webResource = client.resource("http://ikube.be/ikube/service/analyzer/create");
        String response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, body);
        System.out.println("Create response : " + response);

        // User the analyzer to get a specified number of predictions, in this case days into the future, 5 days forecasts
        Analysis analysis = new Analysis();
        analysis.context = context.name;
        analysis.input = "-fieldsToForecast,6,-timeStampField,0,-minLag,1,-maxLag,1,-forecasts,5";
        body = gson.toJson(analysis);
        System.out.println("Analysis body : " + body);
        webResource = client.resource("http://ikube.be/ikube/service/analyzer/analyze");
        response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(String.class, body);
        // The result should be something like:
        // [[[579.3721684789788],[581.4060746802609],[583.233603088952],[584.8823713779697],[586.3763013969173]]]
        System.out.println("Analysis response : " + response);
    }

}