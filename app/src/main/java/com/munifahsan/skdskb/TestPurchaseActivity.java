package com.munifahsan.skdskb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestPurchaseActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    ConsumeResponseListener listener;

    @BindView(R.id.txt_purchase)
    TextView mTxtPurchase;
    @BindView(R.id.rv_purchase)
    RecyclerView mRvPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_purchase);

        ButterKnife.bind(this);

        setupBillingClient();

        mRvPurchase.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvPurchase.setLayoutManager(layoutManager);
        mRvPurchase.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

    }

    private void setupBillingClient() {
        listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(TestPurchaseActivity.this, "Consume OK!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        billingClient = BillingClientSetup.getInstance(this, this);
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(TestPurchaseActivity.this, "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                            .getPurchasesList();
                    handleItemAlreadyPurchase(purchases);
                } else {
                    Toast.makeText(TestPurchaseActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(TestPurchaseActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleItemAlreadyPurchase(List<Purchase> purchases) {
        StringBuilder purchaseItem = new StringBuilder(mTxtPurchase.getText()); //Empty
        for (Purchase purchase : purchases) {
            if (purchase.getSku().equals("forever_buy"))//Consume item
            {
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.consumeAsync(consumeParams, listener);
            }
            purchaseItem.append("\n" + purchase.getSku())
                    .append("\n");
        }
        mTxtPurchase.setText(purchaseItem.toString());
        mTxtPurchase.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_load_purchase)
    public void load() {
        if (billingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(Arrays.asList("consumable_item", "forever_buy"))
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        loadProductToRecyclerView(list);
                    } else {
                        Toast.makeText(TestPurchaseActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loadProductToRecyclerView(List<SkuDetails> list) {
        MyProductAdapter adapter = new MyProductAdapter(this, list, billingClient);
        mRvPurchase.setAdapter(adapter);

        adapter.setProductClickListener(new ProductClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Launch billing flow
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(list.get(position))
                        .build();
                int response = billingClient.launchBillingFlow(TestPurchaseActivity.this, billingFlowParams)
                        .getResponseCode();
                switch (response) {
                    case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                        Toast.makeText(TestPurchaseActivity.this, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        Toast.makeText(TestPurchaseActivity.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                        Toast.makeText(TestPurchaseActivity.this, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        Toast.makeText(TestPurchaseActivity.this, "ITEM_ALREADY_OWNED", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                        Toast.makeText(TestPurchaseActivity.this, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                        Toast.makeText(TestPurchaseActivity.this, "SERVICE_TIMEOUT", Toast.LENGTH_SHORT).show();
                        break;
                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        Toast.makeText(TestPurchaseActivity.this, "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            handleItemAlreadyPurchase(list);
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "User has been cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
        }
    }
}