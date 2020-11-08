package com.munifahsan.skdskb.InformasiPage.repository;

import com.munifahsan.skdskb.EventBus.EventBus;
import com.munifahsan.skdskb.EventBus.GreenRobotEventBus;
import com.munifahsan.skdskb.InformasiPage.InformasiEvent;

public class InformasiRepo implements InformasiRepoInt {
    public InformasiRepo() {
    }

    @Override
    public void getData(){

    }

    public void postEvent(int type, String errorMessage) {
        InformasiEvent event = new InformasiEvent();

        event.setEventType(type);

        if (errorMessage != null) {
            event.setErrorMessage(errorMessage);
        }

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(event);
    }
}
