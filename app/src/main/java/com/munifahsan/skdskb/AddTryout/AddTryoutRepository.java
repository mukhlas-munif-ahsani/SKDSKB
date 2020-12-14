package com.munifahsan.skdskb.AddTryout;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTryoutRepository implements AddTryoutContract.Repository {

    AddTryoutContract.Listener mListener;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("postPhoto");

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPostRef = firebaseFirestore.collection("POST");

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public AddTryoutRepository(AddTryoutContract.Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void post(String halaman, List<String> kategori, String title, String thumbnailUrl, Uri thumbnailUri,
                     String jenis, String jenisImage, String ebookLink, boolean isPremium, boolean isPilihan){

        if (thumbnailUri != null) {
            String fileName = getNowTime() + 14;
            storageRef.child(fileName).putFile(thumbnailUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d("IMAGE_URL", uri.toString());

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("nKategori", kategori);
                                    map.put("nTitle", title);
                                    map.put("nFavo", Arrays.asList());
                                    map.put("nBookmark", Arrays.asList());
                                    map.put("nSeen", Arrays.asList());
                                    map.put("nJenisImage", jenisImage);
                                    map.put("nJenis", jenis);
                                    map.put("nTipe", halaman);
                                    map.put("nUploadTime", new Timestamp(new Date()));
                                    map.put("premium", isPremium);
                                    map.put("nPilihanEditor", isPilihan);
                                    map.put("nThumbnailImageUrl", uri.toString());
                                    map.put("nWebUrl", ebookLink);
                                    mPostRef.document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mListener.onUpSuccessListener("Kategori berhasil di publish");
                                            Log.d("post", "berhasil post");
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("IMAGE_URL_error", e.getMessage());
                                    mListener.onUpErrorListener(e.getMessage());
                                }
                            });

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mListener.progressListener("Mengunggah Gambar Thumbnail...", progress);
                        }
                    });
        } else {

            Map<String, Object> map = new HashMap<>();
            map.put("nKategori", kategori);
            map.put("nTitle", title);
            map.put("nFavo", Arrays.asList());
            map.put("nBookmark", Arrays.asList());
            map.put("nSeen", Arrays.asList());
            map.put("nJenisImage", jenisImage);
            map.put("nJenis", jenis);
            map.put("nTipe", halaman);
            map.put("nUploadTime", new Timestamp(new Date()));
            map.put("premium", isPremium);
            map.put("nPilihanEditor", isPilihan);
            map.put("nThumbnailImageUrl", thumbnailUrl);
            map.put("nWebUrl", ebookLink);

            mPostRef.document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    mListener.onUpSuccessListener("Kategori berhasil di publish");
                    Log.d("post", "berhasil post");
                }
            });
        }

    }

    public String getNowTime() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("ddMMyyyyhhmmssSSS");
        String mNowTime = dateFormat.format(calendar.getTime());
        return mNowTime;
    }
}
