package com.durianeng.crawler;

import com.durianeng.crawler.util.HttpHelper;


import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



/**
 * Created by Eden on 2016/3/23.
 */
public class FiveOneVOACrawler implements ICrawler {
    private final static Logger logger = Logger.getLogger(FiveOneVOACrawler.class);
    private String URL_HOME = "http://www.51voa.com/";

    public FiveOneVOACrawler() {
    }

    public void crawl() {
        try{
            logger.info("开始抓取网页...");
            String html = HttpHelper.getHtml(URL_HOME);
            Document document = Jsoup.parse(html,URL_HOME);


            List<Category> categoryList = readCategory(document);

            for(Category category : categoryList ){
                logger.info("抓取分类：" + category);
            }

            //TODO: 遍历分类，抓取每个分类页面的文章
            readCategoryArticle( categoryList );
            //iterateAllArticle(categoryList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readCategoryArticle(List<Category> categoryList) {
        if (categoryList != null) {
            for(Category category : categoryList ){
                String categoryPageHtml = null;
                try{
                    logger.info("读取分类页面： " + category.url);
                    categoryPageHtml = HttpHelper.getHtml(category.url);
                }catch (Exception e){
                    logger.error("读取URL出错,URL : "+ category.url);
                    continue;
                }

                Document document = Jsoup.parse(categoryPageHtml,URL_HOME);
                // 抓取分页，若有分页，遍历每个分页的Article，若没有分页，读取当前页的Ariticle
                Element pageListElement = document.getElementById("pagelist");
                if( pageListElement != null ){
                    Elements pageListChildren = pageListElement.children();
                    List<String> pageUrlList = new ArrayList<String>();
                    for( Iterator<Element> it = pageListChildren.iterator() ;it.hasNext() ;){
                        Element child = it.next();
                        if( child.tagName().equals("a") ){
                            String pageUrl = child.attr("abs:href");
                            pageUrlList.add(pageUrl);
                        }
                    }
                    logger.info("分页列表：" + pageUrlList);

                    if( pageUrlList != null && pageUrlList.size() > 0 ){
                        for(String pageUrl : pageUrlList ){
                            logger.info("读取分页页面： " + pageUrl);
                            String pageHtml = null;
                            try {
                                pageHtml = HttpHelper.getHtml(pageUrl);
                            } catch (IOException e) {
                                logger.error("读取页面HTML出错:" + pageUrl + ","+ e.getMessage());
                            }
                            document = Jsoup.parse(pageHtml,URL_HOME);
                            List<Article> articleList = readArticleInDocument(document);

                            if( category.articles == null ){
                                category.articles = articleList;
                            }else{
                                category.articles.addAll(articleList);
                            }
                        }
                    }
                }else{  // pageListElement == null
                    List<Article> articleList = readArticleInDocument(document);

                    category.articles = articleList;
                }

            }
        }
    }

    private List<Article> readArticleInDocument(Document document) {
        List<Article> articleList = new ArrayList<Article>();

        Element articleListElement = document.getElementById("list");
        if( articleListElement != null ){
            Elements liElementList = articleListElement.select("ul > li");

            if( liElementList != null ){
                for(Iterator<Element> it = liElementList.iterator() ; it.hasNext() ; ){
                    /*<li>
                        <img src="/images/lrc.gif">
                        <img src="/images/yi.gif">
                        <a href="/VOA_Special_English/apple-annouces-small-iphone-return-68679.html" target="_blank">Apple Announces the Return of a Smaller iPhone</a> (16-3-24)
                    </li>*/
                    Article article = new Article();
                    Element liElement = it.next();
                    Element aTag = liElement.getElementsByTag("a").get(0);
                    String title = aTag.text();

                    article.title = title;

                    articleList.add(article);
                    logger.info(article);
                }
            }
        }
        return articleList;
    }

    /**
     * 从文档中读取所有分类页面 ，即左侧导航栏内的链接地址
     * @param document
     */
    private List<Category> readCategory(Document document) {
        logger.info("读取分类数据");
        List<Category> categoryList = new ArrayList<Category>();
        Element leftNav = document.getElementById("left_nav");
        Elements leftNavChildren = leftNav.children();

        // 是否跳过当前块，边栏除了51voa.com的菜单，还有外链，此标志用于跳过外链
        boolean jumpSection = false;
        for(Iterator<Element> it = leftNavChildren.iterator(); it.hasNext() ; ){
            Element child = it.next();

            if( child.tagName().equals("div") && child.hasClass("left_nav_title")){
                // 一级分类
                //<div class="left_nav_title"><a href="/VOA_Special_English/">VOA慢速英语</a></div>
                if( child.text().equals("友情链接") || child.text().equals("其它节目")
                        || child.text().equals("VOA节目介绍")){
                    // 排除 友情链接 和 其它节目
                    jumpSection = true;
                    continue;
                }else{
                    jumpSection = false;
                }

                Element aTag = child.child(0);
                String categoryName = aTag.text();
                String categoryUrl = aTag.attr("abs:href");


                Category category = new Category();
                category.categoryName = categoryName;
                category.url = categoryUrl;
                categoryList.add(category);

            }else if( child.tagName().equals("ul") ){
                // 二级分类
                /*<ul>
                <li><a href="/VOA_Standard_1.html">VOA常速英语最新(2012-2016)  <img src="/images/new.gif" width="19" height="9" border="0"></a></li>
                <li><a href="/VOA_Standard_1_archiver.html">VOA常速英语存档(2005-2011)</a></li>
                </ul>*/
                if( jumpSection == true ){
                    continue;
                }
                Category parent = null;
                if( categoryList.size() > 0 ){
                    parent = categoryList.get(categoryList.size()-1);
                }

                Elements liList = child.children();
                for(Iterator<Element> liIt = liList.iterator(); liIt.hasNext() ; ){
                    Element li = liIt.next();

                    Element aTag = li.select("a").get(0);
                    String categoryName = aTag.text();
                    String url = aTag.attr("abs:href");

                    Category category = new Category();
                    category.parent = parent;
                    category.categoryName = categoryName;
                    category.url = url;
                    categoryList.add(category);
                }

            }
        }
        return categoryList;
    }

    private void iterateAllArticle(String html){
        Document document = Jsoup.parse(html,URL_HOME);

        // 读取页面文章列表
        //document.select();

        // 如果有下一页，便利没一页，读取所有页面，加入当前分类


    }

    class Category {
        String categoryName;    // 分类名称
        String url;             // 分类页面URL
        Category parent;        // 父分类
        List<Article> articles; //分类下的文章

        @Override
        public String toString() {
            return "Category{" +
                    "categoryName='" + categoryName + '\'' +
                    ", url='" + url + '\'' +
                    ", parent=" + parent +
                    '}';
        }
    }

    class Article{
        String title;
        String content;
        List<String> tags;
        Date createDate;
        String mp3FileUrl;
        String lrcFileUrl;
        String translateFileUrl;
        List<String> articleImgUrlList;

        @Override
        public String toString() {
            return "Article{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
