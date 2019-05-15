package com.medotech.masrofaty01;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatisticsTab extends Fragment {

    public StatisticsTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistics_tab, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        return rootView;
    }
}
