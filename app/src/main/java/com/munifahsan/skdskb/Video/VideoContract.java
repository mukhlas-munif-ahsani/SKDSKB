package com.munifahsan.skdskb.Video;

import java.util.Date;
import java.util.List;

public interface VideoContract {
    interface View {

        void setContent(String content);

        void hideContent();

        void setJenis(String jenis);

        void hideJenis();

        void setTime(String time);

        void setVideo(String imageUrl);

        void setTitle(String title);

        void setBookmark(List<String> uids);

        void setSeen(List<String> uids);

        void setFavo(List<String> uids);
    }

    interface Presenter {
        void getData(String id);

        void addBookmark(String postId, String uid);

        void addSeen(String postId, String uid);

        void addFavo(String postId, String uid);

        void getBookmark(String postId, String uid);

        void getSeen(String postId, String uid);

        void getFavo(String postId, String uid);
    }

    interface Repository {
        void getData(String id);

        void getSeen(String postId, String uid);

        void getFavo(String postId, String uid);

        void getBookmark(String postId, String uid);

        void addBookmark(String postId, String id);

        void addSeen(String postId, String id);

        void addFavo(String postId, String id);
    }

    interface Listener {
        void getBookmarkListener(List<String> uid);

        void getSeenListener(List<String> uid);

        void getFavoListener(List<String> uid);

        void getDataSuccess(String jenis, Date time, String title, String videoUrl,
                            String par1, String par2, String par3, String par4, String par5,
                            String par6, String par7, String par8, String par9, String par10,
                            String par11, String par12, String par13, String par14, String par15);
    }
}
