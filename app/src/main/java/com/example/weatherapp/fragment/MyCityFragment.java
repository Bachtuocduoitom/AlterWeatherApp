package com.example.weatherapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.adapter.CityAdapter;
import com.example.weatherapp.database.CityDatabase;
import com.example.weatherapp.model.City;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCityFragment extends Fragment {

    Button btn_add_city;
    RecyclerView rvCitiesList;
    ItemTouchHelper itemTouchHelper;
    public CityAdapter cityAdapter;
    public List<City> listCity;
    public MyCityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_city, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn_add_city = view.findViewById(R.id.btn_add_city);

        rvCitiesList = view.findViewById(R.id.rvCitiesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvCitiesList.setLayoutManager(linearLayoutManager);

        listCity = CityDatabase.getInstance(getActivity()).cityDAO().getListCity();
        cityAdapter = new CityAdapter(getActivity(), listCity);
        rvCitiesList.setAdapter(cityAdapter);

        btn_add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCityDialog(Gravity.CENTER);
            }
        });

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                City city = listCity.get(viewHolder.getAbsoluteAdapterPosition());
                CityDatabase.getInstance(getActivity()).cityDAO().deleteCity(city);
                listCity = CityDatabase.getInstance(getActivity()).cityDAO().getListCity();
                cityAdapter.setData(listCity);
                Toast.makeText(getActivity(), "Delete city successfully", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(rvCitiesList);

        updateListCity();
    }

    private void openAddCityDialog(int gravity) {
        final Dialog dialog = new Dialog(getActivity());
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
                    Toast.makeText(getActivity(), "Please Enter City Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                City city = new City(strCityName);
                if (isCityExit(city)) {
                    Toast.makeText(getActivity(), "City exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                CityDatabase.getInstance(getActivity()).cityDAO().insertCity(city);

                hideSoftKeyboard();

                listCity = CityDatabase.getInstance(getActivity()).cityDAO().getListCity();
                cityAdapter.setData(listCity);
                rvCitiesList.scrollToPosition(cityAdapter.getItemCount() - 1);

                dialog.dismiss();
                Toast.makeText(getActivity(), "Add city successfully", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    //hide soft keyboard
    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    //check city name
    private boolean isCityExit(City city) {
        List<City> list = CityDatabase.getInstance(getActivity()).cityDAO().checkCity(city.getCityName());
        return list != null && !list.isEmpty();
    }

    private void updateListCity() {
        getParentFragmentManager().setFragmentResultListener("upd_my_city", getActivity(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if(result.getBoolean("updMC")) {
                    listCity = CityDatabase.getInstance(getActivity()).cityDAO().getListCity();
                    cityAdapter.setData(listCity);
                }
            }
        });
    }
}