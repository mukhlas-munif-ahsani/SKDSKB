package com.munifahsan.skdskb.AddEbook;

import android.net.Uri;

import java.util.List;

public interface AddEbookContract {
    interface View{

        void showError(String error);

        void hideError();

        void showProgress(String txt, double progress);

        void showSuccess(String success);
    }

    interface Presenter{

        void post(String halaman, List<String> kategori, String title, String thumbnailUrl, Uri thumbnailUri,
                  String jenis, String jenisImage, String ebookLink, boolean isPremium, boolean isPilihan);
    }

    interface Repository{

        void post(String halaman, List<String> kategori, String title, String thumbnailUrl, Uri thumbnailUri,
                  String jenis, String jenisImage, String ebookLink, boolean isPremium, boolean isPilihan);
    }

    interface Listener{

        void onUpSuccessListener(String kategori_berhasil_di_publish);

        void onUpErrorListener(String message);

        void progressListener(String s, double progress);
    }
}
