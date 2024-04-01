package com.example.project.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Domain.Appointment;
import com.example.project.R;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.Viewholder>
{
    ArrayList<Appointment> appointments;

    public AppointmentAdapter(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_appointments, parent, false);
        return new AppointmentAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.Viewholder holder, int position) {
        holder.Name.setText(appointments.get(position).getBarber().getName());
        holder.PhoneNumber.setText(appointments.get(position).getBarber().getPhoneNumber());
        holder.Date.setText(appointments.get(position).getDate());
        holder.Time.setText(appointments.get(position).getTime());

        Bitmap bitmap = BitmapFactory.decodeFile(appointments.get(position).getBarber().getPicAddress());
        holder.Pic.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView Name, PhoneNumber, Date, Time;
        com.google.android.material.imageview.ShapeableImageView Pic;

        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            Name = itemView.findViewById((R.id.appointment_barbers_name));
            PhoneNumber = itemView.findViewById(R.id.appointment_barbers_phone);
            Date = itemView.findViewById(R.id.appointment_date);
            Time = itemView.findViewById(R.id.appointment_time);
            Pic = itemView.findViewById(R.id.appointment_barber_pic);

        }
    }
}
