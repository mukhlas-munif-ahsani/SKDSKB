package com.munifahsan.skdskb.AddArtikel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.munifahsan.skdskb.Adapters.Adapter;
import com.munifahsan.skdskb.Adapters.KategoriAdapter;
import com.munifahsan.skdskb.Models.KategoriModel;
import com.munifahsan.skdskb.Models.Model;
import com.munifahsan.skdskb.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AddArtikelFragment extends Fragment implements AddArtikelContract.View {

    AddArtikelContract.Presenter mPres;

    @BindView(R.id.cardView_progress)
    CardView mCardProgress;
    @BindView(R.id.textView_progress)
    TextView mTextProgress;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.cardView_success)
    CardView mCardSuccess;
    @BindView(R.id.textView_success)
    TextView mTextSuccess;

    @BindView(R.id.cardView_error)
    CardView mCardError;
    @BindView(R.id.textView_error)
    TextView mTextError;

    @BindView(R.id.button_home)
    Button mHalHome;
    @BindView(R.id.button_informasi)
    Button mHalInformasi;
    @BindView(R.id.button_tryout)
    Button mHalTryout;

    @BindView(R.id.recyclerView_pilihKategori)
    RecyclerView mRvPilihKategori;
    @BindView(R.id.recyclerView_kategoriDipilih)
    RecyclerView mRvKategoriDipilih;

    @BindView(R.id.editText_title)
    EditText mEdtTitle;

    @BindView(R.id.editText_thumbnail)
    EditText mEdtThumbnail;
    @BindView(R.id.imageView_thumnail)
    ImageView mThumbnail;

    @BindView(R.id.editText_imageHeader)
    EditText mEdtHeaderImage;
    @BindView(R.id.imageView_imageHeader)
    ImageView mHeaderImage;


    @BindView(R.id.editText_par1)
    EditText mEdtPar1;
    @BindView(R.id.imageView_par1)
    ImageView mImgPar1;

    @BindView(R.id.editText_par2)
    EditText mEdtPar2;
    @BindView(R.id.imageView_par2)
    ImageView mImgPar2;

    @BindView(R.id.editText_par3)
    EditText mEdtPar3;
    @BindView(R.id.imageView_par3)
    ImageView mImgPar3;

    @BindView(R.id.editText_par4)
    EditText mEdtPar4;
    @BindView(R.id.imageView_par4)
    ImageView mImgPar4;

    @BindView(R.id.editText_par5)
    EditText mEdtPar5;
    @BindView(R.id.imageView_par5)
    ImageView mImgPar5;

    @BindView(R.id.editText_par6)
    EditText mEdtPar6;
    @BindView(R.id.imageView_par6)
    ImageView mImgPar6;

    @BindView(R.id.editText_par7)
    EditText mEdtPar7;
    @BindView(R.id.imageView_par7)
    ImageView mImgPar7;

    @BindView(R.id.editText_par8)
    EditText mEdtPar8;
    @BindView(R.id.imageView_par8)
    ImageView mImgPar8;

    @BindView(R.id.editText_par9)
    EditText mEdtPar9;
    @BindView(R.id.imageView_par9)
    ImageView mImgPar9;

    @BindView(R.id.editText_par10)
    EditText mEdtPar10;
    @BindView(R.id.imageView_par10)
    ImageView mImgPar10;

    @BindView(R.id.editText_par11)
    EditText mEdtPar11;
    @BindView(R.id.imageView_par11)
    ImageView mImgPar11;

    @BindView(R.id.editText_par12)
    EditText mEdtPar12;
    @BindView(R.id.imageView_par12)
    ImageView mImgPar12;

    @BindView(R.id.editText_par13)
    EditText mEdtPar13;
    @BindView(R.id.imageView_par13)
    ImageView mImgPar13;

    @BindView(R.id.editText_par14)
    EditText mEdtPar14;
    @BindView(R.id.imageView_par14)
    ImageView mImgPar14;

    @BindView(R.id.editText_par15)
    EditText mEdtPar15;
    @BindView(R.id.imageView_par15)
    ImageView mImgPar15;

    @BindView(R.id.checkbox_premium)
    CheckBox mPremium;
    @BindView(R.id.checkbox_pilihanEditor)
    CheckBox mPilihanEditor;

    @BindView(R.id.cardView_upload)
    Button mUpload;

    Uri mImageThumbnailUri, mImageHeaderUri, mImagePar1Uri, mImagePar2Uri, mImagePar3Uri, mImagePar4Uri, mImagePar5Uri,
            mImagePar6Uri, mImagePar7Uri, mImagePar8Uri, mImagePar9Uri, mImagePar10Uri,
            mImagePar11Uri, mImagePar12Uri, mImagePar13Uri, mImagePar14Uri, mImagePar15Uri;

    String halaman;

    private static final int PICK_THUMBNAIL_IMAGE_REQUEST = 1;
    private static final int PICK_HEADER_IMAGE_REQUEST = 2;

    private static final int PICK_IMAGE_REQUEST_PAR_1 = 3;
    private static final int PICK_IMAGE_REQUEST_PAR_2 = 4;
    private static final int PICK_IMAGE_REQUEST_PAR_3 = 5;
    private static final int PICK_IMAGE_REQUEST_PAR_4 = 6;
    private static final int PICK_IMAGE_REQUEST_PAR_5 = 7;
    private static final int PICK_IMAGE_REQUEST_PAR_6 = 8;
    private static final int PICK_IMAGE_REQUEST_PAR_7 = 9;
    private static final int PICK_IMAGE_REQUEST_PAR_8 = 10;
    private static final int PICK_IMAGE_REQUEST_PAR_9 = 11;
    private static final int PICK_IMAGE_REQUEST_PAR_10 = 12;
    private static final int PICK_IMAGE_REQUEST_PAR_11 = 13;
    private static final int PICK_IMAGE_REQUEST_PAR_12 = 14;
    private static final int PICK_IMAGE_REQUEST_PAR_13 = 15;
    private static final int PICK_IMAGE_REQUEST_PAR_14 = 16;
    private static final int PICK_IMAGE_REQUEST_PAR_15 = 17;

    List<Model> arrayList = new ArrayList<>();
    List<String> listDipilih = new ArrayList<>();
    Adapter adapter;
    Handler handler = new Handler();

    Query query;
    private KategoriAdapter mKategoriAdapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mKategoriRef = firebaseFirestore.collection("KATEGORI");
    private LinearLayoutManager mLayoutManager;

    private CollectionReference mPostRef = firebaseFirestore.collection("POST");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference("postPhoto");

    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;

    public AddArtikelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_artikel, container, false);

        ButterKnife.bind(this, view);
        mPres = new AddArtikelPresenter(this);

        adapter = new Adapter(arrayList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvKategoriDipilih.setLayoutManager(mLayoutManager);
        mRvKategoriDipilih.setItemAnimator(new DefaultItemAnimator());
        mRvKategoriDipilih.setAdapter(adapter);


//        mHalInformasi.setBackgroundColor(Color.parseColor("#B8CCDC"));
//        mHalInformasi.setTextColor(Color.parseColor("#2b2b2b"));
//        mHalTryout.setBackgroundColor(Color.parseColor("#B8CCDC"));
//        mHalTryout.setTextColor(Color.parseColor("#2b2b2b"));

        mEdtThumbnail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mThumbnail.setVisibility(View.GONE);
                } else {
                    mThumbnail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtHeaderImage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mHeaderImage.setVisibility(View.GONE);
                } else {
                    mHeaderImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar1.setVisibility(View.GONE);
                } else {
                    mImgPar1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar2.setVisibility(View.GONE);
                } else {
                    mImgPar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar3.setVisibility(View.GONE);
                } else {
                    mImgPar3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar4.setVisibility(View.GONE);
                } else {
                    mImgPar4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar5.setVisibility(View.GONE);
                } else {
                    mImgPar5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar6.setVisibility(View.GONE);
                } else {
                    mImgPar6.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar7.setVisibility(View.GONE);
                } else {
                    mImgPar7.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar8.setVisibility(View.GONE);
                } else {
                    mImgPar8.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar9.setVisibility(View.GONE);
                } else {
                    mImgPar9.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar10.setVisibility(View.GONE);
                } else {
                    mImgPar10.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar11.setVisibility(View.GONE);
                } else {
                    mImgPar11.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar12.setVisibility(View.GONE);
                } else {
                    mImgPar12.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar13.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar13.setVisibility(View.GONE);
                } else {
                    mImgPar13.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar14.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar14.setVisibility(View.GONE);
                } else {
                    mImgPar14.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mEdtPar15.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImgPar15.setVisibility(View.GONE);
                } else {
                    mImgPar15.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showKategori(mKategoriRef.whereEqualTo("nHalaman", "MATERI"));

        checkConnection();

        // halaman = "materi";
        home();
        return view;
    }

    public void checkConnection() {
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showKategori(Query query) {
//        /*
//        menampilkan list kategori materi secara horizontal
//         */
//        mKategoriContent.setVisibility(View.GONE);
//        mKategoriMateritxt.setVisibility(View.INVISIBLE);
//        mShimerKategori.setVisibility(View.VISIBLE);

        //query = mKategoriRef;
        FirestoreRecyclerOptions<KategoriModel> options = new FirestoreRecyclerOptions.Builder<KategoriModel>()
                .setQuery(query, KategoriModel.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) {
//                    mKategoriContent.setVisibility(View.VISIBLE);
//                    mKategoriMateritxt.setVisibility(View.VISIBLE);
//                    mShimerKategori.setVisibility(View.GONE);
                    //showMessage("ada");
                } else {
                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
//                    mRvHottestEvent.setVisibility(View.VISIBLE);
                }
            }
        });

        mKategoriAdapter = new KategoriAdapter(options);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvPilihKategori.setLayoutManager(mLayoutManager);
        mRvPilihKategori.setAdapter(mKategoriAdapter);

        mKategoriAdapter.setOnItemClickListener(new KategoriAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id, int position, String kategori, String collection) {
//                Intent intent = new Intent(getActivity(), DetailKategoriActivity.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                intent.putExtra("KATEGORI", kategori);
//                intent.putExtra("COLLECTION", collection);

                arrayList.add(new Model(kategori));
                listDipilih.add(kategori);
                adapter.notifyDataSetChanged();

                //showError(arrayList.toString());
//                if (arrayList.contains(kategori)) {
//                    boolean shouldAdd = true;
//                    for ( ArrayList<String> val : arrayList ) {
//                        if (val.contains(kategori)) {
//                            shouldAdd = false;
//                            break;
//                        }
//                    }
//                    if ( shouldAdd ) {
//                        arrayList.add(kategori);
//                    }
//                }

                //startActivity(intent);
                //showMessage("clicked");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mKategoriAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mKategoriAdapter.startListening();
    }

    private void openFileChooser(int c) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        switch (c) {
            case 1:
                startActivityForResult(intent, PICK_THUMBNAIL_IMAGE_REQUEST);
                break;
            case 2:
                startActivityForResult(intent, PICK_HEADER_IMAGE_REQUEST);
                break;
            case 3:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_1);
                break;
            case 4:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_2);
                break;
            case 5:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_3);
                break;
            case 6:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_4);
                break;
            case 7:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_5);
                break;
            case 8:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_6);
                break;
            case 9:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_7);
                break;
            case 10:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_8);
                break;
            case 11:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_9);
                break;
            case 12:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_10);
                break;
            case 13:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_11);
                break;
            case 14:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_12);
                break;
            case 15:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_13);
                break;
            case 16:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_14);
                break;
            case 17:
                startActivityForResult(intent, PICK_IMAGE_REQUEST_PAR_15);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_THUMBNAIL_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageThumbnailUri = data.getData();
            mThumbnail.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImageThumbnailUri).into(mThumbnail);
        }

        if (requestCode == PICK_HEADER_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageHeaderUri = data.getData();
            mEdtHeaderImage.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImageHeaderUri).into(mHeaderImage);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar1Uri = data.getData();
            mEdtPar1.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImagePar1Uri).into(mImgPar1);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar2Uri = data.getData();
            mEdtPar2.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar2Uri).into(mImgPar2);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_3 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar3Uri = data.getData();
            mEdtPar3.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar3Uri).into(mImgPar3);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_4 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar4Uri = data.getData();
            mEdtPar4.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar4Uri).into(mImgPar4);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_5 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar5Uri = data.getData();
            mEdtPar5.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar5Uri).into(mImgPar5);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_6 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar6Uri = data.getData();
            mEdtPar6.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar6Uri).into(mImgPar6);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_7 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar7Uri = data.getData();
            mEdtPar7.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar7Uri).into(mImgPar7);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_8 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar8Uri = data.getData();
            mEdtPar8.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar8Uri).into(mImgPar8);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_9 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar9Uri = data.getData();
            mEdtPar9.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar9Uri).into(mImgPar9);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_10 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar10Uri = data.getData();
            mEdtPar10.setVisibility(View.GONE);

            //hideTextPhoto();
            Glide.with(this).load(mImagePar10Uri).into(mImgPar10);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_11 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar11Uri = data.getData();
            mEdtPar11.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImagePar11Uri).into(mImgPar11);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_12 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar12Uri = data.getData();
            mEdtPar12.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImagePar12Uri).into(mImgPar12);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_13 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar13Uri = data.getData();
            mEdtPar13.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImagePar13Uri).into(mImgPar13);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_14 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar14Uri = data.getData();
            mEdtPar14.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImagePar14Uri).into(mImgPar14);
        }

        if (requestCode == PICK_IMAGE_REQUEST_PAR_15 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImagePar15Uri = data.getData();
            mEdtPar15.setVisibility(View.GONE);
            //hideTextPhoto();

            Glide.with(this).load(mImagePar15Uri).into(mImgPar15);
        }
    }

    @OnClick(R.id.button_home)
    public void home() {

        halaman = "materi";

        showKategori(mKategoriRef.whereEqualTo("nHalaman", "MATERI"));
        mKategoriAdapter.startListening();

        arrayList.clear();

        mHalHome.setBackgroundColor(Color.parseColor("#0E9BF5"));
        mHalHome.setTextColor(Color.parseColor("#FFFFFF"));
        mHalInformasi.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalInformasi.setTextColor(Color.parseColor("#2b2b2b"));
        mHalTryout.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalTryout.setTextColor(Color.parseColor("#2b2b2b"));
    }

    @OnClick(R.id.button_informasi)
    public void informasi() {

        halaman = "informasi";

        showKategori(mKategoriRef.whereEqualTo("nHalaman", "INFORMASI"));
        mKategoriAdapter.startListening();

        arrayList.clear();

        mHalHome.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalHome.setTextColor(Color.parseColor("#2b2b2b"));
        mHalInformasi.setBackgroundColor(Color.parseColor("#0E9BF5"));
        mHalInformasi.setTextColor(Color.parseColor("#FFFFFF"));
        mHalTryout.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalTryout.setTextColor(Color.parseColor("#2b2b2b"));
    }

    @OnClick(R.id.button_tryout)
    public void tryout() {

        halaman = "tryout";

        showKategori(mKategoriRef.whereEqualTo("nHalaman", "TRYOUT"));
        mKategoriAdapter.startListening();

        arrayList.clear();

        mHalHome.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalHome.setTextColor(Color.parseColor("#2b2b2b"));
        mHalInformasi.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalInformasi.setTextColor(Color.parseColor("#2b2b2b"));
        mHalTryout.setBackgroundColor(Color.parseColor("#0E9BF5"));
        mHalTryout.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @OnClick(R.id.imageView_thumnail)
    public void imageThumbnailClick() {
        openFileChooser(1);
    }

    @OnClick(R.id.imageView_imageHeader)
    public void imageHeaderClick() {
        openFileChooser(2);
    }

    @OnClick(R.id.imageView_par1)
    public void imagePar1() {
        openFileChooser(3);
    }

    @OnClick(R.id.button_reset_thumbnail)
    public void imageThumbnailReset() {
        mEdtThumbnail.setVisibility(View.VISIBLE);
        mEdtThumbnail.setText("");
        mThumbnail.setVisibility(View.VISIBLE);
        mImageThumbnailUri = null;
        mThumbnail.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.button_reset_imageHeader)
    public void imageHeaderReset() {
        mEdtHeaderImage.setVisibility(View.VISIBLE);
        mEdtHeaderImage.setText("");
        mHeaderImage.setVisibility(View.VISIBLE);
        mImageHeaderUri = null;
        mHeaderImage.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.button_reset_par1)
    public void par1Reset() {
        mEdtPar1.setVisibility(View.VISIBLE);
        mEdtPar1.setText("");
        mImgPar1.setVisibility(View.VISIBLE);
        mImagePar1Uri = null;
        mImgPar1.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par2)
    public void imagePar2() {
        openFileChooser(4);
    }

    @OnClick(R.id.button_reset_par2)
    public void par2Reset() {
        mEdtPar2.setVisibility(View.VISIBLE);
        mEdtPar2.setText("");
        mImgPar2.setVisibility(View.VISIBLE);
        mImagePar2Uri = null;
        mImgPar2.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par3)
    public void imagePar3() {
        openFileChooser(5);
    }

    @OnClick(R.id.button_reset_par3)
    public void par3Reset() {
        mEdtPar3.setVisibility(View.VISIBLE);
        mEdtPar3.setText("");
        mImgPar3.setVisibility(View.VISIBLE);
        mImagePar3Uri = null;
        mImgPar3.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par4)
    public void imagePar4() {
        openFileChooser(6);
    }

    @OnClick(R.id.button_reset_par4)
    public void par4Reset() {
        mEdtPar4.setVisibility(View.VISIBLE);
        mEdtPar4.setText("");
        mImgPar4.setVisibility(View.VISIBLE);
        mImagePar4Uri = null;
        mImgPar4.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par5)
    public void imagePar5() {
        openFileChooser(7);
    }

    @OnClick(R.id.button_reset_par5)
    public void par5Reset() {
        mEdtPar5.setVisibility(View.VISIBLE);
        mEdtPar5.setText("");
        mImgPar5.setVisibility(View.VISIBLE);
        mImagePar5Uri = null;
        mImgPar5.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par6)
    public void imagePar6() {
        openFileChooser(8);
    }

    @OnClick(R.id.button_reset_par6)
    public void par6Reset() {
        mEdtPar6.setVisibility(View.VISIBLE);
        mEdtPar6.setText("");
        mImgPar6.setVisibility(View.VISIBLE);
        mImagePar6Uri = null;
        mImgPar6.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par7)
    public void imagePar7() {
        openFileChooser(9);
    }

    @OnClick(R.id.button_reset_par7)
    public void par7Reset() {
        mEdtPar7.setVisibility(View.VISIBLE);
        mEdtPar7.setText("");
        mImgPar7.setVisibility(View.VISIBLE);
        mImagePar7Uri = null;
        mImgPar7.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par8)
    public void imagePar8() {
        openFileChooser(10);
    }

    @OnClick(R.id.button_reset_par8)
    public void par8Reset() {
        mEdtPar8.setVisibility(View.VISIBLE);
        mEdtPar8.setText("");
        mImgPar8.setVisibility(View.VISIBLE);
        mImagePar8Uri = null;
        mImgPar8.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par9)
    public void imagePar9() {
        openFileChooser(11);
    }

    @OnClick(R.id.button_reset_par9)
    public void par9Reset() {
        mEdtPar9.setVisibility(View.VISIBLE);
        mEdtPar9.setText("");
        mImgPar9.setVisibility(View.VISIBLE);
        mImagePar9Uri = null;
        mImgPar9.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par10)
    public void imagePar10() {
        openFileChooser(12);
    }

    @OnClick(R.id.button_reset_par10)
    public void par10Reset() {
        mEdtPar10.setVisibility(View.VISIBLE);
        mEdtPar10.setText("");
        mImgPar10.setVisibility(View.VISIBLE);
        mImagePar10Uri = null;
        mImgPar10.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par11)
    public void imagePar11() {
        openFileChooser(13);
    }

    @OnClick(R.id.button_reset_par11)
    public void par11Reset() {
        mEdtPar11.setVisibility(View.VISIBLE);
        mEdtPar11.setText("");
        mImgPar11.setVisibility(View.VISIBLE);
        mImagePar11Uri = null;
        mImgPar11.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par12)
    public void imagePar12() {
        openFileChooser(14);
    }

    @OnClick(R.id.button_reset_par12)
    public void par12Reset() {
        mEdtPar12.setVisibility(View.VISIBLE);
        mEdtPar12.setText("");
        mImgPar12.setVisibility(View.VISIBLE);
        mImagePar12Uri = null;
        mImgPar12.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par13)
    public void imagePar13() {
        openFileChooser(15);
    }

    @OnClick(R.id.button_reset_par13)
    public void par13Reset() {
        mEdtPar13.setVisibility(View.VISIBLE);
        mEdtPar13.setText("");
        mImgPar13.setVisibility(View.VISIBLE);
        mImagePar13Uri = null;
        mImgPar13.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par14)
    public void imagePar14() {
        openFileChooser(16);
    }

    @OnClick(R.id.button_reset_par14)
    public void par14Reset() {
        mEdtPar14.setVisibility(View.VISIBLE);
        mEdtPar14.setText("");
        mImgPar14.setVisibility(View.VISIBLE);
        mImagePar14Uri = null;
        mImgPar14.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.imageView_par15)
    public void imagePar15() {
        openFileChooser(17);
    }

    @OnClick(R.id.button_reset_par15)
    public void par15Reset() {
        mEdtPar15.setVisibility(View.VISIBLE);
        mEdtPar15.setText("");
        mImgPar15.setVisibility(View.VISIBLE);
        mImagePar15Uri = null;
        mImgPar15.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.cardView_upload)
    public void upload() {

        shoMessage(halaman);

        //upImgThumbnail(mImageThumbnailUri);
        if (isConnected) {

            if (!mEdtTitle.getText().toString().isEmpty()) {

                if (!listDipilih.isEmpty()) {

                    if (!mEdtThumbnail.getText().toString().isEmpty() || mImageThumbnailUri != null) {

                        if (!mEdtHeaderImage.getText().toString().isEmpty() || mImageHeaderUri != null) {

                            if (!mEdtPar1.getText().toString().isEmpty() || mImagePar1Uri != null) {

                                if (!mEdtPar2.getText().toString().isEmpty() || mImagePar2Uri != null) {
                                    hideError();

                                    mUpload.setEnabled(false);

                                    mPres.title(mEdtTitle.getText().toString());
                                    mPres.kategori(listDipilih);
                                    mPres.jenisImage("https://firebasestorage.googleapis.com/v0/b/skdskb-8a08b.appspot.com/o/ICONS%2Fic_artikel.png?alt=media&token=9afb8d13-355a-4a17-a7dd-e71aab6e97b9");
                                    mPres.jenis("Artikel");
                                    mPres.tipe(halaman);
                                    mPres.premium(mPremium.isChecked());
                                    mPres.pilihanEditor(mPilihanEditor.isChecked());
                                    mPres.thumbnailImage(mEdtThumbnail.getText().toString(), mImageThumbnailUri);
                                    mPres.headerImage(mEdtHeaderImage.getText().toString(), mImageHeaderUri);
                                    mPres.par1(mEdtPar1.getText().toString(), mImagePar1Uri);
                                    mPres.par2(mEdtPar2.getText().toString(), mImagePar2Uri);
                                    mPres.par3(mEdtPar3.getText().toString(), mImagePar3Uri);
                                    mPres.par4(mEdtPar4.getText().toString(), mImagePar4Uri);
                                    mPres.par5(mEdtPar5.getText().toString(), mImagePar5Uri);
                                    mPres.par6(mEdtPar6.getText().toString(), mImagePar6Uri);
                                    mPres.par7(mEdtPar7.getText().toString(), mImagePar7Uri);
                                    mPres.par8(mEdtPar8.getText().toString(), mImagePar8Uri);
                                    mPres.par9(mEdtPar9.getText().toString(), mImagePar9Uri);
                                    mPres.par10(mEdtPar10.getText().toString(), mImagePar10Uri);
                                    mPres.par11(mEdtPar11.getText().toString(), mImagePar11Uri);
                                    mPres.par12(mEdtPar12.getText().toString(), mImagePar12Uri);
                                    mPres.par13(mEdtPar13.getText().toString(), mImagePar13Uri);
                                    mPres.par14(mEdtPar14.getText().toString(), mImagePar14Uri);
                                    mPres.par15(mEdtPar15.getText().toString(), mImagePar15Uri);
                                } else {
                                    showError("Mohon isi paragraf 1 dan 2 terlebih dahulu !!");
                                }
                            } else {
                                showError("Mohon isi paragraf 1 dan 2 terlebih dahulu !!");
                            }
                        } else {
                            showError("Gambar Header belum dipilih !!!");
                        }
                    } else {
                        showError("Gambar Thumbnail belum dipilih !!!");
                    }
                } else {
                    showError("Kategori tidak boleh kosong !!!");
                }
            } else {
                Log.d("title", "kosong");
                showError("Title tidak boleh kosng !!!");
            }
        } else {
            showError("Mohon periksa koneksi anda !!");
        }

//        Map<String, Object> map = new HashMap<>();
//        map.put("nKategori", listDipilih);
//
//        mPostRef.document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//               // mListener.listenerPar1(true);
//
//                Log.d("post", "berhasil post");
//            }
//        });

//        Log.d("par1Uri", mImagePar1Uri.toString());
    }

//    public void upImgThumbnail(Uri uri) {
//        String fileName = "nasdhadsfads";
//        storageRef.child(fileName).putFile(uri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        storageRef.child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                Log.d("IMAGE_URL", uri.toString());
//                                //mThumbnailUrl = uri.toString();
//
//                                // thumbnail = true;
//
//                                //post("dari par 1");
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("IMAGE_URL_error", e.getMessage());
//                            }
//                        });
//
//                    }
//                });
//    }

    @Override
    public void showError(String error) {
        mCardError.setVisibility(View.VISIBLE);
        mCardProgress.setVisibility(View.GONE);
        mCardSuccess.setVisibility(View.GONE);
        mTextError.setText(error);

        mUpload.setEnabled(true);

        //mTextError.setError();
    }

    @Override
    public void hideError() {
        mCardError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress(String txt, double progress) {
        mCardError.setVisibility(View.GONE);
        mCardSuccess.setVisibility(View.GONE);
        mCardProgress.setVisibility(View.VISIBLE);
        mTextProgress.setText(txt);
        mProgressBar.setProgress((int) progress);
    }

    @Override
    public void showSuccess(String success) {
        mCardError.setVisibility(View.GONE);
        mCardProgress.setVisibility(View.GONE);
        mCardSuccess.setVisibility(View.VISIBLE);
        mTextSuccess.setText(success);

        mUpload.setEnabled(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardSuccess.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void shoMessage(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}