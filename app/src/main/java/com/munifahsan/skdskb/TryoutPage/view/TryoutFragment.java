package com.munifahsan.skdskb.TryoutPage.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
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
import com.munifahsan.skdskb.Articel.ArticleActivity;
import com.munifahsan.skdskb.BillingClientSetup;
import com.munifahsan.skdskb.DetailKategori.DetailKategoriActivity;
import com.munifahsan.skdskb.Ebook.EbookActivity;
import com.munifahsan.skdskb.Search.SearchActivity;
import com.munifahsan.skdskb.SpacesItemDecoration;
import com.munifahsan.skdskb.Models.KategoriModel;
import com.munifahsan.skdskb.Adapters.AllKategoriAdapter;
import com.munifahsan.skdskb.Adapters.KategoriAdapter;
import com.munifahsan.skdskb.Tryout.TryoutActivity;
import com.munifahsan.skdskb.TryoutPage.adapter.ListTryoutAdapter;
import com.munifahsan.skdskb.TryoutPage.model.TryoutModel;
import com.munifahsan.skdskb.TryoutPage.presenter.TryoutPresInt;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.TryoutPage.presenter.TryoutPres;
import com.munifahsan.skdskb.Video.VideoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TryoutFragment extends Fragment implements TryoutViewInt, PurchasesUpdatedListener {

    @BindView(R.id.imageView_closeKategori_tryout)
    ImageView mCloseAllKategori;
    @BindView(R.id.recyclerView_gridAllKategori_tryout)
    RecyclerView mRvGridKategori;
    @BindView(R.id.bottom_sheet_kategori_tryout)
    FrameLayout mBottomSheetKategori;
    @BindView(R.id.textView_quote_tryout)
    TextView mQuote;
    @BindView(R.id.quoteShimmer_tryout)
    ShimmerFrameLayout mQuoteShimmer;
    @BindView(R.id.textView_talker_tryout)
    TextView mTalker;
    @BindView(R.id.textView_talker_tryout0)
    TextView mTalker0;
    @BindView(R.id.textView_talker_tryout2)
    TextView mTalker2;
    @BindView(R.id.kategoriContent_tryout)
    LinearLayout mKategoriContent;
    @BindView(R.id.kategoriShimmer_tryout)
    ShimmerFrameLayout mShimmerKategori;
    @BindView(R.id.kategorimateri_tryout)
    TextView mKategoriMateri;
    @BindView(R.id.recyclerView_kategoriMateri_tryout)
    RecyclerView mRvKategori;
    @BindView(R.id.recyclerView_listOne_tryout)
    RecyclerView mRvListOne;
    @BindView(R.id.lin_listOne_tryout)
    LinearLayout mInformasiContent;
    @BindView(R.id.shimmer_listOne_tryout)
    ShimmerFrameLayout mShimmerListContent;
    @BindView(R.id.editText_search_tryout)
    EditText mSearchField;

    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    boolean isPremium = false;

    TryoutPresInt mTryoutPres;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mKategoriRef = firebaseFirestore.collection("KATEGORI");
    private CollectionReference mListRef = firebaseFirestore.collection("POST");
    FirebaseUser mCurrentUser;
    Query query;
    private BottomSheetBehavior mBottomSheetBehavior;

    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    KategoriAdapter mKategoriAdapter;
    private AllKategoriAdapter mAllKategoriAdapter;
    ListTryoutAdapter mListTryoutAdapter;

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

        setupSubsBillingClient();
        setupBillingClient();

        mTryoutPres = new TryoutPres(this);
        mTryoutPres.onCreate();

        mTryoutPres.getData();

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

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mTryoutPres.getData();
                showKategori();
                showAllKategori();
                showListInformasi();

            }
        });

        showKategori();
        showAllKategori();
        showListInformasi();

        return view;
    }

    private void setupSubsBillingClient() {

        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //mRvPurchase.setVisibility(View.VISIBLE);
                }
            }
        };

        billingClient = BillingClientSetup.getInstance(getActivity(), this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Toast.makeText(getActivity(), "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                            .getPurchasesList();

                    for (Purchase purchase : purchases) {
                        handleItemAlreadyPurchase(purchase);
                    }

                } else {
                    Toast.makeText(getActivity(), "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getActivity(), "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBillingClient() {

        billingClient = BillingClientSetup.getInstance(getActivity(), this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Toast.makeText(getActivity(), "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList();
                    // if (purchases.size() > 0) {
                    for (Purchase purchase : purchases) {
                        handleItemAlreadyPurchase(purchase);
                    }
                    //}
                } else {
                    Toast.makeText(getActivity(), "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getActivity(), "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for (Purchase purchase : list) {
                handleItemAlreadyPurchase(purchase);
            }
        }
    }

    private void handleItemAlreadyPurchase(Purchase purchases) {
        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchases.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            } else {
                isPremium = true;
                //showMessage("You are premium");
            }
        }
    }

    private void showKategori() {
 /*
        menampilkan list kategori materi secara horizontal
         */
        mKategoriContent.setVisibility(View.GONE);
        mKategoriMateri.setVisibility(View.INVISIBLE);
        mShimmerKategori.setVisibility(View.VISIBLE);

        query = mKategoriRef.whereEqualTo("nHalaman", "TRYOUT");
        ;
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

        mKategoriAdapter = new KategoriAdapter(options);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvKategori.setLayoutManager(mLayoutManager);
        mRvKategori.setAdapter(mKategoriAdapter);

        mKategoriAdapter.setOnItemClickListener(new KategoriAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position, String kategori, String collection) {
                Intent intent = new Intent(getActivity(), DetailKategoriActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                intent.putExtra("KATEGORI", kategori);
                intent.putExtra("COLLECTION", collection);
                intent.putExtra("TIPE", "tryout");

                startActivity(intent);
            }
        });

        mKategoriAdapter.startListening();
    }

    private void showAllKategori() {
        /*
        menampilkan list kategori materi secara horizontal
         */
//        mKategoriContent.setVisibility(View.GONE);
//        mShimerKategori.setVisibility(View.VISIBLE);

        query = mKategoriRef.whereEqualTo("nHalaman", "TRYOUT");
        ;
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
            public void onItemClick(String id, int position, String kategori, String collection) {
                Intent intent = new Intent(getActivity(), DetailKategoriActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                intent.putExtra("KATEGORI", kategori);
                intent.putExtra("COLLECTION", collection);
                intent.putExtra("TIPE", "tryout");

                startActivity(intent);
            }
        });

        mAllKategoriAdapter.startListening();
    }

    private void showListInformasi() {
          /*
        menampilkan list materi pilihan editor
         */
        mInformasiContent.setVisibility(View.GONE);
        mShimmerListContent.setVisibility(View.VISIBLE);

        query = mListRef.whereEqualTo("nTipe", "tryout").orderBy("nUploadTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<TryoutModel> options = new FirestoreRecyclerOptions.Builder<TryoutModel>()
                .setQuery(query, TryoutModel.class)
                .build();
        mListTryoutAdapter = new ListTryoutAdapter(options);

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
        mRvListOne.setAdapter(mListTryoutAdapter);
        mRvListOne.setNestedScrollingEnabled(false);

        mListTryoutAdapter.setOnItemClickListener(new ListTryoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position, String jenis, boolean isPremium) {
//                Intent intent = new Intent(getActivity(), ArticleActivity.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                startActivity(intent);
                switch (jenis) {
                    case "Artikel":
                        sendToArtikel(id, isPremium);
                        break;
                    case "Video":
                        sendToVideo(id, isPremium);
                        break;
                    case "Ebook":
                        sendToEbook(id, isPremium);
                        break;
                    case "Tryout":
                        sendToTryout(id, isPremium);
                        break;
                }
            }
        });

        mListTryoutAdapter.startListening();
    }

    private void sendToArtikel(String id, boolean isPremiumContent) {
        if (isPremiumContent) {
            if (isPremium) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(getActivity(), ArticleActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    private void sendToVideo(String id, boolean isPremiumContent) {
        if (isPremiumContent) {
            if (isPremium) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    private void sendToEbook(String id, boolean isPremiumContent) {
        if (isPremiumContent) {
            if (isPremium) {
                Intent intent = new Intent(getActivity(), EbookActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(getActivity(), EbookActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    private void sendToTryout(String id, boolean isPremiumContent) {
        if (isPremiumContent) {
            if (isPremium) {
                Intent intent = new Intent(getActivity(), TryoutActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(getActivity(), TryoutActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mKategoriAdapter.startListening();
        mAllKategoriAdapter.startListening();
        mListTryoutAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTryoutPres.onDestroy();
        mKategoriAdapter.stopListening();
        mAllKategoriAdapter.stopListening();
        mListTryoutAdapter.stopListening();
    }

    @OnClick(R.id.cardView_allKateoriMateri_tryout)
    public void allKategoriClick() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.imageView_closeKategori_tryout)
    public void allKategoriCloseIcon() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.textView_allListOne_tryout)
    public void allListOneClick() {
        showMessage("click");
    }


    @OnClick(R.id.cardView_search_tryout)
    public void searchOnClick() {
        String value = mSearchField.getText().toString().trim();
        if (!value.isEmpty()) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("SEARCH_VALUE", value);
            intent.putExtra("KATEGORI", "tryout");
            startActivity(intent);
        } else {
            showMessage("kosong bro");
        }
    }


    @Override
    public void setQuote(String quote, String talker) {
        mSearchField.setEnabled(true);
        mQuote.setVisibility(View.VISIBLE);
        mQuoteShimmer.setVisibility(View.INVISIBLE);
        mTalker.setVisibility(View.VISIBLE);
        mQuote.setText(quote);
        mTalker.setText(talker);
        mTalker0.setVisibility(View.VISIBLE);
        mTalker2.setVisibility(View.VISIBLE);

        mRefresh.setRefreshing(false);
    }

    @Override
    public void hideQuote() {
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