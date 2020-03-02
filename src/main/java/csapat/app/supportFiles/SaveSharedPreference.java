package csapat.app.supportFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import csapat.app.teamstructure.model.AppUser;

public class SaveSharedPreference {

    public static final String PREF_USER_NAME = "username";
    public static final String PREF_USER_EMAIL = "email";
    public static final String PREF_USER_PATROL = "patrol";
    public static final String PREF_USER_FIRST_NAME = "firstname";
    public static final String PREF_USER_LAST_NAME = "lastname";
    public static final String PREF_USER_FULL_NAME = "fullname";
    public static final String PREF_USER_PATROL_LEADER_AT = "patrolleaderat";
    public static final String PREF_USER_TROOP_LEADER_AT = "troopleaderat";
    public static final String PREF_USER_RANK = "rank";
    public static final String PREF_USER_ID_IN_DB = "userID";
    public static final String PREF_USER_PROFILE_PICTURE_PATH = "profile_picture_path";
    public static final String PREF_USER_ANSWERED_TO_NEXT_MEETING = "userAnswerToNextMeeting";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setAppUser(Context context, AppUser user, String userID) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putString(PREF_USER_EMAIL, user.getEmail());
        editor.putString(PREF_USER_FIRST_NAME, user.getFirstName());
        editor.putString(PREF_USER_FULL_NAME, user.getFullName());
        editor.putString(PREF_USER_LAST_NAME, user.getLastName());
        editor.putString(PREF_USER_NAME, user.getUsername());
        editor.putString(PREF_USER_PATROL, user.getPatrol());
        editor.putString(PREF_USER_PATROL_LEADER_AT, user.getPatrolLeaderAt());
        editor.putString(PREF_USER_TROOP_LEADER_AT, user.getTroopLeaderAt());
        editor.putInt(PREF_USER_RANK, user.getRank());
        editor.putString(PREF_USER_ID_IN_DB, userID);
        editor.putString(PREF_USER_PROFILE_PICTURE_PATH, user.getProfile_picture());
        editor.putBoolean(PREF_USER_ANSWERED_TO_NEXT_MEETING, user.answeredToNextMeeting());
        editor.apply();
    }


    public static AppUser getAppUser(Context context) {

        SharedPreferences sp = getSharedPreferences(context);

        String usEmail = sp.getString(PREF_USER_EMAIL, "");
        String usUsername = sp.getString(PREF_USER_NAME, "");
        String usFirstName = sp.getString(PREF_USER_FIRST_NAME, "");
        String usLastName = sp.getString(PREF_USER_LAST_NAME, "");
        String usFullName = sp.getString(PREF_USER_FULL_NAME, "");
        String usPatrol = sp.getString(PREF_USER_PATROL, "");
        String usPatrolLeaderAt = sp.getString(PREF_USER_PATROL_LEADER_AT, "");
        String usTroopleaderAt = sp.getString(PREF_USER_TROOP_LEADER_AT, "");
        int usRank = sp.getInt(PREF_USER_RANK, 1);
        String userID = sp.getString(PREF_USER_ID_IN_DB, "");
        String usProfilePicturePath = sp.getString(PREF_USER_PROFILE_PICTURE_PATH, "default");
        boolean answerToMeeting = sp.getBoolean(PREF_USER_ANSWERED_TO_NEXT_MEETING, false);


        return new AppUser(usUsername,usFirstName,usFullName, usLastName, usEmail, usPatrol, usRank, usPatrolLeaderAt, usTroopleaderAt ,userID, usProfilePicturePath, answerToMeeting);


    }

    public static void setPrefUserAnsweredToNextMeeting(Context context, AppUser user, boolean answered){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        editor.putBoolean(PREF_USER_ANSWERED_TO_NEXT_MEETING, user.answeredToNextMeeting());
        editor.apply();

    }

    public static void logOutUser (Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }


}
