package com.munifahsan.skdskb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.munifahsan.skdskb.AddArtikel.AddArtikelFragment;
import com.munifahsan.skdskb.AddEbook.AddEbookFragment;
import com.munifahsan.skdskb.AddKategori.AddKategoriFragment;
import com.munifahsan.skdskb.AddTryout.AddTryoutFragment;
import com.munifahsan.skdskb.AddVideo.AddVideoFragment;
import com.munifahsan.skdskb.AdminPage.AdminPageFragment;
import com.munifahsan.skdskb.BoomarkPage.BookmarkFragment;
import com.munifahsan.skdskb.HomePage.view.HomeFragment;
import com.munifahsan.skdskb.InformasiPage.view.InformasiFragment;
import com.munifahsan.skdskb.PageContent.ContentPageFragment;
import com.munifahsan.skdskb.SettingPage.SettingFragment;
import com.munifahsan.skdskb.TryoutPage.view.TryoutFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdminActivity extends AppCompatActivity {

//    @BindView(R.id.bottomNavigationView)
//    BottomNavigationView mBottomNavigationView;
//    @BindView(R.id.adView)
//    AdView mAdView;

//    private ConsentInformation consentInformation;
//    private ConsentForm consentForm;

    @BindView(R.id.viewpager)
    CustomViewPager mViewPager;
    @BindView(R.id.smarttab)
    SmartTabLayout mSmartTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        ButterKnife.bind(this);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                this.getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("TAMBAH ARTIKEL", AddArtikelFragment.class)
                .add("TAMBAH VIDEO", AddVideoFragment.class)
                .add("TAMBAH EBOOK", AddEbookFragment.class)
                .add("TAMBAH TRYOUT", AddTryoutFragment.class)
                .add("TAMBAH KATEGORI", AddKategoriFragment.class)
                .add("EDIT PAGE", ContentPageFragment.class)
                .create()
        );


        mViewPager.setAdapter(adapter);
        mSmartTabLayout.setViewPager(mViewPager);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
//        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
//                .addTestDeviceHashedId("E46086AE0C9A7854A391939BB1A0887A")
//                .setDebugGeography(ConsentDebugSettings
//                        .DebugGeography
//                        .DEBUG_GEOGRAPHY_NOT_EEA)
//                .addTestDeviceHashedId("TEST-DEVICE-HASHED-ID")
//                .build();
//
//        ConsentRequestParameters params = new ConsentRequestParameters
//                .Builder()
//                .setConsentDebugSettings(debugSettings)
//                .build();
//
//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("E46086AE0C9A7854A391939BB1A0887A")).build();
//        MobileAds.setRequestConfiguration(configuration);
//
//        // Set tag for under age of consent. Here false means users are not under age
//        //params.setTagForUnderAgeOfConsent(false);
//        consentInformation = UserMessagingPlatform.getConsentInformation(this);
//        consentInformation.requestConsentInfoUpdate(
//                this,
//                params,
//                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
//                    @Override
//                    public void onConsentInfoUpdateSuccess() {
//                        // The consent information state was updated.
//                        // You are now ready to check if a form is available.
//
//                        // The consent information state was updated.
//                        // You are now ready to check if a form is available.
//                        if (consentInformation.isConsentFormAvailable()) {
//                            loadForm();
//                        }
//                    }
//                },
//                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
//                    @Override
//                    public void onConsentInfoUpdateFailure(FormError formError) {
//
//                    }
//                });
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                showMessage("ad load");
//            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError adError) {
//                // Code to be executed when an ad request fails.
//                showMessage(adError.getMessage());
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//                showMessage("ad open");
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });
//
//        getFragmentPage(new HomeFragment());
//
//        mBottomNavigationView.setItemIconTintList(null);
//        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void showMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

//    public void loadForm() {
//        UserMessagingPlatform.loadConsentForm(
//                this,
//                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
//                    @Override
//                    public void onConsentFormLoadSuccess(ConsentForm consentForm) {
//                        MainAdminActivity.this.consentForm = consentForm;
//                        if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
//                            consentForm.show(
//                                    MainAdminActivity.this,
//                                    new ConsentForm.OnConsentFormDismissedListener() {
//                                        @Override
//                                        public void onConsentFormDismissed(@Nullable FormError formError) {
//                                            // Handle dismissal by reloading form.
//                                            loadForm();
//                                        }
//                                    });
//
//                        }
//
//                    }
//                },
//                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
//                    @Override
//                    public void onConsentFormLoadFailure(FormError formError) {
//                        /// Handle Error.
//                    }
//                }
//        );
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Fragment fragment = null;
//
//        switch (item.getItemId()) {
//            case R.id.itemHome:
//                fragment = new HomeFragment();
//                break;
//            case R.id.itemInformasi:
//                fragment = new InformasiFragment();
//                break;
//            case R.id.itemTryout:
//                fragment = new TryoutFragment();
//                break;
//            case R.id.itemBookmark:
//                fragment = new BookmarkFragment();
//                break;
//            case R.id.itemSetting:
//                fragment = new AdminPageFragment();
//        }
//
//        return loadFragment(fragment);
//    }
//
//    private void getFragmentPage(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fl_container, fragment)
//                    .commit();
//        }
//    }
//
//    // method untuk load fragment yang sesuai
//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fl_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }
}