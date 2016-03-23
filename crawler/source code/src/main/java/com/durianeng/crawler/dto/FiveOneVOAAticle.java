package com.durianeng.crawler.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Eden on 2016/3/23.
 */
public class FiveOneVOAAticle {
    private String title;
    private String content;
    private List<String> tags;
    private String category;
    private Date createDate;
    private List<FiveOneVoaArticleImage> images;
    private String mp3FileUrl;
    private String lrcFileUrl;
    private String translate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<FiveOneVoaArticleImage> getImages() {
        return images;
    }

    public void setImages(List<FiveOneVoaArticleImage> images) {
        this.images = images;
    }

    public String getMp3FileUrl() {
        return mp3FileUrl;
    }

    public void setMp3FileUrl(String mp3FileUrl) {
        this.mp3FileUrl = mp3FileUrl;
    }

    public String getLrcFileUrl() {
        return lrcFileUrl;
    }

    public void setLrcFileUrl(String lrcFileUrl) {
        this.lrcFileUrl = lrcFileUrl;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}
