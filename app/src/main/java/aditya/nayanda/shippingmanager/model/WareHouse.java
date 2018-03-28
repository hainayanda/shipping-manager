package aditya.nayanda.shippingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nayanda on 28/03/18.
 */

public class WareHouse implements Parcelable, Locator {
    public static final Creator<WareHouse> CREATOR = new Creator<WareHouse>() {
        @Override
        public WareHouse createFromParcel(Parcel in) {
            return new WareHouse(in);
        }

        @Override
        public WareHouse[] newArray(int size) {
            return new WareHouse[size];
        }
    };
    private String name;
    private String address;
    private LatLng location;

    public WareHouse(String name, String address, LatLng location) {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    protected WareHouse(Parcel in) {
        name = in.readString();
        address = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static WareHouse newDummyInstance() {
        return new WareHouse("Sunter Warehouse",
                "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                new LatLng(-6.147094, 106.889167));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLocation() {
        return location;
    }

    @Override
    public String getDescription() {
        return getName();
    }
}
