package com.durianeng.crawler;


public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ICrawler fiveOneVoaCrawler = new FiveOneVOACrawler();
        fiveOneVoaCrawler.crawl();

    }

}
