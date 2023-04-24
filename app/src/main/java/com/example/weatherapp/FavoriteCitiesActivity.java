package com.example.weatherapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.weatherapp.adapter.CityAdapter;
import com.example.weatherapp.database.CityDatabase;
import com.example.weatherapp.model.City;


import java.util.ArrayList;
import java.util.List;

public class FavoriteCitiesActivity extends AppCompatActivity {

    Button btn_add_city;
    RecyclerView rvCitiesList;
    public CityAdapter cityAdapter;
    public List<City> listCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_cities);

        btn_add_city = findViewById(R.id.btn_add_city);

        rvCitiesList = findViewById(R.id.rvCitiesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCitiesList.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCitiesList.addItemDecoration(itemDecoration);

        listCity = CityDatabase.getInstance(FavoriteCitiesActivity.this).cityDAO().getListCity();
        cityAdapter = new CityAdapter(FavoriteCitiesActivity.this, listCity);
        rvCitiesList.setAdapter(cityAdapter);

        btn_add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCityDialog(Gravity.CENTER);
            }
        });
        
    }

    //open add city dialog
    private void openAddCityDialog(int gravity) {
        final Dialog dialog = new Dialog(FavoriteCitiesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_city);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(gravity == Gravity.CENTER) {
            dialog.setCancelable(true);
        }

        EditText edt_city_name = dialog.findViewById(R.id.edt_city_name);
        Button btn_add_city_dialog = dialog.findViewById(R.id.btn_add_city_dialog);

        btn_add_city_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCityName = edt_city_name.getText().toString().trim();
                if (TextUtils.isEmpty(strCityName)){
                    Toast.makeText(FavoriteCitiesActivity.this, "Please Enter City Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                City city = new City(strCityName);
                if (isCityExit(city)) {
                    Toast.makeText(FavoriteCitiesActivity.this, "City exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                CityDatabase.getInstance(FavoriteCitiesActivity.this).cityDAO().insertCity(city);

                hideSoftKeyboard();

                listCity = CityDatabase.getInstance(FavoriteCitiesActivity.this).cityDAO().getListCity();
                cityAdapter.setData(listCity);
                rvCitiesList.scrollToPosition(cityAdapter.getItemCount() - 1);

                dialog.dismiss();
                Toast.makeText(FavoriteCitiesActivity.this, "Add city successfully", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    //hide soft keyboard
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    //check city name
    private boolean isCityExit(City city) {
        List<City> list = CityDatabase.getInstance(FavoriteCitiesActivity.this).cityDAO().checkCity(city.getCityName());
        return list != null && !list.isEmpty();
    }
}