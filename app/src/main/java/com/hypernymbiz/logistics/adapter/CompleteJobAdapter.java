package com.hypernymbiz.logistics.adapter;

import android.content.Context;
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
public class CompleteJobAdapter extends RecyclerView.Adapter<CompleteJobAdapter.MyViewHolder> {
    private List<JobInfo_> jobInfo_s;
    Context context;


    public CompleteJobAdapter(List<JobInfo_> jobInfo_s)
    {

        this.jobInfo_s=jobInfo_s;
    }

    @Override
    public CompleteJobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete_job, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompleteJobAdapter.MyViewHolder holder, int position) {

        holder.jobname.setText(jobInfo_s.get(position).getJob_name());
        holder.jobstatus.setText(jobInfo_s.get(position).getJob_status());
        holder.starttime.setText(AppUtils.getFormattedDate(jobInfo_s.get(position).getJob_start_time())+" "+AppUtils.getTime(jobInfo_s.get(position).getJob_start_time()));
        holder.endtime.setText(AppUtils.getTime(jobInfo_s.get(position).getJob_end_time()));
        //  holder.compled_job.setText(jobInfo_s.size());



    }

    @Override
    public int getItemCount() {
        return jobInfo_s.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jobname, jobstatus, starttime, endtime,compled_job;

        public MyViewHolder(View itemView) {
            super(itemView);

            jobname = (TextView) itemView.findViewById(R.id.txt_jobname);
            jobstatus = (TextView) itemView.findViewById(R.id.txt_jobstatus);
            starttime = (TextView) itemView.findViewById(R.id.txt_starttime);
            endtime = (TextView) itemView.findViewById(R.id.txt_endtime);
            compled_job = (TextView) itemView.findViewById(R.id.txt_no_completed_job);

        }
    }
}
//    private String JN,JS,ST,ET;
//    private List<CompletedJobData> completedJobDatas;
//
//    private List<JobInfo_> jobInfo_s;
//
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete_job, parent, false);
//        return new ViewHolder(view);
//    }
//
//
//
//    @Override
//    public void onBindViewHolder(ListViewHolder holder, int position) {
//
//
//        @Override
//        public void onBindViewHolder (RecyclerView.ViewHolder holder,int position){
////
////        ((ListViewHolder) holder).bindView(position);
////        if(position == 0) {
////            JN = "Job Name";
////            ((ListViewHolder) holder).jobname.setText(JN);
////            JS = "Job Status";
////            ((ListViewHolder) holder).jobstatus.setText(JS);
////            ST = "Start Time";
////            ((ListViewHolder) holder).starttime.setText(ST);
////            ET = "End Time";
////            ((ListViewHolder) holder).endtime.setText(ET);
////        }
//
//        }
//
//        @Override
//        public int getItemCount () {
//            return jobInfo_s.size();
//        }
//
//        @Override
//        public int getItemViewType ( int position){
//            return position;
//        }
//
//
//        public class ListViewHolder extends RecyclerView.ViewHolder {
//
//            private TextView jobname, jobstatus, starttime, endtime;
//
//            public ListViewHolder(View itemview) {
//                super(itemview);
//                jobname = (TextView) itemview.findViewById(R.id.txt_jobname);
//                jobstatus = (TextView) itemview.findViewById(R.id.txt_jobstatus);
//                starttime = (TextView) itemview.findViewById(R.id.txt_starttime);
//                endtime = (TextView) itemview.findViewById(R.id.txt_endtime);
//
//            }
////        public void bindView(int postion) {
////
////            int select=0;
////            if(select==postion)
////            {
////
////                jobname.setTextColor(Color.BLACK);
////                jobstatus.setTextColor(Color.BLACK);
////                starttime.setTextColor(Color.BLACK);
////                endtime.setTextColor(Color.BLACK);
////            }
////
////            jobname.setText(CompletedJobData.jobname[postion]);
////            jobstatus.setText(CompletedJobData.jobstatus[postion]);
////            starttime.setText(CompletedJobData.starttime[postion]);
////            endtime.setText(CompletedJobData.endtime[postion]);
////        }
//
//
//        }
//    }}
