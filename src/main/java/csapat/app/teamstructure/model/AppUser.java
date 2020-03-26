package csapat.app.teamstructure.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AppUser {

    private String username;
    private String firstName;
    private String fullName;
    private String lastName;
    private String email;
    private String patrol;
    private int rank = 0;
    private String patrolLeaderAt;
    private String troopLeaderAt;
    private String userID;
    private String profile_picture;
    private boolean answeredNextMeetingRequest = false;
    private List<Integer> achievedBadges = new ArrayList<>();
    private int score = 0;


    public AppUser(String username, String firstName, String fullName, String lastName, String email, String patrol, int rank, String patrolLeaderAt, String troopLeaderAt) {
        this.username = username;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.patrol = patrol;
        this.rank = rank;
        this.patrolLeaderAt = patrolLeaderAt;
        this.troopLeaderAt = troopLeaderAt;
        fullName = String.format("%s %s", lastName, firstName);

    }

    public AppUser(String username, String firstName, String fullName, String lastName, String email, String patrol,String userID, int rank) {
        this.username = username;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.patrol = patrol;
        this.rank = rank;
        patrolLeaderAt = "none";
        troopLeaderAt = "none";
        this.userID = userID;
    }

    public AppUser(String username, String firstName, String fullName, String lastName, String email, String patrol, int rank, String patrolLeaderAt) {
        this.username = username;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.patrol = patrol;
        this.rank = rank;
        this.patrolLeaderAt = patrolLeaderAt;
        troopLeaderAt = "none";
    }

    public AppUser() {
    }

    public AppUser(String usUsername, String usFirstName, String usFullName, String usLastName, String usEmail, String usPatrol, int usRank, String usPatrolLeaderAt, String userID, String usTroopleaderAt) {

        this.username = usUsername;
        this.firstName = usFirstName;
        this.fullName = usFullName;
        this.lastName = usLastName;
        this.email = usEmail;
        this.patrol = usPatrol;
        this.rank = usRank;
        patrolLeaderAt = usPatrolLeaderAt;
        troopLeaderAt = usTroopleaderAt;
        this.userID = userID;

    }

    public AppUser(String usUsername, String usFirstName, String usFullName, String usLastName, String usEmail, String usPatrol, int usRank, String usPatrolLeaderAt, String userID, String usTroopleaderAt, String usProfilePicturePath) {

        this.username = usUsername;
        this.firstName = usFirstName;
        this.fullName = usFullName;
        this.lastName = usLastName;
        this.email = usEmail;
        this.patrol = usPatrol;
        this.rank = usRank;
        patrolLeaderAt = usPatrolLeaderAt;
        troopLeaderAt = usTroopleaderAt;
        this.userID = userID;
        this.profile_picture = usProfilePicturePath;

    }

    public AppUser(String username, String firstName, String fullName, String lastName, String email, String patrol, int rank, String patrolLeaderAt, String troopLeaderAt, String userID, String profile_picture, boolean answeredNextMeetingRequest) {
        this.username = username;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.patrol = patrol;
        this.rank = rank;
        this.patrolLeaderAt = patrolLeaderAt;
        this.troopLeaderAt = troopLeaderAt;
        this.userID = userID;
        this.profile_picture = profile_picture;
        this.answeredNextMeetingRequest = answeredNextMeetingRequest;
    }

    public AppUser(String username, String firstName, String fullName, String lastName, String email, String patrol, int rank, String patrolLeaderAt, String troopLeaderAt, String userID, String profile_picture, boolean answeredNextMeetingRequest, List<Integer> achievedBadges) {
        this.username = username;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.patrol = patrol;
        this.rank = rank;
        this.patrolLeaderAt = patrolLeaderAt;
        this.troopLeaderAt = troopLeaderAt;
        this.userID = userID;
        this.profile_picture = profile_picture;
        this.answeredNextMeetingRequest = answeredNextMeetingRequest;
        this.achievedBadges = achievedBadges;
    }

    public AppUser(String username, String firstName, String fullName, String lastName, String email, String patrol, int rank, String patrolLeaderAt, String troopLeaderAt, String userID, String profile_picture, boolean answeredNextMeetingRequest, List<Integer> achievedBadges, int score) {
        this.username = username;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.email = email;
        this.patrol = patrol;
        this.rank = rank;
        this.patrolLeaderAt = patrolLeaderAt;
        this.troopLeaderAt = troopLeaderAt;
        this.userID = userID;
        this.profile_picture = profile_picture;
        this.answeredNextMeetingRequest = answeredNextMeetingRequest;
        this.achievedBadges = achievedBadges;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPatrol() {
        return patrol;
    }

    public int getRank() {
        return rank;
    }

    public String getPatrolLeaderAt() {

        return patrolLeaderAt;
    }

    public String getTroopLeaderAt() {
        return troopLeaderAt;
    }

    public  String getUserID () {
        return userID;
    }

    public String getProfile_picture() { return profile_picture;}

    public void setProfile_picture(String path) {
        profile_picture = path;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean answeredToNextMeeting() {
        return answeredNextMeetingRequest;
    }

    public void setAnsweredToNextMeeting(boolean answerToNextMeeting) {
        this.answeredNextMeetingRequest = answerToNextMeeting;
    }

    public boolean isAnsweredNextMeetingRequest() {
        return answeredNextMeetingRequest;
    }

    public List<Integer> getAchievedBadges() {
        return achievedBadges;
    }

    public int getScore() {
        return score;
    }
}
