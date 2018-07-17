package com.discoveries.jeremy.sunrisesunsetapp;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 13.07.2018.
 */

public class CheckLocation {

    private String locationName;

    private float latitude;
    private float longitude;

    private Geocoder geocoder;

    private List<Address> addressList;

    public CheckLocation(String locationName , Geocoder geocoder) {
        this.locationName = locationName;
        this.geocoder = geocoder;
    }

    public float getLatitude() {

        try {
            addressList = geocoder.getFromLocationName(locationName , 1);
            latitude = (float)addressList.get(0).getLatitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latitude;

    }

    public float getLongitude() {

        try {
            addressList = geocoder.getFromLocationName(locationName , 1);
            longitude = (float)addressList.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return longitude;

    }
}
