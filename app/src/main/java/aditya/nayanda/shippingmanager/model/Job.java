package aditya.nayanda.shippingmanager.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;


/**
 * Created by nayanda on 22/03/18.
 */

public class Job implements Parcelable {

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    private String itemName;
    private String itemDetail;
    private String Address;
    private Location location;
    private ItemType type;

    public Job(String itemName, String itemDetail, String address, Location location, ItemType type) {
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        Address = address;
        this.location = location;
        this.type = type;
    }

    public Job(Parcel in) {
        itemName = in.readString();
        itemDetail = in.readString();
        Address = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        int rawType = in.readInt();
        switch (rawType) {
            case 0:
                type = ItemType.OIL;
                break;
            case 1:
                type = ItemType.GAS;
                break;
            case 2:
                type = ItemType.LUBE;
                break;
            case 3:
                type = ItemType.PETROCHEMICAL;
                break;
            default:
                type = ItemType.OTHER;
        }
    }

    public static Job newDummyInstance(int i) {
        int type = new Random().nextInt(4);
        switch (type) {
            case 0:
                return new Job("OIL " + i,
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.OIL);
            case 1:
                return new Job("GAS " + i,
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.GAS);
            case 2:
                return new Job("LUBE " + i,
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.LUBE);
            case 3:
                return new Job("PETROCHEMICAL " + i,
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.PETROCHEMICAL);
            default:
                return new Job("OTHER " + i,
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.OTHER);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemDetail);
        dest.writeString(Address);
        dest.writeParcelable(location, flags);
        dest.writeInt(type.raw);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public String getAddress() {
        return Address;
    }

    public Location getLocation() {
        return location;
    }

    public ItemType getType() {
        return type;
    }

    public enum ItemType {
        OIL(0), GAS(1), LUBE(2), PETROCHEMICAL(3), OTHER(4);

        int raw;

        ItemType(int raw) {
            this.raw = raw;
        }
    }
}
