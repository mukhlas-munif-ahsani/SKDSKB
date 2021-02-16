package com.munifahsan.skdskb.Tryout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TryoutPresenter implements TryoutContract.Presenter, TryoutContract.Listener {

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    TryoutContract.View mView;
    TryoutContract.Repository mRepo;

    public TryoutPresenter(TryoutContract.View mView) {
        this.mView = mView;
        mRepo = new TryoutRepository(this);
    }

    @Override
    public void getData(String id){
        mRepo.getData(id);
        mView.hideJenis();
        mView.hideContent();
    }

    @Override
    public void addBookmark(String postId, String uid) {
        mRepo.addBookmark(postId, uid);
    }

    @Override
    public void addSeen(String postId, String uid){
        mRepo.addSeen(postId, uid);
    }

    @Override
    public void addFavo(String postId, String uid){
        mRepo.addFavo(postId, uid);
    }

    @Override
    public void getBookmark(String postId, String uid){
        mRepo.getBookmark(postId, uid);
    }

    @Override
    public void getSeen(String postId, String uid){
        mRepo.getSeen(postId, uid);
    }

    @Override
    public void getFavo(String postId, String uid){
        mRepo.getFavo(postId, uid);
    }

    @Override
    public void getBookmarkListener(List<String> uid){
        mView.setBookmark(uid);
    }

    @Override
    public void getSeenListener(List<String> uid){
        mView.setSeen(uid);
    }

    @Override
    public void getFavoListener(List<String> uid){
        mView.setFavo(uid);
    }

    @Override
    public void getDataSuccess(String jenis, Date time, String link, String title) {

        mView.setJenis(jenis);
        mView.setTime(getTimeDate(time));
        mView.setWebView(link);
        mView.setTitle(title);

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
