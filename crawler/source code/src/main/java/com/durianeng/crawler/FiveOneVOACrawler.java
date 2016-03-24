package com.durianeng.crawler;

import com.durianeng.crawler.util.HttpHelper;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


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
                System.out.println(category);
            }

            //iterateAllArticle(categoryList);

        }catch (Exception e){
            e.printStackTrace();
        }
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
                if( child.text().equals("友情链接") || child.text().equals("其它节目") ){
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
        Document document = Jsoup.parse(html);

        // 读取页面文章列表
        //document.select();

        // 如果有下一页，便利没一页，读取所有页面，加入当前分类


    }

    class Category {
        String categoryName;    // 分类名称
        String url;             // 分类页面URL
        Category parent;        // 父分类

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
        String category;
        String title;
        List<String> tags;
        Date createDate;
    }
}
