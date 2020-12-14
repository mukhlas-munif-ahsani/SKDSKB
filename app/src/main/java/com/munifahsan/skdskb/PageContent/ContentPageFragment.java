package com.munifahsan.skdskb.PageContent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.munifahsan.skdskb.R;

import butterknife.ButterKnife;

public class ContentPageFragment extends Fragment implements ContentPageContract.View{

    ContentPageContract.Presenter mPres;

    public ContentPageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_content_page, container, false);

        ButterKnife.bind(this, view);
        mPres = new ContentPagePresenter(this);

        return view;
    }
}