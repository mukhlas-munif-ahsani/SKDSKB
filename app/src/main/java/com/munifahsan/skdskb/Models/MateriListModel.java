package com.munifahsan.skdskb.Models;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MateriListModel {
    @DocumentId
    String id;
    String nTitle;
    String nThumbnailImageUrl;
    String nImageHeader;


    String nVideoUrl;
    String nWebUrl;
    String nJenis;
    String nJenisImage;
    boolean premium;
    @ServerTimestamp
    Date nUploadTime;

    String nParagraf1, nParagraf2, nParagraf3, nParagraf4, nParagraf5,
            nParagraf6, nParagraf7, nParagraf8, nParagraf9, nParagraf10,
            nParagraf11, nParagraf12, nParagraf13, nParagraf14, nParagraf15;

    public MateriListModel() {
    }

    public String getnImageHeader() {
        return nImageHeader;
    }

    public void setnImageHeader(String nImageHeader) {
        this.nImageHeader = nImageHeader;
    }

    public String getId() {
        return id;
    }

    public String getnTitle() {
        return nTitle;
    }

    public String getnThumbnailImageUrl() {
        return nThumbnailImageUrl;
    }

    public String getnVideoUrl() {
        return nVideoUrl;
    }

    public String getnWebUrl() {
        return nWebUrl;
    }

    public String getnJenis() {
        return nJenis;
    }

    public String getnJenisImage() {
        return nJenisImage;
    }

    public boolean isPremium() {
        return premium;
    }

    public Date getnUploadTime() {
        return nUploadTime;
    }

    public String getnParagraf1() {
        return nParagraf1;
    }

    public String getnParagraf2() {
        return nParagraf2;
    }

    public String getnParagraf3() {
        return nParagraf3;
    }

    public String getnParagraf4() {
        return nParagraf4;
    }

    public String getnParagraf5() {
        return nParagraf5;
    }

    public String getnParagraf6() {
        return nParagraf6;
    }

    public String getnParagraf7() {
        return nParagraf7;
    }

    public String getnParagraf8() {
        return nParagraf8;
    }

    public String getnParagraf9() {
        return nParagraf9;
    }

    public String getnParagraf10() {
        return nParagraf10;
    }

    public String getnParagraf11() {
        return nParagraf11;
    }

    public String getnParagraf12() {
        return nParagraf12;
    }

    public String getnParagraf13() {
        return nParagraf13;
    }

    public String getnParagraf14() {
        return nParagraf14;
    }

    public String getnParagraf15() {
        return nParagraf15;
    }
}
