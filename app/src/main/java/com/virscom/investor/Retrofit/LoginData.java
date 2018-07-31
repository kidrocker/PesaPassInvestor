package com.virscom.investor.Retrofit;

/**
 * Created by kiura on 3/28/2018.
 */

public class LoginData {
    String id;
    String userID;
    String mobileno;
    String deviceID;
    String pin;
    String loaned;
    String first_name;
    String last_name;
    String national_id;
    String contact_person;
    String contact_mobile;
    String date_created;
    String date_modified;
    String status;
    String loan_limit;

    public String getLoan_limit() {
        return loan_limit;
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getPin() {
        return pin;
    }

    public String getLoaned() {
        return loaned;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getNational_id() {
        return national_id;
    }

    public String getContact_person() {
        return contact_person;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public String getStatus() {
        return status;
    }
}
