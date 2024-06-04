package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Domain.Appointment;
import com.example.project.Helper;
import com.example.project.Interfaces.IRecyclerViewOnAppointmentClick;
import com.example.project.R;

import java.util.ArrayList;

public class AppointmentChooseAdapter extends RecyclerView.Adapter<AppointmentChooseAdapter.Viewholder>{

    ArrayList<Appointment> appointments;
    private final IRecyclerViewOnAppointmentClick iRecyclerViewOnItemClick;

    public AppointmentChooseAdapter(ArrayList<Appointment> appointments, IRecyclerViewOnAppointmentClick iRecyclerViewOnItemClick) {
        this.iRecyclerViewOnItemClick = iRecyclerViewOnItemClick;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentChooseAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_appointment_screen_option, parent, false);
        return new AppointmentChooseAdapter.Viewholder(view, iRecyclerViewOnItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentChooseAdapter.Viewholder holder, int position) {
        holder.Date.setText(Helper.getDateFromTimestamp(appointments.get(position).getAppointmentTime()));
        holder.Time.setText(Helper.getTimeFromTimestamp(appointments.get(position).getAppointmentTime()));
        holder.Price.setText(appointments.get(position).getPrice());
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
        TextView Date, Time, Price;

        public Viewholder(@NonNull View itemView, IRecyclerViewOnAppointmentClick iRecyclerViewOnItemClick)
        {
            super(itemView);
            Date = itemView.findViewById(R.id.appointment_choose_date);
            Time = itemView.findViewById(R.id.appointment_choose_time);
            Price = itemView.findViewById(R.id.appointment_choose_price);

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
