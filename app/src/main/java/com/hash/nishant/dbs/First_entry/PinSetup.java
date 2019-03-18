package com.hash.nishant.dbs.First_entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hash.nishant.dbs.Home.Home;
import com.hash.nishant.dbs.R;

public class PinSetup extends AppCompatActivity {

    EditText editText_pin;
    String pin_string = null;
    Button pin_auth;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_setup);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);
        editText_pin = (EditText)findViewById(R.id.pin);
        pin_auth = (Button)findViewById(R.id.pin_auth);




        pin_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin_string = editText_pin.getText().toString();
                if(pin_string.length()==4) {

                    SharedPreferences.Editor editor = getSharedPreferences("Version", MODE_PRIVATE).edit();
                    editor.putString("pin", pin_string);
                    editor.apply();

                    SharedPreferences prefs = getSharedPreferences("Version", MODE_PRIVATE);
                    String username = prefs.getString("username", "0");
                    String password = prefs.getString("password", "0");
                    String pin = prefs.getString("pin", "0");

                    Toast.makeText(getApplicationContext(), "PIN setup successful", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(PinSetup.this, Home.class);
                    startActivity(intent);
                    finish();

                }
                else {
//                    Toast.makeText(getApplicationContext(), "4 digits required", Toast.LENGTH_LONG).show();
                    Snackbar.make(coordinatorLayout, "PIN is 4 digits only", Snackbar.LENGTH_LONG).show();
                }

            }
        });


    }
}

