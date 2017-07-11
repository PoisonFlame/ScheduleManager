package ca.ziggs.schedulemanager;

/**
 * Created by Robby Sharma on 7/7/2017.
 */

public class SchedHeader {

    private int id;
    private String startTime;
    private String endTime;
    private String test;

    public SchedHeader(int id, String startTime, String endTime, String test){
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.test = test;
    }

    public int getId() {
        return id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
