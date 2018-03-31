package aditya.nayanda.shippingmanager.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nayanda on 28/03/18.
 */

public interface Locator {
    LatLng getLocation();

    String getDescription();
}
