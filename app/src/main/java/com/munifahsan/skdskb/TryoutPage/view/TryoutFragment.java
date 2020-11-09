package com.munifahsan.skdskb.TryoutPage.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.munifahsan.skdskb.TryoutPage.adapter.AllKategoriAdapter;
import com.munifahsan.skdskb.TryoutPage.adapter.KategoriTryoutAdapter;
import com.munifahsan.skdskb.TryoutPage.adapter.ListTryoutAdapter;
import com.munifahsan.skdskb.TryoutPage.presenter.TryoutPresInt;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.TryoutPage.presenter.TryoutPres;
import com.munifahsan.skdskb.TryoutPage.presenter.TryoutPresInt;

import butterknife.ButterKnife;

public class TryoutFragment extends Fragment implements TryoutViewInt {

    TryoutPresInt mTryoutPres;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mKategoriRef = firebaseFirestore.collection("KATEGORI_INFORMASI");
    private CollectionReference mListRef = firebaseFirestore.collection("POST");
    FirebaseUser mCurrentUser;
    Query query;
    private BottomSheetBehavior mBottomSheetBehavior;

    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    KategoriTryoutAdapter mKategoriAdapter;
    private AllKategoriAdapter mAllKategoriAdapter;
    ListTryoutAdapter mListInformasiAdapter;

    public TryoutFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tryout, container, false);

        ButterKnife.bind(this, view);
        mTryoutPres = new TryoutPres(this);
        mTryoutPres.onCreate();

        mTryoutPres.getData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTryoutPres.onDestroy();
    }

    @Override
    public void setQuote(String quote, String talker){

    }

    @Override
    public void hideQuote(){

    }

    @Override
    public void showMessage(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}