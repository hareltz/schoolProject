package com.example.project.Domain;

import com.example.project.Helper;
import com.google.firebase.Timestamp;

public class Appointment
{
    private String price;
    private Timestamp time;
    private String user_id;
    private String documentName;
    private Barber barber;

    public Appointment() {} // Empty constructor required for Firestorm

    public Timestamp getAppointmentTime() {
        return time;
    }

    public void setAppointmentTime(Timestamp time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getDate()
    {
        return Helper.getDateFromTimestamp(time);
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
