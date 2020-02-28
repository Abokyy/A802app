package csapat.app.calendar;

public class Event implements Comparable<Event>{

    private String creator;
    private int year;
    private int startmonth;
    private int startday;
    private int starthour;
    private int startminute;
    private String title;
    private int endmonth;
    private int endday;
    private int endhour;
    private int endminute;

    public void setStarthour(int starthour) {
        this.starthour = starthour;
    }

    public void setStartminute(int startminute) {
        this.startminute = startminute;
    }

    public void setEndhour(int endhour) {
        this.endhour = endhour;
    }

    public void setEndminute(int endminute) {
        this.endminute = endminute;
    }

    public Event(String creator, int year, int startmonth, int startday, int starthour, int startminute, String title, int endmonth, int endday, int endhour, int endminute) {
        this.creator = creator;
        this.year = year;
        this.startmonth = startmonth;
        this.startday = startday;
        this.starthour = starthour;
        this.startminute = startminute;
        this.title = title;
        this.endmonth = endmonth;
        this.endday = endday;
        this.endhour = endhour;
        this.endminute = endminute;
    }


    public Event(){}

    public Event(int year, int month, int today) {
        this.year = year;
        this.startmonth = month;
        this.startday = today;
    }


    public Event(String creatorID, int year, int startmonth, int startday, int starthour, int startminute, String title) {
        this.creator = creatorID;
        this.year = year;
        this.startmonth = startmonth;
        this.startday = startday;
        this.starthour = starthour;
        this.startminute = startminute;
        this.title = title;
    }

    public Event(String creatorID, int year, int startmonth, int startday, String title) {
        this.creator = creatorID;
        this.year = year;
        this.startmonth = startmonth;
        this.startday = startday;
        starthour = 0;
        startminute = 0;
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public int getStartmonth() {
        return startmonth;
    }

    public int getStartday() {
        return startday;
    }

    public int getStarthour() {
        return starthour;
    }

    public int getStartminute() {
        return startminute;
    }

    public int getEndmonth() {
        return endmonth;
    }

    public int getEndday() {
        return endday;
    }

    public int getEndhour() {
        return endhour;
    }

    public int getEndminute() {
        return endminute;
    }

    public String getCreator() {
        return creator;
    }


    public String getTitle() {
        return title;
    }

    public int compareTo(Event anotherEvent) {

        if(year != anotherEvent.getYear()) return year - anotherEvent.getYear();
        else {
            if (startmonth != anotherEvent.getStartmonth()) return startmonth - anotherEvent.getStartmonth();
            else {
                if (startday != anotherEvent.getStartday()) return startday - anotherEvent.getStartday();
                else {
                    if(starthour != anotherEvent.getStarthour()) return starthour - anotherEvent.getStarthour();
                    else return startminute - anotherEvent.getStartminute();
                }
            }

        }
    }


    public void setAllEnd(int i) {
        endmonth = endday = endhour = endminute = i;

    }
}
