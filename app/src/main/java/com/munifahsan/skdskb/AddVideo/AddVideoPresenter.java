package com.munifahsan.skdskb.AddVideo;

import android.net.Uri;
import android.util.Log;

import java.util.List;

public class AddVideoPresenter implements AddVideoContract.Presenter, AddVideoContract.Listener {
    AddVideoContract.View mView;
    AddVideoContract.Repository mRepo;

    public AddVideoPresenter(AddVideoContract.View mView) {
        this.mView = mView;
        mRepo = new AddVideoRepository(this);
    }

    @Override
    public void title(String title) {
        mRepo.upTxtTitle(title);
    }

    @Override
    public void kategori(List<String> kategori) {
        mRepo.setKategori(kategori);
    }

    @Override
    public void jenis(String txt) {
        mRepo.upTxtJenis(txt);
    }

    @Override
    public void jenisImage(String txt) {
        mRepo.setmJenisImage(txt);
    }

    @Override
    public void tipe(String txt) {
        mRepo.setmTipe(txt);
    }

    @Override
    public void premium(boolean b) {
        mRepo.setPremium(b);
    }

    @Override
    public void pilihanEditor(boolean b) {
        mRepo.setPilihanEditor(b);
    }

    @Override
    public void thumbnailImage(String edt, Uri uri) {
        mRepo.upImgThumbnail(uri);
        if (uri != null) {
            mRepo.upImgThumbnail(uri);
        }
        if (!edt.isEmpty()) {
            Log.d("par1", edt);
            mRepo.upTxtThumbnail(edt);
        }
        Log.d("thumnail uri", String.valueOf(uri));
    }

    @Override
    public void headerImage(String edt) {
        if (!edt.isEmpty()) {
            Log.d("par1", edt);
            mRepo.upTxtHeader(edt);
        }
    }

    @Override
    public void par1(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            Log.d("par1", edt);
            mRepo.upTxtPar1(edt);
        }

        if (uri != null) {
            mRepo.upImgPar1(uri);
            Log.d("par1", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar1("");
        }
    }

    @Override
    public void par2(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar2(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar2(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar2("");
        }
    }

    @Override
    public void par3(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar3(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar3(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar3("");
            Log.d("par3", "kosong");
        }
    }

    @Override
    public void par4(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar4(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar4(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar4("");
        }
    }

    @Override
    public void par5(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar5(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar5(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar5("");
        }
    }

    @Override
    public void par6(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar6(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar6(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar6("");
        }
    }

    @Override
    public void par7(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar7(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar7(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar7("");
        }
    }

    @Override
    public void par8(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar8(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar8(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar8("");
        }
    }

    @Override
    public void par9(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar9(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar9(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar9("");
        }
    }

    @Override
    public void par10(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar10(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar10(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar10("");
        }
    }

    @Override
    public void par11(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar11(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar11(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar11("");
        }
    }

    @Override
    public void par12(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar12(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar12(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar12("");
        }
    }

    @Override
    public void par13(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar13(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar13(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar13("");
        }
    }

    @Override
    public void par14(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar14(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar14(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar14("");
        }
    }

    @Override
    public void par15(String edt, Uri uri) {
        if (!edt.isEmpty()) {
            mRepo.upTxtPar15(edt);
            Log.d("par1", edt);
        }

        if (uri != null) {
            mRepo.upImgPar15(uri);
            Log.d("par2", uri.toString());
        }

        if (edt.isEmpty() && uri == null) {
            mRepo.upTxtPar15("");
        }
    }

    @Override
    public void listenerPar1(boolean b) {

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
