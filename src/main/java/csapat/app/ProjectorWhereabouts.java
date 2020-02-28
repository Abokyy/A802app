package csapat.app;

public class ProjectorWhereabouts {

    private String location;
    private int year;
    private int month;
    private int day;
    private String publisher;

    public ProjectorWhereabouts() {

    }

    public ProjectorWhereabouts(String location, int year, int month, int day, String publisher) {
        this.location = location;
        this.year = year;
        this.month = month;
        this.day = day;
        this.publisher = publisher;
    }

    public String getLocation() {
        return location;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getPublisher() {
        return publisher;
    }
}