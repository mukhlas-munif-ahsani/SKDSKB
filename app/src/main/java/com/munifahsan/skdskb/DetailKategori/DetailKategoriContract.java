package com.munifahsan.skdskb.DetailKategori;

public interface DetailKategoriContract {
    interface View{

        void setImage(String image);

        void hideImage();

        void setTitle(String title);

        void hideTitle();

        void setDesc(String desc);

        void hideDesc();

        void showMessage(String message);
    }

    interface Presenter{

        void getData(String collection, String id);
    }

    interface Repository{

        void getData(String collection, String id);
    }

    interface Listener{

        void getDataSuccess(String imageUrl, String title, String desc);

        void getDataError(String errorMessage);
    }
}
