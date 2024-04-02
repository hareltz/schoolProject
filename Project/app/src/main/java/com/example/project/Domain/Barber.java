package com.example.project.Domain;

public class Barber
{
    private String name;
    private String phoneNumber;
    private int picAddress;
    private String address;
    private String Price;

    public Barber(String name, String phoneNumber, int picAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.picAddress = picAddress;
    }

    public Barber(String name, String phoneNumber, int picAddress, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.picAddress = picAddress;
        this.address = address;
    }

    public Barber(String name, String phoneNumber, int picAddress, String address, String price) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.picAddress = picAddress;
        this.address = address;
        Price = price;
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

    public int getPicAddress() {
        return picAddress;
    }

    public void setPicAddress(int picAddress) {
        this.picAddress = picAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
