package com.example.blooddonorapp;

public class Donor {
    private String name;
    private String bloodGroup;
    private String city;
    private String phone;
    private String age;
    private String lastDonated;

    public Donor(String name, String bloodGroup, String city, String phone, String age, String lastDonated) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.phone = phone;
        this.age = age;
        this.lastDonated = lastDonated;
    }

    public String getName() { return name; }
    public String getBloodGroup() { return bloodGroup; }
    public String getCity() { return city; }
    public String getPhone() { return phone; }
    public String getAge() { return age; }
    public String getLastDonated() { return lastDonated; }
}