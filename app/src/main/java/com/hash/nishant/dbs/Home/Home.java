package com.hash.nishant.dbs.Home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hash.nishant.dbs.Chat.Chat;
import com.hash.nishant.dbs.Phone.PhoneActivity;
import com.hash.nishant.dbs.R;
import com.hash.nishant.dbs.Status.StatusActivity;
import com.hash.nishant.dbs.Team.TeamActivity;

public class Home extends AppCompatActivity {

    LinearLayout staus, chat, phone, our_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        staus = (LinearLayout)findViewById(R.id.case_status);
        chat = (LinearLayout)findViewById(R.id.chat);
        phone = (LinearLayout)findViewById(R.id.page_phone);
        our_team = (LinearLayout)findViewById(R.id.our_team);

        staus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, StatusActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Chat.class);
                startActivity(intent);
                finish();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PhoneActivity.class);
                startActivity(intent);
                finish();
            }
        });

        our_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, TeamActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
