package com.hypernymbiz.logistics.adapter;

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.hypernymbiz.logistics.R;
        import com.hypernymbiz.logistics.models.JobInfo_;
        import com.hypernymbiz.logistics.models.Maintenance;
        import com.hypernymbiz.logistics.utils.AppUtils;

        import java.util.List;

/**
 * Created by Metis on 12-Mar-18.
 */

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.MyViewHolder> {

public List<Maintenance> maintenance;
    public MaintenanceAdapter(List<Maintenance> maintenances)
    {

        this.maintenance=maintenances;
    }


    @Override
    public MaintenanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_maintenance, parent, false);
        return new MaintenanceAdapter.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(MaintenanceAdapter.MyViewHolder holder, int position) {

        holder.maintenancename.setText(maintenance.get(position).getMaintenanceName());
        holder.maintenancestatus.setText(maintenance.get(position).getStatus());
        holder.starttime.setText(AppUtils.getFormattedDate(maintenance.get(position).getDueDate()) + " " + AppUtils.getTime(maintenance.get(position).getDueDate()));
//        holder.endtime.setTex
//        holder.maintenancetype.setText("ghghjggggghgjgh");


    }

    @Override
    public int getItemCount() {
        return maintenance.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView maintenancename, maintenancestatus, starttime, maintenancetype;
        public MyViewHolder(View itemView) {
            super(itemView);

            maintenancename = (TextView) itemView.findViewById(R.id.txt_maintenance_name);
            maintenancestatus = (TextView) itemView.findViewById(R.id.txt_maintenance_status);
            starttime = (TextView) itemView.findViewById(R.id.txt_starttime);
            maintenancetype = (TextView) itemView.findViewById(R.id.txt_maintenance_type);


        }
    }
}
