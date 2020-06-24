package de.htwBerlin.ois.viewModels;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class ViewModelNavigation extends ViewModel
{

    //------------Others------------

    /**
     * Gets the last know position of the device from the android LocationManager
     *
     * @return Location of the device
     */
    public Location getLastKnownLocation()
    {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers)
        {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                //just here to dismiss the warning/error ...checked it before
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null)
            {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())
            {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}