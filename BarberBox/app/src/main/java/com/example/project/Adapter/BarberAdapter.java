package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Domain.Barber;
import com.example.project.Helper;
import com.example.project.Interfaces.IRecyclerViewOnBarberClick;
import com.example.project.R;

import java.io.File;
import java.util.ArrayList;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.Viewholder>
{
    ArrayList<Barber> barbers;
    private final IRecyclerViewOnBarberClick iRecyclerViewOnBarberClick;
    private final int type; // 1 - Favourites // 2 - Popular

    public BarberAdapter(ArrayList<Barber> barbers, IRecyclerViewOnBarberClick iRecyclerViewOnBarberClick, int type) {
        this.barbers = barbers;
        this.iRecyclerViewOnBarberClick = iRecyclerViewOnBarberClick;
        this.type = type;
    }

    @NonNull
    @Override
    public BarberAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_barber_box, parent, false);
        return new Viewholder(view, iRecyclerViewOnBarberClick, type);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberAdapter.Viewholder holder, int position) {
        holder.Name.setText(barbers.get(position).getName());
        holder.PhoneNumber.setText(barbers.get(position).getPhone_number());

        String picAdd = barbers.get(position).getPicture_reference();
        File localFile = Helper.getImageFile(barbers.get(position).getName().replace(" ", "_") + ".png");

        if (localFile.exists())
        {
            Glide.with(holder.itemView.getContext())
                    .load(localFile)
                    .into(holder.pic);
        }
    }

    @Override
    public int getItemCount() {
        return barbers.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView Name, PhoneNumber;
        com.google.android.material.imageview.ShapeableImageView pic;

        public Viewholder(@NonNull View itemView, IRecyclerViewOnBarberClick iRecyclerViewOnBarberClick, int type)
        {
            super(itemView);
            Name = itemView.findViewById((R.id.barber_name));
            PhoneNumber = itemView.findViewById(R.id.barbers_phone);
            pic = itemView.findViewById(R.id.barbers_pic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iRecyclerViewOnBarberClick != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            iRecyclerViewOnBarberClick.onBarberClick(position, type);
                        }
                    }
                }
            });
        }
    }

    public Barber GetBarberByPosition(int position)
    {
        return barbers.get(position);
    }
}
