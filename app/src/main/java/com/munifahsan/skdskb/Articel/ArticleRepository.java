package com.munifahsan.skdskb.Articel;

public class ArticleRepository implements ArticleContract.Repository {
    private ArticleContract.Listener mListener;

    public ArticleRepository(ArticleContract.Listener mListener) {
        this.mListener = mListener;
    }
}
