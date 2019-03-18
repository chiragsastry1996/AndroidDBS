package com.hash.nishant.dbs.Status;

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

import com.hash.nishant.dbs.Utility.HttpAuthHandler;
import com.hash.nishant.dbs.Utility.Registration;
import com.hash.nishant.dbs.Home.Home;
import com.hash.nishant.dbs.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatusActivity extends AppCompatActivity {
    private List<Status> statusList;
    private StatusAdapter statusAdapter;
    private RecyclerView recyclerView;
    ProgressBar progressBar;

    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusactivity);

        backbutton = (ImageView)findViewById(R.id.back);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatusActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        statusList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.news_list_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        new GetStatus().execute();
     }

    public class GetStatus extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "PhoneActivity";
        ArrayList<ArrayList<String>> detailLists = new ArrayList<>();

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
            String jsonStr = sh.makeServiceCall("https://hclitsmsbox.service-now.com/api/now/table/incident?sysparm_query=caller_id%3D" +
                    Registration.system_id +
                    "&sysparm_display_value=true&sysparm_exclude_reference_link=false");


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray response = jsonObj.getJSONArray("result");

                    for(int i=0;i<response.length();i++){

                        JSONObject details = response.getJSONObject(i);
                        String incidentnumber = details.getString("number");
                        String status = details.getString("state");
                        JSONObject display = details.getJSONObject("u_reported_ci_service");
                        String display_value = display.getString("display_value");

//                        System.out.println("ccccc" + display_value);

                        ArrayList<String> detail = new ArrayList<>();

                        detail.add(incidentnumber);
                        detail.add(status);
                        detail.add(display_value);
//                        detail.add(value);
//                        detail.add(flag);

                        detailLists.add(detail);

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
            for (int j = 0; j < detailLists.size(); j++){
                statusList.add(new com.hash.nishant.dbs.Status.Status(detailLists.get(j).get(0), detailLists.get(j).get(1), detailLists.get(j).get(2)));
            }
            statusAdapter = new StatusAdapter(StatusActivity.this, statusList);
            recyclerView.setAdapter(statusAdapter);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(StatusActivity.this, Home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();  // optional depending on your needs
    }

}

