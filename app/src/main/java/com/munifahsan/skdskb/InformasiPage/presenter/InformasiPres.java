package com.munifahsan.skdskb.InformasiPage.presenter;

import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.InformasiPage.InformasiEvent;
import com.munifahsan.skdskb.InformasiPage.repository.InformasiRepo;
import com.munifahsan.skdskb.InformasiPage.repository.InformasiRepoInt;
import com.munifahsan.skdskb.InformasiPage.view.InformasiViewInt;

import org.greenrobot.eventbus.Subscribe;

import static com.munifahsan.skdskb.InformasiPage.InformasiEvent.onGetDataError;
import static com.munifahsan.skdskb.InformasiPage.InformasiEvent.onGetDataSuccess;

public class InformasiPres implements InformasiPresInt{
    InformasiViewInt mInformasiView;
    InformasiRepoInt mInformasiRepo;
    EventBus mEventBus;

    public InformasiPres(InformasiViewInt mInformasiView) {
        this.mInformasiView = mInformasiView;
        mInformasiRepo = new InformasiRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mInformasiView = null;
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onEventMainThread(InformasiEvent event){
        switch (event.getEventType()){
            case onGetDataSuccess:
                getDataSuccess();
                break;
            case onGetDataError:
                getDataError(event.getErrorMessage());
                break;
        }
    }

    private void getDataError(String errorMessage) {
    }

    private void getDataSuccess() {
    }

    @Override
    public void getData(){

    }
}
