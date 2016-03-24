package com.durianeng.crawler;


import org.apache.log4j.BasicConfigurator;

public class App {
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);

        ICrawler fiveOneVoaCrawler = new FiveOneVOACrawler();
        fiveOneVoaCrawler.crawl();

    }

}
