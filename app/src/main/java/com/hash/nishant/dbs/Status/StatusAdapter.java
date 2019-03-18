package com.hash.nishant.dbs.Status;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hash.nishant.dbs.R;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder>{

    private Context mContext;
    List<Status> statusList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView incidentView, statusView, valueView, stateView;



        public MyViewHolder(View view) {
            super(view);
            incidentView = (TextView) view.findViewById(R.id.incident_number);
            statusView = (TextView) view.findViewById(R.id.case_status);
            valueView = (TextView) view.findViewById(R.id.value);
            stateView = (TextView) view.findViewById(R.id.state);


        }
    }

    public StatusAdapter(Context mContext, List<Status> newsList) {
        this.mContext = mContext;
        this.statusList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Status status = statusList.get(position);
        holder.incidentView.setText(status.getIncedent_number());
        holder.statusView.setText(status.getStatus());
        if(status.getStatus().contentEquals("In Progress")) {
            holder.statusView.setTextColor(Color.parseColor("#5972e5"));
        }
        else if(status.getStatus().contentEquals("Completed")) {
            holder.statusView.setTextColor(Color.parseColor("#ffaa56"));
        }else if(status.getStatus().contentEquals("Resolved")) {
            holder.statusView.setTextColor(Color.parseColor("#33a95a"));
        }

        if(status.getStatus().equals("Pending")){

            holder.valueView.setText("Teams are working on fixing this issue. We have alerted the team on your" +
                    " enquiry and an agent will contact you soon with a more detailed update.");

        }
        else if(status.getStatus().equals("Work In Progress")){

            holder.valueView.setText("Teams are working on fixing this issue. We have alerted the team on your " +
                    "enquiry and an agent will contact you soon with a more detailed update.");

        } else if(status.getStatus().equals("Assigned")){

            holder.valueView.setText("Teams are working on fixing this issue. We have alerted the team on your " +
                    "enquiry and an agent will contact you soon with a more detailed update.");

        } else if(status.getStatus().equals("Resolved")){

            holder.valueView.setText("The reported issue is resolved. Please contact the DBS team " +
                    "@ __<<Mailbox +      Phone number>>_______ to reopen the ticket if issue is not fixed.");

        } else if(status.getStatus().equals("Closed")){

            holder.valueView.setText("This ticket has been closed for more than 10 days. If issue " +
                    "persists, please register a new ticket with DBS for the same.");

        }

        holder.stateView.setText("Reported Service: " + status.getValue());


    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

}