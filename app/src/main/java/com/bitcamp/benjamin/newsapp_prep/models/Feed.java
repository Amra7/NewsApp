package com.bitcamp.benjamin.newsapp_prep.models;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A collection of articles, for now
 * it loads a few articles statically
 * this will be changed soon enough
 * Created by benjamin on 06/04/15.
 */
public class Feed {
    private static Feed ourInstance = new Feed();

    public static Feed getInstance() {
        return ourInstance;
    }

    private ArrayList<Article> articles;

    private Feed() {
        articles = new ArrayList<Article>();



        /*
        for(int i = 0; i < 50; i++){
            articles.add(new Article(
                    "Article title " + (i+1),
                    "Some content for article # " + (i+1),
                    i%2 == 0
            ));

        }
        */

    }

    public ArrayList<Article> getArticles(){
        return articles;
    }


    public Article getArticle(UUID id){

        for( Article a : articles){
            if(a.getId().equals(id))
                return a;
        }
        return null;
    }

    //TODO implement method we call to get articles from the web
    public void loadFeed(String url) throws XmlPullParserException, IOException {
        String xmlString = getXml(url);
        Log.d("TAGXML", xmlString);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlString));
        parseItems(parser);
    }

    private String getXml(String url) {
        URL requestUrl;
        try {
            requestUrl = new URL(url);
        } catch (MalformedURLException e) {
           // Log.e(TAG, "Malformed URL exception");
            e.printStackTrace();
            return null;
        }

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) requestUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
           // Log.e(TAG, "Open connection fail");
            return "";
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return "";
    }



    void parseItems(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        int eventType = parser.next();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.next() == XmlPullParser.START_TAG &&
                    "item".equals(parser.getName())) {

                String title = null, content = null;

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();

                    if (name.equals("title")) {
                        title = readField(parser, "title");
                    } else if (name.equals("clanak")) {
                        content = readField(parser, "clanak");
                    } else {
                        skip(parser);
                    }
                }
                articles.add(new Article(title, content, false));
            }

        }
    }

    private String readField(XmlPullParser parser, String field) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, field);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, field);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
