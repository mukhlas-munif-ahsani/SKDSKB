package com.munifahsan.skdskb.Articel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munifahsan.skdskb.R;
import com.munifahsan.skdskb.SignIn.SignInActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleActivity extends AppCompatActivity implements Html.ImageGetter, ArticleContract.View {
    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    private final static String TAG = "TestImageGetter";
    TextView mTv;

    @BindView(R.id.imageView_bookmark_artikel)
    ImageView mBookmark;
    @BindView(R.id.textView_jenis_artikel)
    TextView mJenis;
    @BindView(R.id.textView_time_artikel)
    TextView mTime;
    @BindView(R.id.textView_title_artikel)
    TextView mTitle;
    @BindView(R.id.shimmer_content_artikel)
    ShimmerFrameLayout mContentShimmer;
    @BindView(R.id.shimmer_title_artikel)
    ShimmerFrameLayout mJenisShimmer;
    @BindView(R.id.imageView_header_artikel)
    ImageView mImage;
    @BindView(R.id.textView_content_artikel)
    TextView mContent;

    @BindView(R.id.rel_bookmark_artikel)
    RelativeLayout mRelBookmark;
    @BindView(R.id.relative_seen)
    RelativeLayout mRelSeen;
    @BindView(R.id.textView_number)
    TextView mSeenNumber;
    @BindView(R.id.relative_favo)
    RelativeLayout mRelFavo;
    @BindView(R.id.imageView_favo)
    ImageView mImageFavo;
    @BindView(R.id.textView_number_favo)
    TextView mFavoNumber;
    @BindView(R.id.relative_font)
    RelativeLayout mRelFont;
    @BindView(R.id.relative_share)
    RelativeLayout mRelShare;
    @BindView(R.id.relative_textSizeSet)
    RelativeLayout mTextSizeSet;
    @BindView(R.id.textView_textSize)
    TextView mTextSize;
    int fontSize = 16;
    boolean isFavo = false;

    ArticleContract.Presenter mArtikelPres;

    FirebaseUser mCurrentUser;

    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;

    String paragraf1, paragraf2, paragraf3, paragraf4, paragraf5,
            paragraf6, paragraf7, paragraf8, paragraf9, paragraf10,
            paragraf11, paragraf12, paragraf13, paragraf14, paragraf15;

    String document_id;


    boolean isMarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        ButterKnife.bind(this);
        mArtikelPres = new ArticlePresenter(this);

        Intent intent = getIntent();
        document_id = intent.getStringExtra("DOCUMENT_ID");
        mArtikelPres.getData(document_id);

        if (mCurrentUser != null){
            mArtikelPres.getBookmark(document_id, mCurrentUser.getUid());
            mArtikelPres.getFavo(document_id, mCurrentUser.getUid());
        }

        String content = "" + paragraf1 + "";

        String source = "this is a test of <b>ImageGetter</b> it contains asjfdasfaskf asjlfasjdfa sd fasdjfas sajdfasfdj  " +
                "two images: <br/>" +
                "<img src=\"https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png\"><br/>and<br/>" +
                "<img src=\"https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png\"><br/><br/>" +
                "<img src=\"\">";
        String imgs = "<p><img alt=\"\" src=\"https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png\" " +
                "style=\"height:1000px; width:1000px\" />Test Article, Test Article, Test Article, Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,v</p>";
        String src = "<p><img alt=\"\" src=\"http://stylonica.com/wp-content/uploads/2014/02/Beauty-of-nature-random-4884759-1280-800.jpg\" />Test Attractions Test Attractions Test Attractions Test Attractions</p>";
        String img = "<p><img alt=\"\" src=\"/site_media/photos/gallery/75b3fb14-3be6-4d14-88fd-1b9d979e716f.jpg\" style=\"height:508px; width:640px\" />Test Article, Test Article, Test Article, Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,v</p>";

        //setContent(source);

        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        mRelBookmark.setEnabled(false);
        mRelShare.setEnabled(false);
        mRelFavo.setEnabled(false);
        mRelFont.setEnabled(false);
    }

    @Override
    public void setContent(String content) {
        Spanned spanned = Html.fromHtml(content, this, null);
        mContent.setText(spanned);

        mRelBookmark.setEnabled(true);
        mRelShare.setEnabled(true);
        mRelFavo.setEnabled(true);
        mRelFont.setEnabled(true);

        //default font size
        mContent.setTextSize(fontSize);
        mTextSize.setText(String.valueOf(fontSize));

        if (mCurrentUser != null){
            //add content seen
            mArtikelPres.addSeen(document_id, mCurrentUser.getUid());
        }

        mContent.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);
        mContentShimmer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideContent() {
        mContentShimmer.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.INVISIBLE);
        mTitle.setVisibility(View.INVISIBLE);
        mImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setJenis(String jenis) {
        mJenis.setText(jenis);
        mJenis.setVisibility(View.VISIBLE);
        mJenisShimmer.setVisibility(View.GONE);
    }

    @Override
    public void hideJenis() {
        mJenisShimmer.setVisibility(View.VISIBLE);
        mJenis.setVisibility(View.INVISIBLE);
        mTime.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTime(String time) {
        mTime.setText(time);
        mTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void setImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .into(mImage);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setBookmark(List<String> uids) {
        if (uids.contains(mCurrentUser.getUid())) {
            mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark));
            isMarked = true;
            //showMessage("Artikel tersimpan");
        } else {
            mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark_border));
            isMarked = false;
            //showMessage("Artikel ");
        }
    }

    @Override
    public void setSeen(List<String> uids) {
        mSeenNumber.setText(String.valueOf(uids.size()));
    }

    @Override
    public void setFavo(List<String> uids) {
        if (uids.contains(mCurrentUser.getUid())) {
            mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite));
            isFavo = true;
            //showMessage("Artikel tersimpan");
        } else {
            mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border));
            isFavo = false;
            //showMessage("Artikel ");
        }
        mFavoNumber.setText(String.valueOf(uids.size()));
    }

    @OnClick(R.id.rel_back_artikel)
    public void backOnClick() {
        finish();
    }

    @OnClick(R.id.rel_bookmark_artikel)
    public void bookmarkOnClick() {
        if (mCurrentUser != null) {
            if (isMarked) {
                mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark_border));
                isMarked = true;
            } else {
                mBookmark.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_bookmark));
                isMarked = false;
            }

            mArtikelPres.addBookmark(document_id, mCurrentUser.getUid());
