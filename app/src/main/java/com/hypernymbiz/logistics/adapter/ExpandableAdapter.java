package com.hypernymbiz.logistics.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.models.ItemModel;

import java.util.List;

/**
 * Created by Metis on 16-Feb-18.
 */

public class ExpandableAdapter extends RecyclerView.Adapter<ExpandableAdapter.ViewHolder> {

    private final List<ItemModel> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public ExpandableAdapter(final List<ItemModel> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.expandable_list_row, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemModel item = data.get(position);
        holder.setIsRecyclable(false);
        holder.textView.setText(item.description);
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, item.colorId1));
        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, item.colorId2));
        holder.expandableLayout.setInterpolator(item.interpolator);
        holder.expandableLayout.setExpanded(expandState.get(position));

        holder.starttime.setText(item.start_time);
        if (item.rowCheck) {
            holder.endtime.setText(item.end_time);
        }
        else{
            holder.endtime.setVisibility(View.GONE);
        }
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter()
        {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.buttonLayout, 0f, 180f).start();
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.buttonLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

        public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,starttime,endtime;
        public RelativeLayout buttonLayout;

        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            starttime = (TextView) v.findViewById(R.id.txt_start);
            endtime = (TextView) v.findViewById(R.id.txt_fail);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);

        }
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
