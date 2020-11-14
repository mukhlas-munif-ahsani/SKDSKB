package com.munifahsan.skdskb.DetailKategori;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class DetailKategoriModel {

    @DocumentId
    String id;
    String nImageUrl;
    String nTitle;
    String nDesc;
    String nKategori;
    String nCollection;
    int nFont;

    public DetailKategoriModel() {
    }

    public String getId() {
        return id;
    }

    public String getnImageUrl() {
        return nImageUrl;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnKategori() {
        return nKategori;
    }

    public int getnFont() {
        return nFont;
    }

    public String getnCollection() {
        return nCollection;
    }

    public String getnDesc() {
        return nDesc;
    }

}
