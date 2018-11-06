package com.kram.vlad.weather.geolocation;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kram.vlad.weather.activitys.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by vlad on 13.08.17.
 * Get user location
 */

public class GeoLocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = GeoLocationProvider.class.getSimpleName();

    private static final int PLAY_SERVICES_REQUEST = 1000;

    private GoogleApiClient googleApiClient;

    public GeoLocationProvider(MainActivity context) {
        if (checkPlayServices(context)) {
            googleApiClient = createApiClient(context);

            createLocationRequest(context);

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private GoogleApiClient createApiClient(Context context) {
        GoogleApiClient googleApiClient;

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
        return googleApiClient;
    }

    private void createLocationRequest(final GeoLocationCallback geoLocationCallback) {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        //mainActivity.geolocation(48.510473, 32.251812);

        result.setResultCallback(locationSettingsResult -> {
            final Status status = locationSettingsResult.getStatus();
            try {
                status.startResolutionForResult((AppCompatActivity)geoLocationCallback, LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Location location = getLocation((Context) geoLocationCallback);
                    Log.d(TAG, location.getLatitude() + "," + location.getLongitude());
                    geoLocationCallback.geolocation(location.getLatitude(), location.getLongitude());
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.d(TAG, "resolution");
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });

    }

    private Location getLocation(Context context) {
        //if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        // }

        // if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //    ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        //}

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;

        }
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private boolean checkPlayServices(AppCompatActivity context) {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(context, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    public String getAddress(double latitude, double longitude, Context context) throws IOException {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
        return addresses.get(0).getLocality();
    }
}

