package com.virscom.investor.Retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kiura on 3/28/2018.
 */

public class Response {
    String status;
    String message = "";
    @SerializedName("data")
    LoginData loginData;
    @SerializedName("account")
    LoanStatus loanStatus;
    String loaned;
    String loan_limit;

    public String getLoaned() {
        return loaned;
    }

    public String getLoan_limit() {
        return loan_limit;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }
}
