package com.discoveries.jeremy.sunrisesunsetapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.discoveries.jeremy.sunrisesunsetapp.CheckAddress;
import com.discoveries.jeremy.sunrisesunsetapp.CheckLocation;
import com.discoveries.jeremy.sunrisesunsetapp.GPS;
import com.discoveries.jeremy.sunrisesunsetapp.LauncherActivity;
import com.discoveries.jeremy.sunrisesunsetapp.R;
import com.discoveries.jeremy.sunrisesunsetapp.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    TextView
            humidityView ,
            minTempView , maxTempView ,
            pressureSeaView , pressureGroundView ,
            windSpeedView ,
            cloudsView ,
            rainView , snowView;

    Button run , reset;

    EditText insertLocation;

    private String  latitude , longitude ,
            location ,
            humidity ,
            minTemp , maxTemp ,
            pressureSea , pressureGround ,
            windSpeed ,
            cloudness ,
            rainStatus , snowStatus;

    Geocoder geocoder;

    Handler handler;

    Runnable runWeatherRunnable;
    private String WEATHER_API_URL;
    private final String WEATHER_API_KEY = "1a96a3571f545b2d239ad1af5db63911";


    public WeatherFragment() {
        // Required empty public constructor
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        identify(v);
        initialize();
        setListeners();
        return v;
    }

    public void identify(View v) {

        run = (Button) v.findViewById(R.id.runBtn);
        reset = (Button) v.findViewById(R.id.resetBtn);
        insertLocation = (EditText) v.findViewById(R.id.insertLocation);
        humidityView = (TextView) v.findViewById(R.id.humidity);
        minTempView = (TextView) v.findViewById(R.id.temp_min);
        maxTempView = (TextView) v.findViewById(R.id.temp_max);
        pressureSeaView = (TextView) v.findViewById(R.id.pressure_sea_level);
        pressureGroundView = (TextView) v.findViewById(R.id.pressure_ground_level);
        windSpeedView = (TextView) v.findViewById(R.id.wind_speed);
        cloudsView = (TextView) v.findViewById(R.id.clouds_status);
        rainView = (TextView) v.findViewById(R.id.rain_status);
        snowView  =(TextView) v.findViewById(R.id.snow_status);

    }

    public void initialize() {

        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        latitude = getString(R.string.unknown);
        longitude = getString(R.string.unknown);
        handler = new Handler();
        runWeatherRunnable = new Runnable() {
            @Override
            public void run() {
                humidityView.setText("Humidity: " + humidity);
                minTempView.setText("Minimal Temperature: " + minTemp);
                maxTempView.setText("Maximum Temperature: " + maxTemp);
                pressureSeaView.setText("Pressure On Sea Level: " + pressureSea);
                pressureGroundView.setText("Pressure On Ground Level: " + pressureGround);
                windSpeedView.setText("Wind Speed Status: " + windSpeed);
                cloudsView.setText("Clouds Status: " + cloudness);
                rainView.setText("Rain Status: " + rainStatus);
                snowView.setText("Snow Status: " + snowStatus);
            }
        };
    }

    public void setEmptyFields() {

        insertLocation.setText("");
        insertLocation.setHint("Enter location...");

        longitude = getString(R.string.unknown);
        latitude = getString(R.string.unknown);

        humidityView.setText(R.string.humidity);
        minTempView.setText(R.string.temp_min);
        maxTempView.setText(R.string.temp_max);
        pressureSeaView.setText(R.string.pressure_sea_level);
        pressureGroundView.setText(R.string.pressure_ground_level);
        windSpeedView.setText(R.string.clouds_status);
        cloudsView.setText(R.string.clouds_status);
        rainView.setText(R.string.rain_status);
        snowView.setText(R.string.snow_status);

    }
    private void setListeners() {

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread gettingData = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("geoData" , Context.MODE_PRIVATE);
                        longitude = sharedPreferences.getString("longitude" , null);
                        latitude = sharedPreferences.getString("latitude" , null);
                    }
                });
                gettingData.start();

                location = insertLocation.getText().toString();

                if ((latitude.equals("unknown")|| longitude.equals("unknown")) && !location.equals("")) {
                    Thread runLocation = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CheckLocation checkLocation = new CheckLocation(location, geocoder);
                            latitude = String.valueOf(checkLocation.getLatitude());
                            longitude = String.valueOf(checkLocation.getLongitude());
                        }
                    });
                    runLocation.start();
                    Toast.makeText(getActivity().getApplicationContext() , "It works" , Toast.LENGTH_SHORT).show();
                }
                else if ((latitude.equals("unknown") || longitude.equals("unknown")) && location.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext() , "Please run GPS service or set location" , Toast.LENGTH_SHORT).show();
                }
                else if ((!latitude.equals("unknown") || !longitude.equals("unknown")) && location.equals("")) {
                    runWeather();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmptyFields();
                handler.removeCallbacks(runWeatherRunnable);
            }
        });

    }

    public void runWeather() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + longitude + "&lon=" + latitude + "&appid=" + WEATHER_API_KEY;

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                //That request doesn't work yet:(
                final JsonObjectRequest jsonObjectWeatherRequest = new JsonObjectRequest(Request.Method.GET, WEATHER_API_URL , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject main = response.getJSONObject("main");
                            JSONObject wind = response.getJSONObject("wind");
                            JSONObject clouds = response.getJSONObject("clouds");
                            JSONObject rain = response.getJSONObject("rain");
                            JSONObject snow = response.getJSONObject("snow");

                            //main
                            humidity = String.valueOf(main.getInt("humidity"));
                            pressureSea = String.valueOf(main.getInt("sea_level"));
                            pressureGround = String.valueOf(main.getInt("grnd_level"));
                            Weather weather = new Weather(main.getDouble("temp_min") , main.getDouble("temp_max") , wind.getDouble("speed") , clouds.getInt("all") , rain.getInt("3h") , snow.getInt("3h"));
                            minTemp = weather.toMinCelsius();
                            maxTemp = weather.toMaxCelsius();


                            //wind
                            windSpeed = weather.speedStatus();

                            //clouds
                            cloudness = weather.cloudsStatus();

                            //rain
                            rainStatus = weather.rainStatus();

                            //snow
                            snowStatus = weather.snowStatus();

                            handler.post(runWeatherRunnable);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext() , "Please connect to Internet!" , Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonObjectWeatherRequest);
            }
        });
        thread.start();
    }

}
