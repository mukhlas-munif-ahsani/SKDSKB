package com.munifahsan.skdskb.Tryout;

import java.util.Date;
import java.util.List;

public interface TryoutContract {
    interface View{

        void setWebView(String link);

        void hideContent();

        void setJenis(String jenis);

        void hideJenis();

        void setTime(String time);

        void setBookmark(List<String> uids);

        void setSeen(List<String> uids);

        void setFavo(List<String> uids);
    }

    interface Presenter{

        void getData(String id);

        void addBookmark(String postId, String uid);

        void addSeen(String postId, String uid);

        void addFavo(String postId, String uid);

        void getBookmark(String postId, String uid);

        void getSeen(String postId, String uid);

        void getFavo(String postId, String uid);
    }

    interface Repository{

        void getData(String id);

        void getSeen(String postId, String uid);

        void getFavo(String postId, String uid);

        void getBookmark(String postId, String uid);

        void addBookmark(String postId, String id);

        void addSeen(String postId, String id);

        void addFavo(String postId, String id);
    }

    interface Listener{

        void getBookmarkListener(List<String> uid);

        void getSeenListener(List<String> uid);

        void getFavoListener(List<String> uid);

        void getDataSuccess(String jenis, Date time, String link);
    }
}
