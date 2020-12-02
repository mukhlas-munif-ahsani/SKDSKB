package com.munifahsan.skdskb.AddKategori;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.munifahsan.skdskb.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AddKategoriFragment extends Fragment implements AddKategoriContract.View {

    private String halaman;


    @BindView(R.id.button_home)
    Button mHalHome;
    @BindView(R.id.button_informasi)
    Button mHalInformasi;
    @BindView(R.id.button_tryout)
    Button mHalTryout;

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

    @BindView(R.id.button_upload)
    Button mUpload;

    @BindView(R.id.editText_title)
    EditText mTitle;
    @BindView(R.id.editText_kategori)
    EditText mKategori;
    @BindView(R.id.editText_description)
    EditText mDesc;
    @BindView(R.id.editText_imageHeader)
    EditText mEdtImageHeader;
    @BindView(R.id.imageView_imageHeader)
    ImageView mImageHeader;
    @BindView(R.id.editText_font)
    EditText mFont;

    Uri mImageHeaderUri;
    Handler handler = new Handler();

    private static final int PICK_HEADER_IMAGE_REQUEST = 1;

    AddKategoriContract.Presenter mPres;

    public AddKategoriFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_kategori, container, false);

        ButterKnife.bind(this, view);
        mPres = new AddKategoriPresenter(this);

        mEdtImageHeader.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() != 0) {
                    mImageHeader.setVisibility(View.GONE);
                } else {
                    mImageHeader.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        home();

        return view;
    }

    @OnClick(R.id.button_home)
    public void home() {

        halaman = "MATERI";

        mHalHome.setBackgroundColor(Color.parseColor("#0E9BF5"));
        mHalHome.setTextColor(Color.parseColor("#FFFFFF"));
        mHalInformasi.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalInformasi.setTextColor(Color.parseColor("#2b2b2b"));
        mHalTryout.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalTryout.setTextColor(Color.parseColor("#2b2b2b"));
    }

    @OnClick(R.id.button_informasi)
    public void informasi() {

        halaman = "INFORMASI";

        mHalHome.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalHome.setTextColor(Color.parseColor("#2b2b2b"));
        mHalInformasi.setBackgroundColor(Color.parseColor("#0E9BF5"));
        mHalInformasi.setTextColor(Color.parseColor("#FFFFFF"));
        mHalTryout.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalTryout.setTextColor(Color.parseColor("#2b2b2b"));
    }

    @OnClick(R.id.button_tryout)
    public void tryout() {

        halaman = "TRYOUT";

        mHalHome.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalHome.setTextColor(Color.parseColor("#2b2b2b"));
        mHalInformasi.setBackgroundColor(Color.parseColor("#B8CCDC"));
        mHalInformasi.setTextColor(Color.parseColor("#2b2b2b"));
        mHalTryout.setBackgroundColor(Color.parseColor("#0E9BF5"));
        mHalTryout.setTextColor(Color.parseColor("#FFFFFF"));
    }

    @OnClick(R.id.imageView_imageHeader)
    public void imageClick() {
        openFileChooser();
    }

    @OnClick(R.id.button_reset_image)
    public void reset() {
        mEdtImageHeader.setVisibility(View.VISIBLE);
        mEdtImageHeader.setText("");
        mImageHeader.setVisibility(View.VISIBLE);
        mImageHeaderUri = null;
        mImageHeader.setImageResource(R.drawable.image_placeholder);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_HEADER_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_HEADER_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageHeaderUri = data.getData();
            mEdtImageHeader.setVisibility(View.GONE);
            //hideTextPhoto();
            Glide.with(this).load(mImageHeaderUri).into(mImageHeader);
        }

    }

    @OnClick(R.id.button_upload)
    public void upload() {
        if (!mTitle.getText().toString().isEmpty()) {

            if (!mKategori.getText().toString().isEmpty()) {

                if (!mEdtImageHeader.getText().toString().isEmpty() || mImageHeaderUri != null) {

                    if (!mDesc.getText().toString().isEmpty()) {
                        mUpload.setEnabled(false);

                        mPres.upData(mTitle.getText().toString(),
                                halaman,
                                mKategori.getText().toString(),
                                mDesc.getText().toString(),
                                mEdtImageHeader.getText().toString(),
                                mImageHeaderUri,
                                Integer.parseInt(mFont.getText().toString())
                        );
                    } else {
                        showError("Deskripsi kategori belum dimasukan !!");
                    }
                } else {
                    showError("Gambar thumbnail harus diisi !!");
                }
            } else {
                showError("Kategori harus diisi !!");
            }
        } else {
            showError("Title harus diisi !!");
        }
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

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardSuccess.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void shoMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}