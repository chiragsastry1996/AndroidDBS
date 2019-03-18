package com.hash.nishant.dbs.Phone;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hash.nishant.dbs.Utility.HttpHandler;
import com.hash.nishant.dbs.Home.Home;
import com.hash.nishant.dbs.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends AppCompatActivity {
    private List<Phone> phoneList;
    private PhoneAdapter phoneAdapter;
    private RecyclerView recyclerView;
    ImageView backbutton;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneactivity);

        backbutton = (ImageView)findViewById(R.id.back);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        phoneList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.phone_list_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        phoneList.add(new Phone("India", "+918066914531", "support.dbs.india@volvo.com"));
        phoneList.add(new Phone("AU, NZ and Ocenia", "+61287138333", "support.dbs.india@volvo.com"));
        phoneList.add(new Phone("Middle East", "+97148038445", "support.dbs.india@volvo.com"));
        phoneList.add(new Phone("Africa", "+27871502400", "support.dbs.india@volvo.com"));
        phoneList.add(new Phone("North America", "0000000000", "support.dbs.india@volvo.com"));

        phoneAdapter = new PhoneAdapter(PhoneActivity.this, phoneList);
        recyclerView.setAdapter(phoneAdapter);

        //new GetNumbers().execute();

    }

    public class GetNumbers extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "PhoneActivity";
        ArrayList<ArrayList<String>> detailList = new ArrayList<>();

        @Override
        public void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        //GET request done in background
        @Override
        public Void doInBackground(Void... arg0) {
            //Calling the HTTPHandler
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://40.113.216.159/api/ourteam");


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray response = jsonObj.getJSONArray("result");

                    for(int i=0;i<response.length();i++){

                        JSONObject details = response.getJSONObject(i);
                        String user_name = details.getString("userName");
                        String email = details.getString("email");
                        String phone = details.getString("phone");
                        String image = details.getString("image");

                        ArrayList<String> detail = new ArrayList<>();

                        detail.add(user_name);
                        detail.add(email);
                        detail.add(phone);
                        detail.add(image);

                        detailList.add(detail);

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
//            for (int j = 0; j < detailList.size(); j++){
//                phoneList.add(new Phone(detailList.get(j).get(0), detailList.get(j).get(1), detailList.get(j).get(2)));
//            }
            progressBar.setVisibility(View.GONE);


            phoneAdapter = new PhoneAdapter(PhoneActivity.this, phoneList);
            recyclerView.setAdapter(phoneAdapter);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(PhoneActivity.this, Home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

}
