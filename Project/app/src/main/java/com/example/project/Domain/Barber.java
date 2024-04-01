package com.example.project.Domain;

public class Barber
{
    protected String name;
    protected String phoneNumber;
    protected String picAddress;

    public Barber(String name, String phoneNumber, String picAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.picAddress = picAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(String picAddress) {
        this.picAddress = picAddress;
    }
}
