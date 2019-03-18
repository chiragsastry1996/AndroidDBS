package com.hash.nishant.dbs.Utility;

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
import android.widget.Toast;

import com.hash.nishant.dbs.First_entry.Login;
import com.hash.nishant.dbs.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Registration extends AppCompatActivity {

    EditText user_id, editEmail, password, confirm_password;
    String userID, email, pass;
    Button auth;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    public static String system_id, first_name, last_name;
    public static final String REGISTER_URL = "http://23.97.53.92/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);
        user_id = (EditText)findViewById(R.id.user_id);
        editEmail = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_horizontal);
        auth = (Button) findViewById(R.id.auth);

        progressBar.setVisibility(View.GONE);

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = user_id.getText().toString();
                email = editEmail.getText().toString();
                pass = password.getText().toString();
                String confirm = confirm_password.getText().toString();

                if(userID.isEmpty()||email.isEmpty()||pass.isEmpty()||confirm.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_LONG).show();
                    Snackbar.make(coordinatorLayout, "All fields are mandatory", Snackbar.LENGTH_LONG).show();
                }
                else if(pass.equals(confirm)) {
                   new GetDetails().execute();
                }

            }
        });

    }

    public class GetDetails extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "RegisterActivity";

        @Override
        public void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        String sys_id = "empty";
        //GET request done in background
        @Override
        public Void doInBackground(Void... arg0) {
            //Calling the HTTPHandler
            HttpAuthHandler sh = new HttpAuthHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://hclitsmsbox.service-now.com/api/now/table/sys_user?sysparm_query=employee_number%3D" +
                    userID +
                    "&sysparm_display_value=true&sysparm_exclude_reference_link=false&sysparm_fields=sys_id%2Cfirst_name%2Clast_name&sysparm_limit=1");


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray response = jsonObj.getJSONArray("result");

                    for(int i=0;i<response.length();i++){

                        JSONObject details = response.getJSONObject(i);
                        sys_id = system_id = details.getString("sys_id");
                        last_name = details.getString("last_name");
                        first_name = details.getString("first_name");
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
            if(!sys_id.equals("empty")) {
                register(userID,email,pass);
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "User doesn't exist in Volvo", Toast.LENGTH_LONG).show();
                        Snackbar.make(coordinatorLayout, "User doesn't exist in Volvo", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void register(String userID, final String email, String pass) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("userName", params[0]);
                data.put("email", params[1]);
                data.put("password", params[2]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                final String message = s;
                progressBar.setVisibility(View.GONE);
                if(s.contains("Registered Successfully!")){

                    SharedPreferences.Editor editor = getSharedPreferences("Version", MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_LONG).show();
//                            Snackbar.make(coordinatorLayout, "Registered Successfully", Snackbar.LENGTH_LONG).show();
                        }
                    });

                    Intent intent = new Intent(Registration.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }

        }

        RegisterUser ru = new RegisterUser();
        ru.execute(userID,email,pass);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Registration.this, Login.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

}
