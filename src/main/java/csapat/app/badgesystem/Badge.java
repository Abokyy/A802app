package csapat.app.badgesystem;

import java.io.Serializable;

public class Badge implements Serializable {
    private String badgeImageSrc = "null";
    private String unlockedImgSrc = "null";
    private int points = 100;
    private String name = "badge";
    private int badgeID = 0;
    private String badgeDescription = "null";
    private int level = 1; //level 1 = egyéni, level 2 = őrsi szint

    public Badge () {}

    public Badge(String badgeImageSrc, int points, String name, int badgeID) {
        this.badgeImageSrc = badgeImageSrc;
        this.points = points;
        this.name = name;
        this.badgeID = badgeID;
    }

    public Badge(String badgeImageSrc, int points, String name, int badgeID, String badgeDescription) {
        this.badgeImageSrc = badgeImageSrc;
        this.points = points;
        this.name = name;
        this.badgeID = badgeID;
        this.badgeDescription = badgeDescription;
    }

    public Badge(String badgeImageSrc, String unlockedImgSrc, int points, String name, int badgeID, String badgeDescription) {
        this.badgeImageSrc = badgeImageSrc;
        this.unlockedImgSrc = unlockedImgSrc;
        this.points = points;
        this.name = name;
        this.badgeID = badgeID;
        this.badgeDescription = badgeDescription;
    }

    public Badge(String badgeImageSrc, String unlockedImgSrc, int points, String name, int badgeID, String badgeDescription, int level) {
        this.badgeImageSrc = badgeImageSrc;
        this.unlockedImgSrc = unlockedImgSrc;
        this.points = points;
        this.name = name;
        this.badgeID = badgeID;
        this.badgeDescription = badgeDescription;
        this.level = level;
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

    public String getBadgeDescription() {
        return badgeDescription;
    }

    public String getUnlockedImgSrc() {
        return unlockedImgSrc;
    }

    public int getLevel() {
        return level;
    }
}
