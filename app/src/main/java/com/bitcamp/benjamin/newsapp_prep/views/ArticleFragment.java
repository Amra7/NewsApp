package com.bitcamp.benjamin.newsapp_prep.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcamp.benjamin.newsapp_prep.R;
import com.bitcamp.benjamin.newsapp_prep.models.Article;
import com.bitcamp.benjamin.newsapp_prep.models.Feed;

import java.util.UUID;

/**
 * This is the "view" or fragment we are going to use
 * to display one single article
 * Created by benjamin on 06/04/15.
 */
public class ArticleFragment extends Fragment {

    public static String EXTRA_ARTICLE_ID = "bitcamp.news_feed.article_fragment.article_id";
    private Article mArticle;

    //the view components
    private TextView mArticleTitle;
    private TextView mArticleContent;

    public static ArticleFragment getArticleFragmentInstance(UUID articleID){
        Bundle articleBundle = new Bundle();
        articleBundle.putSerializable(EXTRA_ARTICLE_ID, articleID);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(articleBundle);
        return articleFragment;
    }
    /*you have seen this before*/
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        UUID  articelID = (UUID)getArguments().getSerializable(EXTRA_ARTICLE_ID);
        Log.d("TESTTAG", articelID.toString());
        mArticle = Feed.getInstance().getArticle(articelID);


        //TODO the user picked article
        //just to have something to work with

        /* kreira dummy clanak
        mArticle = new Article("Test Title", "Some test content to be testy and stuff", false);
        for(int i = 0; i < 5; i++){
            mArticle.setContent(mArticle.getContent() + mArticle.getContent());
        }

        */

        /*
        UUID articleID = (UUID)(getActivity().getIntent().getSerializableExtra(EXTRA_ARTICLE_ID));
        Log.d("TESTTAG", articleID.toString());
        */
    }


    /*The fun part, here our view is actually created
    first we inflate the layout, then we set the values and in case we need it
    there is a savedInstanceState to hold some data
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.article_fragment, parent, false);

        mArticleTitle = (TextView) v.findViewById(R.id.article_title);
        mArticleTitle.setText(mArticle.getTitle());

        mArticleContent = (TextView) v.findViewById(R.id.article_content);
        mArticleContent.setText(mArticle.getContent());

        return v;

    }

}
