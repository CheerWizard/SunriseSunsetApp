package com.discoveries.jeremy.sunrisesunsetapp;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 13.07.2018.
 */

public class CheckAddress {

    private float longitude;
    private float latitude;

    private String country;
    private String city;
    private String area;
    private String addressLine;
    private String postalCode;

    private Geocoder geocoder;

    private List<Address> addressList;

    public CheckAddress(float longitude , float latitude , Geocoder geocoder) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.geocoder = geocoder;

    }

    public String getCountry() {

        try {
            addressList = geocoder.getFromLocation(latitude, longitude , 1);
            country = addressList.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return country;

    }

    public String getCity() {

        try {
            addressList = geocoder.getFromLocation(latitude , longitude , 1);
            city = addressList.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;

    }

    public String getArea() {

        try {
            addressList = geocoder.getFromLocation(latitude , longitude , 1);
            area = addressList.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return area;

    }

    public String getAddressLine() {

        try {
            addressList = geocoder.getFromLocation(latitude , longitude , 1);
            addressLine = addressList.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressLine;

    }

    public String getPostalCode() {

        try {
            addressList = geocoder.getFromLocation(latitude , longitude , 1);
            postalCode = addressList.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postalCode;

    }

}
