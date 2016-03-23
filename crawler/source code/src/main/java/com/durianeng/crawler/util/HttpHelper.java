package com.durianeng.crawler.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by Eden on 2016/3/23.
 */
public class HttpHelper {
    public static String getHtml(String url)throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);

        InputStream contentIs = response.getEntity().getContent();

        StringWriter writer = new StringWriter();
        IOUtils.copy(contentIs, writer, "UTF8");
        String html = writer.toString();

        return html;
    }
}
