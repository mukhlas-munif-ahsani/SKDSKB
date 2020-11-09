package com.munifahsan.skdskb.TryoutPage.presenter;

import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.TryoutPage.TryoutEvent;
import com.munifahsan.skdskb.TryoutPage.repository.TryoutRepo;
import com.munifahsan.skdskb.TryoutPage.repository.TryoutRepoInt;
import com.munifahsan.skdskb.TryoutPage.view.TryoutViewInt;

import org.greenrobot.eventbus.Subscribe;

import static com.munifahsan.skdskb.TryoutPage.TryoutEvent.onGetDataError;
import static com.munifahsan.skdskb.TryoutPage.TryoutEvent.onGetDataSuccess;

public class TryoutPres implements TryoutPresInt {
    TryoutRepoInt mTryoutRepo;
    TryoutViewInt mTryoutView;
    EventBus mEventBus;

    public TryoutPres(TryoutViewInt mTryoutView) {
        this.mTryoutView = mTryoutView;
        mTryoutRepo = new TryoutRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mTryoutView = null;
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onEventMainThread(TryoutEvent event){
        switch (event.getEventType()){
            case onGetDataSuccess:
                onSuccess(event.getQuote(), event.getTalker());
                break;
            case onGetDataError:
                onError(event.getErrorMessage());
        }
    }

    @Override
    public void getData(){
        mTryoutRepo.getData();
        mTryoutView.hideQuote();
    }

    private void onError(String errorMessage) {
        mTryoutView.showMessage(errorMessage);
    }

    private void onSuccess(String quote, String talker) {
        mTryoutView.setQuote(quote, talker);
    }
}
