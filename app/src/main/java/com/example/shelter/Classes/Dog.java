package com.example.shelter.Classes;


import android.os.Parcel;
import android.os.Parcelable;

public class Dog implements Parcelable {

    private String uId;
    private String Name;
    private String Age;
    private String Breed;
    private String Gender;
    private String Description;
    private String ImageUri;
    private String dId;


    public Dog(String uId, String name, String age, String breed, String gender, String description, String dId , String ImageUri) {
        this.uId = uId;
        Name = name;
        Age = age;
        Breed = breed;
        Gender = gender;
        Description = description;
        this.dId = dId;
        this.ImageUri = ImageUri;
    }

    public Dog()
    {}

    public Dog(String uId, String name, String age, String breed, String gender, String description , String ImageUri) {
        this.uId = uId;
        Name = name;
        Age = age;
        Breed = breed;
        Gender = gender;
        Description = description;
        this.ImageUri = ImageUri;
    }


    protected Dog(Parcel in) {
        uId = in.readString();
        Name = in.readString();
        Age = in.readString();
        Breed = in.readString();
        Gender = in.readString();
        Description = in.readString();
        ImageUri = in.readString();
        dId = in.readString();
    }

    public static final Creator<Dog> CREATOR = new Creator<Dog>() {
        @Override
        public Dog createFromParcel(Parcel in) {
            return new Dog(in);
        }

        @Override
        public Dog[] newArray(int size) {
            return new Dog[size];
        }
    };

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getBreed() {
        return Breed;
    }

    public void setBreed(String breed) {
        Breed = breed;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getdId() {
        return dId;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uId);
        parcel.writeString(Name);
        parcel.writeString(Age);
        parcel.writeString(Breed);
        parcel.writeString(Gender);
        parcel.writeString(Description);
        parcel.writeString(ImageUri);
        parcel.writeString(dId);
    }
}
