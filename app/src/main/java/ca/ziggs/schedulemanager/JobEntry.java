package ca.ziggs.schedulemanager;

/**
 * Created by Robby Sharma on 6/11/2017.
 */

public class JobEntry {

    int id;
    String title;
    String location;
    String duration;
    String startTime;
    String endTime;
    String breaks;
    String date;
    String formattedDate;
    String formattedTime;
    String dateTime;

    public JobEntry(){

    }

    public JobEntry(int id, String title, String location, String duration, String startTime, String endTime, String breaks, String date, String formattedDate, String formattedTime, String dateTime){
        this.id = id;
        this.title = title;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breaks = breaks;
        this.date = date;
        this.formattedDate = formattedDate;
        this.formattedTime = formattedTime;
        this.dateTime = dateTime;
    }

    public JobEntry(String title, String location, String duration, String startTime, String endTime, String breaks, String date, String formattedDate, String formattedTime, String dateTime){
        this.title = title;
        this.location = location;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breaks = breaks;
        this.date = date;
        this.formattedDate = formattedDate;
        this.formattedTime = formattedTime;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBreaks() {
        return breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
    }
}
