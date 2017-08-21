package com.example.android.newsapp;

/**
 * Created by jnbcb on 8/2/2017.
 */

public class Article {

    private String title;
    private String section;
    private String authors;
    private String date;
    private String url;

    public Article(String title, String section, String authors, String date, String url){
        this.title = title;
        this.section = section;
        this.authors = authors;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
