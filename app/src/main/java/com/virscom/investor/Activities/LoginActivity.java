package com.virscom.investor.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.virscom.investor.R;
import com.virscom.investor.Retrofit.Response;
import com.virscom.investor.Retrofit.ServiceGenerator;
import com.virscom.investor.Utils.RetrofitCalls;


import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText pin;
    FirebaseUser user;
    ProgressDialog pd;
    SharedPreferences prefs;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if session.
        //user should not login without session set
        prefs = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        boolean session = prefs.getBoolean("session",false);
        if (!session){
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        user = FirebaseAuth.getInstance().getCurrentUser();
        login = findViewById(R.id.bt_login);
        pin = findViewById(R.id.ed_pin);
        dialog = new Dialog(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.getText().toString().length() > 3) {
                    if (isconnected()) {
                        pd = new ProgressDialog(LoginActivity.this);
                        pd.setTitle("LOGIN");
                        pd.setMessage("Just a moment while we log you in");
                        pd.setCancelable(false);
                        pd.show();
                        RetrofitCalls calls = ServiceGenerator.createService(RetrofitCalls.class);
                        Call<Response> call = calls.loginUser("customer_login", Integer.parseInt(pin.getText().toString()), user.getUid());
                        call.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(retrofit2.Call<Response> call, retrofit2.Response<Response> response) {
                                pd.dismiss();
                               Log.e("RESPONSE", response.body().toString());

                                //check if status is equal to 1 which is what is expected to load data
                                if (response.body().getStatus().contentEquals("1")) {
                                    Log.e("RESPONSE", response.toString());
                                    //user exist prompt to login
                                    Bundle args = new Bundle();
                                    args.putString("loan_limit",response.body().getLoginData().getLoan_limit());
                                    args.putString("loaned",response.body().getLoginData().getLoaned());
                                    args.putString("first_name",response.body().getLoginData().getFirst_name());
                                    args.putString("last_name",response.body().getLoginData().getLast_name());
                                    args.putString("national_id",response.body().getLoginData().getNational_id());
                                    args.putString("contact_person",response.body().getLoginData().getContact_person());
                                    args.putString("contact_mobile",response.body().getLoginData().getContact_mobile());

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtras(args);
                                    startActivity(intent);
                                    finish();


                                } else if (response.body().getStatus().contentEquals("0")) {
                                    //user pin  is  wrong
                                    pin.setText("");
                                    dialog.setContentView(R.layout.prompt_popup);
                                    final Button btOk = dialog.findViewById(R.id.bt_ok);
                                    final TextView tvMessage = dialog.findViewById(R.id.tv_message);
                                    tvMessage.setText("You have entered the wrong pin. Double check that and try again");
                                    btOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<Response> call, Throwable t) {
                                //failed Prompt
                                pd.dismiss();
                                Log.e("ERROR", t.getLocalizedMessage());

                                dialog.setContentView(R.layout.prompt_popup);
                                final Button btOk = dialog.findViewById(R.id.bt_ok);
                                final TextView tvMessage = dialog.findViewById(R.id.tv_message);
                                tvMessage.setText("Unable to connect to Pesa Pass. Please check your connection and retry");
                                btOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                            }
                        });
                    }else {
                        dialog.setContentView(R.layout.prompt_popup);
                        final Button btOk = dialog.findViewById(R.id.bt_ok);
                        final TextView tvMessage = dialog.findViewById(R.id.tv_message);
                        tvMessage.setText("It seems like you are not connected to the internet. Turn it on and lets try that again");
                        btOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }

                }else {
                    Toast.makeText(LoginActivity.this,"PIN MUST BE 4 DIGITS",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public boolean isconnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
