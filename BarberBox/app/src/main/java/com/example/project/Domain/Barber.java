package com.example.project.Domain;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Barber
{
    private String _id;
    private String name;
    private String phone_number;
    private GeoPoint location;
    private List<Appointment> appointments_ = new ArrayList<>();
    private String picture_reference;

    public Barber() {} // Empty constructor for Firestore

    // Constructor with parameters
    public Barber(String name, GeoPoint location)
    {
        this.name = name;
        this.location = location;
    }

    public void addAppointment(Appointment a)
    {
        appointments_.add(a);
    }
    public void changeAppointmentUserId(String document)
    {
        for (Appointment appointment : appointments_)
        {
            if (Objects.equals(appointment.getDocumentName(), document))
            {
                appointment.setUser_id("");
                break;
            }
        }
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
        return appointments_;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments_ = appointments;
    }
}