package com.munifahsan.skdskb.AddEbook;

import android.net.Uri;

import java.util.List;

public class AddEbookPresenter implements AddEbookContract.Presenter, AddEbookContract.Listener {

    AddEbookContract.View mView;
    AddEbookContract.Repository mRepo;

    public AddEbookPresenter(AddEbookContract.View mView) {
        this.mView = mView;
        mRepo = new AddEbookRepository(this);
    }

    @Override
    public void post(String halaman, List<String> kategori, String title, String thumbnailUrl, Uri thumbnailUri,
                     String jenis, String jenisImage, String ebookLink, boolean isPremium, boolean isPilihan){
        mRepo.post(halaman, kategori, title, thumbnailUrl, thumbnailUri, jenis, jenisImage, ebookLink, isPremium, isPilihan);
    }

    @Override
    public void onUpSuccessListener(String txt) {
        mView.showSuccess(txt);
    }

    @Override
    public void onUpErrorListener(String txt) {
        mView.showError(txt);
    }

    @Override
    public void progressListener(String txt, double progress) {
        mView.showProgress(txt, progress);
    }
}