//            showMessage("not border");
        } else {
            showDialogOnSignIn();
        }
    }

//    @OnClick(R.id.relative_seen)
//    public void seenOnClick(){
//
//    }

    @OnClick(R.id.relative_favo)
    public void favoOnClick() {
        if (mCurrentUser != null) {
            if (isFavo) {
                mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_border));
                isMarked = true;
            } else {
                mImageFavo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite));
                isMarked = false;
            }

            mArtikelPres.addFavo(document_id, mCurrentUser.getUid());
//            showMessage("not border");
        } else {
            showDialogOnSignIn();
        }
    }

    @OnClick(R.id.relative_font)
    public void fontOnClick() {
        if (mTextSizeSet.getVisibility() == View.GONE) {
            mTextSizeSet.setVisibility(View.VISIBLE);
        } else {
            mTextSizeSet.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.relative_textSizeSet2)
    public void relFont() {
        mTextSizeSet.setVisibility(View.GONE);
    }

    @OnClick(R.id.imageView_fontSize_close)
    public void fontSizeCloseClick() {
        mTextSizeSet.setVisibility(View.GONE);
    }

    @OnClick(R.id.lin_default_textSize)
    public void fontSizeDefault(){
        mContent.setTextSize(16);
        mTextSize.setText(String.valueOf(16));
        fontSize = 16;
    }

    @OnClick(R.id.relative_share)
    public void shareOnClick() {

    }

    @OnClick(R.id.imageView_min)
    public void minTextSize() {
        if (fontSize > 12) {
            fontSize = fontSize - 2;
        }
        mContent.setTextSize(fontSize);
        mTextSize.setText(String.valueOf(fontSize));
    }

    @OnClick(R.id.imageView_plus)
    public void plusTextSize() {
        if (fontSize < 28) {
           fontSize = fontSize + 2;
        }
        mContent.setTextSize(fontSize);
        mTextSize.setText(String.valueOf(fontSize));
    }

    public void showDialogOnSignIn() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title dialog
        alertDialogBuilder.setTitle("Sign In Kedalam Aplikasi");
        alertDialogBuilder.setMessage("Untuk menyimpan anda harus melakukan Sign In");

        // set pesan dari dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        navigateToSignIn();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void navigateToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public Drawable getDrawable(String s) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.image_placeholder);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(s, d);

        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = mContent.getText();
                mContent.setText(t);
            }
        }
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}