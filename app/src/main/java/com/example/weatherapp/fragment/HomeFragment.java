package com.example.weatherapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.R;
import com.example.weatherapp.adapter.WeatherForecastAdapter;
import com.example.weatherapp.model.WeatherForecast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    final String APP_ID = "bffca17bcb552b8c8e4f3b82f64cccd2";
    final int REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView imageView;
    TextView temptv, time, longitude, latitude, humidity, sunrise, sunset, pressure, wind, country, city_nam, max_temp, min_temp, feels;
    RecyclerView rvWeatherForecast;
    private ArrayList<WeatherForecast> weatherForecastArrayList;
    private WeatherForecastAdapter weatherForecastAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        imageView = view.findViewById(R.id.imageDetailWCondition);
        temptv = view.findViewById(R.id.detailTemp);
        time = view.findViewById(R.id.detailTime);

        longitude = view.findViewById(R.id.detailLongitude);
        latitude = view.findViewById(R.id.detailLatitude);
        humidity = view.findViewById(R.id.detailHumidity);
        sunrise = view.findViewById(R.id.detailSunrise);
        sunset = view.findViewById(R.id.detailSunset);
        pressure = view.findViewById(R.id.detailPressure);
        wind = view.findViewById(R.id.detailWind);
        country = view.findViewById(R.id.textDetailCountry);
        city_nam = view.findViewById(R.id.textHomeCityName);
        max_temp = view.findViewById(R.id.detailTemp_max);
        min_temp = view.findViewById(R.id.detailMin_temp);
        feels = view.findViewById(R.id.detailFeels);
        rvWeatherForecast = view.findViewById(R.id.rvDetailWeatherForecast);
        weatherForecastArrayList = new ArrayList<>();
        weatherForecastAdapter = new WeatherForecastAdapter(getActivity(), weatherForecastArrayList);
        rvWeatherForecast.setAdapter(weatherForecastAdapter);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                getWeather(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        }
        else {
            askPermission();
        }
    }

    public void getWeather(double lat, double lon)
    {
        String url ="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+APP_ID+"&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //find temperature
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("main");
                            int temp = object.getInt("temp");
                            temptv.setText("Temperature\n"+temp+"°C");

                            //find country
                            JSONObject object8 = jsonObject.getJSONObject("sys");
                            String count = object8.getString("country");
                            country.setText(count+"  :");

                            //find city
                            String city = jsonObject.getString("name");
                            city_nam.setText(city);

                            //find icon
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String icon = obj.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

                            //find date & time
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy");
                            String date = std.format(calendar.getTime());
                            time.setText(date);

                            //find latitude
                            JSONObject object2 = jsonObject.getJSONObject("coord");
                            double lat_find = object2.getDouble("lat");
                            latitude.setText(lat_find+"°  N");

                            //find longitude
                            JSONObject object3 = jsonObject.getJSONObject("coord");
                            double long_find = object3.getDouble("lon");
                            longitude.setText(long_find+"°  E");

                            //find humidity
                            JSONObject object4 = jsonObject.getJSONObject("main");
                            int humidity_find = object4.getInt("humidity");
                            humidity.setText(humidity_find+"  %");

                            //find sunrise
                            JSONObject object5 = jsonObject.getJSONObject("sys");
                            String sunrise_find = object5.getString("sunrise");
                            sunrise.setText(sunrise_find);

                            //find sunrise
                            JSONObject object6 = jsonObject.getJSONObject("sys");
                            String sunset_find = object6.getString("sunset");
                            sunset.setText(sunset_find);

                            //find pressure
                            JSONObject object7 = jsonObject.getJSONObject("main");
                            String pressure_find = object7.getString("pressure");
                            pressure.setText(pressure_find+"  hPa");

                            //find wind speed
                            JSONObject object9 = jsonObject.getJSONObject("wind");
                            String wind_find = object9.getString("speed");
                            wind.setText(wind_find+"  km/h");

                            //find min temperature
                            JSONObject object10 = jsonObject.getJSONObject("main");
                            int mintemp = object10.getInt("temp_min") ;
                            min_temp.setText("Min Temp\n"+mintemp+" °C");

                            //find max temperature
                            JSONObject object12 = jsonObject.getJSONObject("main");
                            int maxtemp = object12.getInt("temp_max");
                            max_temp.setText("Max Temp\n"+maxtemp+" °C");

                            //find feels
                            JSONObject object13 = jsonObject.getJSONObject("main");
                            int feels_find = object13.getInt("feels_like");
                            feels.setText(feels_find+" °C");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        getWeatherForecast(lat, lon);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getWeatherForecast(double lat, double lon) {
        String url ="http://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+APP_ID+"&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        weatherForecastArrayList.clear();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray forecastArray = jsonObject.getJSONArray("list");
                            for(int i = 0; i <forecastArray.length(); i++) {
                                JSONObject forecastObj = forecastArray.getJSONObject(i);
                                String time = forecastObj.getString("dt_txt");
                                String temp =Integer.toString(forecastObj.getJSONObject("main").getInt("temp"));
                                String icon = forecastObj.getJSONArray("weather").getJSONObject(0).getString("icon");
                                weatherForecastArrayList.add(new WeatherForecast(time, temp, icon));

                            }
                            weatherForecastAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherForCurrentLocation();
            }
            else {
                Toast.makeText(getActivity(), "Required Permission",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (weatherForecastAdapter != null ) {
            weatherForecastAdapter.release();
        }

    }
}