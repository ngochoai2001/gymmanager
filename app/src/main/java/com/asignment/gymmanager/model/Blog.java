package com.asignment.gymmanager.model;

import lombok.Data;

@Data
public class Blog {
    private String id;
    private String title;
    private String content;
    private String intro;
    private String imageUrl;

    public Blog(String id, String title, String content, String intro, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.intro = intro;
        this.imageUrl = imageUrl;
    }

    public Blog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
