package com.munifahsan.skdskb.InformasiPage.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munifahsan.skdskb.InformasiPage.adapter.KategoriInformasiAdapter;
import com.munifahsan.skdskb.InformasiPage.adapter.ListInformasiAdapter;
import com.munifahsan.skdskb.InformasiPage.presenter.InformasiPres;
import com.munifahsan.skdskb.InformasiPage.presenter.InformasiPresInt;
import com.munifahsan.skdskb.R;

import butterknife.ButterKnife;


public class InformasiFragment extends Fragment implements InformasiViewInt {

    InformasiPresInt mInformasiPres;
    KategoriInformasiAdapter mKategoriAdapter;
    ListInformasiAdapter mListInformasiAdapter;

    public InformasiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informasi, container, false);

        ButterKnife.bind(this, view);
        mInformasiPres = new InformasiPres(this);
        mInformasiPres.onCreate();

        showKategori();
        showListInformasi();
        return view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mInformasiPres.onDestroy();
    }

    private void showKategori(){

    }
    private void showListInformasi() {

    }
}