package com.hash.nishant.dbs.Standard_Entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hash.nishant.dbs.Utility.HttpAuthHandler;
import com.hash.nishant.dbs.First_entry.Login;
import com.hash.nishant.dbs.Utility.Registration;
import com.hash.nishant.dbs.Home.Home;
import com.hash.nishant.dbs.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class QuickAccess extends AppCompatActivity {

    EditText editText_pin;
    String pin_string = null,
            user_string = null,
            pass_string = null,
            access =null,
            pin = null;
    Button pin_auth, forgot_password;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_access);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);
        editText_pin = (EditText)findViewById(R.id.pin);
        pin_auth = (Button)findViewById(R.id.pin_auth);
        forgot_password = (Button)findViewById(R.id.forgot_password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_horizontal);

        progressBar.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("Version", MODE_PRIVATE);
        user_string = prefs.getString("username", "0");
        pass_string = prefs.getString("password", "0");
        access = prefs.getString("access_token","0");
        pin = prefs.getString("pin", "0");



        pin_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin_string = editText_pin.getText().toString();
                if (pin_string.equals(pin)) {

                    SharedPreferences.Editor editor = getSharedPreferences("Version", MODE_PRIVATE).edit();
                    editor.putString("username", user_string);
                    editor.apply();

                    Login.access_token=access;
                    new GetDetails().execute();


                } else {

//                    Toast.makeText(getApplicationContext(), "Incorrect pin", Toast.LENGTH_LONG).show();
                    Snackbar.make(coordinatorLayout, "Incorrect pin", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuickAccess.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public class GetDetails extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "RegisterActivity";
        String sys_id = "empty";
        @Override
        public void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        //GET request done in background
        @Override
        public Void doInBackground(Void... arg0) {
            //Calling the HTTPHandler
            HttpAuthHandler sh = new HttpAuthHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://hclitsmsbox.service-now.com/api/now/table/sys_user?sysparm_query=employee_number%3D" +
                    user_string +
                    "&sysparm_display_value=true&sysparm_exclude_reference_link=false&sysparm_fields=sys_id%2Cfirst_name%2Clast_name&sysparm_limit=1");


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray response = jsonObj.getJSONArray("result");

                    for(int i=0;i<response.length();i++){

                        JSONObject details = response.getJSONObject(i);
                        Registration.system_id = sys_id = details.getString("sys_id");
                        Registration.last_name = details.getString("last_name");
                        Registration.first_name = details.getString("first_name");

                    }

                } catch (final Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            if(!sys_id.equals("empty")) {
                Intent intent = new Intent(QuickAccess.this, Home.class);
                startActivity(intent);
                finish();
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "User doesn't exist in Volvo", Toast.LENGTH_LONG).show();
                        Snackbar.make(coordinatorLayout, "User doesn't exist in Volvo", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

}
