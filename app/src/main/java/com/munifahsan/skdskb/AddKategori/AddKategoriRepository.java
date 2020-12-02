package com.munifahsan.skdskb.AddKategori;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddKategoriRepository implements AddKategoriContract.Repository {
    AddKategoriContract.Listener mListener;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("postPhoto");

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPostRef = firebaseFirestore.collection("KATEGORI");

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public AddKategoriRepository(AddKategoriContract.Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void upData(String title, String halaman, String kategori, String desc, String imageUrl, Uri imageUri, int font) {

        if (imageUri != null) {
            String fileName = getNowTime() + 14;
            storageRef.child(fileName).putFile(imageUri)
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
                                    map.put("nDesc", desc);
                                    map.put("nHalaman", halaman);
                                    map.put("nFont", font);
                                    map.put("nImageUrl", uri.toString());

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
            map.put("nHalaman", halaman);
            map.put("nFont", font);
            map.put("nImageUrl", imageUrl);

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
