package com.munifahsan.skdskb.AddTryout;

import android.content.Intent;
import android.graphics.Color;
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

public class AddTryoutFragment extends Fragment implements AddTryoutContract.View {

    AddTryoutContract.Presenter mPres;

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
//
    @BindView(R.id.editText_webUrl)
    EditText mWebLink;

    @BindView(R.id.checkbox_premium)
    CheckBox mPremium;
    @BindView(R.id.checkbox_pilihanEditor)
    CheckBox mPilihanEditor;

    @BindView(R.id.cardView_upload)
    Button mUpload;

    List<Model> arrayList = new ArrayList<>();
    List<String> listDipilih = new ArrayList<>();
    Adapter adapter;
    Handler handler = new Handler();

    Query query;
    private KategoriAdapter mKategoriAdapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mKategoriRef = firebaseFirestore.collection("KATEGORI");
    private LinearLayoutManager mLayoutManager;
    private String halaman;

    Uri mImageThumbnailUri;

    private static final int PICK_THUMBNAIL_IMAGE_REQUEST = 1;

    public AddTryoutFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_tryout, container, false);

        ButterKnife.bind(this, view);
        mPres = new AddTryoutPresenter(this);

        adapter = new Adapter(arrayList);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvKategoriDipilih.setLayoutManager(mLayoutManager);
        mRvKategoriDipilih.setItemAnimator(new DefaultItemAnimator());
        mRvKategoriDipilih.setAdapter(adapter);

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


        showKategori(mKategoriRef.whereEqualTo("nHalaman", "MATERI"));
        home();

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_THUMBNAIL_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_THUMBNAIL_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageThumbnailUri = data.getData();
            mEdtThumbnail.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImageThumbnailUri).into(mThumbnail);
        }
    }

    private void showKategori(Query query) {
//        /*
//        menampilkan list kategori materi secara horizontal
//         */

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

                arrayList.add(new Model(kategori));
                listDipilih.add(kategori);
                adapter.notifyDataSetChanged();

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
        mKategoriAdapter.stopListening();
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
        openFileChooser();
    }

    @OnClick(R.id.button_reset_thumbnail)
    public void imageThumbnailReset() {
        mEdtThumbnail.setVisibility(View.VISIBLE);
        mEdtThumbnail.setText("");
        mThumbnail.setVisibility(View.VISIBLE);
        mImageThumbnailUri = null;
        mThumbnail.setImageResource(R.drawable.image_placeholder);
    }

    @OnClick(R.id.cardView_upload)
    public void upload(){
        if (!listDipilih.isEmpty()){

            if (!mEdtTitle.getText().toString().isEmpty()){

                if (!mEdtThumbnail.getText().toString().isEmpty() || mImageThumbnailUri != null){

                    if (!mWebLink.getText().toString().isEmpty()){

                        mUpload.setEnabled(false);

                        mPres.post(halaman,
                                listDipilih,
                                mEdtTitle.getText().toString(),
                                mEdtThumbnail.getText().toString(),
                                mImageThumbnailUri,
                                "Tryout",
                                "https://firebasestorage.googleapis.com/v0/b/skdskb-8a08b.appspot.com/o/ICONS%2Fic_tryout.png?alt=media&token=e0d493d8-f0bf-4dd4-9d71-90836bc56b7c",
                                mWebLink.getText().toString(),
                                mPremium.isChecked(),
                                mPilihanEditor.isChecked());
                    } else {
                        showError("Web link belum diisi");
                    }
                } else {
                    showError("Thumbnail belum dipilih");
                }
            } else {
                showError("Judul belum diisi");
            }
        } else {
            showError("Kategori belum dipilih");
        }

    }

    public void resetAll(){
        home();
        arrayList.clear();
        listDipilih.clear();
        mEdtTitle.setText("");
        imageThumbnailReset();
        mWebLink.setText("");
        mPremium.setChecked(false);
        mPilihanEditor.setChecked(false);
    }


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
        resetAll();

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