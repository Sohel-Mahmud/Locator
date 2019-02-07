package com.example.sohel.locator.Model;

public class User {
    private String firstName, lastName, email;
    private Double latitude, longitude;
    private String postKey;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }



    public User(){

    }

    public User(String firstName, String lastName, String email, Double latitude, Double longitude) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
