package com.munifahsan.skdskb.AddKategori;

import android.net.Uri;

public interface AddKategoriContract {
    interface View{

        void showError(String error);

        void hideError();

        void showProgress(String txt, double progress);

        void showSuccess(String success);
    }

    interface Presenter{

        void upData(String title, String halaman, String kategori, String desc, String imageUrl, Uri imageUri, int font);
    }

    interface Repository{

        void upData(String title, String halaman, String kategori, String desc, String imageUrl, Uri imageUri, int font);
    }

    interface Listener{

        void onUpErrorListener(String message);

        void progressListener(String s, double progress);

        void onUpSuccessListener(String artikel_berhasil_di_publish);
    }
}
