package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.Viewholder>
{
    ArrayList<Barber> barbers;
    Context context;


    public BarberAdapter(ArrayList<Barber> barbers) {
        this.barbers = barbers;
    }

    @NonNull
    @Override
    public BarberAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_barber_box, parent, false);
        context = parent.getContext();
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberAdapter.Viewholder holder, int position) {
        holder.Name.setText(barbers.get(position).getName());
        holder.PhoneNumber.setText(barbers.get(position).getPhoneNumber());

        Bitmap bitmap = BitmapFactory.decodeFile(barbers.get(position).getPicAddress());
        holder.pic.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return barbers.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView Name, PhoneNumber;
        com.google.android.material.imageview.ShapeableImageView pic;

        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            Name = itemView.findViewById((R.id.barber_name));
            PhoneNumber = itemView.findViewById(R.id.barbers_phone);
            pic = itemView.findViewById(R.id.barbers_pic);

        }
    }
}
