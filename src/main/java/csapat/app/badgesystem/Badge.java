package csapat.app.badgesystem;

public class Badge {
    private String badgeImageSrc = "null";
    private int points = 100;
    private String name = "badge";
    private int badgeID = 0;

    public Badge () {}

    public Badge(String badgeImageSrc, int points, String name, int badgeID) {
        this.badgeImageSrc = badgeImageSrc;
        this.points = points;
        this.name = name;
        this.badgeID = badgeID;
    }

    public String getBadgeImageSrc() {
        return badgeImageSrc;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public int getBadgeID() {
        return badgeID;
    }

    public void setBadgeID(int badgeID) {
        this.badgeID = badgeID;
    }
}
