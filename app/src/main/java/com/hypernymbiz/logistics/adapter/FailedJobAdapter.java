package com.hypernymbiz.logistics.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.utils.AppUtils;

import java.util.List;

/**
 * Created by Metis on 22-Jan-18.
 */

public class FailedJobAdapter extends RecyclerView.Adapter<FailedJobAdapter.MyViewHolder> {
    private List<JobInfo_> jobInfo_s;


    public FailedJobAdapter(List<JobInfo_> jobInfo_s)
    {

        this.jobInfo_s=jobInfo_s;
    }

    @Override
    public FailedJobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_failed_job, parent, false);
        return new FailedJobAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FailedJobAdapter.MyViewHolder holder, int position) {

        holder.jobname.setText(jobInfo_s.get(position).getJob_name());
        holder.jobstatus.setText(jobInfo_s.get(position).getJob_status());
        holder.starttime.setText(AppUtils.getFormattedDate(jobInfo_s.get(position).getJob_start_time()) + " " + AppUtils.getTime(jobInfo_s.get(position).getJob_start_time()));
        holder.endtime.setText(AppUtils.getFormattedDate(jobInfo_s.get(position).getJob_end_time()) + " " + AppUtils.getTime(jobInfo_s.get(position).getJob_end_time()));

//        holder.endtime.setText(jobInfo_s.get(position).getJob_end_time());
        // holder.failed_job.setText(jobInfo_s.size());


    }

    @Override
    public int getItemCount() {
        return jobInfo_s.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobname, jobstatus, starttime, endtime,failed_job;

        public MyViewHolder(View itemView) {
            super(itemView);

            jobname = (TextView) itemView.findViewById(R.id.txt_jobname);
            jobstatus = (TextView) itemView.findViewById(R.id.txt_jobstatus);
            starttime = (TextView) itemView.findViewById(R.id.txt_starttime);
            endtime = (TextView) itemView.findViewById(R.id.txt_endtime);
            failed_job = (TextView) itemView.findViewById(R.id.txt_no_failed_job);

        }
    }
}