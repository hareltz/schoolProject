package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Domain.Appointment;
import com.example.project.Helper;
import com.example.project.Interfaces.IRecyclerViewOnAppointmentClick;
import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.Viewholder>
{
    ArrayList<Appointment> appointments;
    private final IRecyclerViewOnAppointmentClick iRecyclerViewOnItemClick;

    StorageReference storageReference;

    public AppointmentAdapter(ArrayList<Appointment> appointments, IRecyclerViewOnAppointmentClick iRecyclerViewOnItemClick) {
        this.appointments = appointments;
        this.iRecyclerViewOnItemClick = iRecyclerViewOnItemClick;
    }

    @NonNull
    @Override
    public AppointmentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_appointments, parent, false);
        return new AppointmentAdapter.Viewholder(view, iRecyclerViewOnItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.Viewholder holder, int position) {
        holder.Name.setText(appointments.get(position).getBarber().getName().split(" ")[0]);
        holder.PhoneNumber.setText(appointments.get(position).getBarber().getPhone_number());
        holder.Date.setText(Helper.getDateFromGeoPoint(appointments.get(position).getAppointmentTime()));
        holder.Time.setText(Helper.getTimeFromGeoPoint(appointments.get(position).getAppointmentTime()));

        String picAdd = appointments.get(position).getBarber().getPicture_reference();
        File localFile = Helper.getImageFile(appointments.get(position).getBarber().getName().replace(" ", "_") + ".png");

        if (localFile.exists())
        {
            Glide.with(holder.itemView.getContext())
                    .load(localFile)
                    .into(holder.Pic);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public Appointment GetAppointmentByPosition(int position) {
        return appointments.get(position);
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView Name, PhoneNumber, Date, Time;

        com.google.android.material.imageview.ShapeableImageView Pic;

        public Viewholder(@NonNull View itemView, IRecyclerViewOnAppointmentClick iRecyclerViewOnItemClick)
        {
            super(itemView);
            Name = itemView.findViewById((R.id.appointment_barbers_name));
            PhoneNumber = itemView.findViewById(R.id.appointment_barbers_phone);
            Date = itemView.findViewById(R.id.appointment_date);
            Time = itemView.findViewById(R.id.appointment_time);
            Pic = itemView.findViewById(R.id.appointment_barber_pic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iRecyclerViewOnItemClick != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            iRecyclerViewOnItemClick.onAppointmentClick(position);
                        }
                    }
                }
            });
        }
    }
}
