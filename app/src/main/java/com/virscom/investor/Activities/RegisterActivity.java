package com.virscom.investor.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.virscom.investor.R;
import com.virscom.investor.Retrofit.Response;
import com.virscom.investor.Retrofit.ServiceGenerator;
import com.virscom.investor.Utils.RetrofitCalls;

import retrofit2.Call;
import retrofit2.Callback;


public class RegisterActivity extends AppCompatActivity {
    ProgressDialog pd;
    FirebaseUser user;
    SharedPreferences prefs;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user = FirebaseAuth.getInstance().getCurrentUser();
        prefs = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        dialog = new Dialog(RegisterActivity.this);

        pd = new ProgressDialog(RegisterActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("We are checking your account. Be patient");
        pd.show();

        RetrofitCalls calls = ServiceGenerator.createService(RetrofitCalls.class);
        Call<Response> call = calls.checkUser("check_account", user.getUid());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(retrofit2.Call<Response> call, retrofit2.Response<Response> response) {
                pd.dismiss();
                Log.e("RESPONSE",response.toString());
                if (response.body().getStatus().contentEquals("1")){
                    Log.e("RESPONSE",response.toString());
                    //user exist prompt to login
                    //add session in to user preferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("session", true);
                    editor.apply();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();


                }else if (response.body().getStatus().contentEquals("0")){
                    //user does not exist add pin
                    startActivity(new Intent(RegisterActivity.this, UserPinActivity.class));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Response> call, Throwable t) {
                //failed Prompt
                Log.e("ERROR",t.getLocalizedMessage());
                dialog.setContentView(R.layout.prompt_popup);
                final Button btOk = dialog.findViewById(R.id.bt_ok);
                final TextView tvMessage = dialog.findViewById(R.id.tv_message);
                tvMessage.setText("An error occurred. Try that again while we check on the problem");
                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, Splash.class));
        finish();
    }


}
