package com.munifahsan.skdskb.Subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.TestPurchaseActivity;
import com.munifahsan.skdskb.SubscribeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subsciption);

        ButterKnife.bind(this);

        //subscribe();

    }

    @OnClick(R.id.button2)
    public void purchase(){
        startActivity(new Intent(this, TestPurchaseActivity.class));
    }

    @OnClick(R.id.button3)
    public void subscribe(){
        startActivity(new Intent(this, SubscribeActivity.class));
    }
}