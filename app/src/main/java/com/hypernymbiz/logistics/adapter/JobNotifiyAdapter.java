package com.hypernymbiz.logistics.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.fragments.JobAssignedFragment;
import com.hypernymbiz.logistics.fragments.JobDetailsFragment;
import com.hypernymbiz.logistics.fragments.MaintenanceAssignedFragment;
import com.hypernymbiz.logistics.fragments.MaintenanceDetailFragment;
import com.hypernymbiz.logistics.models.JobInfo;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.utils.Constants;

import java.util.List;

/**
 * Created by Metis on 21-Jan-18.
 */

public class JobNotifiyAdapter extends RecyclerView.Adapter<JobNotifiyAdapter.ViewHolder> {

    private List<JobInfo> jobInfo_s;
    public Context context;

    public JobNotifiyAdapter(List<JobInfo> data, Context context) {
        this.jobInfo_s = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_notification, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final JobNotifiyAdapter.ViewHolder holder, final int position) {

        holder.job.setText(jobInfo_s.get(position).getJobName());





            holder.itemView.setOnClickListener(new View.OnClickListener() {
                int pos = holder.getAdapterPosition();
                Integer id = jobInfo_s.get(position).getJobId();


                @Override
                public void onClick(View v) {


               Toast.makeText(context, ""+id.toString(), Toast.LENGTH_SHORT).show();
                    if(jobInfo_s.get(position).getJobType()==9)
                    {
                        Intent intent = new Intent(context, FrameActivity.class);
                        intent.putExtra("jobid", Integer.toString(id));
                        intent.putExtra(Constants.FRAGMENT_NAME, MaintenanceDetailFragment.class.getName());
                        context.startActivity(intent);

                    }
                    else {
                        Intent intent = new Intent(context, FrameActivity.class);
                        intent.putExtra("jobid", Integer.toString(id));
                        intent.putExtra(Constants.FRAGMENT_NAME, JobDetailsFragment.class.getName());
//                    intent.putExtra(Constants.DATA, bundle);
                        context.startActivity(intent);
                    }
                }

            });



        }



    @Override
    public int getItemCount() {
        return jobInfo_s.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView job;
        ImageView imageView;
        CardView cardView;
        int pos=getAdapterPosition();

        private ViewHolder(final View itemView) {
            super(itemView);

            job = (TextView) itemView.findViewById(R.id.jobname);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.layout_cardview);
            String j = job.getText().toString();

        }

    }
}