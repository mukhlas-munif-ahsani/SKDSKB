package com.munifahsan.skdskb.SettingPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

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
import com.munifahsan.skdskb.DarkMode;
import com.munifahsan.skdskb.MainAdminActivity;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.SignIn.SignInActivity;
import com.munifahsan.skdskb.SplashScreenActivity;
import com.munifahsan.skdskb.Subscription.SubscriptionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

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

    @BindView(R.id.cardView_darkMode)
    CardView mCardDarkMode;
    @BindView(R.id.textView_darkMode)
    TextView mTextDarkMode;
    @BindView(R.id.darkMode)
    SwitchMaterial mDarkMode;

    @BindView(R.id.admin)
    CardView mAdminButton;

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

        return view;
    }

    private void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.cardView_signIn_setting)
    public void signInClick() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        showMessage("clikc");
    }

    @OnClick(R.id.cardView_upgrade)
    public void upgradeClick() {
        showMessage("click");
        Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
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