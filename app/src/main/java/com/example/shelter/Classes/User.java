package com.example.shelter.Classes;

public class User {
    private String Uid;
    private String fName;
    private String email;
    private String Mobile;
    private String Gender;
    private String Bio;
    private String Uri;

    public User(String uid, String fName, String email, String mobile, String gender, String Bio, String Uri) {
        Uid = uid;
        this.fName = fName;
        this.Bio = Bio;
        this.email = email;
        Mobile = mobile;
        Gender = gender;
        this.Uri = Uri;
    }

    public User()
    {

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }
}
