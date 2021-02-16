package com.munifahsan.skdskb.SettingPage;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.skdskb.BillingClientSetup;
import com.munifahsan.skdskb.DarkMode;
import com.munifahsan.skdskb.MainActivity;
import com.munifahsan.skdskb.MainAdminActivity;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.SignIn.SignInActivity;
import com.munifahsan.skdskb.SplashScreenActivity;
import com.munifahsan.skdskb.SubscribeActivity;
import com.munifahsan.skdskb.Subscription.SubscriptionActivity;
import com.munifahsan.skdskb.SyaratKetentuanActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

public class SettingFragment extends Fragment implements PurchasesUpdatedListener {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    @BindView(R.id.textView_name_setting)
    TextView mNama;
    @BindView(R.id.shimmer_nama_setting)
    ShimmerFrameLayout mShimmerNama;
    @BindView(R.id.textView_email_setting)
    TextView mEmail;
    @BindView(R.id.circleImage_photo_setting)
    CircleImageView mPhoto;
    @BindView(R.id.shimmer_photo_setting)
    ShimmerFrameLayout mShimmerPhoto;
    @BindView(R.id.imageView_signInCheck_setting)
    ImageView mSignInCheck;
    @BindView(R.id.cardView_signIn_setting)
    CardView mSignInBtn;
    @BindView(R.id.logout)
    CardView mLogout;

    @BindView(R.id.imageView_upgrade_setting)
    ImageView mUpgradeCheck;

    @BindView(R.id.cardView_darkMode)
    CardView mCardDarkMode;
    @BindView(R.id.textView_darkMode)
    TextView mTextDarkMode;
    @BindView(R.id.darkMode)
    SwitchMaterial mDarkMode;

    @BindView(R.id.admin)
    CardView mAdminButton;

    @BindView(R.id.rel_about)
    RelativeLayout mRelAbout;

    BillingClient billingClient;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;
    boolean isPremium = false;

    String mCurrentId;
    private FirebaseFirestore firebaseFirestore;
    boolean isConnected;
    NetworkInfo activeNetwork;
    ConnectivityManager cm;
    Handler handler = new Handler();

    DarkMode darkMode;
//    SharedPreferences sharedPreferences;
//    boolean isDarkModeOn;
//    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        if (!DarkMode.isNightModeEnabled(getActivity())) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
////            mDarkMode.setChecked(true);
////            mTextDarkMode.setText("Gelap");
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            //mDarkMode.setChecked(false);
////            mTextDarkMode.setText("Terang");
//        }

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        ButterKnife.bind(this, view);

        darkMode = new DarkMode();

        setupSubsBillingClient();
        setupBillingClient();

        firebaseFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        /*
        set to dark mode
         */

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            mDarkMode.setChecked(true);
            mTextDarkMode.setText("Gelap");
        } else {
            mDarkMode.setChecked(false);
            mTextDarkMode.setText("Terang");
        }

        mDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // change text of Button
                    //DarkMode.getInstance().setIsNightModeEnabled(true);
                    mTextDarkMode.setText("Gelap");
                } else {
                    // change text of Button
                  //  DarkMode.getInstance().setIsNightModeEnabled(false);
                    mTextDarkMode.setText("Terang");
                }
            }
        });

//        sharedPreferences = this.getActivity().getSharedPreferences(
//                "sharedPrefs", MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//
//        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);



        mDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DarkMode.isNightModeEnabled(getActivity())) {

                    // if dark mode is on it
                    // will turn it off
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    // it will set isDarkModeOn
//                    // boolean to false
//                    editor.putBoolean("isDarkModeOn", false);
//                    editor.apply();

                    DarkMode.setIsNightModeEnabled(getActivity(), false);

                    // change text of Button
                    mDarkMode.setChecked(false);
                    mTextDarkMode.setText("Terang");


                    Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                    startActivity(intent);

                }
                else {

                    // if dark mode is off
                    // it will turn it on
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//
//                    // it will set isDarkModeOn
//                    // boolean to true
//                    editor.putBoolean("isDarkModeOn", true);
//                    editor.apply();

                    DarkMode.setIsNightModeEnabled(getContext(), true);

                    // change text of Button
                    mDarkMode.setChecked(true);
                    mTextDarkMode.setText("Gelap");


                    Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                    startActivity(intent);


                }
                getActivity().finish();
            }
        });

