package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jnbcb on 8/2/2017.
 */

public class ParseArticle {

    private static URL formUrl(String url){
        URL urlArticle = null;
        try {
            urlArticle = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("ParseArticle", e.getMessage());
        }
        return urlArticle;
    }

    private static String getJsonResponse(URL url) {
        String response = "";
        if (url == null) return response;
        HttpsURLConnection connection = null;
        InputStream input = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            int readTimeout = 10000;
            connection.setReadTimeout(readTimeout);
            int connectionTimeout = 15000;
            connection.setConnectTimeout(connectionTimeout);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                input = connection.getInputStream();
                response = getStringResponse(input);
            }
        } catch (IOException e) {
            Log.e("ParseArticle", e.getMessage());
        }
        connection.disconnect();
        try {
            input.close();
        } catch (IOException e) {
            Log.e("ParseArticle", e.getMessage());
        } catch (NullPointerException x) {
            Log.e("ParseArticle", x.getMessage());
        }
        return response;
    }

    private static String getStringResponse(InputStream input) throws IOException {
        StringBuilder response = new StringBuilder();
        if (input != null) {
            InputStreamReader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader bReader = new BufferedReader(reader);
            String line = bReader.readLine();
            while (line != null) {
                response.append(line);
                line = bReader.readLine();
            }
        }
        return response.toString();
    }

    public static List<Article> fillArticleList(String url){
        URL urlArticle = formUrl(url);
        String JSONresponse = getJsonResponse(urlArticle);
        List<Article> articles = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(JSONresponse);
            JSONObject response;
            if (object.has("response")){
                response = object.getJSONObject("response");
            } else {
                response = null;
                Log.e("Parsing", "No response");
            }
            JSONArray array;
            if (response.has("results")){
                array = response.getJSONArray("results");
            } else {
                array = null;
                Log.e("Parsing", "Unable to get results array");
            }
            for (int index = 0; index < array.length(); index++){
                JSONObject articleResponse = array.getJSONObject(index);
                String title;
                if (articleResponse.has("webTitle")){
                    title = articleResponse.getString("webTitle");
                } else {
                    title = null;
                }
                String section;
                if (articleResponse.has("sectionName")){
                    section = articleResponse.getString("sectionName");
                } else {
                    section = null;
                    Log.e("Parsing", "Unable to parse section");
                }
                // Not provided in responses
                String authors = "";
                String date;
                if (articleResponse.has("webPublicationDate")){
                    date = modDate(articleResponse.getString("webPublicationDate"));
                } else {
                    date = null;
                }
                String articleUrl;
                if (articleResponse.has("webUrl")){
                    articleUrl = articleResponse.getString("webUrl");
                } else {
                    articleUrl = null;
                }
                Article article = new Article(title, section, authors, date, articleUrl);
                articles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return articles;
    }

    private static String modDate(String date){
        return date.substring(0, 10);
    }
}
