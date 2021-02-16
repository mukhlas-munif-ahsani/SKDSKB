package com.munifahsan.skdskb.Ebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munifahsan.skdskb.Articel.ArticleContract;
import com.munifahsan.skdskb.BillingClientSetup;
import com.munifahsan.skdskb.BuildConfig;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.SignIn.SignInActivity;
import com.munifahsan.skdskb.SubscribeActivity;
import com.munifahsan.skdskb.Tryout.TryoutActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class EbookActivity extends AppCompatActivity implements EbookContract.View, PurchasesUpdatedListener {

    @BindView(R.id.rel_iklan_space)
    RelativeLayout mIklanSpace;
    @BindView(R.id.lin_fake_iklan)
    LinearLayout mFakeIklan;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    boolean isPremium = false;

    //    @BindView(R.id.webView_link)
//    WebView mWebView;
    @BindView(R.id.imageView_bookmark_ebook)
    ImageView mBookmark;
    @BindView(R.id.textView_jenis_ebook)
    TextView mJenis;
    @BindView(R.id.textView_time_ebook)
    TextView mTime;
    @BindView(R.id.shimmer_content_ebook)
    ShimmerFrameLayout mContentShimmer;
    @BindView(R.id.shimmer_title_ebook)
    ShimmerFrameLayout mJenisShimmer;

    @BindView(R.id.rel_bookmark_ebook)
    RelativeLayout mRelBookmark;
    @BindView(R.id.relative_seen)
    RelativeLayout mRelSeen;
    @BindView(R.id.textView_number)
    TextView mSeenNumber;
    @BindView(R.id.relative_favo)
    RelativeLayout mRelFavo;
    @BindView(R.id.imageView_favo)
    ImageView mImageFavo;
    @BindView(R.id.textView_number_favo)
    TextView mFavoNumber;
    @BindView(R.id.relative_font)
    RelativeLayout mRelFont;
    @BindView(R.id.relative_share)
    RelativeLayout mRelShare;
    @BindView(R.id.relative_textSizeSet)
    RelativeLayout mTextSizeSet;
    @BindView(R.id.textView_textSize)
    TextView mTextSize;

    @BindView(R.id.adView)
    AdView mAdView;

    int fontSize = 16;
    boolean isFavo = false;

    @BindView(R.id.pdfView)
    PDFView pdfView;

    EbookContract.Presenter mPres;


    FirebaseUser mCurrentUser;

    String document_id;

    boolean isMarked = false;
    private String title;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        ButterKnife.bind(this);
        mPres = new EbookPresenter(this);

        setupSubsBillingClient();
        setupBillingClient();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mFakeIklan.setVisibility(View.GONE);

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                showMessage(adError.getMessage());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                showMessage("ad open");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        Intent intent = getIntent();
        document_id = intent.getStringExtra("DOCUMENT_ID");

        if (mCurrentUser != null) {
            mPres.getBookmark(document_id, mCurrentUser.getUid());
            mPres.getFavo(document_id, mCurrentUser.getUid());
        }
        mPres.getData(document_id);

        mRelBookmark.setEnabled(false);
        mRelShare.setEnabled(false);
        mRelFavo.setEnabled(false);

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
                    //Toast.makeText(getActivity(), "Success to connect billing", Toast.LENGTH_SHORT).show();
                    //Query
                    List<Purchase> purchases = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                            .getPurchasesList();

                    if (purchases != null) {
                        for (Purchase purchase : purchases) {
                            handleItemAlreadyPurchase(purchase);
                        }
                    }


                } else {
                    Toast.makeText(EbookActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(EbookActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBillingClient() {

        billingClient = BillingClientSetup.getInstance(this, this);
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
                    Toast.makeText(EbookActivity.this, "Error code : " + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(EbookActivity.this, "You ara disconnected from Billing Service", Toast.LENGTH_SHORT).show();
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
                mIklanSpace.setVisibility(View.GONE);
                //showMessage("You are premium");
            }
        }
    }

    @OnClick(R.id.btn_upgrade)
    public void upgrade() {
        Intent intent = new Intent(this, SubscribeActivity.class);
        startActivity(intent);
    }

    @Override
    public void setWebView(String link) {
        // mWebView.setVisibility(View.VISIBLE);

        String url = "https://drive.google.com/file/d/1g-XOjPXcFcoaIIx8uvRMXYnlL3xIjQ_r";

        String kiling = "https://www.adobe.com/content/dam/acom/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
        new RetrivePdfStream().execute(link);
    }

    private class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    mContentShimmer.setVisibility(View.INVISIBLE);

                    if (mCurrentUser != null) {
                        mPres.addSeen(document_id, mCurrentUser.getUid());
                    }

                    mRelBookmark.setEnabled(true);
                    mRelShare.setEnabled(true);
                    mRelFavo.setEnabled(true);
                }
            }).load();

        }
    }

    @Override
    public void hideContent() {
        mContentShimmer.setVisibility(View.VISIBLE);
//        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setJenis(String jenis) {
        mJenis.setText(jenis);
        mJenis.setVisibility(View.VISIBLE);
        mJenisShimmer.setVisibility(View.GONE);
    }

    @Override
    public void hideJenis() {
        mJenisShimmer.setVisibility(View.VISIBLE);
        mJenis.setVisibility(View.INVISIBLE);
        mTime.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTime(String time) {
        mTime.setText(time);
        mTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBookmark(List<String> uids) {
        if (uids.contains(mCurrentUser.getUid())) {
            mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark));
            isMarked = true;
            showMessage("Artikel tersimpan");
        } else {
            mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark_border));
            isMarked = false;
            //showMessage("Artikel ");
        }
    }

    @Override
    public void setSeen(List<String> uids) {
        mSeenNumber.setText(String.valueOf(uids.size()));
    }

    @Override
    public void setFavo(List<String> uids) {
        if (uids.contains(mCurrentUser.getUid())) {
            mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite));
            isFavo = true;
            //showMessage("Artikel tersimpan");
        } else {
            mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border));
            isFavo = false;
            //showMessage("Artikel ");
        }
        mFavoNumber.setText(String.valueOf(uids.size()));
    }

    @OnClick(R.id.rel_back_ebook)
    public void backOnClick() {
        finish();
    }

    @OnClick(R.id.rel_bookmark_ebook)
    public void bookmarkOnClick() {
        if (mCurrentUser != null) {
            if (isMarked) {
                mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark_border));
                //isMarked = true;
            } else {
                mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark));
                //isMarked = false;
            }

            mPres.addBookmark(document_id, mCurrentUser.getUid());
