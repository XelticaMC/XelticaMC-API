package io.servertap.api.v1.models;

import java.util.List;

import com.google.gson.annotations.Expose;

public class Ranking {
    public Ranking(String name, String displayName, boolean isPlayerMode, String mode, List<RankingRecord> records) {
        this.name = name;
        this.displayName = displayName;
        this.isPlayerMode = isPlayerMode;
        this.mode = mode;
        this.records = records;
    }

    public String getName() { return name; }
    public void setName(String value) { name = value; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String value) { displayName = value; }

    public boolean isPlayerMode() { return isPlayerMode; }
    public void setIsPlayerMode(boolean value) { isPlayerMode = value; }

    public String getMode() { return mode; }
    public void setMode(String value) { mode = value; }

    public List<RankingRecord> getRankingRecord() { return records; }
    public void setRankingRecord(List<RankingRecord> value) { records = value; }

    @Expose
    private String name;

    @Expose
    private String displayName;

    @Expose
    private boolean isPlayerMode;

    @Expose
    private String mode;

    @Expose
    private List<RankingRecord> records;
}
