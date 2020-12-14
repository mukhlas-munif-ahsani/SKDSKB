package com.munifahsan.skdskb.PageContent;

public class ContentPageRepository implements ContentPageContract.Repository {

    ContentPageContract.Listener mListener;

    public ContentPageRepository(ContentPageContract.Listener mListener) {
        this.mListener = mListener;
    }
}