//            showMessage("not border");
        } else {
            showDialogOnSignIn();
        }
    }

    @OnClick(R.id.relative_favo)
    public void favoOnClick() {
        if (mCurrentUser != null) {
            if (isFavo) {
                mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border));
                isMarked = true;
            } else {
                mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite));
                isMarked = false;
            }

            mPres.addFavo(document_id, mCurrentUser.getUid());
//            showMessage("not border");
        } else {
            showDialogOnSignIn();
        }
    }

    @OnClick(R.id.relative_font)
    public void fontOnClick() {
        if (mTextSizeSet.getVisibility() == View.GONE) {
            mTextSizeSet.setVisibility(View.VISIBLE);
        } else {
            mTextSizeSet.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.relative_textSizeSet2)
    public void relFont() {
        mTextSizeSet.setVisibility(View.GONE);
    }

    @OnClick(R.id.imageView_fontSize_close)
    public void fontSizeCloseClick() {
        mTextSizeSet.setVisibility(View.GONE);
    }

    @OnClick(R.id.lin_default_textSize)
    public void fontSizeDefault() {
        mTextSize.setText(String.valueOf(16));
        fontSize = 16;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @OnClick(R.id.relative_share)
    public void shareOnClick() {
        if (!title.equals("")) {
            String shareMessage = "\n" + title + "\n\nBaca Menggunakan aplikasi SKD SKB Indonesia\n";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    @OnClick(R.id.imageView_min)
    public void minTextSize() {
        if (fontSize > 12) {
            fontSize = fontSize - 2;
        }
        mTextSize.setText(String.valueOf(fontSize));
    }

    @OnClick(R.id.imageView_plus)
    public void plusTextSize() {
        if (fontSize < 28) {
            fontSize = fontSize + 2;
        }
        mTextSize.setText(String.valueOf(fontSize));
    }


    public void showDialogOnSignIn() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        alertDialogBuilder.setTitle("Sign In Kedalam Aplikasi");
        alertDialogBuilder.setMessage("Untuk menyimpan anda harus melakukan Sign In");

        // set pesan dari dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        navigateToSignIn();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void navigateToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
        super.onBackPressed();
//        }
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


//    @Override
//    public void onSuccess(String url, String destinationPath) {
//        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
//        remotePDFViewPager.setAdapter(adapter);
//        //updateLayout();
//        //showDownloadButton();
//    }
//
//    @Override
//    public void onFailure(Exception e) {
//
//    }
//
//    @Override
//    public void onProgressUpdate(int progress, int total) {
//
//    }
}