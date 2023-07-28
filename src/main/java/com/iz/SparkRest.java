import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkRestIntegration {
    public static void main(String[] args) {
        String url = "https://example.com/api/endpoint"; // Replace with your actual endpoint URL
        String requestBody = "{\"key\": \"value\"}"; // Replace this JSON with your desired JSON body

        String jsonResponse = makePostRequest(url, requestBody);

        // Assuming the JSON response contains an array of objects with the schema you desire
        SparkSession spark = SparkSession.builder().appName("SparkRestIntegration").getOrCreate();
        Dataset<Row> jsonData = spark.read().json(spark.sparkContext().parallelize(Collections.singletonList(jsonResponse)));

        // Perform further transformations and processing using DataFrame API
        jsonData.show();

        spark.stop();
    }

    public static String makePostRequest(String url, String jsonBody) {
        String responseString = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    responseString = EntityUtils.toString(responseEntity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }
}
