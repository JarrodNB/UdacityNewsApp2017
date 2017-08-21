package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by jnbcb on 8/2/2017.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String url;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (url == null) return null;
        return ParseArticle.fillArticleList(url);
    }
}
