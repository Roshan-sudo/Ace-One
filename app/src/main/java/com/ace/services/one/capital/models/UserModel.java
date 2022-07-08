package com.ace.services.one.capital.models;

public class UserModel {
    private String userId, phone, name, course, institute, profilePicUrl;
    private int kycStatus;

    public UserModel() {
        // Required empty constructor
    }

    public UserModel(String userId, String phone, String name, String course, String institute, String profilePicUrl, int kycStatus) {
        this.userId = userId;
        this.phone = phone;
        this.name = name;
        this.course = course;
        this.institute = institute;
        this.profilePicUrl = profilePicUrl;
        this.kycStatus = kycStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public int getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(int kycStatus) {
        this.kycStatus = kycStatus;
    }
}
