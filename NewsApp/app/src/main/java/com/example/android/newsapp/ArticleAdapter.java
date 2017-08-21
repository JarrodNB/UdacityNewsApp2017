package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jnbcb on 8/2/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Article> articles) {
        super(context, resource, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.article_title);
            holder.section = (TextView) view.findViewById(R.id.article_section);
            holder.authors = (TextView) view.findViewById(R.id.article_author);
            holder.date = (TextView) view.findViewById(R.id.article_date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Article article = getItem(position);
        holder.title.setText(article.getTitle());
        holder.section.setText(article.getSection());
        holder.authors.setText(article.getAuthors());
        holder.date.setText(article.getDate());
        return view;
    }

    private static class ViewHolder{
        private TextView title;
        private TextView section;
        private TextView authors;
        private TextView date;
    }
}
