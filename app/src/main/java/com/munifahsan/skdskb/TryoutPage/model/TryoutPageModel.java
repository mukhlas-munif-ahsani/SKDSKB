package com.munifahsan.skdskb.TryoutPage.model;

import com.google.firebase.firestore.DocumentId;

public class TryoutPageModel {
    @DocumentId
    String id;
    String nQuote;
    String nTalker;

    public TryoutPageModel() {
    }

    public String getId() {
        return id;
    }

    public String getnQuote() {
        return nQuote;
    }

    public String getnTalker() {
        return nTalker;
    }
}
