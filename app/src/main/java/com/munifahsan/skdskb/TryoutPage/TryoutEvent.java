package com.munifahsan.skdskb.TryoutPage;

public class TryoutEvent {
    public static final int onGetDataSuccess = 0;
    public static final int onGetDataError = 1;

    int eventType;
    String errorMessage;

    String quote;
    String talker;

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

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }
}
