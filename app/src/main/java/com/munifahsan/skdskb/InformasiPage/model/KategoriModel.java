package com.munifahsan.skdskb.InformasiPage.model;

import com.google.firebase.firestore.DocumentId;

public class KategoriModel {

    @DocumentId
    String id;
    String nImageUrl;
    String nTitle;
    String nKategori;
    int nFont;

    public KategoriModel() {
    }

    public KategoriModel(String id, String nImageUrl, String nTitle, String nKategori) {
        this.id = id;
        this.nImageUrl = nImageUrl;
        this.nTitle = nTitle;
        this.nKategori = nKategori;
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
}
