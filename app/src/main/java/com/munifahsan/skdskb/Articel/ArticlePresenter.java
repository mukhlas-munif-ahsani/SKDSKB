package com.munifahsan.skdskb.Articel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticlePresenter implements ArticleContract.Presenter, ArticleContract.Listener {

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    private ArticleContract.View mArticleView;
    private ArticleRepository mArticleRepo;


    public ArticlePresenter(ArticleContract.View mArticleView) {
        this.mArticleView = mArticleView;
        mArticleRepo = new ArticleRepository(this);
    }


    @Override
    public void getData(String id) {
        mArticleRepo.getData(id);
        mArticleView.hideJenis();
        mArticleView.hideContent();
    }

    @Override
    public void addBookmark(String postId, String uid) {
        mArticleRepo.addBookmark(postId, uid);
    }

    @Override
    public void addSeen(String postId, String uid) {
        mArticleRepo.addSeen(postId, uid);
    }

    @Override
    public void addFavo(String postId, String uid) {
        mArticleRepo.addFavo(postId, uid);
    }

    @Override
    public void getBookmark(String postId, String uid) {
        mArticleRepo.getBookmark(postId, uid);
    }

    @Override
    public void getSeen(String postId, String uid) {
        mArticleRepo.getSeen(postId, uid);
    }

    @Override
    public void getFavo(String postId, String uid) {
        mArticleRepo.getFavo(postId, uid);
    }

    @Override
    public void getBookmarkListener(List<String> uid) {
        mArticleView.setBookmark(uid);
    }

    @Override
    public void getSeenListener(List<String> uid) {
        mArticleView.setSeen(uid);
    }

    @Override
    public void getFavoListener(List<String> uid) {
        mArticleView.setFavo(uid);
    }

    @Override
    public void getDataSuccess(String jenis, Date time, String title, String imageurl, String imageHeaderUrl,
                               String par1, String par2, String par3, String par4, String par5,
                               String par6, String par7, String par8, String par9, String par10,
                               String par11, String par12, String par13, String par14, String par15) {
        String paragraf1, paragraf2, paragraf3, paragraf4, paragraf5,
                paragraf6, paragraf7, paragraf8, paragraf9, paragraf10,
                paragraf11, paragraf12, paragraf13, paragraf14, paragraf15;

        if (isUrl(par1)) {
            paragraf1 = "<img src=\"" + par1 + "\"><br/><br/>";
        } else {
            paragraf1 = "" + par1 + "<br/><br/>";
        }

        if (isUrl(par2)) {
            paragraf2 = "<img src=\"" + par2 + "\"><br/><br/>";
        } else {
            paragraf2 = "" + par2 + "<br/><br/>";
        }

        if (isUrl(par3)) {
            paragraf3 = "<img src=\"" + par3 + "\"><br/><br/>";
        } else {
            paragraf3 = "" + par3 + "<br/><br/>";
        }

        if (isUrl(par4)) {
            paragraf4 = "<img src=\"" + par4 + "\"><br/><br/>";
        } else {
            paragraf4 = "" + par4 + "<br/><br/>";
        }

        if (isUrl(par5)) {
            paragraf5 = "<img src=\"" + par5 + "\"><br/><br/>";
        } else {
            paragraf5 = "" + par5 + "<br/><br/>";
        }

        mArticleView.setJenis(jenis);
        mArticleView.setTime(getTimeDate(time));
        mArticleView.setTitle(title);
        mArticleView.setContent(paragraf1 + paragraf2 + paragraf3 + paragraf4 + paragraf5);
        mArticleView.setImage(imageHeaderUrl);

    }

    public static String getTimeDate(Date timestamp) {
        try {
            //Date netDate = (timestamp);
            SimpleDateFormat sfd = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return sfd.format(timestamp);
        } catch (Exception e) {
            return "date";
        }
    }

    public boolean isUrl(String string) {
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(string);//replace with string to compare
        //System.out.println("String contains URL");
        return m.find();
    }
}
