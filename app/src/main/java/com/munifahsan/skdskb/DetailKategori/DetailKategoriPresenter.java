package com.munifahsan.skdskb.DetailKategori;

public class DetailKategoriPresenter implements DetailKategoriContract.Presenter, DetailKategoriContract.Listener {
    DetailKategoriContract.View mDetailKategoriView;
    DetailKategoriRepository mDetailKategoriRepo;

    public DetailKategoriPresenter(DetailKategoriContract.View mDetailKategoriView) {
        this.mDetailKategoriView = mDetailKategoriView;
        mDetailKategoriRepo = new DetailKategoriRepository(this);
    }

    @Override
    public void getData(String collection, String id){
        mDetailKategoriView.hideImage();
        mDetailKategoriView.hideTitle();
        mDetailKategoriView.hideDesc();
        mDetailKategoriRepo.getData(collection, id);
    }

    @Override
    public void getDataSuccess(String imageUrl, String title, String desc){
        mDetailKategoriView.setImage(imageUrl);
        mDetailKategoriView.setTitle(title);
        mDetailKategoriView.setDesc(desc);
    }

    @Override
    public void getDataError(String errorMessage){

    }
}
