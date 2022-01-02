package ru.nsu.ccfit.GH;

import ru.nsu.ccfit.GH.Hit;

public class GHResponse {
    private Hit[] hits;
    private String locale;

    public Hit[] getHits() {
        return hits;
    }

    public void setHits(Hit[] hits) {
        this.hits = hits;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public GHResponse() {

    }
}
