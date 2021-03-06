package com.munifahsan.skdskb.InformasiPage.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.InformasiPage.InformasiEvent;
import com.munifahsan.skdskb.InformasiPage.model.InformasiModel;
import com.munifahsan.skdskb.InformasiPage.model.InformasiPageModel;

public class InformasiRepo implements InformasiRepoInt {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference mPageRef = firebaseFirestore.collection("PAGE_CONTENT").document("INFORMASI");

    public InformasiRepo() {
    }

    @Override
    public void getData(){
        mPageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    InformasiPageModel model = documentSnapshot.toObject(InformasiPageModel.class);
                    postEvent(InformasiEvent.onGetDataSuccess, null,
                            model.getnQuote(),
                            model.getnTalker());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(InformasiEvent.onGetDataError, e.getMessage(), null, null);
            }
        });
    }

    public void postEvent(int type, String errorMessage, String quote, String talker) {
        InformasiEvent event = new InformasiEvent();

        event.setEventType(type);

        if (errorMessage != null) {
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
