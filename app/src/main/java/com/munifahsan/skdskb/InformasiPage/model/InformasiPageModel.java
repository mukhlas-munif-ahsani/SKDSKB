package com.munifahsan.skdskb.InformasiPage.model;

import com.google.firebase.firestore.DocumentId;

public class InformasiPageModel {
    @DocumentId
    String id;
    String nQuote;
    String nTalker;

    public InformasiPageModel() {
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
