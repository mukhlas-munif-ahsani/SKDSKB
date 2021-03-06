package com.munifahsan.skdskb.TryoutPage.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class TryoutModel {
    @DocumentId
    String id;
    String nTitle;
    String nThumbnailImageUrl;
    String nJenis;
    String nJenisImage;
    boolean premium;
    @ServerTimestamp
    Date nUploadTime;

    public TryoutModel() {
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
}
