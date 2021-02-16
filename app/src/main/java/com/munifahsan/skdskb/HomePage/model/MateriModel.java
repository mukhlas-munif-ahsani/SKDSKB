package com.munifahsan.skdskb.HomePage.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class MateriModel {
    @DocumentId
    String id;
    String nTitle;
    String nThumbnailImageUrl;
    String nJenis;
    String nJenisImage;
    List<String> nSeen;
    boolean premium;
    @ServerTimestamp
    Date nUploadTime;

    public MateriModel() {
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

    public List<String> getnSeen() {
        return nSeen;
    }
}
