package com.android.csvtu.raktdevdoot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myadapter extends RecyclerView.Adapter<myadapter.MyViewHolder> {

    Context context;
    ArrayList<user> userArrayList;

    public myadapter(Context context, ArrayList<user> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    public void add(user usr){
        userArrayList.add(usr);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myadapter.MyViewHolder holder, int position) {

        user user1 = userArrayList.get(position);

        holder.fullname.setText(user1.getFullname());
        holder.phoneno.setText(String.valueOf(user1.getPhone()));
        holder.age.setText(String.valueOf(user1.getAge()));
        holder.gender.setText(user1.getGender());
        holder.bloodgrp.setText(user1.getBloodgroup());

    }
    public void clearData() {
        userArrayList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView fullname, phoneno, age, gender, bloodgrp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fname);
            phoneno = itemView.findViewById(R.id.ph);
            age = itemView.findViewById(R.id.age);
            gender = itemView.findViewById(R.id.gender);
            bloodgrp = itemView.findViewById(R.id.bgp);


        }
    }
}

