package com.munifahsan.skdskb.HomePage.presenter;

import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.HomePage.HomeEvent;
import com.munifahsan.skdskb.HomePage.repository.HomeRepo;
import com.munifahsan.skdskb.HomePage.repository.HomeRepoInt;
import com.munifahsan.skdskb.HomePage.view.HomeViewInt;

import org.greenrobot.eventbus.Subscribe;

import static com.munifahsan.skdskb.HomePage.HomeEvent.onError;
import static com.munifahsan.skdskb.HomePage.HomeEvent.onGetNamaImageSuccess;
import static com.munifahsan.skdskb.HomePage.HomeEvent.onSuccess;

public class HomePres implements HomePresInt {
    HomeRepoInt mHomeRepo;
    HomeViewInt mHomeVIew;
    EventBus mEventBus;

    public HomePres(HomeViewInt mHomeVIew) {
        this.mHomeRepo = new HomeRepo();
        this.mHomeVIew = mHomeVIew;
        this.mEventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void onCreate() {
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mHomeVIew = null;
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onEventMainThread(HomeEvent event) {
        switch (event.getEventType()) {
            case onSuccess:
                onSuccess(event.getGreeting(), event.getQuote(), event.getTalker());
                break;
            case onGetNamaImageSuccess:
                onGetNamaImageSuccess(event.getNama(), event.getImageUrl());
                break;
            case onError:
                onError(event.getErrorMessage());
                break;
        }
    }

    private void onGetNamaImageSuccess(String nama, String imageUrl) {
        mHomeVIew.setmNama(nama);
        mHomeVIew.setmPhoto(imageUrl);
        //mHomeVIew.showMessage(nama);
    }

    private void onError(String error) {
        mHomeVIew.showMessage(error);
    }

    private void onSuccess(String greeting, String quote, String talker) {
        mHomeVIew.setmGreeting(greeting);
        mHomeVIew.setmQuote(quote);
        mHomeVIew.setTalker(talker);
    }

    @Override
    public void getData() {
        mHomeVIew.hideTalker();
        mHomeVIew.hideQuote();
//        mHomeVIew.hideNama();
//        mHomeVIew.hidePhoto();
        mHomeVIew.hideGreeting();
        mHomeRepo.getData();
    }

    @Override
    public void getUserData(String id) {
        if (id != null) {
            mHomeRepo.getUserData(id);
            mHomeVIew.showMessage("not null");
        } else {
            mHomeRepo.getFakeNamaProfile();
            //mHomeVIew.showMessage("null");
        }
    }
}
