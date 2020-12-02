package com.munifahsan.skdskb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser mCurrentUser;
    String mCurrentId;
    private FirebaseFirestore firebaseFirestore;
    boolean isConnected;
    NetworkInfo activeNetwork;
    ConnectivityManager cm;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        /*
        Change status bar color
         */
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    protected void onStart() {
        super.onStart();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToMain();
            }
        }, 2000);
        // if (isConnected){


//        if (mCurrentUser != null) {
//
//            mCurrentId = mCurrentUser.getUid();
//
//            //showMessage(mCurrentId);
//
//            firebaseFirestore.collection("USERS").document(mCurrentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                    if (task.isSuccessful()) {
//
//                        String level = task.getResult().getString("nLevel");
//
//                        if (level.equals("ADMIN")) {
//                            /*
//                            Set delay 2 sec
//                            */
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    navigateToMainAdmin();
//                                }
//                            }, 2000);
//                        } else {
//                            /*
//                            Set delay 2 sec
//                            */
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    navigateToMain();
//                                }
//                            }, 2000);
//                        }
//
//                    } else {
//
//                        String errorMessage = task.getException().getMessage();
//                        //showMessage(errorMessage);
//                    }
//                }
//            });
//
//
//        } else {
//
//            /*
//            Set delay 2 sec
//             */
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    navigateToMain();
//                }
//            }, 2000);
//        }




//        } else {
//
//            //showDialogInternetIssue();
//        }
    }

    private void showMessage(String mCurrentId) {
        Toast.makeText(this, mCurrentId, Toast.LENGTH_LONG).show();
    }

    private void showDialogInternetIssue() {
    }

    private void navigateToMainAdmin() {
        Intent intent = new Intent(this, MainAdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}