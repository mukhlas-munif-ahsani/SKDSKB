package com.munifahsan.skdskb.Tryout;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.skdskb.Models.MateriListModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TryoutRepository implements TryoutContract.Repository {

    TryoutContract.Listener mListener;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPostRef = firebaseFirestore.collection("POST");

    public TryoutRepository(TryoutContract.Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void getData(String id) {

        mPostRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MateriListModel model = documentSnapshot.toObject(MateriListModel.class);
                mListener.getDataSuccess(model.getnJenis(), model.getnUploadTime(), model.getnWebUrl());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //mListener.getDataSuccess("Lorem ipsum dolor jsdfhas df jshdfhasdf ashdf sdhfkjahsdf ",
        // "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png", "", "", "");
    }

    @Override
    public void getSeen(String postId, String uid){
        mPostRef.document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                mListener.getSeenListener((List<String>) document.get("nSeen"));
            }
        });
    }

    @Override
    public void getFavo(String postId, String uid){
        mPostRef.document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                mListener.getFavoListener((List<String>) document.get("nFavo"));
            }
        });
    }

    @Override
    public void getBookmark(String postId, String uid) {
        mPostRef.document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                mListener.getBookmarkListener((List<String>) document.get("nBookmark"));
            }
        });
    }

    @Override
    public void addBookmark(String postId, String id) {

        mPostRef.document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                List<String> marks = (List<String>) document.get("nBookmark");
                //mListener.getBookmarkListener();
                if (marks.contains(id)) {

                    mPostRef.document(postId)
                            .update("nBookmark", FieldValue.arrayRemove(id))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("bookmark", "succes");
                                    getBookmark(postId, id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("bookmark error", e.getMessage());
                        }
                    });

                } else {

                    mPostRef.document(postId)
                            .update("nBookmark", FieldValue.arrayUnion(id))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("bookmark", "succes");
                                    getBookmark(postId, id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("bookmark error", e.getMessage());
                        }
                    });

                }
            }
        });


    }

    @Override
    public void addSeen(String postId, String id){

        mPostRef.document(postId)
                .update("nSeen", FieldValue.arrayUnion(id+getNowTime()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("seen", "succes");
                        getSeen(postId, id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("seen error", e.getMessage());
            }
        });

    }

    @Override
    public void addFavo(String postId, String id){
        mPostRef.document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                List<String> marks = (List<String>) document.get("nFavo");
                //mListener.getBookmarkListener();
                if (marks.contains(id)) {

                    mPostRef.document(postId)
                            .update("nFavo", FieldValue.arrayRemove(id))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("bookmark", "succes");
                                    getFavo(postId, id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("bookmark error", e.getMessage());
                        }
                    });

                } else {

                    mPostRef.document(postId)
                            .update("nFavo", FieldValue.arrayUnion(id))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.e("bookmark", "succes");
                                    getFavo(postId, id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("bookmark error", e.getMessage());
                        }
                    });

                }
            }
        });
    }

    public String getNowTime() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("hhmmssddMyyyy");
        String mNowTime = dateFormat.format(calendar.getTime());
        return mNowTime;
    }
}
