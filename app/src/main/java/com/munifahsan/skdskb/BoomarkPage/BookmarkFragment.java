package com.munifahsan.skdskb.BoomarkPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.munifahsan.skdskb.Adapters.SearchAdapter;
import com.munifahsan.skdskb.Articel.ArticleActivity;
import com.munifahsan.skdskb.Adapters.ListOneAdapter;
import com.munifahsan.skdskb.Ebook.EbookActivity;
import com.munifahsan.skdskb.HomePage.model.MateriModel;
import com.munifahsan.skdskb.Models.MateriListModel;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.Tryout.TryoutActivity;
import com.munifahsan.skdskb.Video.VideoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkFragment extends Fragment {

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @BindView(R.id.recyclerView_list_bookmark)
    RecyclerView mRvBookmark;
    @BindView(R.id.editText_search_bookmark)
    EditText mSearchField;
    @BindView(R.id.shimmer_listOne_bookmark)
    ShimmerFrameLayout mShimmerList;

    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    Query query;
    FirebaseUser mCurrentUser;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mListRef = firebaseFirestore.collection("POST");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        ButterKnife.bind(this, view);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrentUser != null){
            showList(mListRef.whereArrayContains("nBookmark", mCurrentUser.getUid()));
        }

        return view;
    }

//    private void showList() {
//         /*
//        menampilkan list materi pilihan editor
//         */
//        mRvBookmark.setVisibility(View.GONE);
//        mShimmerList.setVisibility(View.VISIBLE);
//
//        query = mListRef.whereArrayContains("nBookmark", mCurrentUser.getUid());
//
//        FirestoreRecyclerOptions<MateriModel> options = new FirestoreRecyclerOptions.Builder<MateriModel>()
//                .setQuery(query, MateriModel.class)
//                .build();
//        mListOneAdapter = new SearchAdapter();
//
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (!task.getResult().isEmpty()) {
//                    mRvBookmark.setVisibility(View.VISIBLE);
//                    mShimmerList.setVisibility(View.GONE);
////                    mShimmer.setVisibility(View.INVISIBLE);
//                } else {
//                    //mRvBeasiswaList.setVisibility(View.INVISIBLE);
////                    mRvHottestEvent.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        mLayoutManager = new LinearLayoutManager(getActivity()) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//        mRvBookmark.setLayoutManager(mLayoutManager);
//        mRvBookmark.setAdapter(mListOneAdapter);
//        mRvBookmark.setNestedScrollingEnabled(false);
//
//        mListOneAdapter.setOnItemClickListener(new ListOneAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String id, int position, String jenis, boolean isPremium) {
//                Intent intent = new Intent(getActivity(), ArticleActivity.class);
//                intent.putExtra("DOCUMENT_ID", id);
//                startActivity(intent);
//
//                switch (jenis){
//                    case "Artikel":
//                        sendToArtikel(isPremium);
//                        break;
//                    case "Video":
//                        sendToVideo(isPremium);
//                        break;
//                    case "Ebook":
//                        sendToEbook(isPremium);
//                        break;
//                    case "Tryout":
//                        sendToTryout(isPremium);
//                        break;
//                }
//
////                showMessage("clicked");
//            }
//        });
//    }

    public void showList(Query queryList) {

        mRvBookmark.setVisibility(View.GONE);
        mShimmerList.setVisibility(View.VISIBLE);
        //mNoData.setVisibility(View.GONE);

        mAdapter = new SearchAdapter();

        mLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        queryList.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                mRvBookmark.setVisibility(View.VISIBLE);
                mShimmerList.setVisibility(View.GONE);
                //mNoData.setVisibility(View.GONE);

                mAdapter.setListModels(task.getResult().toObjects(MateriListModel.class));
                mRvBookmark.setLayoutManager(mLayoutManager);
                mRvBookmark.setAdapter(mAdapter);
                mRvBookmark.setNestedScrollingEnabled(false);

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

                    mRvBookmark.setVisibility(View.GONE);
                    mShimmerList.setVisibility(View.GONE);
                    //mNoData.setVisibility(View.VISIBLE);

                    //showMessage("data kosong");
                } else {

                    mRvBookmark.setVisibility(View.VISIBLE);
                    mShimmerList.setVisibility(View.GONE);
                    //mNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //showList(mListRef.whereArrayContains("nKategori", kategori).orderBy("nTitle").startAt(charSequence.toString().trim()));

                mAdapter.getFilter().filter(charSequence.toString());

                if (mAdapter.getItemCount() == 0) {

                    mRvBookmark.setVisibility(View.GONE);
                    mShimmerList.setVisibility(View.GONE);
                    //mNoData.setVisibility(View.VISIBLE);

                    //showMessage("data kosong");
                } else {

                    mRvBookmark.setVisibility(View.VISIBLE);
                    mShimmerList.setVisibility(View.GONE);
                    // mNoData.setVisibility(View.GONE);
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
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToVideo(String id, boolean isPremium) {
        Intent intent = new Intent(getActivity(), VideoActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToEbook(String id, boolean isPremium) {
        Intent intent = new Intent(getActivity(), EbookActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    private void sendToTryout(String id, boolean isPremium) {
        Intent intent = new Intent(getActivity(), TryoutActivity.class);
        intent.putExtra("DOCUMENT_ID", id);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // mListOneAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mListOneAdapter.stopListening();
    }

    private void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }


}