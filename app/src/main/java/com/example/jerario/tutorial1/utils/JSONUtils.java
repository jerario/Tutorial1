package com.example.jerario.tutorial1.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by jerario on 11/17/14.
 */
public class JSONUtils {

    public static String ejecuteQuery(String uriString) throws IOException {
        HttpUriRequest request = new HttpGet(uriString);
        HttpClient client = new DefaultHttpClient();
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        request.setParams(basicHttpParams);
        request.setHeader(new BasicHeader("Content-Type","application/json"));
        HttpResponse response = client.execute(request);
        String responseStr = EntityUtils.toString(response.getEntity());
        return responseStr;

    }
}
