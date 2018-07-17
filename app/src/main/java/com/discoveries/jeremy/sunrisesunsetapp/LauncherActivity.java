package com.discoveries.jeremy.sunrisesunsetapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LauncherActivity extends AppCompatActivity {

    EditText insertLocation;

    Button run , check , reset;

    TextView countryCityView ,
             latitudeView , longitudeView ,
             sunsetView , sunriseView ,
             solarNoonView ,
             dayLengthView ,
             civilTwilightBeginView , civilTwilightEndView ,
             nauticalTwilightBeginView , nauticalTwilightEndView ,
             astronomicalTwilightBeginView  , astronomicalTwilightEndView;

    RadioGroup radioGroup;

    RadioButton myLocation , otherLocation;

    BroadcastReceiver broadcastReceiver;

    private String  latitude , longitude ,
                    location , country , city ,
                    sunrise , sunset ,
                    day_length ,
                    solar_noon ,
                    civil_twilight_begin , civil_twilight_end ,
                    nautical_twilight_begin , nautical_twilight_end ,
                    astronomical_twilight_begin , astronomical_twilight_end;

    Geocoder geocoder;

    Intent intent;

    Handler handler;

    Runnable checkDataRunnable , runDataRunnable;

    private String API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        identify();
        initialize();

        if(!runtime_permissions()) setListeners();
    }

    public void identify() {

        countryCityView = (TextView) findViewById(R.id.country_city);
        latitudeView = (TextView) findViewById(R.id.latitudeTextView);
        longitudeView = (TextView) findViewById(R.id.longitudeTextView);
        run = (Button) findViewById(R.id.runBtn);
        check = (Button) findViewById(R.id.checkBtn);
        reset = (Button) findViewById(R.id.resetBtn);
        insertLocation = (EditText) findViewById(R.id.insertLocation);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        myLocation = (RadioButton) findViewById(R.id.myLocation);
        otherLocation = (RadioButton) findViewById(R.id.otherLocation);
        sunriseView = (TextView) findViewById(R.id.sunrise);
        sunsetView = (TextView) findViewById(R.id.sunset);
        solarNoonView = (TextView) findViewById(R.id.solarNoon);
        dayLengthView = (TextView) findViewById(R.id.dayLength);
        civilTwilightBeginView = (TextView) findViewById(R.id.civilTwilightBegin);
        civilTwilightEndView= (TextView) findViewById(R.id.civilTwilightEnd);
        nauticalTwilightBeginView = (TextView) findViewById(R.id.nauticalTwilightBegin);
        nauticalTwilightEndView= (TextView) findViewById(R.id.nauticalTwilightEnd);
        astronomicalTwilightBeginView = (TextView) findViewById(R.id.astronomicalTwilightBegin);
        astronomicalTwilightEndView = (TextView) findViewById(R.id.astronomicalTwilightEnd);

    }

    public void initialize() {

        intent = new Intent(getApplicationContext(), GPS.class);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        latitude = latitudeView.getText().toString();
        longitude = longitudeView.getText().toString();
        handler = new Handler();
        checkDataRunnable = new Runnable() {
            @Override
            public void run() {
                latitudeView.setText(latitude);
                longitudeView.setText(longitude);
                countryCityView.setText(country + "/" + city);
            }
        };
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

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitude = intent.getStringExtra("latitude");
                    longitude = intent.getStringExtra("longitude");

                    runCheckAddress(Float.valueOf(latitude) , Float.valueOf(longitude));

                    latitudeView.setText(latitude);
                    longitudeView.setText(longitude);

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("check_location"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void runCheckLocation() {
        location = insertLocation.getText().toString();
        if (!location.equals("")) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    CheckLocation checkLocation = new CheckLocation(location, geocoder);
                    latitude = String.valueOf(checkLocation.getLatitude());
                    longitude = String.valueOf(checkLocation.getLongitude());

                    CheckAddress checkAddress = new CheckAddress(Float.valueOf(longitude), Float.valueOf(latitude), geocoder);
                    city = checkAddress.getCity();
                    country = checkAddress.getCountry();

                    handler.post(checkDataRunnable);

                }
            });
            thread.start();
        } else {
            Toast.makeText(getApplicationContext(), "Please insert location , that you want to check!", Toast.LENGTH_SHORT).show();
        }
    }

    public void runCheckAddress(float latitude , float longitude) {

        CheckAddress checkAddress = new CheckAddress(longitude , latitude , geocoder);
        city = checkAddress.getCity();
        country = checkAddress.getCountry();
        countryCityView.setText(country + "/" + city);

    }

    private void setListeners() {

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myLocation.isChecked()) startService(intent);
                else if (otherLocation.isChecked()) runCheckLocation();

                }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude.equals("unknown")|| longitude.equals("unknown")) {
                    Toast.makeText(LauncherActivity.this , "Set location , for running!" , Toast.LENGTH_SHORT).show();
                }
                else {
                    runSunRiseSunSet();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmptyFields();
                handler.removeCallbacks(checkDataRunnable);
                handler.removeCallbacks(runDataRunnable);
            }
        });

    }

    public void runSunRiseSunSet() {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        API_URL = "https://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude;

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL , new Response.Listener<JSONObject>() {
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
                                Toast.makeText(LauncherActivity.this , "Please connect to Internet!" , Toast.LENGTH_LONG).show();
                            }
                        });

                        requestQueue.add(jsonObjectRequest);
                    }
                });
                thread.start();

            }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                setListeners();
            }else {
                LauncherActivity.this.finish();
            }
        }
    }

    public void setEmptyFields() {

        insertLocation.setText("");
        insertLocation.setHint("Enter location...");
        latitudeView.setText(R.string.unknown);
        longitudeView.setText(R.string.unknown);
        countryCityView.setText(R.string.CountryCity);
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
        stopService(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.launcher_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.exit:
                LauncherActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
