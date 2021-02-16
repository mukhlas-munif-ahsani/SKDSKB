package com.munifahsan.skdskb.HomePage.view;

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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.skdskb.Adapters.KategoriAdapter;
import com.munifahsan.skdskb.Articel.ArticleActivity;
import com.munifahsan.skdskb.BillingClientSetup;
import com.munifahsan.skdskb.DetailKategori.DetailKategoriActivity;
import com.munifahsan.skdskb.Adapters.AllKategoriAdapter;
import com.munifahsan.skdskb.Adapters.ListOneAdapter;
import com.munifahsan.skdskb.Ebook.EbookActivity;
import com.munifahsan.skdskb.HomePage.adapter.ListTwoAdapter;
import com.munifahsan.skdskb.MainAdminActivity;
import com.munifahsan.skdskb.Models.KategoriModel;
import com.munifahsan.skdskb.HomePage.model.MateriModel;
import com.munifahsan.skdskb.HomePage.presenter.HomePres;
import com.munifahsan.skdskb.HomePage.presenter.HomePresInt;
import com.munifahsan.skdskb.NotifPage.NotifActivity;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.Search.SearchActivity;
import com.munifahsan.skdskb.SpacesItemDecoration;
import com.munifahsan.skdskb.SubscribeActivity;
import com.munifahsan.skdskb.Tryout.TryoutActivity;
import com.munifahsan.skdskb.Video.VideoActivity;
import com.readystatesoftware.viewbadger.BadgeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements HomeViewInt, PurchasesUpdatedListener {

    private static final String LOG_TAG = "HomeFragment";
    @BindView(R.id.editText_search_home)
    EditText mSearchField;
    @BindView(R.id.imageView_closeKategori_home)
    ImageView mCloseAllKategori;
    @BindView(R.id.recyclerView_gridAllKategori_home)
    RecyclerView mRvGridKategori;
    @BindView(R.id.bottom_sheet_kategori_materi_home)
    FrameLayout mBottomSheetKategori;
    @BindView(R.id.kategoriContent)
    LinearLayout mKategoriContent;
    @BindView(R.id.kategorimateri)
    TextView mKategoriMateritxt;
    @BindView(R.id.recyclerView_kategoriMateri_home)
    RecyclerView mRvKategori;
    @BindView(R.id.kategoriShimmer_home)
    ShimmerFrameLayout mShimerKategori;
    @BindView(R.id.recyclerView_listOne_home)
    RecyclerView mRvListOne;
    @BindView(R.id.shimmer_listOne_home)
    ShimmerFrameLayout mShimmerListOne;
    @BindView(R.id.lin_listOne_home)
    LinearLayout mListOneContent;
    @BindView(R.id.recyclerView_listTwo_home)
    RecyclerView mRvListTwo;
    @BindView(R.id.shimmer_listTwo_home)
    ShimmerFrameLayout mShimmerListTwo;
    @BindView(R.id.lin_listTwo_home)
    LinearLayout mListTwoContent;
    @BindView(R.id.textView_greating_home)
    TextView mGreeting;
    @BindView(R.id.textView_greatingShimmer_home)
    ShimmerFrameLayout mGreetingShimmer;
    @BindView(R.id.textView_name_home)
    TextView mNama;
    @BindView(R.id.textView_namaShimmer_home)
    ShimmerFrameLayout mShimmerNama;
    @BindView(R.id.circleImage_photo_home)
    CircleImageView mPhoto;
    @BindView(R.id.photoShimmer_home)
    ShimmerFrameLayout mShimmerPhoto;
    @BindView(R.id.textView_quote_home)
    TextView mQuote;
    @BindView(R.id.quoteShimmer_home)
    ShimmerFrameLayout mQuoteShimmer;
    @BindView(R.id.textView_talker_home0)
    TextView mTalker0;
    @BindView(R.id.textView_talker_home)
    TextView mTalker;
    @BindView(R.id.textView_talker_home2)
    TextView mTalker2;

    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;

    @BindView(R.id.relative_notif_home)
    RelativeLayout mNotifImage;

    private FirebaseAuth mAuth;
    private String mCurrent_id;

    HomePresInt mHomePres;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mKategoriRef = firebaseFirestore.collection("KATEGORI");
    private CollectionReference mListRef = firebaseFirestore.collection("POST");
    private KategoriAdapter mKategoriAdapter;
    private AllKategoriAdapter mAllKategoriAdapter;
    private ListOneAdapter mListOneAdapter;
    private ListTwoAdapter mListTwoAdapter;
    FirebaseUser mCurrentUser;
    Query query;

    Bundle bundle = new Bundle();

    private BottomSheetBehavior mBottomSheetBehavior;

    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    boolean isPremium = false;

    BillingProcessor bp;

    int a, b;

    private static HomeFragment instance;

    public static HomeFragment getInstance() {
        return instance;
    }

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        setupSubsBillingClient();
        setupBillingClient();

//        bp = new BillingProcessor(getActivity(), null, new BillingProcessor.IBillingHandler() {
//            @Override
//            public void onProductPurchased(String productId, TransactionDetails details) {
//
//            }
//
//            @Override
//            public void onPurchaseHistoryRestored() {
//                showMessage("onPurchaseHistoryRestored");
//                for (String sku : bp.listOwnedProducts())
//                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
//                for (String sku : bp.listOwnedSubscriptions())
//                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
//            }
//
//            @Override
//            public void onBillingError(int errorCode, Throwable error) {
//
//            }
//
//            @Override
//            public void onBillingInitialized() {
//                showMessage("initialized");
//            }
//        });
        //bp.initialize();

//        boolean consumed = bp.consumePurchase("forever_buy");
//
//        if (consumed){
//            showMessage("consumed");
//        }

//        if (bp.isPurchased("forever_buy")){
//            //showMessage("purces");
//            isPremium = true;
//        }

//        if (bp.loadOwnedPurchasesFromGoogle() && bp.isSubscribed("1_bulan")) {
//            isPremium = true;
//        }

//        boolean purchaseResult = bp.loadOwnedPurchasesFromGoogle();
//
//        if (purchaseResult) {
//            TransactionDetails subscriptionTransactionDetails = bp.getPurchaseTransactionDetails("forever_buy");
//            if (subscriptionTransactionDetails != null) {
//                //User is still subscribed
//                isPremium = true;
//                Log.d("BILLING v", "Subscription is valid");
//            } else {
//                //Not subscribed
//                Log.d("BILLING v", "Subscription is NOT valid");
//            }
//        }
//
//        if (purchaseResult) {
//            TransactionDetails subscriptionTransactionDetails = bp.getSubscriptionTransactionDetails("1_bulan");
//            if (subscriptionTransactionDetails != null) {
//                //User is still subscribed
//                isPremium = true;
//                Log.d("BILLING v", "Subscription is valid");
//            } else {
//                //Not subscribed
//                Log.d("BILLING v", "Subscription is NOT valid");
//            }
//        }

        instance = this;

//        setupSubsBillingClient();
//        setupBillingClient();

        mHomePres = new HomePres(this);
        mHomePres.onCreate();
        mAuth = FirebaseAuth.getInstance();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        /*
        mengambil data nama dan foto user
         */
        hideNama();
        hidePhoto();
        if (mCurrentUser != null) {
//            mCurrent_id = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("USERS").document(mCurrentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        setmNama(mAuth.getCurrentUser().getProviderData().get(1).getDisplayName());
                        setmPhoto(mAuth.getCurrentUser().getProviderData().get(1).getPhotoUrl().toString());
                    } else {
                        mHomePres.getUserData(null);
                    }
                }
            });

        } else {
            mHomePres.getUserData(null);
            //showMessage("kosong");
        }

        /*
        mengambil data halaman home
         */
        mHomePres.getData();

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
                mHomePres.getData();
                showKategori();
                showAllKategori();
                showListOne();
                showListTwo();

                notifCount();
            }
        });

        showKategori();
        showAllKategori();
        showListOne();
        showListTwo();

        notifCount();

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

                    if (purchases != null){
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                if (isAdded()){
                    Toast.makeText(getActivity(), "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
                }
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
                    if (purchases != null){
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    }

                    //}
                } else {
                    Toast.makeText(getActivity(), "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                if (isAdded()){
                    Toast.makeText(getActivity(), "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
                }
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

    public void notifCount() {

        mListRef.whereArrayContains("nSeen", mCurrentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                Toast.makeText(getActivity(), queryDocumentSnapshots.size()+"", Toast.LENGTH_LONG).show();
                a = queryDocumentSnapshots.size();

                mListRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        b = queryDocumentSnapshots.size();

                        //   Toast.makeText(getActivity(), b-a+"", Toast.LENGTH_LONG).show();
                        showNotifCount(b - a);

                        //showMessage("refresed");
                    }
                });
            }
        });

        //refresh(1000);
    }

    public void showNotifCount(int count) {
        if (mCurrentUser != null) {
            if (count != 0) {
                if (isAdded()) {
                    BadgeView badge = new BadgeView(getActivity(), mNotifImage);
                    badge.setText(String.valueOf(count));
                    badge.show();
                }
            } else {
                BadgeView badge = new BadgeView(getActivity(), mNotifImage);
                badge.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static String getTimeDate(Date timestamp) {
        try {
            //Date netDate = (timestamp);
            SimpleDateFormat sfd = new SimpleDateFormat("yyyyMddhhmmss", Locale.getDefault());
            return sfd.format(timestamp);
        } catch (Exception e) {
            return "date";
        }
    }

    private void refresh(int milliseconds) {
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                notifCount();
                getActivity();
            }
        };

        handler.postDelayed(runnable, milliseconds);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHomePres.onDestroy();
    }

    private void showKategori() {
        /*
        menampilkan list kategori materi secara horizontal
         */
        mKategoriContent.setVisibility(View.GONE);
        mKategoriMateritxt.setVisibility(View.INVISIBLE);
        mShimerKategori.setVisibility(View.VISIBLE);

        query = mKategoriRef.whereEqualTo("nHalaman", "MATERI");
        FirestoreRecyclerOptions<KategoriModel> options = new FirestoreRecyclerOptions.Builder<KategoriModel>()
                .setQuery(query, KategoriModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mKategoriContent.setVisibility(View.VISIBLE);
                    mKategoriMateritxt.setVisibility(View.VISIBLE);
                    mShimerKategori.setVisibility(View.GONE);
                    //showMessage("ada");
                } else {
                    hideQuote();
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
                intent.putExtra("TIPE", "materi");

                startActivity(intent);
                showMessage("clicked");
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

        query = mKategoriRef.whereEqualTo("nHalaman", "MATERI");
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
                intent.putExtra("TIPE", "materi");

                startActivity(intent);
            }
        });

        mAllKategoriAdapter.startListening();

    }

    private void showListOne() {
        /*
        menampilkan list materi pilihan editor
         */
        mListOneContent.setVisibility(View.GONE);
        mShimmerListOne.setVisibility(View.VISIBLE);

        query = mListRef.whereEqualTo("nPilihanEditor", true).orderBy("nUploadTime", Query.Direction.DESCENDING).limit(7);

        FirestoreRecyclerOptions<MateriModel> options = new FirestoreRecyclerOptions.Builder<MateriModel>()
                .setQuery(query, MateriModel.class)
                .build();
        mListOneAdapter = new ListOneAdapter(options);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mListOneContent.setVisibility(View.VISIBLE);
                    mShimmerListOne.setVisibility(View.GONE);
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
        mRvListOne.setAdapter(mListOneAdapter);
        mRvListOne.setNestedScrollingEnabled(false);

        mListOneAdapter.setOnItemClickListener(new ListOneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position, String jenis, boolean isPremium) {
//                Intent intent = new Intent(getActivity(), ArticleActivity.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                startActivity(intent);
//                showMessage("clicked");

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

        mListOneAdapter.startListening();
    }

    private void showListTwo() {
        /*
        menampilkan list materi
         */
        mListTwoContent.setVisibility(View.GONE);
        mShimmerListTwo.setVisibility(View.VISIBLE);

        query = mListRef.whereEqualTo("nTipe", "materi").orderBy("nUploadTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MateriModel> options = new FirestoreRecyclerOptions.Builder<MateriModel>()
                .setQuery(query, MateriModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mListTwoContent.setVisibility(View.VISIBLE);
                    mShimmerListTwo.setVisibility(View.GONE);
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mListTwoAdapter = new ListTwoAdapter(options);

        mLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvListTwo.setLayoutManager(mLayoutManager);
        mRvListTwo.setAdapter(mListTwoAdapter);
        mRvListTwo.setNestedScrollingEnabled(false);

        mListTwoAdapter.setOnItemClickListener(new ListTwoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position, String jenis, boolean isPremium) {
//                Intent intent = new Intent(getActivity(), VideoActivity.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                startActivity(intent);
//                showMessage("clicked");
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

        mListTwoAdapter.startListening();

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
        mListOneAdapter.startListening();
        mListTwoAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHomePres.onDestroy();
        mKategoriAdapter.stopListening();
        mAllKategoriAdapter.stopListening();
        mListOneAdapter.stopListening();
        mListTwoAdapter.stopListening();
    }

    @OnClick(R.id.relative_notif_home)
    public void onNotifClick() {
        Intent intent = new Intent(getActivity(), NotifActivity.class);
        startActivity(intent);
//        showMessage("Notif page");
    }

    @OnClick(R.id.cardView_allKateoriMateri_home)
    public void allKategoriClick() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.imageView_closeKategori_home)
    public void allKategoriCloseIcon() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.cardView_allKagoriBG_home)
    public void allKategoriBGClick() {
        //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.cardView_search_home)
    public void searchClick() {
        String value = mSearchField.getText().toString().trim();
        if (!value.isEmpty()) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("SEARCH_VALUE", value);
            intent.putExtra("KATEGORI", "materi");
            // intent.putExtra("TIPE", "materi");

            startActivity(intent);
        } else {
            showMessage("kosong bro");
        }
        //showMessage(value);
    }

    @OnClick(R.id.textView_allListOne_home)
    public void allListOneClick() {
        showMessage("click");
    }

    @OnClick(R.id.textView_allListTwo_home)
    public void allListTwoClick() {
        showMessage("click");
    }

    @Override
    public void setmGreeting(String greeting) {
        mSearchField.setEnabled(true);
        mGreeting.setVisibility(View.VISIBLE);
        mGreetingShimmer.setVisibility(View.INVISIBLE);
        mGreeting.setText(greeting);
    }

    @Override
    public void hideGreeting() {
        mSearchField.setEnabled(false);
        mGreeting.setVisibility(View.INVISIBLE);
        mGreetingShimmer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmNama(String nama) {
        mShimmerNama.setVisibility(View.INVISIBLE);
        mNama.setVisibility(View.VISIBLE);
        mNama.setText(nama);
    }

    @Override
    public void hideNama() {
        mShimmerNama.setVisibility(View.VISIBLE);
        mNama.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setmPhoto(String photo) {
        mShimmerPhoto.setVisibility(View.INVISIBLE);
        mPhoto.setVisibility(View.VISIBLE);
        if (isAdded()) {
            Glide.with(this)
                    .load(photo)
                    .fitCenter()
                    .into(mPhoto);
        }
    }

    @Override
    public void hidePhoto() {
        mPhoto.setVisibility(View.INVISIBLE);
        mShimmerPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmQuote(String quote) {
        mQuoteShimmer.setVisibility(View.INVISIBLE);
        mQuote.setVisibility(View.VISIBLE);
        mQuote.setText(quote);
        mRefresh.setRefreshing(false);
    }

    @Override
    public void setTalker(String talker) {
        mTalker2.setVisibility(View.VISIBLE);
        mTalker0.setVisibility(View.VISIBLE);
        mTalker.setVisibility(View.VISIBLE);
        mTalker.setText(talker);
    }

    @Override
    public void hideTalker() {
        mTalker2.setVisibility(View.INVISIBLE);
        mTalker0.setVisibility(View.INVISIBLE);
        mTalker.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideQuote() {
        mQuoteShimmer.setVisibility(View.VISIBLE);
        mQuote.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}