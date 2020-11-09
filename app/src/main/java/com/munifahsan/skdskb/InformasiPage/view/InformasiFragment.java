package com.munifahsan.skdskb.InformasiPage.view;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.skdskb.InformasiPage.adapter.AllKategoriAdapter;
import com.munifahsan.skdskb.InformasiPage.model.InformasiModel;
import com.munifahsan.skdskb.InformasiPage.model.KategoriModel;
import com.munifahsan.skdskb.InformasiPage.adapter.KategoriInformasiAdapter;
import com.munifahsan.skdskb.InformasiPage.adapter.ListInformasiAdapter;
import com.munifahsan.skdskb.InformasiPage.presenter.InformasiPres;
import com.munifahsan.skdskb.InformasiPage.presenter.InformasiPresInt;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.SpacesItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InformasiFragment extends Fragment implements InformasiViewInt {

    @BindView(R.id.imageView_closeKategori_informasi)
    ImageView mCloseAllKategori;
    @BindView(R.id.recyclerView_gridAllKategori_informasi)
    RecyclerView mRvGridKategori;
    @BindView(R.id.bottom_sheet_kategori_informasi)
    FrameLayout mBottomSheetKategori;
    @BindView(R.id.textView_quote_informasi)
    TextView mQuote;
    @BindView(R.id.quoteShimmer_informasi)
    ShimmerFrameLayout mQuoteShimmer;
    @BindView(R.id.textView_talker_informasi)
    TextView mTalker;
    @BindView(R.id.textView_talker_informasi0)
    TextView mTalker0;
    @BindView(R.id.textView_talker_informasi2)
    TextView mTalker2;
    @BindView(R.id.kategoriContent_informasi)
    LinearLayout mKategoriContent;
    @BindView(R.id.kategoriShimmer_informasi)
    ShimmerFrameLayout mShimmerKategori;
    @BindView(R.id.kategorimateri_informasi)
    TextView mKategoriMateri;
    @BindView(R.id.recyclerView_kategoriMateri_informasi)
    RecyclerView mRvKategori;
    @BindView(R.id.recyclerView_listOne_informasi)
    RecyclerView mRvListOne;
    @BindView(R.id.lin_listOne_informasi)
    LinearLayout mInformasiContent;
    @BindView(R.id.shimmer_listOne_informasi)
    ShimmerFrameLayout mShimmerListContent;
    @BindView(R.id.editText_search_informasi)
    EditText mSearchField;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mKategoriRef = firebaseFirestore.collection("KATEGORI_INFORMASI");
    private CollectionReference mListRef = firebaseFirestore.collection("POST");
    FirebaseUser mCurrentUser;
    Query query;
    InformasiPresInt mInformasiPres;
    private BottomSheetBehavior mBottomSheetBehavior;

    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    KategoriInformasiAdapter mKategoriAdapter;
    private AllKategoriAdapter mAllKategoriAdapter;
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

        mInformasiPres.getData();

        /*
        bottom sheet behaviour
         */
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetKategori);
        mBottomSheetBehavior.setDraggable(false);
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mCloseAllKategori.setRotation(slideOffset * 180);
            }
        });

        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    getActivity().finish();
                }
            }
        });

        showKategori();
        showListInformasi();
        showAllKategori();
        return view;
    }

    private void showKategori() {
 /*
        menampilkan list kategori materi secara horizontal
         */
        mKategoriContent.setVisibility(View.GONE);
        mKategoriMateri.setVisibility(View.INVISIBLE);
        mShimmerKategori.setVisibility(View.VISIBLE);

        query = mKategoriRef;
        FirestoreRecyclerOptions<KategoriModel> options = new FirestoreRecyclerOptions.Builder<KategoriModel>()
                .setQuery(query, KategoriModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mKategoriContent.setVisibility(View.VISIBLE);
                    mKategoriMateri.setVisibility(View.VISIBLE);
                    mShimmerKategori.setVisibility(View.GONE);
                    //showMessage("ada");
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mKategoriAdapter = new KategoriInformasiAdapter(options);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvKategori.setLayoutManager(mLayoutManager);
        mRvKategori.setAdapter(mKategoriAdapter);

        mKategoriAdapter.setOnItemClickListener(new KategoriInformasiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position) {
                showMessage("clicked");
            }
        });
    }

    private void showAllKategori() {
        /*
        menampilkan list kategori materi secara horizontal
         */
//        mKategoriContent.setVisibility(View.GONE);
//        mShimerKategori.setVisibility(View.VISIBLE);

        query = mKategoriRef;
        FirestoreRecyclerOptions<KategoriModel> options = new FirestoreRecyclerOptions.Builder<KategoriModel>()
                .setQuery(query, KategoriModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
//                    mKategoriContent.setVisibility(View.VISIBLE);
//                    mShimerKategori.setVisibility(View.GONE);
                    //showMessage("ada");
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mAllKategoriAdapter = new AllKategoriAdapter(options);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mRvGridKategori.setLayoutManager(mGridLayoutManager);
        mRvGridKategori.setAdapter(mAllKategoriAdapter);
        mRvGridKategori.addItemDecoration(new SpacesItemDecoration(25));

        mAllKategoriAdapter.setOnItemClickListener(new AllKategoriAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position) {
                showMessage("clicked");
            }
        });
    }

    private void showListInformasi() {
          /*
        menampilkan list materi pilihan editor
         */
        mInformasiContent.setVisibility(View.GONE);
        mShimmerListContent.setVisibility(View.VISIBLE);

        query = mListRef.whereEqualTo("nTipe", "informasi").orderBy("nUploadTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<InformasiModel> options = new FirestoreRecyclerOptions.Builder<InformasiModel>()
                .setQuery(query, InformasiModel.class)
                .build();
        mListInformasiAdapter = new ListInformasiAdapter(options);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mInformasiContent.setVisibility(View.VISIBLE);
                    mShimmerListContent.setVisibility(View.GONE);
//                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvListOne.setLayoutManager(mLayoutManager);
        mRvListOne.setAdapter(mListInformasiAdapter);
        mRvListOne.setNestedScrollingEnabled(false);

        mListInformasiAdapter.setOnItemClickListener(new ListInformasiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position) {
                showMessage("clicked");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mKategoriAdapter.startListening();
        mListInformasiAdapter.startListening();
        mAllKategoriAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInformasiPres.onDestroy();
        mKategoriAdapter.stopListening();
        mListInformasiAdapter.stopListening();
        mAllKategoriAdapter.stopListening();
    }

    @OnClick(R.id.cardView_allKateoriMateri_informasi)
    public void allKategoriClick() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.imageView_closeKategori_informasi)
    public void allKategoriCloseIcon() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.textView_allListOne_informasi)
    public void allListOneClick() {
        showMessage("click");
    }


    @OnClick(R.id.cardView_search_informasi)
    public void searchOnClick(){
        String value = mSearchField.getText().toString();
        if (value.isEmpty()){
            showMessage("kosong bro");
        }
    }

    @Override
    public void setQuote(String quote, String talker){
        mSearchField.setEnabled(true);
        mQuote.setVisibility(View.VISIBLE);
        mQuoteShimmer.setVisibility(View.INVISIBLE);
        mTalker.setVisibility(View.VISIBLE);
        mQuote.setText(quote);
        mTalker.setText(talker);
        mTalker0.setVisibility(View.VISIBLE);
        mTalker2.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideQuote(){
        mQuote.setVisibility(View.INVISIBLE);
        mSearchField.setEnabled(false);
        mTalker.setVisibility(View.INVISIBLE);
        mTalker0.setVisibility(View.INVISIBLE);
        mTalker2.setVisibility(View.INVISIBLE);
        mQuoteShimmer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}