package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;

/**
 * Created by Metis on 07-Mar-18.
 */

public class MaintenanceFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Maintenance");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        return view;
    }
}
