package com.discoveries.jeremy.sunrisesunsetapp;

import android.widget.Chronometer;

import java.util.Date;

/**
 * Created by User on 30.07.2018.
 */

public class NavigatorHelper {

    private String speed , distance , time , lifeStatus , format;
    private Chronometer chronometer;
    private String date;

    public NavigatorHelper(Chronometer chronometer , String distance , String date , String format) {
        this.chronometer = chronometer;
        this.distance = distance;
        this.date = date;
        this.format = format;
    }

    public void convertTime() {
        char second = chronometer.getText().charAt(4);
        char minute = chronometer.getText().charAt(1);
    }

    public String getSpeed() {
        convertTime();
        speed = String.valueOf(Integer.valueOf(distance) / Integer.valueOf(time));
        return speed;
    }

    public String getLifeStatus() {
        switch (format) {
            case "m":
                int m = Integer.valueOf(distance);
                if (m == 0) {
                    lifeStatus = "You have big problems!";
                }
                else if (m > 0 && m < 2000) {
                    lifeStatus = "Your health is in dangerous!";
                }
                else if (m > 2000 && m < 4000) {
                    lifeStatus = "Your health will have some difficulties in future! You must walk more often!";
                }
                else if (m > 4000 && m < 6000) {
                    lifeStatus = "Your health level is normal! It's typically for most people , so try to change your lifestyle!";
                }
                else if (m > 6000 && m < 8000) {
                    lifeStatus = "You have really good health level! You are not like most of people , and this is great!";
                }
                else if (m > 8000) {
                    lifeStatus = "You have strong and athletic health level! You can be actually the best!";
                }
                break;
            case "km":
                int km = Integer.valueOf(distance);
                if (km == 0) {
                    lifeStatus = "You have big problems!";
                }
                else if (km > 0 && km < 2) {
                    lifeStatus = "Your health is in dangerous!";
                }
                else if (km > 2 && km < 4) {
                    lifeStatus = "Your health will have some difficulties in future! You must walk more often!";
                }
                else if (km > 4 && km < 6) {
                    lifeStatus = "Your health level is normal! It's typically for most people , so try to change your lifestyle!";
                }
                else if (km > 6 && km < 8) {
                    lifeStatus = "You have really good health level! You are not like most of people , and this is great!";
                }
                else if (km > 8) {
                    lifeStatus = "You have strong and athletic health level! You can be actually the best!";
                }
                break;
            case "cm":
                int cm = Integer.valueOf(distance);
                if (cm == 0) {
                    lifeStatus = "You have big problems!";
                }
                else if (cm > 0 && cm < 20000) {
                    lifeStatus = "Your health is in dangerous!";
                }
                else if (cm > 20000 && cm < 40000) {
                    lifeStatus = "Your health will have some difficulties in future! You must walk more often!";
                }
                else if (cm > 40000 && cm < 60000) {
                    lifeStatus = "Your health level is normal! It's typically for most people , so try to change your lifestyle!";
                }
                else if (cm > 60000 && cm < 80000) {
                    lifeStatus = "You have really good health level! You are not like most of people , and this is great!";
                }
                else if (cm > 80000) {
                    lifeStatus = "You have strong and athletic health level! You can be actually the best!";
                }
                break;
        }
        return lifeStatus;
    }
}
