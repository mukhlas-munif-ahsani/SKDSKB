package com.munifahsan.skdskb.AddArtikel;

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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddArtikelRepository implements AddArtikelContract.Repository {

    AddArtikelContract.Listener mListener;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    List<String> kategori;

    String mTitle, mJenis, mJenisImage, mTipe, mThumbnailUrl, mHeaderUrl,
            mPar1, mPar2, mPar3, mPar4, mPar5,
            mPar6, mPar7, mPar8, mPar9, mPar10,
            mPar11, mPar12, mPar13, mPar14, mPar15;

    boolean prem, pil, kategoriBol, title, jenis, jenisImage, tipe, thumbnail, headerImage,
            par1, par2, par3, par4, par5,
            par6, par7, par8, par9, par10,
            par11, par12, par13, par14, par15 = false;

    boolean premium, pilihanEditor = false;

    String document_id;

    File file;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("postPhoto");

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPostRef = firebaseFirestore.collection("POST");

    public AddArtikelRepository(AddArtikelContract.Listener mListener) {
        this.mListener = mListener;

        document_id = getNowTime();

    }

    @Override
    public void setKategori(List<String> kategori) {
        this.kategori = kategori;

        kategoriBol = true;

        Log.d("kategori", "true");
        post("dari kategori");
    }

    @Override
    public void upTxtTitle(String txt) {
        mTitle = txt;

        title = true;

        Log.d("title", "true");

        post("dari title");
    }

    @Override
    public void setmJenisImage(String mJenisImage) {
        this.mJenisImage = mJenisImage;

        jenisImage = true;

        Log.d("jenis", "true");
        post("dari jenis image");
    }

    @Override
    public void upTxtJenis(String txt) {
        mJenis = txt;

        jenis = true;

        Log.d("jenis", "true");
        post("dari jenis");
    }

    @Override
    public void setmTipe(String mTipe) {
        this.mTipe = mTipe;

        tipe = true;

        post("dari tipe");
    }

    @Override
    public void setPremium(boolean premium) {
        this.premium = premium;

        prem = true;

        post("dari prem");
    }

    @Override
    public void setPilihanEditor(boolean pilihanEditor) {
        this.pilihanEditor = pilihanEditor;

        pil = true;

        post("dari pil");
    }

    @Override
    public void upTxtThumbnail(String txt) {
        thumbnail = true;

        mThumbnailUrl = txt;

        post("dari par 1");
    }

    @Override
    public void upImgThumbnail(Uri uri) {
        String fileName = getNowTime() + 21;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mThumbnailUrl = uri.toString();

                                thumbnail = true;

                                post("dari par 1");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("IMAGE_URL_error", e.getMessage());
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                mListener.progressListener("Mengunggah Gambar Thumbnail...", progress);
            }
        });

    }

    @Override
    public void upTxtHeader(String txt) {
        headerImage = true;

        mHeaderUrl = txt;

        post("dari par 1");
    }

    @Override
    public void upImgHeader(Uri uri) {
        String fileName = getNowTime() + 20;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mHeaderUrl = uri.toString();

                                headerImage = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Header...", progress);
                    }
                });
    }


    @Override
    public void upTxtPar1(String txt) {
        par1 = true;

        mPar1 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar1(Uri uri) {
        String fileName = getNowTime() + 1;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar1 = uri.toString();

                                par1 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 1...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar2(String txt) {
        par2 = true;

        mPar2 = txt;

        post("dari par 2");
    }

    @Override
    public void upImgPar2(Uri uri) {
        String fileName = getNowTime() + 2;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar2 = uri.toString();

                                par2 = true;

                                post("dari par 2");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 2...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar3(String txt) {
        par3 = true;

        mPar3 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar3(Uri uri) {
        String fileName = getNowTime() + 3;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar3 = uri.toString();

                                par3 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 3...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar4(String txt) {
        par4 = true;

        mPar4 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar4(Uri uri) {
        String fileName = getNowTime() + 4;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar4 = uri.toString();

                                par4 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 4...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar5(String txt) {
        par5 = true;

        mPar5 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar5(Uri uri) {
        String fileName = getNowTime() + 5;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar5 = uri.toString();

                                par5 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 5...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar6(String txt) {
        par6 = true;

        mPar6 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar6(Uri uri) {
        String fileName = getNowTime() + 6;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar6 = uri.toString();

                                par6 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 6...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar7(String txt) {
        par7 = true;

        mPar7 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar7(Uri uri) {
        String fileName = getNowTime() + 7;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar7 = uri.toString();

                                par7 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 7...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar8(String txt) {
        par8 = true;

        mPar8 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar8(Uri uri) {
        String fileName = getNowTime() + 8;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar8 = uri.toString();

                                par8 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 8...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar9(String txt) {
        par9 = true;

        mPar9 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar9(Uri uri) {
        String fileName = getNowTime() + 9;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar9 = uri.toString();

                                par9 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 9...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar10(String txt) {
        par10 = true;

        mPar10 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar10(Uri uri) {
        String fileName = getNowTime() + 10;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar10 = uri.toString();

                                par10 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 10...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar11(String txt) {
        par11 = true;

        mPar11 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar11(Uri uri) {
        String fileName = getNowTime() + 11;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar11 = uri.toString();

                                par11 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 11...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar12(String txt) {
        par12 = true;

        mPar12 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar12(Uri uri) {
        String fileName = getNowTime() + 12;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar12 = uri.toString();

                                par12 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 12...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar13(String txt) {
        par13 = true;

        mPar13 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar13(Uri uri) {
        String fileName = getNowTime() + 13;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar13 = uri.toString();

                                par13 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 13...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar14(String txt) {
        par14 = true;

        mPar14 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar14(Uri uri) {
        String fileName = getNowTime() + 14;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar14 = uri.toString();

                                par14 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 14...", progress);
                    }
                });
    }

    @Override
    public void upTxtPar15(String txt) {
        par15 = true;

        mPar15 = txt;

        post("dari par 1");
    }

    @Override
    public void upImgPar15(Uri uri) {
        String fileName = getNowTime() + 15;
        storageRef.child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("IMAGE_URL", uri.toString());
                                mPar15 = uri.toString();

                                par15 = true;

                                post("dari par 1");

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
                        mListener.progressListener("Mengunggah Gambar Paragraf 15...", progress);
                    }
                });
    }

    public void post(String txt) {

        Log.d("par bool", String.valueOf(prem) + pil + kategoriBol + title + jenis + tipe + thumbnail + headerImage +
                par1 + par2 + par3 + par4 + par5 +
                par6 + par7 + par8 + par9 + par10 +
                par11 + par12 + par13 + par14 + par15);

        if (prem && pil && kategoriBol && title && jenis && tipe && thumbnail && headerImage &&
                par1 && par2 && par3 && par4 && par5 &&
                par6 && par7 && par8 && par9 && par10 &&
                par11 && par12 && par13 && par14 && par15) {


            Map<String, Object> map = new HashMap<>();
            map.put("nKategori", kategori);
            map.put("nTitle", mTitle);
            map.put("nFavo", Arrays.asList());
            map.put("nBookmark", Arrays.asList());
            map.put("nSeen", Arrays.asList());
            map.put("nJenisImage", mJenisImage);
            map.put("nJenis", mJenis);
            map.put("nTipe", mTipe);
            map.put("nUploadTime", new Timestamp(new Date()));
            map.put("premium", premium);
            map.put("nPilihanEditor", pilihanEditor);
            map.put("nThumbnailImageUrl", mThumbnailUrl);
            map.put("nImageHeader", mHeaderUrl);
            map.put("nParagraf1", mPar1);
            map.put("nParagraf2", mPar2);
            map.put("nParagraf3", mPar3);
            map.put("nParagraf4", mPar4);
            map.put("nParagraf5", mPar5);
            map.put("nParagraf6", mPar6);
            map.put("nParagraf7", mPar7);
            map.put("nParagraf8", mPar8);
            map.put("nParagraf9", mPar9);
            map.put("nParagraf10", mPar10);
            map.put("nParagraf11", mPar11);
            map.put("nParagraf12", mPar12);
            map.put("nParagraf13", mPar13);
            map.put("nParagraf14", mPar14);
            map.put("nParagraf15", mPar15);

            mPostRef.document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mListener.listenerPar1(true);

                    mListener.onUpSuccessListener("Artikel berhasil di publish");
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
