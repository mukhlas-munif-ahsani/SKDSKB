package com.munifahsan.skdskb.DetailKategori;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailKategoriRepository implements DetailKategoriContract.Repository {
    DetailKategoriContract.Listener mListener;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference mPageRef;

    public DetailKategoriRepository(DetailKategoriContract.Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void getData(String collection, String id){
        mPageRef = firebaseFirestore.collection("KATEGORI").document(id);
        mPageRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DetailKategoriModel model = documentSnapshot.toObject(DetailKategoriModel.class);
                mListener.getDataSuccess(
                        model.nImageUrl,
                        model.nTitle,
                        model.nDesc
                );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mListener.getDataError(e.getMessage());
            }
        });
        //mListener.getDataSuccess();
    }
}
