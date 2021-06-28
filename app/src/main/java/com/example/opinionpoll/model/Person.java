package com.example.opinionpoll.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    private int personId;
    private String personType;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int voted;

    public Person() {
    }

    public Person(int personId, String personType, String username, String password, String firstName, String lastName, int voted) {
        super();
        setPersonId(personId);
        setPersonType(personType);
        setUsername(username);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setVoted(voted);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getVoted() {
        return voted;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.personId);
        dest.writeString(this.personType);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeInt(this.voted);
    }

    public void readFromParcel(Parcel source) {
        this.personId = source.readInt();
        this.personType = source.readString();
        this.username = source.readString();
        this.password = source.readString();
        this.firstName = source.readString();
        this.lastName = source.readString();
        this.voted = source.readInt();
    }

    protected Person(Parcel in) {
        this.personId = in.readInt();
        this.personType = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.voted = in.readInt();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
