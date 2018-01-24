package com.hypernymbiz.logistics.adapter;

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
import com.hypernymbiz.logistics.fragments.JobDetailsFragment;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.utils.Constants;

import java.util.List;

/**
 * Created by Metis on 21-Jan-18.
 */

public class JobNotifiyAdapter extends RecyclerView.Adapter<JobNotifiyAdapter.ViewHolder> {

    private List<JobInfo_> jobInfo_s;
    public Context context;

    public JobNotifiyAdapter(List<JobInfo_> data, Context context) {
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
    public void onBindViewHolder(JobNotifiyAdapter.ViewHolder holder, int position) {

        holder.job.setText(jobInfo_s.get(position).getJob_name());
        if(holder.job.getText().toString().equals("job1")) {
           // holder.cardView.setCardBackgroundColor(Color.parseColor("#63a4ff"));
        }

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
            cardView=(CardView)itemView.findViewById(R.id.layout_cardview);
            String j = job.getText().toString();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(context, "notify", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, job.getText(),Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(context, JobDetailFragment.class);
//                        context.startActivity(intent);
//                    ActivityUtils.startActivity(context, FrameActivity.class,JobDetailFragment.class.getName(),null);
                    // ((Activity)context).finish();
                    Toast.makeText(context,String.valueOf(pos), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, FrameActivity.class);
                    intent.putExtra(Constants.FRAGMENT_NAME, JobDetailsFragment.class.getName());
//                    intent.putExtra(Constants.DATA, bundle);
                    context.startActivity(intent);

//                    }
//
//                    else
//                    {
//                        Toast.makeText(context, "not selected", Toast.LENGTH_SHORT).show();
//                    }

                }
            });

        }

    }
}