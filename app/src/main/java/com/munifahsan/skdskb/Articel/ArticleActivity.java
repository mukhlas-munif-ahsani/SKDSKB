package com.munifahsan.skdskb.Articel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.munifahsan.skdskb.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleActivity extends AppCompatActivity implements Html.ImageGetter {
    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    private final static String TAG = "TestImageGetter";
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);


        String source = "this is a test of <b>ImageGetter</b> it contains " +
                "two images: <br/>" +
                "<img src=\"https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png\"><br/>and<br/>" +
                "<img src=\"https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png\"><br/><br/>" +
                "<img src=\"\">";
        String imgs = "<p><img alt=\"\" src=\"https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png\" " +
                "style=\"height:1000px; width:1000px\" />Test Article, Test Article, Test Article, Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,v</p>";
        String src = "<p><img alt=\"\" src=\"http://stylonica.com/wp-content/uploads/2014/02/Beauty-of-nature-random-4884759-1280-800.jpg\" />Test Attractions Test Attractions Test Attractions Test Attractions</p>";
        String img = "<p><img alt=\"\" src=\"/site_media/photos/gallery/75b3fb14-3be6-4d14-88fd-1b9d979e716f.jpg\" style=\"height:508px; width:640px\" />Test Article, Test Article, Test Article, Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,Test Article,v</p>";
        Spanned spanned = Html.fromHtml(source + imgs, this, null);
        mTv = (TextView) findViewById(R.id.coba);
        mTv.setText(spanned);


        if (isUrl("https://firebasestorage.googleapis.com/v0/b/skdskb-8a08b.appspot.com/o/ICONS%2Fic_artikel.png?alt=media&token=9afb8d13-355a-4a17-a7dd-e71aab6e97b9")){
            showMessage("ini url");
        } else {
            showMessage("bukan url");
        }
    }

    @Override
    public Drawable getDrawable(String s) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.ic_launcher_foreground);
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
                CharSequence t = mTv.getText();
                mTv.setText(t);
            }
        }
    }

    public boolean isUrl(String string) {

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(string);//replace with string to compare
        //System.out.println("String contains URL");
        return m.find();

    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}