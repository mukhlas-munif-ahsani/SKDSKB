package com.munifahsan.skdskb.PageContent;

public class ContentPagePresenter implements ContentPageContract.Presenter, ContentPageContract.Listener {

    ContentPageContract.View mView;
    ContentPageContract.Repository mRepo;

    public ContentPagePresenter(ContentPageContract.View mView) {
        this.mView = mView;
        mRepo = new ContentPageRepository(this);
    }
}
