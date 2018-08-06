package com.discoveries.jeremy.sunrisesunsetapp;

/**
 * Created by User on 20.07.2018.
 */

public class Weather {

    private double minKelvin , maxKelvin , speed;
    private int cloudness , rain , snow;

    public Weather(double minKelvin , double maxKelvin , double speed , int cloudness , int rain , int snow) {
        this.minKelvin = minKelvin;
        this.maxKelvin = maxKelvin;
        this.speed = speed;
        this.cloudness = cloudness;
        this.rain = rain;
        this.snow = snow;
    }

    public String toMinCelsius() {
        double celsius = minKelvin - 273.15;
        return String.valueOf(celsius);
    }

    public String toMaxCelsius() {
        double celsius = maxKelvin - 273.15;
        return String.valueOf(celsius);
    }

    public String speedStatus() {
        String status = "";
        if (speed == 0) status = "Absent";
        else if (speed > 0 && speed < 3) status = "Little";
        else if (speed > 3 && speed < 6) status = "Medium";
        else if (speed > 6 && speed < 11) status = "Heavy";
        else if (speed > 11 && speed < 15) status = "Extreme";
        else if (speed > 15) status = "Disaster";
        return status;
    }

    public String cloudsStatus() {
        String status = "";
        if (cloudness == 0) status = "Clear sky";
        else if (cloudness > 0 && cloudness < 25) status = "Light";
        else if (cloudness > 25 && cloudness < 50) status = "Moderate";
        else if (cloudness > 50 && cloudness < 75) status = "Cloudy";
        else if (cloudness > 75 && cloudness < 95) status = "Very cloudy";
        else if (cloudness > 95 && cloudness <= 100) status = "Full of clouds";
        return status;
    }

    public String rainStatus() {
        String status = "";
        if (rain == 0) status = "Absent";
        else if (rain > 0 && rain < 0.5) status = "Slight";
        else if (rain > 0.5 && rain < 4) status = "Moderate";
        else if (rain > 4 && rain < 8) status = "Heavy";
        else if (rain > 8 && rain < 10) status = "Moderate shower";
        else if (rain > 10 && rain < 50) status = "Heavy shower";
        else if (rain > 50) status = "Violent shower";
        return status;
    }

    public String snowStatus() {
        String status = "";
        if (snow == 0) status = "Absent";
        else if (snow> 0 && snow < 0.5) status = "Slight";
        else if (snow > 0.5 && snow < 4) status = "Moderate";
        else if (snow > 4 && snow < 8) status = "Heavy";
        else if (snow > 8 && snow < 10) status = "Very heavy";
        else if (snow > 10 && snow < 50) status = "Insane";
        else if (snow > 50) status = "Extreme";
        return status;
    }
}
