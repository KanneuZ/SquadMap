package com.example.squadmaps;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MapHelper implements UserLocationObjectListener {
    private String APIkey;
    private final Context context;
    private MyApplication myApplication;

    private MapView mapView;
    private MapKit mapKit;
    private Map map;

    public boolean isInit = false;

    private com.yandex.mapkit.location.LocationManager locationManager;
    private static final double DESIRED_ACCURACY = 0;
    private static final long MINIMAL_TIME = 10;
    private static final double MINIMAL_DISTANCE = 1;
    private static final boolean USE_IN_BACKGROUND = false;

    private MapObjectCollection lineCol;
    private MapObjectCollection marksCol;
    private MapObjectCollection moveLineCol;
    private MapObjectCollection moveMarkerCol;
    private MapObjectCollection userlocCol;
    private MapObjectCollection memberlocCol;

    private java.util.List<Point> points = new ArrayList<>();
    private PolylineMapObject line;
    private PlacemarkMapObject moveMark = null;
    private PlacemarkMapObject userloc = null;

    private final int USER_MARKER = android.R.drawable.btn_star_big_on;
    private final int MEMBER_MARKER = android.R.drawable.btn_star_big_off;

    public static final int MOVE_MARKER = android.R.drawable.ic_delete;
    public static final int HOUSE_MARKER = android.R.drawable.ic_dialog_alert;

    private int curMarkerType = MOVE_MARKER;
    private int curColor = 0;

    private LinkedHashMap<Integer, PlacemarkMapObject> members = new LinkedHashMap<>();
    private LinkedHashMap<Integer, PlacemarkMapObject> markers = new LinkedHashMap<>();
    private ArrayList<PlacemarkMapObject> marks = new ArrayList<>();
    private ArrayList<MapObjectCollection> groupsCol = new ArrayList<>();

    private final com.yandex.mapkit.location.LocationListener myLocationListener = new com.yandex.mapkit.location.LocationListener() {
        @Override
        public void onLocationUpdated(@NonNull Location location) {
            if (userloc == null) userloc = userlocCol.addPlacemark(location.getPosition(), ImageProvider.fromResource(context, USER_MARKER));
            else userloc.setGeometry(location.getPosition());

            userloc.setOpacity(1f);
            if (moveMark != null) placeLine(moveLineCol, userloc.getGeometry(), moveMark.getGeometry());
            myApplication.getCli().PKTUserCord(userloc.getGeometry());
        }

        @Override
        public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {}
    };

    private final InputListener tl = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            int type, color;
            if (curMarkerType == MOVE_MARKER) {
                type = 0;
                placeMoveMark(point);
            } else if (curMarkerType == HOUSE_MARKER) {
                type = 1;
                placePoint(marksCol, point, curMarkerType);
            } else return;

            myApplication.getCli().PKTMarkerCreate(type, 1, "TEST", point);
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {}
    };

    private MapObjectTapListener mopt = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            marksCol.remove(mapObject);
            return true;
        }
    };

    Observer<UserInfo> memberMarkersObs = new Observer<UserInfo>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            PlacemarkMapObject point = members.get(userInfo.getId());
            if (point == null) {
                point = placePoint(memberlocCol, new Point(userInfo.getLatitude(), userInfo.getLongitude()), MEMBER_MARKER);
                members.put(userInfo.getId(), point);
                return;
            }

            point.setGeometry(new Point(userInfo.getLatitude(), userInfo.getLongitude()));
        }
    };

    Observer<ArrayList<MarkersInfo>> markersObserver = new Observer<ArrayList<MarkersInfo>>() {
        @Override
        public void onChanged(ArrayList<MarkersInfo> markersInfo) {
            for (int i = 0; i < markersInfo.size(); i++) {
                MarkersInfo mark = markersInfo.get(i);
                PlacemarkMapObject point = markers.get(mark.getId());

                Log.d("TAG", "onChanged: "+mark.getType()+" "+point);
                if (point != null) {
                    point.setGeometry(mark.getPoint());
                    if (mark.getType() == 0 && userloc != null) placeLine(moveLineCol, mark.getPoint(), userloc.getGeometry());
                    return;
                }

                switch (mark.getType()) {
                    case 0:
                        point = placeMoveMark(mark.getPoint());
                        break;
                    case 1:
                        point = placePoint(marksCol, mark.getPoint(), HOUSE_MARKER);
                        break;
                }

                markers.put(mark.getId(), point);
                marks.add(point);
            }
        }
    };

    public MapHelper(String key, Context c, MyApplication myApp) {
        APIkey = key;
        context = c;
        myApplication = myApp;
        initializeAPI();
    }

    public void initializeAPI() {
        MapKitFactory.setApiKey(APIkey);
        MapKitFactory.initialize(context);
        isInit = true;
    }

    public void init(View view, int id, LifecycleOwner onw) {
        initMap(view, id);
        initCols();

        SData.userPoint.observe(onw, memberMarkersObs);
        SData.marker.observe(onw, markersObserver);

        locationManager = mapKit.createLocationManager();
    }

    public void initMap(View view, int id) {
        mapView = view.findViewById(id);
        mapKit = MapKitFactory.getInstance();
        map = mapView.getMap();
    }

    private void initCols() {
        lineCol       = map.getMapObjects().addCollection();
        marksCol      = map.getMapObjects().addCollection();
        userlocCol    = map.getMapObjects().addCollection();
        moveLineCol   = map.getMapObjects().addCollection();
        memberlocCol  = map.getMapObjects().addCollection();
        moveMarkerCol = map.getMapObjects().addCollection();
    }

    public void initListeners() {
        mapView.getMap().addInputListener(tl);
	}

    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        locationManager.unsubscribe(myLocationListener);
    }

    public void onStart() {
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
        subscribeToLocationUpdate();
    }

    private PlacemarkMapObject placeMoveMark(Point point) {
        if (point == null) return null;

        if (moveMark == null) moveMark = placePoint(moveMarkerCol, point, MOVE_MARKER);
        else moveMark.setGeometry(point);

        if (userloc != null) placeLine(moveLineCol, userloc.getGeometry(), moveMark.getGeometry());
        return moveMark;
    }

    private PlacemarkMapObject placePoint(MapObjectCollection col, Point point, int marker) {
        if (point == null) return null;
        PlacemarkMapObject placemark;
        placemark = col.addPlacemark(point, ImageProvider.fromResource(context, marker));
        placemark.setOpacity(1f);
        return placemark;
    }

    private void placeLine(MapObjectCollection col, Point point1, Point point2) {
        if (point1 == null || point2 == null) return;
        col.clear();
        points.clear();

        points.add(point1);
        points.add(point2);
        line = col.addPolyline(new Polyline(points));
    }

    private void subscribeToLocationUpdate() {
        if (locationManager == null) return;
        locationManager.subscribeForLocationUpdates(DESIRED_ACCURACY, MINIMAL_TIME, MINIMAL_DISTANCE, USE_IN_BACKGROUND, FilteringMode.OFF, myLocationListener);
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationView.getArrow().setVisible(false);
        userLocationView.getPin().setVisible(false);
        userLocationView.getAccuracyCircle().setVisible(false);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }

	public int getCurColor() {
		return curColor;
	}

	public void setCurColor(int curColor) {
		this.curColor = curColor;
	}

    public int getCurMarkerType() {
        return curMarkerType;
    }

    public void setCurMarkerType(int CurMarkerType) {
        this.curMarkerType = CurMarkerType;
    }
}
