package com.example.project.Domain;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Barber
{
    private String _id;
    private String name;
    private String phone_number;
    private GeoPoint location;
    private List<Appointment> appointments;
    private String picture_reference;

    public Barber() {} // Empty constructor for Firestorm

    // Constructor with parameters
    public Barber(String name, GeoPoint location) {
        this.name = name;
        this.location = location;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getPicture_reference() {
        return picture_reference;
    }

    public void setPicture_reference(String picture_reference) {
        this.picture_reference = picture_reference;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
