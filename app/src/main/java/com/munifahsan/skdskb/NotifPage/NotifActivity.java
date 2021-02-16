package com.munifahsan.skdskb.NotifPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.skdskb.Adapters.ListOneAdapter;
import com.munifahsan.skdskb.Adapters.NotifAdapter;
import com.munifahsan.skdskb.Articel.ArticleActivity;
import com.munifahsan.skdskb.BillingClientSetup;
import com.munifahsan.skdskb.DetailKategori.DetailKategoriActivity;
import com.munifahsan.skdskb.Ebook.EbookActivity;
import com.munifahsan.skdskb.HomePage.model.MateriModel;
import com.munifahsan.skdskb.HomePage.view.HomeFragment;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.Tryout.TryoutActivity;
import com.munifahsan.skdskb.Video.VideoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    @BindView(R.id.recyclerView_noitf)
    RecyclerView mRvNotif;
    @BindView(R.id.shimmer_notif)
    ShimmerFrameLayout mShimmer;

    NotifAdapter mNotifAdapter;
    Query query;
    private LinearLayoutManager mLayoutManager;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mListRef = firebaseFirestore.collection("POST");

    FirebaseUser mCurrentUser;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    boolean isPremium = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        ButterKnife.bind(this);

        setupBillingClient();
        setupSubsBillingClient();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

           /*
        Change status bar color
         */
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

         // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.bgPageHeader));

        showLisNotif();
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

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Toast.makeText(DetailKategoriActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                            .getPurchasesList();
                    if (purchases.size() > 0) {
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    }
                } else {
                    Toast.makeText(NotifActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(NotifActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBillingClient() {

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //Toast.makeText(DetailKategoriActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList();
                    if (purchases.size() > 0) {
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    }
                } else {
                    Toast.makeText(NotifActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(NotifActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
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


    @OnClick(R.id.rel_back)
    public void backOnClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeFragment.getInstance().notifCount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNotifAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNotifAdapter.stopListening();
    }

    private void showLisNotif() {
        /*
        menampilkan list materi pilihan editor
         */
        mShimmer.setVisibility(View.GONE);
        mRvNotif.setVisibility(View.VISIBLE);

        query = mListRef.orderBy("nUploadTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MateriModel> options = new FirestoreRecyclerOptions.Builder<MateriModel>()
                .setQuery(query, MateriModel.class)
                .build();
        mNotifAdapter = new NotifAdapter(options);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
                    mRvNotif.setVisibility(View.VISIBLE);
                    mShimmer.setVisibility(View.GONE);
//                    mShimmer.setVisibility(View.INVISIBLE);
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvNotif.setLayoutManager(mLayoutManager);
        mRvNotif.setAdapter(mNotifAdapter);
        mRvNotif.setNestedScrollingEnabled(false);

        mNotifAdapter.setOnItemClickListener(new NotifAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position, String jenis, boolean isPremium) {
//                Intent intent = new Intent(getActivity(), ArticleActivity.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                startActivity(intent);
//                showMessage("clicked");

                mListRef.document(id)
                        .update("nSeen", FieldValue.arrayUnion(mCurrentUser.getUid()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e("seen", "succes");
                                //getSeen(postId, id);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("seen error", e.getMessage());
                    }
                });

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
    }

    private void sendToArtikel(String id, boolean isPremiumContent) {
        if (isPremiumContent){
            if (isPremium){
                Intent intent = new Intent(this, ArticleActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    private void sendToVideo(String id, boolean isPremiumContent) {
        if (isPremiumContent){
            if (isPremium){
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    private void sendToEbook(String id, boolean isPremiumContent) {
        if (isPremiumContent){
            if (isPremium){
                Intent intent = new Intent(this, EbookActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(this, EbookActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    private void sendToTryout(String id, boolean isPremiumContent) {
        if (isPremiumContent){
            if (isPremium){
                Intent intent = new Intent(this, TryoutActivity.class);
                intent.putExtra("DOCUMENT_ID", id);
                startActivity(intent);
            } else {
                showMessage("Upgrade untuk mengakses konten premium");
            }
        } else {
            Intent intent = new Intent(this, TryoutActivity.class);
            intent.putExtra("DOCUMENT_ID", id);
            startActivity(intent);
        }

    }

    public void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}