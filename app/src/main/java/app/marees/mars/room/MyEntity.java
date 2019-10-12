package app.marees.mars.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "marees_mark_iv")
public class MyEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String isInterval;
    private String isRun;
    private String isImmediate;
    private String isNetwork;
    private String isCommand;
    private String intervalTime;
    private String date;
    private String time;
    private String isIntervalDone;
    private String prevInterval;
    private String isSms;
    private String isCall;
    private String isCont;


    public MyEntity(String isInterval, String isRun, String isImmediate, String isNetwork, String isCommand, String intervalTime, String date, String time, String isIntervalDone, String prevInterval, String isSms, String isCall, String isCont) {
        this.isInterval = isInterval;
        this.isRun = isRun;
        this.isImmediate = isImmediate;
        this.isNetwork = isNetwork;
        this.isCommand = isCommand;
        this.intervalTime = intervalTime;
        this.date = date;
        this.time = time;
        this.isIntervalDone = isIntervalDone;
        this.prevInterval = prevInterval;
        this.isSms = isSms;
        this.isCall = isCall;
        this.isCont = isCont;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsSms() {
        return isSms;
    }

    public String getIsCall() {
        return isCall;
    }

    public String getIsCont() {
        return isCont;
    }

    public String getIsInterval() {
        return isInterval;
    }

    public String getIsRun() {
        return isRun;
    }

    public String getIsImmediate() {
        return isImmediate;
    }

    public String getIsNetwork() {
        return isNetwork;
    }

    public String getIsCommand() {
        return isCommand;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getIsIntervalDone() {
        return isIntervalDone;
    }

    public String getPrevInterval() {
        return prevInterval;
    }
}
