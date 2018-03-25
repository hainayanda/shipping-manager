package aditya.nayanda.shippingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nayanda on 25/03/18.
 */

public class Receiver implements Parcelable {

    public static final Creator<Receiver> CREATOR = new Creator<Receiver>() {
        @Override
        public Receiver createFromParcel(Parcel in) {
            return new Receiver(in);
        }

        @Override
        public Receiver[] newArray(int size) {
            return new Receiver[size];
        }
    };
    private String firstName;
    private String lastName;
    private Gender gender;
    private String address;
    private LatLng location;

    protected Receiver(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        int gender = in.readInt();
        switch (gender) {
            case 0:
                this.gender = Gender.Female;
                break;
            default:
                this.gender = Gender.Male;
        }
        address = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
    }

    public Receiver(String firstName, String lastName, Gender gender, String address, LatLng location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.location = location;
    }

    public static Receiver newDummyInstance(@IntRange(from = 0, to = 9) int index) {
        switch (index) {
            case 0:
                return new Receiver("James", "Bond", Gender.Male,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.269977, 106.765158));
            case 1:
                return new Receiver("Amelia", "Earheart", Gender.Female,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.259576, 106.767668));
            case 2:
                return new Receiver("Bill", "Gates", Gender.Male,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.253413, 106.773304));
            case 3:
                return new Receiver("Meliana", "Monika", Gender.Female,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.251206, 106.778160));
            case 4:
                return new Receiver("Tommy", "Gunawan", Gender.Male,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.258368, 106.77156));
            case 5:
                return new Receiver("Ling", "Ming", Gender.Female,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.279392, 106.772587));
            case 6:
                return new Receiver("Muhammad", "Djarot", Gender.Male,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.277470, 106.781733));
            case 7:
                return new Receiver("Siti", "Aminah", Gender.Female,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.260239, 106.781849));
            case 8:
                return new Receiver("Markus", "Antonius", Gender.Male,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.250212, 106.781236));
            default:
                return new Receiver("Jessica", "Christalle", Gender.Female,
                        "Lorem ipsum dolor sit amet, ne cum ipsum atqui voluptaria, vocibus intellegam vis et",
                        new LatLng(-6.245706, 106.781456));

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Receiver)) return false;

        Receiver receiver = (Receiver) o;

        return (firstName != null ? firstName.equals(receiver.firstName) : receiver.firstName == null)
                && (lastName != null ? lastName.equals(receiver.lastName) : receiver.lastName == null)
                && gender == receiver.gender && (address != null ? address.equals(receiver.address) : receiver.address == null)
                && (location != null ? location.equals(receiver.location) : receiver.location == null);
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeInt(gender.raw);
        dest.writeString(address);
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLocation() {
        return location;
    }

    public Gender getGender() {
        return gender;
    }

    public enum Gender {
        Female(0), Male(1);

        int raw;

        Gender(int raw) {
            this.raw = raw;
        }
    }

}
