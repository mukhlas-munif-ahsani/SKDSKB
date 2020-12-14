package com.munifahsan.skdskb.SignIn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.munifahsan.skdskb.MainActivity;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.SplashScreenActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 120;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mCurrent_id;
    private String sMail;
    private String sNama;
    private String sNotlp;
    private String sImageUrl;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient googleSignInClient;

    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionReference = mFirebaseFirestore.collection("USERS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();


        //mCurrent_id = mCurrentUser.getUid();

        ButterKnife.bind(this);
//
//        Intent intent = googleSignInClient.getSignInIntent();
//        startActivityForResult(intent, RC_SIGN_IN);

        /*Google*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();

        googleSignInClick();
    }

    public void doGoogleSignIn(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d("googleSignUp", "signInWithCredential:success");
                            //postEvent(RegisterEvent.onSuccess, null);
                            mCurrent_id = mAuth.getCurrentUser().getUid();
                            showMessage("sign in success" + mCurrent_id);
                            sMail = mAuth.getCurrentUser().getEmail();
                            sNama = mAuth.getCurrentUser().getDisplayName();
                            sImageUrl = mAuth.getCurrentUser().getPhotoUrl().toString();
//
                            inputGoogleAndFacebookSignIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("googleSignUp", "signInWithCredential:failure", task.getException());
                            showMessage("SignIn Gagal Silahkan Coba Lagi 1");
                            finish();
//                            postEvent(RegisterEvent.onError, task.getException().toString());
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            String ex = task.getException().toString();
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("googleSignUp", "firebaseAuthWithGoogle:" + account.getId());
                    showProgress();
                    doGoogleSignIn(account.getIdToken());
                    //firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("googleSignUp", "Google sign in failed", e);
                    showMessage("Sign In Gagal Silahkan Coba lagi 3");
                    finish();
                    // ...
                }
            } else {
                Log.w("googleSignUp", task.getException().getMessage());
                showMessage("Sign In Gagal Silahkan Coba lagi : " + task.getException().getMessage());
                finish();
            }
        }
    }


    public void inputGoogleAndFacebookSignIn() {
        //mCurrentUser = mAuth.getCurrentUser();

        showMessage(mCurrent_id);
        mCollectionReference.document(mCurrent_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {

                    //String user_id = mAuth.getCurrentUser().getUid();

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("nNama", sNama);
                    userMap.put("nUserId", mCurrent_id);
                    userMap.put("nEmail", sMail);

                    userMap.put("nCreated_at", FieldValue.serverTimestamp());
                    userMap.put("nLevel", "");
                    userMap.put("nImageUrl", sImageUrl);

                    mCollectionReference.document(mCurrent_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //postEvent(RegisterEvent.onSuccess, null);
                            navigateToSplash();
                            showMessage("Data tersimpan");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            postEvent(RegisterEvent.onError, e.getMessage());
                            showMessage(e.getMessage());
                        }
                    });

                } else {
                    //showMessage("ada");
                    navigateToSplash();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
            }
        });
    }

    private void navigateToSplash() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }


    //@OnClick(R.id.sign_in_button)
    public void googleSignInClick() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
        // showMessage("google clik");
    }

    private void showProgress() {

    }

    public void hideProgress() {

    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}