package com.discoveries.jeremy.sunrisesunsetapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.discoveries.jeremy.sunrisesunsetapp.CheckLocation;
import com.discoveries.jeremy.sunrisesunsetapp.LauncherActivity;
import com.discoveries.jeremy.sunrisesunsetapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SunriseSunsetFragment extends Fragment {

    TextView
            sunsetView , sunriseView ,
            solarNoonView ,
            dayLengthView ,
            civilTwilightBeginView , civilTwilightEndView ,
            nauticalTwilightBeginView , nauticalTwilightEndView ,
            astronomicalTwilightBeginView  , astronomicalTwilightEndView;

    Button run , reset;

    EditText insertLocation;

    private String  latitude , longitude ,
            location ,
            sunrise , sunset ,
            day_length ,
            solar_noon ,
            civil_twilight_begin , civil_twilight_end ,
            nautical_twilight_begin , nautical_twilight_end ,
            astronomical_twilight_begin , astronomical_twilight_end;

    Geocoder geocoder;

    Handler handler;

    Runnable runDataRunnable;

    private String SUNSET_API_URL;

    public SunriseSunsetFragment() {
        // Required empty public constructor
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sunrise_sunset, container, false);
        identify(v);
        initialize();
        setListeners();
        return v;
    }

    public void identify(View v) {

        run = (Button) v.findViewById(R.id.runBtn);
        reset = (Button) v.findViewById(R.id.resetBtn);
        insertLocation = (EditText) v.findViewById(R.id.insertLocation);
        sunriseView = (TextView) v.findViewById(R.id.sunrise);
        sunsetView = (TextView) v.findViewById(R.id.sunset);
        solarNoonView = (TextView) v.findViewById(R.id.solarNoon);
        dayLengthView = (TextView) v.findViewById(R.id.dayLength);
        civilTwilightBeginView = (TextView) v.findViewById(R.id.civilTwilightBegin);
        civilTwilightEndView= (TextView) v.findViewById(R.id.civilTwilightEnd);
        nauticalTwilightBeginView = (TextView) v.findViewById(R.id.nauticalTwilightBegin);
        nauticalTwilightEndView= (TextView) v.findViewById(R.id.nauticalTwilightEnd);
        astronomicalTwilightBeginView = (TextView) v.findViewById(R.id.astronomicalTwilightBegin);
        astronomicalTwilightEndView = (TextView) v.findViewById(R.id.astronomicalTwilightEnd);

    }

    public void initialize() {

        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        longitude = getString(R.string.unknown);
        latitude = getString(R.string.unknown);
        handler = new Handler();
        runDataRunnable = new Runnable() {
            @Override
            public void run() {
                sunriseView.setText("Sunrise:" + sunrise);
                sunsetView.setText("Sunset:" + sunset);
                solarNoonView.setText("Solar Noon:" + solar_noon);
                dayLengthView.setText("Day Length:" + day_length);
                civilTwilightBeginView.setText("Civil Twilight begin at:" + civil_twilight_begin);
                civilTwilightEndView.setText("Civil Twilight end at:" + civil_twilight_end);
                nauticalTwilightBeginView.setText("Nautical Twilight start at:" + nautical_twilight_begin);
                nauticalTwilightEndView.setText("Nautical Twilight end at:" + nautical_twilight_end);
                astronomicalTwilightBeginView.setText("Astronomical Twilight start at:" + astronomical_twilight_begin);
                astronomicalTwilightEndView.setText("Astronomical Twilight end at:" + astronomical_twilight_end);
            }
        };
    }

    public void setEmptyFields() {

        insertLocation.setText("");
        insertLocation.setHint("Enter location...");

        sunriseView.setText(R.string.Sunrise);
        sunsetView.setText(R.string.Sunset);
        solarNoonView.setText(R.string.SolarNoon);
        dayLengthView.setText(R.string.DayLength);
        civilTwilightBeginView.setText(R.string.CivilTwilightBegin);
        civilTwilightEndView.setText(R.string.CivilTwilightEnd);
        nauticalTwilightBeginView.setText(R.string.NauticalTwilightBegin);
        nauticalTwilightEndView.setText(R.string.NauticalTwilightEnd);
        astronomicalTwilightBeginView.setText(R.string.AstronomicalTwilightBegin);
        astronomicalTwilightEndView.setText(R.string.AstronomicalTwilightEnd);

        longitude = getString(R.string.unknown);
        latitude = getString(R.string.unknown);

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
                    runSunsetSunrise();
                    Toast.makeText(getActivity().getApplicationContext() , "It works" , Toast.LENGTH_SHORT).show();
                }
                else if ((latitude.equals("unknown") || longitude.equals("unknown")) && location.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext() , "Please run GPS service or set location" , Toast.LENGTH_SHORT).show();
                }
                else if ((!latitude.equals("unknown") || !longitude.equals("unknown")) && location.equals("")) {
                    runSunsetSunrise();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmptyFields();
                handler.removeCallbacks(runDataRunnable);
            }
        });

    }

    public void runSunsetSunrise() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SUNSET_API_URL = "https://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude;

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                final JsonObjectRequest jsonObjectSunsetriseRequest = new JsonObjectRequest(Request.Method.GET, SUNSET_API_URL , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonObjectResult = response.getJSONObject("results");

                            sunrise = jsonObjectResult.getString("sunrise");
                            sunset = jsonObjectResult.getString("sunset");
                            solar_noon = jsonObjectResult.getString("solar_noon");
                            day_length = jsonObjectResult.getString("day_length");
                            civil_twilight_begin = jsonObjectResult.getString("civil_twilight_begin");
                            civil_twilight_end = jsonObjectResult.getString("civil_twilight_end");
                            nautical_twilight_begin = jsonObjectResult.getString("nautical_twilight_begin");
                            nautical_twilight_end = jsonObjectResult.getString("nautical_twilight_end");
                            astronomical_twilight_begin = jsonObjectResult.getString("astronomical_twilight_begin");
                            astronomical_twilight_end = jsonObjectResult.getString("astronomical_twilight_end");

                            handler.post(runDataRunnable);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please connect to Internet!" , Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonObjectSunsetriseRequest);
            }
        });
        thread.start();
    }

}
