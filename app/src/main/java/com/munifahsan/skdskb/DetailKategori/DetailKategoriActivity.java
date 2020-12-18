package com.munifahsan.skdskb.DetailKategori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.skdskb.Adapters.SearchAdapter;
import com.munifahsan.skdskb.Articel.ArticleActivity;
import com.munifahsan.skdskb.Ebook.EbookActivity;
import com.munifahsan.skdskb.Models.MateriListModel;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.Tryout.TryoutActivity;
import com.munifahsan.skdskb.Video.VideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailKategoriActivity extends AppCompatActivity implements DetailKategoriContract.View {

    @BindView(R.id.editText_search_detailKategori)
    EditText mSearchField;
    @BindView(R.id.recyclerView_list_detailKategori)
    RecyclerView mRvList;
    @BindView(R.id.shimmer_listOne_detailKategori)
    ShimmerFrameLayout mShimmerList;
    @BindView(R.id.imgeView_image_detailKategori)
    ImageView mImage;
    @BindView(R.id.shimmer_image_detailKategori)
    ShimmerFrameLayout mShimmerImage;
    @BindView(R.id.textView_title_detailKategori)
    TextView mTitle;
    @BindView(R.id.shimmer_title_detailKategori)
    ShimmerFrameLayout mShimmerTitle;
    @BindView(R.id.textView_desc_detailKategori)
    TextView mDesc;
    @BindView(R.id.shimmer_desc_detailKategori)
    ShimmerFrameLayout mShimmerDesc;
    @BindView(R.id.bottom_sheet_search_filter)
    FrameLayout mBottomSheetFilter;
    @BindView(R.id.imageView_closeKategori_filter)
    ImageView mCloseFilter;
    @BindView(R.id.button_filter_terapkan)
    Button mTerapkan;
    @BindView(R.id.button_filter_reset)
    Button mReset;
    @BindView(R.id.imageViewNoData_detailKategori)
    ImageView mNoData;

    @BindView(R.id.radio_button_default)
    RadioButton mRadioDefault;
    @BindView(R.id.radio_button_view1to0)
    RadioButton mRadioView1to0;
    @BindView(R.id.radio_button_view0to1)
    RadioButton mRadioView0to1;
    @BindView(R.id.radio_button_time0to1)
    RadioButton mRadioTime0to1;
    @BindView(R.id.radio_button_time1to0)
    RadioButton mRadioTime1to0;

    String document_id;
    String kategori;
    String collection;
    String tipe;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mListRef = firebaseFirestore.collection("POST");
    private BottomSheetBehavior mBottomSheetBehavior;

    DetailKategoriContract.Presenter mDetailkategoriPres;
    SearchAdapter mAdapter;
    Query query;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kategori);

        ButterKnife.bind(this);
        mDetailkategoriPres = new DetailKategoriPresenter(this);

        Intent intent = getIntent();
        document_id = intent.getStringExtra("DOCUMENT_ID");
        kategori = intent.getStringExtra("KATEGORI");
        collection = intent.getStringExtra("COLLECTION");
        tipe = intent.getStringExtra("TIPE");

        mDetailkategoriPres.getData(collection, document_id);

        //showMessage(document_id + kategori + collection);

        bottomSheetFilter();
//        showList(mListRef.whereArrayContains("nKategori", kategori).orderBy("nUploadTime", Query.Direction.DESCENDING));

        showList(mListRef.whereArrayContains("nKategori", kategori).whereEqualTo("nTipe", tipe)
                .orderBy("nUploadTime", Query.Direction.DESCENDING));


        /*
        Change status bar color
         */
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.bgPageHeader));

    }

    private void bottomSheetFilter() {
        /*
        bottom sheet behaviour
         */
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetFilter);
        mBottomSheetBehavior.setDraggable(false);
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mCloseFilter.setRotation(slideOffset * 180);
            }
        });
    }

    public void showList(Query queryList) {

        mRvList.setVisibility(View.GONE);
        mShimmerList.setVisibility(View.VISIBLE);
        mNoData.setVisibility(View.GONE);

        mAdapter = new SearchAdapter();

        mLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        queryList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                mRvList.setVisibility(View.VISIBLE);
                mShimmerList.setVisibility(View.GONE);
                mNoData.setVisibility(View.GONE);

                mAdapter.setListModels(task.getResult().toObjects(MateriListModel.class));
                mRvList.setLayoutManager(mLayoutManager);
                mRvList.setAdapter(mAdapter);
                mRvList.setNestedScrollingEnabled(false);

                mAdapter.getFilter().filter(mSearchField.getText().toString());
            }
        });

