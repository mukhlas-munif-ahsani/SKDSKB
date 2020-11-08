package com.munifahsan.skdskb.HomePage.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.HomePage.HomeEvent;
import com.munifahsan.skdskb.HomePage.model.HomeModel;

public class HomeRepo implements HomeRepoInt {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference mPageRef = firebaseFirestore.collection("PAGE_CONTENT").document("HOME");

    public HomeRepo() {
    }

    @Override
    public void getData() {
        mPageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    HomeModel model = documentSnapshot.toObject(HomeModel.class);
                    postEvent(HomeEvent.onSuccess, null,
                            model.getnGreeting(),
                            model.getnQuote());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(HomeEvent.onError, e.getMessage(), null, null);
            }
        });
    }

    @Override
    public void getUserData(String id){

    }

    @Override
    public void getFakeNamaProfile(){
        mPageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    HomeModel model = documentSnapshot.toObject(HomeModel.class);
                    postEvent(HomeEvent.onGetNamaImageSuccess,
                            model.getnNama(),
                            model.getnImageUrl());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(HomeEvent.onError, e.getMessage(), null, null);
            }
        });
    }

    public void postEvent(int type, String errorMessage, String greeting, String quote) {
        HomeEvent event = new HomeEvent();

        event.setEventType(type);

        if (errorMessage != null) {
            event.setErrorMessage(errorMessage);
        }

        if (greeting != null) {
            event.setGreeting(greeting);
        }

        if (quote != null) {
            event.setQuote(quote);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }

    public void postEvent(int type, String nama, String imageUrl) {
        HomeEvent event = new HomeEvent();

        event.setEventType(type);

        if (nama != null) {
            event.setNama(nama);
        }

        if (imageUrl != null) {
            event.setImageUrl(imageUrl);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }
}
