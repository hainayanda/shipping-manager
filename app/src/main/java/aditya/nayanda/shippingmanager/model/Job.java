package aditya.nayanda.shippingmanager.model;

import android.location.Location;

import java.util.Random;


/**
 * Created by nayanda on 22/03/18.
 */

public class Job {

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

    public static Job newDummyInstance() {
        int type = new Random().nextInt(4);
        switch (type) {
            case 0:
                return new Job("OIL NAME",
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.OIL);
            case 1:
                return new Job("GAS NAME",
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.GAS);
            case 2:
                return new Job("LUBE NAME",
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.LUBE);
            case 3:
                return new Job("PETROCHEMICAL NAME",
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.PETROCHEMICAL);
            default:
                return new Job("OTHER NAME",
                        "Lorem ipsum dolor sit amet",
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        null, ItemType.OTHER);
        }
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
        OIL, GAS, LUBE, PETROCHEMICAL, OTHER
    }
}