//        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_SEARCH) {
//
//
//
//                    //performSearch();
//                    return true;
//                }
//                return false;
//            }
//        });

        mAdapter.notifyDataSetChanged();

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //showList(mListRef.whereArrayContains("nKategori", kategori).orderBy("nTitle").startAt(charSequence.toString().trim()));

                mAdapter.getFilter().filter(charSequence.toString());

                if (mAdapter.getItemCount() == 0) {

                    mRvList.setVisibility(View.GONE);
                    mShimmerList.setVisibility(View.GONE);
                    mNoData.setVisibility(View.VISIBLE);

                    showMessage("data kosong");
                } else {

                    mRvList.setVisibility(View.VISIBLE);
                    mShimmerList.setVisibility(View.GONE);
                    mNoData.setVisibility(View.GONE);
                }

                showMessage(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAdapter.setOnListItemCliked(new SearchAdapter.OnListItemCliked() {
            @Override
            public void onItemCliked(String id, int position, String jenis, boolean isPremium) {
                switch (jenis) {
                    case "Artikel":
                        sendToArtikel(id, isPremium);
                        break;
                    case "Video":
                        sendToVideo(id, isPremium);
                        break;
                    case "Ebook":
                        sendToEbook(id, isPremium);
                        break;
                    case "Tryout":
                        sendToTryout(id, isPremium);
                        break;
                }
            }
        });
    }

    private void sendToArtikel(String id, boolean isPremium) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToVideo(String id, boolean isPremium) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToEbook(String id, boolean isPremium) {
        Intent intent = new Intent(this, EbookActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToTryout(String id, boolean isPremium) {
        Intent intent = new Intent(this, TryoutActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  mAdapter.stopListening();
    }

    private void performSearch() {
        mRvList.setVisibility(View.GONE);
        mShimmerList.setVisibility(View.VISIBLE);
        mNoData.setVisibility(View.GONE);


        //showMessage(String.valueOf(mAdapter.getItemCount()));
        //mAdapter2.getFilter().filter(mSearchField.getText().toString().trim());
//        if (!mSearchField.getText().toString().isEmpty()) {
//            showMessage(mSearchField.getText().toString());
//
//        } else {
//            showMessage("kooosong");
//        }
//
//        String s = mSearchField.getText().toString();
//

    }

    @OnClick(R.id.rel_back_detailKategori)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.rel_filter_detailKategori)
    public void filterOnClick() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @OnClick(R.id.imageView_closeKategori_filter)
    public void closeFilterClick() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.button_filter_terapkan)
    public void terapkanOnClick() {
        if (mRadioDefault.isChecked()) {
            showList(mListRef.whereArrayContains("nKategori", kategori)
                    .whereEqualTo("nTipe", tipe).orderBy("nUploadTime", Query.Direction.DESCENDING));
        }

        if (mRadioTime0to1.isChecked()) {
            showList(mListRef.whereArrayContains("nKategori", kategori)
                    .whereEqualTo("nTipe", tipe).orderBy("nUploadTime", Query.Direction.ASCENDING));
        }

        if (mRadioTime1to0.isChecked()) {
            showList(mListRef.whereArrayContains("nKategori", kategori)
                    .whereEqualTo("nTipe", tipe).orderBy("nUploadTime", Query.Direction.DESCENDING));
        }

        if (mRadioView0to1.isChecked()) {
            showList(mListRef.whereArrayContains("nKategori", kategori)
                    .whereEqualTo("nTipe", tipe).orderBy("nUploadTime", Query.Direction.ASCENDING));
        }

        if (mRadioView1to0.isChecked()) {
            showList(mListRef.whereArrayContains("nKategori", kategori)
                    .whereEqualTo("nTipe", tipe).orderBy("nUploadTime", Query.Direction.DESCENDING));
        }
    }

    @OnClick(R.id.button_filter_reset)
    public void resetOnClick() {
        mRadioDefault.setChecked(true);
    }

    @Override
    public void setImage(String image) {
        mShimmerImage.setVisibility(View.INVISIBLE);
        mImage.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(mImage);
    }

    @Override
    public void hideImage() {
        mShimmerImage.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setVisibility(View.VISIBLE);
        mShimmerTitle.setVisibility(View.INVISIBLE);
        mTitle.setText(title);
    }

    @Override
    public void hideTitle() {
        mTitle.setVisibility(View.INVISIBLE);
        mShimmerTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDesc(String desc) {
        mDesc.setVisibility(View.VISIBLE);
        mDesc.setText(desc);
        mShimmerDesc.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideDesc() {
        mDesc.setVisibility(View.INVISIBLE);
        mShimmerDesc.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}