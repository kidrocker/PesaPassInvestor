package com.virscom.investor.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.intentfilter.androidpermissions.PermissionManager;
import com.virscom.investor.R;

import java.util.Arrays;
import java.util.List;


public class Splash extends AppCompatActivity {
    Button accept;
    private static final int RC_SIGN_IN = 123;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if session started
        prefs = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
         boolean session = prefs.getBoolean("session",false);
         if (session){
             startActivity(new Intent(Splash.this,LoginActivity.class));
             finish();
         }

        setContentView(R.layout.activity_splash);
        changeStatusBarColor();
        getSupportActionBar().hide();
        accept = findViewById(R.id.bt_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog();
            }
        });


    }
    private void changeStatusBarColor() {
        // set status bar clear
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.bluegrey));
        }
    }
    private void loadDialog(){
        final Dialog dialog = new Dialog(Splash.this);
        dialog.setContentView(R.layout.permission_popup);
        TextView _continue = dialog.findViewById(R.id.tv_continue);

        _continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                TODO ask for permissions then dismiss
                TODO authenticate the phone number when all permissions
                 */

                checkUserPermissions();

                dialog.dismiss();


            }
        });
        dialog.show();
    }

    private void checkUserPermissions() {
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS,Manifest.permission.READ_CALL_LOG};
        List<String> PERMISSIONS_AS_COLLECTION = Arrays.asList(PERMISSIONS);
        PermissionManager permissionManager = PermissionManager.getInstance(this);
        permissionManager.checkPermissions(PERMISSIONS_AS_COLLECTION, new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                Log.e("Permissions","Granted");
                getPhoneAuth();
            }

            @Override
            public void onPermissionDenied() {
                Log.e("Permissions","Denied");
            }
        });
    }

    private void getPhoneAuth() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(Splash.this, RegisterActivity.class));
        }else{
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID;

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(Splash.this, "Authenticated", Toast.LENGTH_LONG).show();
                userID = user.getUid();
                Intent x = new Intent(Splash.this, RegisterActivity.class);
                startActivity(x);

            } else {
                Toast.makeText(Splash.this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

}
