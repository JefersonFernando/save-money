package com.example.save_money_app;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationListener implements android.location.LocationListener {

    private static LocationListener instance;
    private double longitude;
    private double latitude;

    private LocationListener() {
    }

    public static LocationListener getInstance() {
        if (instance == null) {
            instance = new LocationListener();
        }
        return instance;
    }

    @Override
    public void onLocationChanged(Location loc) {
//        editLocation.setText("");
//        pb.setVisibility(View.INVISIBLE);
//        Toast.makeText(
//                getBaseContext(),
//                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//        String longitude = "Longitude: " + loc.getLongitude();
//        Log.v(TAG, longitude);
//        String latitude = "Latitude: " + loc.getLatitude();
//        Log.v(TAG, latitude);
//
//        /*------- To get city name from coordinates -------- */
//        String cityName = null;
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(loc.getLatitude(),
//                    loc.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                System.out.println(addresses.get(0).getLocality());
//                cityName = addresses.get(0).getLocality();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                + cityName;
//        editLocation.setText(s);

        this.longitude = loc.getLongitude();
        this.latitude = loc.getLatitude();

        Log.d("Localização:", "Latitude:" + this.latitude + " Longitude:" + this.longitude);

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
