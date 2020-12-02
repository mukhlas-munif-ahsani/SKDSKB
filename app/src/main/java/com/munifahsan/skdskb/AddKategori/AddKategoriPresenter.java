package com.munifahsan.skdskb.AddKategori;

import android.net.Uri;

public class AddKategoriPresenter implements AddKategoriContract.Presenter, AddKategoriContract.Listener {

    AddKategoriContract.View mView;
    AddKategoriContract.Repository mRepo;

    public AddKategoriPresenter(AddKategoriContract.View mView) {
        this.mView = mView;
        mRepo = new AddKategoriRepository(this);
    }

    @Override
    public void upData(String title, String halaman, String kategori, String desc, String imageUrl, Uri imageUri, int font) {
        mRepo.upData(title, halaman, kategori, desc, imageUrl, imageUri, font);
    }

    @Override
    public void onUpErrorListener(String message) {
        mView.showError(message);
    }

    @Override
    public void progressListener(String s, double progress) {
        mView.showProgress(s, progress);
    }

    @Override
    public void onUpSuccessListener(String artikel_berhasil_di_publish) {
        mView.showSuccess(artikel_berhasil_di_publish);
    }
}
