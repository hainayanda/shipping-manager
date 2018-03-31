package aditya.nayanda.shippingmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mongk on 3/25/2018.
 */

public class Agent implements Parcelable {

    public static final Creator<Agent> CREATOR = new Creator<Agent>() {
        @Override
        public Agent createFromParcel(Parcel in) {
            return new Agent(in);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };
    private String employeeId;
    private String firstName;
    private String lastName;
    private String userPhone;
    private String userEmail;
    private String userPassword;

    protected Agent(Parcel in) {
        employeeId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        userPhone = in.readString();
        userEmail = in.readString();
        userPassword = in.readString();
    }

    public Agent(String employeeId, String firstName, String lastName, String userPhone, String userEmail, String userPassword) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public static Agent newDummyInstance() {
        return new Agent("2001607733", "Pranata",
                "Chandra", "08748650142", "pranata@gmail.com",
                "12345");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(employeeId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(userPhone);
        dest.writeString(userEmail);
        dest.writeString(userPassword);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
