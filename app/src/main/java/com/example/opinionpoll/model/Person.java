package com.example.opinionpoll.model;

public class Person {
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
}
