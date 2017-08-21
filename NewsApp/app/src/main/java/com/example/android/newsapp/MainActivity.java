package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private String url;
    private ArticleAdapter adapter;
    @BindView(R.id.list_view) ListView listView;
    @BindView(R.id.empty_state) TextView emptyState;
    @BindView(R.id.bar) ProgressBar bar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    public void reload(){
        getLoaderManager().destroyLoader(0);
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            url = String.format("https://content.guardianapis.com/search?q=%s&api-key=e245da5d-918a-48e3-869a-95646bfe617f", query);
            reload();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        url = "https://content.guardianapis.com/search?q=android&api-key=e245da5d-918a-48e3-869a-95646bfe617f";
        List<Article> list = new ArrayList<>();
        adapter = new ArticleAdapter(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = adapter.getItem(position);
                Uri uri = Uri.parse(article.getUrl());
                Log.i("url", article.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            }
        });
        if (checkConnection()){
            getLoaderManager().initLoader(0, null, this);
        } else {
            emptyState.setText(R.string.no_connection);
        }
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        bar.setVisibility(View.VISIBLE);
        return new ArticleLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        adapter.clear();
        bar.setVisibility(View.INVISIBLE);
        if (data != null && !data.isEmpty()){
            adapter.addAll(data);
        } else {
            emptyState.setText(R.string.no_articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }

}
