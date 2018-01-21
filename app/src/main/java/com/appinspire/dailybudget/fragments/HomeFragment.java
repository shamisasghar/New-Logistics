package com.appinspire.dailybudget.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appinspire.dailybudget.FrameActivity;
import com.appinspire.dailybudget.R;
import com.appinspire.dailybudget.models.AssignedTime;
import com.appinspire.dailybudget.models.Time;
import com.appinspire.dailybudget.toolbox.ToolbarListener;
import com.appinspire.dailybudget.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private ViewHolder mHolder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Home");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        ExpandableLayout sectionLinearLayout = (ExpandableLayout) view.findViewById(R.id.layout_expandable);

        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<AssignedTime,Time>()
        {
            @Override
            public void renderParent(View view, AssignedTime model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.up_arrow : R.drawable.up_arrow);
            }

            @Override
            public void renderChild(View view, Time model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tvChild)).setText(model.name);

            }
        });

        sectionLinearLayout.addSection(getsection());
        sectionLinearLayout.addSection(getsection());
        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
    //    mHolder.button.setOnClickListener(this);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ActivityUtils.startActivity(getActivity(), FrameActivity.class,HomeFragment.class.getName(),null);

    }

    public static class ViewHolder {

        Button button;
        public ViewHolder(View view) {
         //   button = (Button) view.findViewById(R.id.button);

        }

    }
    public Section<AssignedTime,Time> getsection() {
        Section<AssignedTime, Time> section = new Section<>();
        AssignedTime phoneCategory=new AssignedTime("Phone");
        List<Time> list=new ArrayList<Time>();
        {
            for (int i=0;i<=5;i++)

                list.add(new Time("21313"+i));
            section.parent=phoneCategory;
            section.children.addAll(list);

        }
        return section;
    }

}
