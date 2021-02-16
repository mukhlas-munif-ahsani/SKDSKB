package com.munifahsan.skdskb;

import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.PurchasesUpdatedListener;

public class BillingClientSetup {
    private static BillingClient instance;

    public static BillingClient getInstance(Context context, PurchasesUpdatedListener listener){
        BillingClient billingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(listener)
                .build();
        return billingClient;
    }
}
