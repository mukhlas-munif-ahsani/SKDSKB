package com.munifahsan.skdskb.TryoutPage.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.TryoutPage.TryoutEvent;
import com.munifahsan.skdskb.TryoutPage.model.TryoutPageModel;

public class TryoutRepo implements TryoutRepoInt {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference mPageRef = firebaseFirestore.collection("PAGE_CONTENT").document("TRYOUT");

    public TryoutRepo() {

    }

    @Override
    public void getData(){
        mPageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    TryoutPageModel model = documentSnapshot.toObject(TryoutPageModel.class);
                    postEvent(TryoutEvent.onGetDataSuccess, null,
                            model.getnQuote(),
                            model.getnTalker());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(TryoutEvent.onGetDataError, e.getMessage(), null, null);
            }
        });
    }

    public void postEvent(int type, String errorMessage, String quote, String talker){
        TryoutEvent event = new TryoutEvent();

        event.setEventType(type);

        if (errorMessage != null){
            event.setErrorMessage(errorMessage);
        }

        if (quote != null){
            event.setQuote(quote);
        }

        if (talker != null){
            event.setTalker(talker);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }
}
