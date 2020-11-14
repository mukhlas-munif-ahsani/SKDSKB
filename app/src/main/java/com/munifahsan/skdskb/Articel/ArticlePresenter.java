package com.munifahsan.skdskb.Articel;

public class ArticlePresenter implements ArticleContract.Presenter, ArticleContract.Listener {

    private ArticleContract.View mArticleView;
    private ArticleRepository mArticleRepo;

    public ArticlePresenter(ArticleContract.View mArticleView) {
        this.mArticleView = mArticleView;
        mArticleRepo = new ArticleRepository(this);
    }
}
