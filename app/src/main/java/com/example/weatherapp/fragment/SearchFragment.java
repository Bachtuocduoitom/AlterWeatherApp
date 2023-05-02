package com.example.weatherapp.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.DetailCityActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.adapter.WeatherForecastAdapter;
import com.example.weatherapp.database.CityDatabase;
import com.example.weatherapp.model.City;
import com.example.weatherapp.model.WeatherForecast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    final String APP_ID = "bffca17bcb552b8c8e4f3b82f64cccd2";
    ImageView imageView;
    AutoCompleteTextView autoCompleteTextView;
    ImageButton imageButton;
    Button btnAdd;
    TextView temptv, time, longitude, latitude, humidity, sunrise, sunset, pressure, wind, country, city_nam, max_temp, min_temp, feels, visibility;
    ArrayAdapter<String> arrayAdapter;
    RecyclerView rvWeatherForecast;
    private ArrayList<WeatherForecast> weatherForecastArrayList;
    private WeatherForecastAdapter weatherForecastAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        imageButton = view.findViewById(R.id.img_button);
        btnAdd = view.findViewById(R.id.btn_add_city);
        imageView = view.findViewById(R.id.imageView);
        temptv = view.findViewById(R.id.textView3);
        time = view.findViewById(R.id.textView2);

        humidity = view.findViewById(R.id.humidity);
        pressure = view.findViewById(R.id.pressure);
        wind = view.findViewById(R.id.wind);
        country = view.findViewById(R.id.country);
        city_nam = view.findViewById(R.id.city_nam);
        sunrise = view.findViewById(R.id.sunrise);
        sunset = view.findViewById(R.id.sunset);
        max_temp = view.findViewById(R.id.temp_max);
        min_temp = view.findViewById(R.id.min_temp);
        visibility = view.findViewById(R.id.visibility);
        feels = view.findViewById(R.id.feels);
        rvWeatherForecast = view.findViewById(R.id.rvWeatherForecast);
        weatherForecastArrayList = new ArrayList<>();
        weatherForecastAdapter = new WeatherForecastAdapter(getActivity(), weatherForecastArrayList);
        rvWeatherForecast.setAdapter(weatherForecastAdapter);

        String[] cities = getResources().getStringArray(R.array.cities);
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cities);
        autoCompleteTextView.setAdapter(arrayAdapter);

        btnAdd.setVisibility(View.INVISIBLE);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = autoCompleteTextView.getText().toString();
                if (Arrays.asList(cities).contains(cityName)) {
                    getWeather(cityName);
                    addMyCity(cityName);
                } else {
                    Toast.makeText(getActivity(),"City not found", Toast.LENGTH_SHORT).show();
                }
                hideSoftKeyboard();

            }
        });
    }

    private void addMyCity(String cityName) {
        List<City> list = CityDatabase.getInstance(getActivity()).cityDAO().checkCity(cityName);
        if (list != null && !list.isEmpty()) {
            btnAdd.setVisibility(View.INVISIBLE);
            return;
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city = new City(cityName);
                    CityDatabase.getInstance(getActivity()).cityDAO().insertCity(city);

                    Bundle dataTransfer = new Bundle();
                    dataTransfer.putBoolean("updMC", true);
                    getParentFragmentManager().setFragmentResult("upd_my_city", dataTransfer);

                    btnAdd.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),"Add city successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getWeather(String city)
    {
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+APP_ID+"&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //find temperature
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("main");
                            int temp = object.getInt("temp");
                            temptv.setText(temp+"°");

                            //find country
                            JSONObject object8 = jsonObject.getJSONObject("sys");
                            String count = object8.getString("country");
                            country.setText(count);

                            //find city
                            String city = jsonObject.getString("name");
                            city_nam.setText(city+",  ");

                            //find icon
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String icon = obj.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

                            //find date & time
                            //Calendar calendar = Calendar.getInstance();
                            //SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy");
                            //String date = std.format(calendar.getTime());
                            //time.setText(date);

                            //find humidity
                            JSONObject object4 = jsonObject.getJSONObject("main");
                            int humidity_find = object4.getInt("humidity");
                            humidity.setText(humidity_find+"%");

                            //find pressure
                            JSONObject object7 = jsonObject.getJSONObject("main");
                            String pressure_find = object7.getString("pressure");
                            pressure.setText(pressure_find);

                            //find wind speed
                            JSONObject object9 = jsonObject.getJSONObject("wind");
                            String wind_find = object9.getString("speed");
                            wind.setText(wind_find);

                            //find sunrise
                            JSONObject object5 = jsonObject.getJSONObject("sys");
                            Long sunrise_find = object5.getLong("sunrise");
                            SimpleDateFormat std = new SimpleDateFormat("HH:mm");
                            String sunrise_result = std.format(sunrise_find);
                            sunrise.setText(sunrise_result);

                            //find sunset
                            JSONObject object6 = jsonObject.getJSONObject("sys");
                            Long sunset_find = object6.getLong("sunset");
                            String sunset_result = std.format(sunset_find);
                            sunset.setText(sunset_result);

                            //find visibility
                            int visibility_find = jsonObject.getInt("visibility") / 1000;
                            visibility.setText(Integer.toString(visibility_find) + "km");

//                            //find min temperature
//                            JSONObject object10 = jsonObject.getJSONObject("main");
//                            int mintemp = object10.getInt("temp_min") ;
//                            min_temp.setText("Min Temp\n"+mintemp+" °C");
//
//                            //find max temperature
//                            JSONObject object12 = jsonObject.getJSONObject("main");
//                            int maxtemp = object12.getInt("temp_max");
//                            max_temp.setText("Max Temp\n"+maxtemp+" °C");

                            //find feels
                            JSONObject object13 = jsonObject.getJSONObject("main");
                            int feels_find = object13.getInt("feels_like");
                            feels.setText("Feel like:"+feels_find+"°C");



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

        getWeatherForecast(city);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getWeatherForecast(String city) {
        String url ="http://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid="+APP_ID+"&units=metric";
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

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (weatherForecastAdapter != null ) {
            weatherForecastAdapter.release();
        }

    }


}