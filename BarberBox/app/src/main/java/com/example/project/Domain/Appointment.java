package com.example.project.Domain;

import com.example.project.Activity.BarberInfo;
import com.example.project.Helper;
import com.google.firebase.Timestamp;

public class Appointment
{

    private Barber barber;
    private Timestamp time;
    private String price;
    private String user_email = "NULL";

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

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
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

}
