package com.munifahsan.skdskb.AddArtikel;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public interface AddArtikelContract {
    interface View {
        void showError(String error);

        void hideError();

        void showProgress(String txt, double progress);

        void showSuccess(String success);
    }

    interface Presenter {
        void title(String title);

        void kategori(List<String> kategori);

        void jenis(String txt);

        void jenisImage(String txt);

        void tipe(String txt);

        void premium(boolean b);

        void pilihanEditor(boolean b);

        void thumbnailImage(String edt, Uri uri);

        void headerImage(String edt, Uri uri);

        void par1(String edt, Uri uri);

        void par2(String edt, Uri uri);

        void par3(String edt, Uri uri);

        void par4(String edt, Uri uri);

        void par5(String edt, Uri uri);

        void par6(String edt, Uri uri);

        void par7(String edt, Uri uri);

        void par8(String edt, Uri uri);

        void par9(String edt, Uri uri);

        void par10(String edt, Uri uri);

        void par11(String edt, Uri uri);

        void par12(String edt, Uri uri);

        void par13(String edt, Uri uri);

        void par14(String edt, Uri uri);

        void par15(String edt, Uri uri);
    }

    interface Repository {
        void setKategori(List<String> kategori);

        void upTxtTitle(String txt);

        void setmJenisImage(String mJenisImage);

        void upTxtJenis(String txt);

        void setmTipe(String mTipe);

        void setPremium(boolean premium);

        void setPilihanEditor(boolean pilihanEditor);

        void upTxtThumbnail(String txt);

        void upImgThumbnail(Uri uri);

        void upTxtHeader(String txt);

        void upImgHeader(Uri uri);

        void upTxtPar1(String txt);

        void upTxtPar3(String txt);

        void upTxtPar4(String txt);

        void upTxtPar5(String txt);

        void upTxtPar6(String txt);

        void upTxtPar7(String txt);

        void upTxtPar8(String txt);

        void upTxtPar9(String txt);

        void upTxtPar10(String txt);

        void upTxtPar11(String txt);

        void upTxtPar12(String txt);

        void upTxtPar13(String txt);

        void upTxtPar14(String txt);

        void upTxtPar15(String txt);

        void upImgPar1(Uri uri);

        void upTxtPar2(String txt);

        void upImgPar2(Uri uri);

        void upImgPar3(Uri uri);

        void upImgPar4(Uri uri);

        void upImgPar5(Uri uri);

        void upImgPar6(Uri uri);

        void upImgPar7(Uri uri);

        void upImgPar8(Uri uri);

        void upImgPar9(Uri uri);

        void upImgPar10(Uri uri);

        void upImgPar11(Uri uri);

        void upImgPar12(Uri uri);

        void upImgPar13(Uri uri);

        void upImgPar14(Uri uri);

        void upImgPar15(Uri uri);
    }

    interface Listener {
        void listenerPar1(boolean b);

        void onUpSuccessListener(String txt);

        void onUpErrorListener(String txt);

        void progressListener(String txt, double progress);
    }
}
