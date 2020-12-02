package com.munifahsan.skdskb.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.munifahsan.skdskb.DetailKategori.DetailKategoriContract;
import com.munifahsan.skdskb.Ebook.EbookActivity;
import com.munifahsan.skdskb.Models.MateriListModel;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.Tryout.TryoutActivity;
import com.munifahsan.skdskb.Video.VideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.bottom_sheet_search_filter)
    FrameLayout mBottomSheetFilter;
    @BindView(R.id.imageView_closeKategori_filter)
    ImageView mCloseFilter;
    @BindView(R.id.rel_back_search)
    RelativeLayout mBack;
    @BindView(R.id.editText_search_search)
    EditText mSearchField;
    @BindView(R.id.rel_filter_search)
    RelativeLayout mFilter;
    @BindView(R.id.recyclerView_list_search)
    RecyclerView mRvList;
    @BindView(R.id.shimmer_listOne_search)
    ShimmerFrameLayout mShimmerList;
    @BindView(R.id.imageViewNoData_search)
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

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mListRef = firebaseFirestore.collection("POST");
    private BottomSheetBehavior mBottomSheetBehavior;

    DetailKategoriContract.Presenter mDetailkategoriPres;
    SearchAdapter mAdapter;
    Query query;
    private LinearLayoutManager mLayoutManager;

    String searchValue;
    String kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        searchValue = intent.getStringExtra("SEARCH_VALUE");
        kategori = intent.getStringExtra("KATEGORI");

        mSearchField.setText(searchValue);

        showList(mListRef.whereEqualTo("nTipe", kategori));
        bottomSheetFilter();
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
        Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToVideo(String id, boolean isPremium) {
        Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToEbook(String id, boolean isPremium) {
        Intent intent = new Intent(getApplicationContext(), EbookActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToTryout(String id, boolean isPremium) {
        Intent intent = new Intent(getApplicationContext(), TryoutActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }


    @OnClick(R.id.rel_back_search)
    public void backOnClick() {
        finish();
    }

    @OnClick(R.id.rel_filter_search)
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
            //mSearchField.setText(searchValue);
            showList(mListRef.orderBy("nUploadTime", Query.Direction.DESCENDING).whereEqualTo("nTipe", kategori));
        }

        if (mRadioTime0to1.isChecked()) {
            // mSearchField.setText(searchValue);
            showList(mListRef.orderBy("nUploadTime", Query.Direction.ASCENDING).whereEqualTo("nTipe", kategori));
        }

        if (mRadioTime1to0.isChecked()) {
            //  mSearchField.setText(searchValue);
            showList(mListRef.orderBy("nUploadTime", Query.Direction.DESCENDING).whereEqualTo("nTipe", kategori));
        }

        if (mRadioView0to1.isChecked()) {
            // mSearchField.setText(searchValue);
            showList(mListRef.orderBy("nUploadTime", Query.Direction.ASCENDING).whereEqualTo("nTipe", kategori));
        }

        if (mRadioView1to0.isChecked()) {
            // mSearchField.setText(searchValue);
            showList(mListRef.orderBy("nUploadTime", Query.Direction.DESCENDING).whereEqualTo("nTipe", kategori));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}