//        hideNama();
//        hidePhoto();

        if (mCurrentUser != null) {

            firebaseFirestore.collection("USERS").document(mCurrentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        setmNama(mAuth.getCurrentUser().getProviderData().get(1).getDisplayName());
                        setmPhoto(mAuth.getCurrentUser().getProviderData().get(1).getPhotoUrl().toString());
                        mEmail.setText(mAuth.getCurrentUser().getEmail());

                        //signin
                        mSignInBtn.setAlpha((float) 0.8);
                        mSignInBtn.setElevation(0);
                        mSignInBtn.setClickable(false);
                        mSignInCheck.setVisibility(View.VISIBLE);

                        //logout
                        mLogout.setClickable(true);
                        mLogout.setAlpha((float) 1.0);
                    } else {
                        setmNama("Nama");
                        setEmail("email@gmail.com");
                        setmPhoto("https://www.searchpng.com/wp-content/uploads/2019/02/Deafult-Profile-Pitcher.png");
                        // mHomePres.getUserData(null);

                        //signin
                        mSignInBtn.setAlpha((float) 1.0);
                        mSignInBtn.setClickable(true);
                        mSignInBtn.setElevation(5);
                        mSignInCheck.setVisibility(View.INVISIBLE);

                        //logout
                        mLogout.setClickable(false);
                        mLogout.setAlpha((float) 0.8);
                    }
                }
            });

            //signin
            mSignInBtn.setAlpha((float) 0.8);
            mSignInBtn.setElevation(0);
            mSignInBtn.setClickable(false);
            mSignInCheck.setVisibility(View.VISIBLE);

            //logout
            mLogout.setClickable(true);
            mLogout.setAlpha((float) 1.0);
        } else {

            setmNama("Nama");
            setEmail("email@gmail.com");
            setmPhoto("https://www.searchpng.com/wp-content/uploads/2019/02/Deafult-Profile-Pitcher.png");
            //mHomePres.getUserData(null);
            //showMessage("kosong");

            //signin
            mSignInBtn.setAlpha((float) 1.0);
            mSignInBtn.setClickable(true);
            mSignInBtn.setElevation(5);
            mSignInCheck.setVisibility(View.INVISIBLE);

            //logout
            mLogout.setClickable(false);
            mLogout.setAlpha((float) 0.8);
        }

        if (mCurrentUser != null) {

            mCurrentId = mCurrentUser.getUid();

            firebaseFirestore.collection("USERS").document(mCurrentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {
                            String level = task.getResult().getString("nLevel");

                            if (level.equals("ADMIN")) {
                                mAdminButton.setVisibility(View.VISIBLE);
                            } else {
                                mAdminButton.setVisibility(View.INVISIBLE);
                            }
                        }

                    }
                }
            });

        } else {

        }

        if (isPremium){
            mUpgradeCheck.setVisibility(View.VISIBLE);
        }

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
                mUpgradeCheck.setVisibility(View.VISIBLE);
                //showMessage("You are premium");
            }
        }
    }


    private void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.cardView_signIn_setting)
    public void signInClick() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cardView_upgrade)
    public void upgradeClick() {
        Intent intent = new Intent(getActivity(), SubscribeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logout)
    public void logout() {
        showDialogOnLogOutBtnOnClick();
    }

    @OnClick(R.id.admin)
    public void adminOnClick() {
        Intent intent = new Intent(getActivity(), MainAdminActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.cardView_darkMode)
    public void cardDarkModeClick() {
        DarkMode.setIsToogleEnabled(getActivity(), true);
        if (DarkMode.isNightModeEnabled(getActivity())) {

            DarkMode.setIsNightModeEnabled(getActivity(), false);

            // if dark mode is on it
            // will turn it off
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            // it will set isDarkModeOn
//            // boolean to false
//            editor.putBoolean("isDarkModeOn", false);
//            editor.apply();

            // change text of Button
            mDarkMode.setChecked(false);
            mTextDarkMode.setText("Terang");


        }
        else {

//            // if dark mode is off
//            // it will turn it on
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//
//            // it will set isDarkModeOn
//            // boolean to true
//            editor.putBoolean("isDarkModeOn", true);
//            editor.apply();

            DarkMode.setIsNightModeEnabled(getActivity(), true);

            // change text of Button
            mDarkMode.setChecked(true);
            mTextDarkMode.setText("Gelap");

        }

        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    @OnClick(R.id.hubungiKami)
    public void hubungiKami(){

        clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        String txtcopy = "kelasnesia@gmail.com";
        clipData = ClipData.newPlainText("text",txtcopy);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(getActivity(), "kelasnesia@gmail.com\n disalin ke clipboard", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.syaratKetentuan)
    public void syaratKeten(){
        Intent intent = new Intent(getActivity(), SyaratKetentuanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cardView_rate)
    public void rateClick(){
        try{
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
    }

    private String getPackageName() {
        return "com.munifahsan.skdskb";
    }

    @OnClick(R.id.cardView_about)
    public void aboutClick(){
        mRelAbout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.aboutBg)
    public void aboutBgClick(){
        mRelAbout.setVisibility(View.GONE);
    }

    @OnClick(R.id.imageView_aboutClose)
    public void aboutClose(){
        mRelAbout.setVisibility(View.GONE);
    }

    @OnClick(R.id.facebook)
    public void openFacebook(){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(getActivity());
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }

    public static String FACEBOOK_URL = "https://web.facebook.com/kelasnesia/?_rdc=1&_rdr";
    public static String FACEBOOK_PAGE_ID = "YourPageName";
    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @OnClick(R.id.instagram)
    public void openInstagram(){

        startActivity(newInstagramProfileIntent(getActivity().getPackageManager(), "https://www.instagram.com/kelasnesia.id"));

    }

    public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    @OnClick(R.id.twitter)
    public void openTwitter(){
        String urlTw="https://twitter.com/kelasnesia";

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(urlTw));
            intent.setPackage("com.twitter.android");
            startActivity(intent);
        }
        catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(urlTw)));
        }
    }

    public void showDialogOnLogOutBtnOnClick() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title dialog
        alertDialogBuilder.setTitle("Apakah anda yakin ingin Logout dari aplikasi ?");

        // set pesan dari dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        mAuth.signOut();
                        //navigateToSplash();
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

    private void navigateToSplash() {
        Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
        startActivity(intent);
    }

    public void setmNama(String nama) {
        mNama.setText(nama);
        mNama.setVisibility(View.VISIBLE);
        mEmail.setVisibility(View.VISIBLE);
        mShimmerNama.setVisibility(View.INVISIBLE);
    }

    public void setEmail(String email) {
        mEmail.setText(email);
    }

    public void hideNama() {
        mNama.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);
        mShimmerNama.setVisibility(View.VISIBLE);
    }

    public void setmPhoto(String imageUrl) {
        if (isAdded()) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(mPhoto);
        }
        mPhoto.setVisibility(View.VISIBLE);
        mShimmerPhoto.setVisibility(View.INVISIBLE);
    }

    public void hidePhoto() {
        mPhoto.setVisibility(View.INVISIBLE);
        mShimmerPhoto.setVisibility(View.VISIBLE);
    }
}