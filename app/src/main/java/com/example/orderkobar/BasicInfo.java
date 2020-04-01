package com.example.orderkobar;

public class BasicInfo {
    private String userEmail;
    private String userId;
    private String Club;
    private String ClubId;
    private static BasicInfo basicInfo;
    private int ClubListPosition = -1;

    public static BasicInfo getInstance(){
        if(basicInfo == null)
            basicInfo = new BasicInfo();
        return basicInfo;
    }

    public String getClubId() {
        return ClubId;
    }

    public void setClubId(String clubId) {
        ClubId = clubId;
    }

    public int getClubListPosition() {
        return ClubListPosition;
    }

    public void setClubListPosition(int clubListPosition) {
        ClubListPosition = clubListPosition;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClub() {
        return Club;
    }

    public void setClub(String club) {
        Club = club;
    }

    public static BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public static void setBasicInfo(BasicInfo basicInfo) {
        BasicInfo.basicInfo = basicInfo;
    }
}
