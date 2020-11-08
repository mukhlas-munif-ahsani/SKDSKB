package com.munifahsan.skdskb.InformasiPage;

public class InformasiEvent {
    public static final int onGetDataSuccess = 0;
    public static final int onGetDataError = 1;

    int eventType;
    String errorMessage;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
