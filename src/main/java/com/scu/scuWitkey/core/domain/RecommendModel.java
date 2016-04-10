package com.scu.scuWitkey.core.domain;

import java.io.Serializable;

public class RecommendModel implements Serializable {
    private static final long serialVersionUID = -3465085715281292809L;

    private long id;
    private String imgUrl;
    private String title;
    private String description;
    private long recommendPublisherId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRecommendPublisherId() {
        return recommendPublisherId;
    }

    public void setRecommendPublisherId(long recommendPublisherId) {
        this.recommendPublisherId = recommendPublisherId;
    }
}
