package io.servertap.api.v1.models;

import com.google.gson.annotations.Expose;

public class RankingRecord {
    public RankingRecord(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Expose
    private String key;

    @Expose
    private String value;
}
