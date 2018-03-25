package aditya.nayanda.shippingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

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
    private Receiver receiver;
    private ItemType type;
    private DeliveryStatus deliveryStatus;

    public Job(String itemName, String itemDetail, Receiver receiver, ItemType type, DeliveryStatus deliveryStatus) {
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.receiver = receiver;
        this.type = type;
        this.deliveryStatus = deliveryStatus;
    }

    public Job(Parcel in) {
        itemName = in.readString();
        itemDetail = in.readString();
        receiver = in.readParcelable(Receiver.class.getClassLoader());
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
        rawType = in.readInt();
        switch (rawType) {
            case 0:
                deliveryStatus = DeliveryStatus.UNPROCESSED;
                break;
            case 1:
                deliveryStatus = DeliveryStatus.DELIVERY_START;
                break;
            case 2:
                deliveryStatus = DeliveryStatus.DELIVERY_FAILED;
                break;
            case 3:
                deliveryStatus = DeliveryStatus.DELIVERY_COMPLETED;
                break;
            default:
                deliveryStatus = DeliveryStatus.UNPROCESSED;
        }
    }

    public static Job newDummyInstance(int i) {
        int type = new Random().nextInt(4);
        int index = i % 10;
        switch (type) {
            case 0:
                return new Job("OIL " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.OIL,
                        DeliveryStatus.UNPROCESSED);
            case 1:
                return new Job("GAS " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.GAS,
                        DeliveryStatus.UNPROCESSED);
            case 2:
                return new Job("LUBE " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.LUBE,
                        DeliveryStatus.UNPROCESSED);
            case 3:
                return new Job("PETROCHEMICAL " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.PETROCHEMICAL,
                        DeliveryStatus.UNPROCESSED);
            default:
                return new Job("OTHER " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.OTHER,
                        DeliveryStatus.UNPROCESSED);
        }
    }

    static Job newDummyHistoryInstance(int i) {
        int type = new Random().nextInt(4);
        int index = i % 10;

        int rawStatus = new Random().nextInt(100);
        DeliveryStatus deliveryStatus;
        if (rawStatus <= 10) {
            deliveryStatus = DeliveryStatus.DELIVERY_FAILED;
        } else {
            deliveryStatus = DeliveryStatus.DELIVERY_COMPLETED;
        }

        switch (type) {
            case 0:
                return new Job("OIL " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.OIL,
                        deliveryStatus);
            case 1:
                return new Job("GAS " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.GAS,
                        deliveryStatus);
            case 2:
                return new Job("LUBE " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.LUBE,
                        deliveryStatus);
            case 3:
                return new Job("PETROCHEMICAL " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.PETROCHEMICAL,
                        deliveryStatus);
            default:
                return new Job("OTHER " + i,
                        "Lorem ipsum dolor sit amet",
                        Receiver.newDummyInstance(index), ItemType.OTHER,
                        deliveryStatus);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemDetail);
        dest.writeParcelable(receiver, flags);
        dest.writeInt(type.raw);
        dest.writeInt(deliveryStatus.raw);
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

    public Receiver getReceiver() {
        return receiver;
    }

    public LatLng getLocation() {
        return receiver.getLocation();
    }

    public String getAddress() {
        return receiver.getAddress();
    }

    public ItemType getType() {
        return type;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public boolean isDelivered() {
        return deliveryStatus != DeliveryStatus.DELIVERY_FAILED;
    }

    public enum ItemType {
        OIL(0), GAS(1), LUBE(2), PETROCHEMICAL(3), OTHER(4);

        int raw;

        ItemType(int raw) {
            this.raw = raw;
        }
    }

    public enum DeliveryStatus {
        UNPROCESSED(0),
        DELIVERY_START(1),
        DELIVERY_FAILED(2),
        DELIVERY_COMPLETED(3);

        int raw;

        DeliveryStatus(int raw) {
            this.raw = raw;
        }
    }
}
