package com.epam.cisen.sms;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Class required for sending requests to SMS gate.
 */
public class PostClient {

    private final static String SMS_GATE_URL = "http://speedsms.com.ua/cgi-bin/api2.0.cgi";

    private static HttpPost request = new HttpPost(SMS_GATE_URL);
    private static CloseableHttpClient client = HttpClients.createDefault();

    static {
        request.setHeader("ContentType", "application/xml; charset=utf=8");
    }

    public static void prepareRequest(final String requestBody) {
        request.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_XML));
    }

    public static void doRequest() {
        try {
            client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
