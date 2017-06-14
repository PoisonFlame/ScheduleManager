package ca.ziggs.schedulemanager;

/**
 * Created by Robby Sharma on 6/10/2017.
 */

public class Item {
    private int id;
    private String name;
    private  String time;
    private String date;
    private  String duration;
    private String iconURL;
    private String formattedTime;
    private String dateTime;

    public Item(int id,String name,String date,String time,String duration, String iconURL,String formattedTime,String dateTime){
        this.id = id;
        this.date = date;
        this.name = name;
        this.time = time;
        this.duration = duration;
        this.iconURL = iconURL;
        this.formattedTime = formattedTime;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
}
