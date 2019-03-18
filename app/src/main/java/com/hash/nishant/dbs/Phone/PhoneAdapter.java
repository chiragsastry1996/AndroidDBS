package com.hash.nishant.dbs.Phone;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hash.nishant.dbs.R;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.MyViewHolder>{

    private Context mContext;
    List<Phone> phoneList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        ImageView numberView, emailView;



        public MyViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            numberView = (ImageView) view.findViewById(R.id.number);
            emailView = (ImageView) view.findViewById(R.id.email);


        }
    }

    public PhoneAdapter(Context mContext, List<Phone> newsList) {
        this.mContext = mContext;
        this.phoneList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Phone phone = phoneList.get(position);
        holder.nameView.setText(phone.getName());
        holder.numberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone.getNumber()));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException activityException) {
                    Toast.makeText(mContext, "Call has failed", Toast.LENGTH_LONG).show();
                }

            }
        });
        holder.emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{phone.getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Issue: ");

                mContext.startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

}