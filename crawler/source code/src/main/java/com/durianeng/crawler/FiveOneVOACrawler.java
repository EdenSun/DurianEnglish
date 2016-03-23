package com.durianeng.crawler;

import com.durianeng.crawler.util.HttpHelper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by Eden on 2016/3/23.
 */
public class FiveOneVOACrawler implements ICrawler {
    public final static String URL_HOME = "http://www.51voa.com/";

    public FiveOneVOACrawler() {
    }

    public void crawl() {
        String html = HttpHelper.getHtml(URL_HOME);
        List<> parseVOAArticle(html);

    }

    private void parseVOAArticle(){

    }
}
