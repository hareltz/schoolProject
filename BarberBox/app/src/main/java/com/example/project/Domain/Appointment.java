package com.example.project.Domain;

public class Appointment
{

    private Barber barber;
    private String date;
    private String time;

    public Appointment(Barber barber, String date, String time) {
        this.barber = barber;
        this.date = date;
        this.time = time;
    }

    public Barber getBarber() {
        return barber;
    }

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
