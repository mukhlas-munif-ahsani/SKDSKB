package com.munifahsan.skdskb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.skdskb.Adapters.ImageSliderAdapter;
import com.munifahsan.skdskb.Models.ImageSliderModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscribeActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    ConsumeResponseListener listener;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    @BindView(R.id.premiumTxt)
    TextView mTxtPurchase;
    @BindView(R.id.rv_purchase)
    RecyclerView mRvPurchase;
    @BindView(R.id.rv_purchase2)
    RecyclerView mRvPurchase2;

    @BindView(R.id.imageSlider)
    SliderView mImageSlider;
    @BindView(R.id.subs_sroll)
    HorizontalScrollView mSubsContent;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mSliderRef = firebaseFirestore.collection("UPGRADE_PAGE_SLIDER");

    ImageSliderAdapter mImageAdapter;

    boolean subsPurchased = false, itemPurchased = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_subscribe);

        ButterKnife.bind(this);

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

        mImageAdapter = new ImageSliderAdapter();

        mSliderRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ImageSliderModel> mPromoList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ImageSliderModel model = documentSnapshot.toObject(ImageSliderModel.class);
                        mImageAdapter.addCardItem(model);
                    }

                    mImageSlider.setSliderAdapter(mImageAdapter);
                } else {
                    Log.d("GET_DATA_ERROR", task.getException().toString());
                }
            }
        });

        mImageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        mImageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mImageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        mImageSlider.setIndicatorSelectedColor(Color.parseColor("#0E9BF5"));
        mImageSlider.setIndicatorUnselectedColor(Color.parseColor("#F5F5F5"));
        mImageSlider.setScrollTimeInSec(3);
        mImageSlider.setAutoCycle(true);
        mImageSlider.startAutoCycle();

        setupSubsBillingClient();
        setupBillingClient();

        mRvPurchase.setHasFixedSize(true);
        mRvPurchase2.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvPurchase.setLayoutManager(layoutManager);
        mRvPurchase2.setLayoutManager(layoutManager2);
        //mRvPurchase.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        //loadAllSubscribePackage();
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
                    Toast.makeText(SubscribeActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                            .getPurchasesList();
                    if ((purchases).size() > 0) {
                        mSubsContent.setVisibility(View.INVISIBLE);
                        for (Purchase purchase : purchases) {
                            handleSubsAlreadyPurchase(purchase);
                        }
                    } else {
//                        mTxtPurchase.setVisibility(View.INVISIBLE);
//                        mSubsContent.setVisibility(View.VISIBLE);
                        loadAllSubscribePackage();
                    }
                } else {
                    Toast.makeText(SubscribeActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(SubscribeActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBillingClient() {
        listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(SubscribeActivity.this, "Consume OK!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(SubscribeActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList() ;
//                    handleItemAlreadyPurchase(purchases);
//                    loadAllItemPackage();

                    if (purchases.size() > 0) {
                        //mRvPurchase2.setVisibility(View.INVISIBLE);
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    } else {
//                        mTxtPurchase.setVisibility(View.INVISIBLE);
//                        mSubsContent.setVisibility(View.VISIBLE);
                        loadAllItemPackage();
                    }

                } else {
                    Toast.makeText(SubscribeActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(SubscribeActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllSubscribePackage() {

        showRv();

        if (billingClient.isReady()) {

            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList("1_bulan", "1_tahun"))
                    .setType(BillingClient.SkuType.SUBS)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        MyProductAdapter adapter = new MyProductAdapter(SubscribeActivity.this, list, billingClient);
                        mRvPurchase.setAdapter(adapter);

                        adapter.setProductClickListener(new ProductClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                //Launch billing flow
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(position))
                                        .build();
                                int response = billingClient.launchBillingFlow(SubscribeActivity.this, billingFlowParams)
                                        .getResponseCode();
                                switch (response) {
                                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                                        Toast.makeText(SubscribeActivity.this, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                                        Toast.makeText(SubscribeActivity.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                                        Toast.makeText(SubscribeActivity.this, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                                        Toast.makeText(SubscribeActivity.this, "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                                        Toast.makeText(SubscribeActivity.this, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                                        Toast.makeText(SubscribeActivity.this, "SERVICE_TIMEOUT", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                                        Toast.makeText(SubscribeActivity.this, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        //mRvPurchase.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SubscribeActivity.this, "Error : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Billing Client not ready!", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadAllItemPackage() {

        showRv();

        if (billingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList("forever_buy"))
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        MyProductAdapter adapter = new MyProductAdapter(SubscribeActivity.this, list, billingClient);
                        mRvPurchase2.setAdapter(adapter);
                        //mSubsContent.setVisibility(View.VISIBLE);

                        adapter.setProductClickListener(new ProductClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                //Launch billing flow
                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(list.get(position))
                                        .build();
                                int response = billingClient.launchBillingFlow(SubscribeActivity.this, billingFlowParams)
                                        .getResponseCode();
                                switch (response) {
                                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                                        Toast.makeText(SubscribeActivity.this, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                                        Toast.makeText(SubscribeActivity.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                                        Toast.makeText(SubscribeActivity.this, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                                        Toast.makeText(SubscribeActivity.this, "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                                        Toast.makeText(SubscribeActivity.this, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                                        Toast.makeText(SubscribeActivity.this, "SERVICE_TIMEOUT", Toast.LENGTH_SHORT).show();
                                        break;
                                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                                        Toast.makeText(SubscribeActivity.this, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SubscribeActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleSubsAlreadyPurchase(Purchase purchases) {
        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchases.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }

            subsPurchased = true;
            showRv();
//            mSubsContent.setVisibility(View.INVISIBLE);
//            mTxtPurchase.setVisibility(View.VISIBLE);
        } else {

            //subsPurchased = false;

//            mSubsContent.setVisibility(View.VISIBLE);
//            mTxtPurchase.setVisibility(View.INVISIBLE);

            showRv();
            //mTxtPurchase.setText("You are premium");
            Toast.makeText(this, "Premium", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleItemAlreadyPurchase(Purchase purchases) {

        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchases.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }

            itemPurchased = true;
            showRv();
//            mSubsContent.setVisibility(View.INVISIBLE);
//            mTxtPurchase.setVisibility(View.VISIBLE);
        } else {

            //itemPurchased = false;

//            mSubsContent.setVisibility(View.VISIBLE);
//            mTxtPurchase.setVisibility(View.INVISIBLE);

            showRv();
            //mTxtPurchase.setText("You are premium");
            Toast.makeText(this, "Premium", Toast.LENGTH_SHORT).show();
        }

//        StringBuilder purchaseItem = new StringBuilder(mTxtPurchase.getText()); //Empty
//        // for (Purchase purchase : purchases) {
//        if (purchases.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
//            if (!purchases.isAcknowledged()) {
//                if (purchases.getSku().equals("consumable_item"))//Consume item
//                {
//                    ConsumeParams consumeParams = ConsumeParams.newBuilder()
//                            .setPurchaseToken(purchases.getPurchaseToken())
//                            .build();
//                    billingClient.consumeAsync(consumeParams, listener);
//                }
//            } else {
//                mSubsContent.setVisibility(View.INVISIBLE);
//                mTxtPurchase.setVisibility(View.VISIBLE);
//            }
//        }


//            purchaseItem.append("\n" + purchase.getSku())
//                    .append("\n");
//        }

    }

    public void showRv(){

        if (itemPurchased && !subsPurchased){
            mSubsContent.setVisibility(View.INVISIBLE);
            mTxtPurchase.setVisibility(View.VISIBLE);
        }

        if (!itemPurchased && subsPurchased){
            mSubsContent.setVisibility(View.INVISIBLE);
            mTxtPurchase.setVisibility(View.VISIBLE);
        }

        if (!itemPurchased && !subsPurchased){
            Toast.makeText(this, ""+itemPurchased+" "+subsPurchased, Toast.LENGTH_SHORT).show();
            mSubsContent.setVisibility(View.VISIBLE);
            mTxtPurchase.setVisibility(View.INVISIBLE);
        }

    }

//    private void loadAllSubscribePackage(List<SkuDetails> list) {
//        MyProductAdapter adapter = new MyProductAdapter(this, list, billingClient);
//        mRvPurchase.setAdapter(adapter);
//    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

            for (Purchase purchase : list) {
                handleItemAlreadyPurchase(purchase);
                handleSubsAlreadyPurchase(purchase);
            }

            Intent intent = new Intent(this, SplashScreenActivity.class);
            startActivity(intent);
            finish();

        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "User has been cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_cancel_purchase)
    public void cancelSubscribe() {
        finish();
    }

    @OnClick(R.id.close_subs_page)
    public void closeSubscribe() {
        finish();
    }
}
