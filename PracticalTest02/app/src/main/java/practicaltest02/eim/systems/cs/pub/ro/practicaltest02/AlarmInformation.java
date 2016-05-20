package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

/**
 * Created by Ariel on 5/20/2016.
 */
public class AlarmInformation {

    private String hour;
    private String minutes;
    private String status;

    public AlarmInformation() {
        this.hour = null;
        this.minutes = null;
        this.status = null;
    }

    public AlarmInformation(
            String hour,
            String minutes,
            String status) {
        this.hour = hour;
        this.minutes = minutes;
        this.status = status;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Hour" + ": " + hour + "\n\r" +
                "Minutes" + ": " + minutes + "\n\r" +
                "Status" + ": " + status + "\n\r";
    }
}
