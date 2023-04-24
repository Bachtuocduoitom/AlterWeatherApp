package com.example.weatherapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.DetailCityActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.model.City;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private List<City> cityList;

    public CityAdapter(Context context, List<City> list) {
        this.cityList = list;
        this.context = context;
    }

    public void setData(List<City> list) {
        this.cityList = list;
        notifyDataSetChanged();
    }

    public void add(City city) {
        this.cityList.add(city);
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        final City city = cityList.get(position);
        if (city == null) {
            return;
        }
        holder.rvCityName.setText(city.getCityName());

        holder.rvLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDetail(city);
            }
        });
    }

    private void gotoDetail(City city) {
        Intent intent = new Intent(context, DetailCityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_city", city);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rvCityName;
        RelativeLayout rvLayoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvLayoutItem = itemView.findViewById(R.id.rvLayoutCityItem);
            rvCityName = itemView.findViewById(R.id.rvCityName);
        }
    }
}
