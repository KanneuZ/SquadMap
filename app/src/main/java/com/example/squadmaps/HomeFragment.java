package com.example.squadmaps;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

/*класс фрагмента главного экрана.*/
public class HomeFragment extends Fragment  {
    private MapHelper mapHelper;

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
	/*обработка для выбора метки.
 	входные значения: нажатая метка???.
  	возвращаемые значения: - .*/  
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            int id = rb.getId();

            if (id == R.id.moveMarker)           mapHelper.setCurMarkerType(SData.MOVE_MARKER);
            else if (id == R.id.attentionMarker) mapHelper.setCurMarkerType(SData.ATTENTION_MARKER);
            else if (id == R.id.carMarker)       mapHelper.setCurMarkerType(SData.CAR_MARKER);
            else if (id == R.id.dangerMarker)    mapHelper.setCurMarkerType(SData.DANGER_MARKER);
            else if (id == R.id.deathMarker)     mapHelper.setCurMarkerType(SData.DEATH_MARKER);
            else if (id == R.id.fireMarker)      mapHelper.setCurMarkerType(SData.FIRE_MARKER);
            else if (id == R.id.fishMarker)      mapHelper.setCurMarkerType(SData.FISH_MARKER);
            else if (id == R.id.foodMarker)      mapHelper.setCurMarkerType(SData.FOOD_MARKER);
            else if (id == R.id.happyMarker)     mapHelper.setCurMarkerType(SData.HAPPY_MARKER);
            else if (id == R.id.houseMarker)     mapHelper.setCurMarkerType(SData.HOUSE_MARKER);
            else if (id == R.id.pilzMarker)      mapHelper.setCurMarkerType(SData.PILZ_MARKER);
            else if (id == R.id.secretMarker)    mapHelper.setCurMarkerType(SData.SECRET_MARKER);
            else if (id == R.id.wetMarker)       mapHelper.setCurMarkerType(SData.WET_MARKER);
            else if (id == R.id.workMarker)      mapHelper.setCurMarkerType(SData.WORK_MARKER);
        }
    };

	/*констурктор.*/
    public HomeFragment(MapHelper mapHlp) {
        mapHelper = mapHlp;
    }

    @Override
	/*создание фрагмента.
 	входные значения: сохраненное состояние.
  	возвращаемые значения: - .*/ 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	/*опять эти вью дурацкие пиши сам или удали для меня это окошко.*/ 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapHelper.init(view, R.id.mapview, getViewLifecycleOwner());
        mapHelper.initMap(view, R.id.mapview);
        mapHelper.initListeners();
        mapHelper.loadMarkers();

        view.findViewById(R.id.moveMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.attentionMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.carMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.dangerMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.deathMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.fireMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.fishMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.foodMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.happyMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.houseMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.pilzMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.secretMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.wetMarker).setOnClickListener(radioButtonClickListener);
        view.findViewById(R.id.workMarker).setOnClickListener(radioButtonClickListener);

        return view;
    }

    @Override
	/*остановка фрагмента*/
    public void onStop() {
        mapHelper.onStop();
        super.onStop();
    }

    @Override
	/*старт фрагмента*/
    public void onStart() {
        super.onStart();
        mapHelper.onStart();
    }
}
