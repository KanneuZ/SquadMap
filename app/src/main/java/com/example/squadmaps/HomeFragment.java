package com.example.squadmaps;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class HomeFragment extends Fragment  {
    private MapHelper mapHelper;

    private RadioButton rbtnMove;
    private RadioButton rbtnHome;

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            int id = rb.getId();

            Log.d("TAG", "onClick: "+id);
            if (id == R.id.moveMarker) mapHelper.setCurMarkerType(SData.MOVE_MARKER);
            else if (id == R.id.homeMarker) {
                mapHelper.setCurMarkerType(SData.HOUSE_MARKER);
            }
        }
    };

    public HomeFragment(MapHelper mapHlp) {
        mapHelper = mapHlp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapHelper.init(view, R.id.mapview, getViewLifecycleOwner());
        mapHelper.initMap(view, R.id.mapview);
        mapHelper.initListeners();
        mapHelper.loadMarkers();

        rbtnMove = view.findViewById(R.id.moveMarker);
        rbtnMove.setOnClickListener(radioButtonClickListener);

        rbtnHome = view.findViewById(R.id.homeMarker);
        rbtnHome.setOnClickListener(radioButtonClickListener);

        return view;
    }

    @Override
    public void onStop() {
        mapHelper.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapHelper.onStart();
    }
}