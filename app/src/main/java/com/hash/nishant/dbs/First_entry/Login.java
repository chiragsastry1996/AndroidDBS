package com.hash.nishant.dbs.First_entry;

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
import android.widget.TextView;

import com.hash.nishant.dbs.R;
import com.hash.nishant.dbs.Utility.HttpAuthHandler;
import com.hash.nishant.dbs.Utility.RegisterUserClass;
import com.hash.nishant.dbs.Utility.Registration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView registration;
    String user_string, pass_string;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    public static String access_token = "NULL";
    public static final String REGISTER_URL = "http://23.97.53.92/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator);
        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.auth);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_horizontal);
        registration = (TextView)findViewById(R.id.register);

        progressBar.setVisibility(View.GONE);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_string = username.getText().toString();
                pass_string = password.getText().toString();
                new GetDetails().execute();
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
            if(!sys_id.equals("empty")) {
                register(user_string,pass_string);
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getApplicationContext(), "User doesn't exist in Volvo", Toast.LENGTH_LONG).show();
                        Snackbar.make(coordinatorLayout, "User doesn't exist in Volvo", Snackbar.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    public void register(final String user_string, final String pass_string) {
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
                data.put("password", params[1]);

                String result = ruc.sendPostRequest(REGISTER_URL, data);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);

                if(s.contains("error")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Authentication Error", Toast.LENGTH_LONG).show();
                            Snackbar.make(coordinatorLayout, "Authentication Error", Snackbar.LENGTH_LONG).show();

                        }
                    });
                }
                else if(s.contains("User does not exist")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Authentication Error", Toast.LENGTH_LONG).show();
                            Snackbar.make(coordinatorLayout, "User doesn't exist, Please Register", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                else if(s.contains("Wrong Password")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
                            Snackbar.make(coordinatorLayout, "Wrong password", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    access_token = s;
                    SharedPreferences.Editor editor = getSharedPreferences("Version", MODE_PRIVATE).edit();
                    editor.putString("username", user_string);
                    editor.putString("password", pass_string);
                    editor.putString("access_token", access_token);
                    editor.putString("login", "true");
                    editor.apply();

                    Intent intent = new Intent(Login.this, PinSetup.class);
                    startActivity(intent);
                    finish();
                }
            }

        }

        RegisterUser ru = new RegisterUser();
        ru.execute(user_string,pass_string);
    }

}
