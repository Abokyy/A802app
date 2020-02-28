package csapat.app.teamstructure.model;
import android.widget.LinearLayout;

import java.util.List;
import java.util.ArrayList;

public class Patrol {

    private String leader;
    private List<String> members;
    private List<String> nextMeetingAttendance = new ArrayList<>();
    private String name;
    private int meetingDay;
    private int meetingHour;
    private int meetingMinute;
    private String meetingLocation;
    private boolean activePatrol = true;



    public Patrol (){}

    public Patrol(String leader, List<String> members, String name, int meetingDay, int meetingHour, int meetingMinute, String meetingLocation) {
        this.leader = leader;
        this.members = members;
        this.name = name;
        this.meetingDay = meetingDay;
        this.meetingHour = meetingHour;
        this.meetingMinute = meetingMinute;
        this.meetingLocation = meetingLocation;
    }

    public Patrol(String leader, List<String> members, String name) {
        this.leader = leader;
        this.members = members;
        this.name = name;
        meetingDay = -1;
        meetingHour = -1;
        meetingMinute = -1;
        meetingLocation = "NA";
    }

    public Patrol(String leader, List<String> members, List<String> nextMeetingAttendance, String name, int meetingDay, int meetingHour, int meetingMinute, String meetingLocation) {
        this.leader = leader;
        this.members = members;
        this.nextMeetingAttendance = nextMeetingAttendance;
        this.name = name;
        this.meetingDay = meetingDay;
        this.meetingHour = meetingHour;
        this.meetingMinute = meetingMinute;
        this.meetingLocation = meetingLocation;
    }

    public Patrol(String leader, List<String> members, List<String> nextMeetingAttendance, String name, int meetingDay, int meetingHour, int meetingMinute, String meetingLocation, boolean activePatrol) {
        this.leader = leader;
        this.members = members;
        this.nextMeetingAttendance = nextMeetingAttendance;
        this.name = name;
        this.meetingDay = meetingDay;
        this.meetingHour = meetingHour;
        this.meetingMinute = meetingMinute;
        this.meetingLocation = meetingLocation;
        this.activePatrol = activePatrol;
    }

    public String getLeader() {
        return leader;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public int getMeetingDay() {
        return meetingDay;
    }

    public int getMeetingHour() {
        return meetingHour;
    }

    public int getMeetingMinute() {
        return meetingMinute;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public List<String> getNextMeetingAttendance() {
        return nextMeetingAttendance;
    }

    public boolean isActivePatrol() {
        return activePatrol;
    }
}
