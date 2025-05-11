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

/*вспомогательный класс для работы с картой.*/
public class MapHelper implements UserLocationObjectListener {
    private String APIkey;
    private final Context context;
    private MyApplication myApplication;

    private MapView mapView;
    private MapKit mapKit;
    private Map map;

    public boolean isInitAPI = false;
    public boolean isInit = false;

    private com.yandex.mapkit.location.LocationManager locationManager;
    private static final double DESIRED_ACCURACY = 0;
    private static final long MINIMAL_TIME = 10;
    private static final double MINIMAL_DISTANCE = 1;
    private static final boolean USE_IN_BACKGROUND = false;

    private MapObjectCollection lineCol = null;
    private MapObjectCollection marksCol = null;
    private MapObjectCollection moveLineCol = null;
    private MapObjectCollection moveMarkerCol = null;
    private MapObjectCollection userlocCol = null;
    private MapObjectCollection memberlocCol = null;

    private java.util.List<Point> points = new ArrayList<>();
    private PlacemarkMapObject moveMark = null;
    private PlacemarkMapObject userloc = null;
    private PolylineMapObject line;

    private int curMarkerType = SData.MOVE_MARKER;
    private int curColor = 0;

    private LinkedHashMap<Integer, PlacemarkMapObject> members = new LinkedHashMap<>();
    private LinkedHashMap<Integer, PlacemarkMapObject> markers = new LinkedHashMap<>();
    private ArrayList<PlacemarkMapObject> marks = new ArrayList<>();

	
	/*слушатель для геолокации пользователя.
	входные значения: местоположение.
  	возвращаемые значения: - .*/ 
    private final com.yandex.mapkit.location.LocationListener myLocationListener = new com.yandex.mapkit.location.LocationListener() {
        @Override
        public void onLocationUpdated(@NonNull Location location) {
            if (userloc == null) userloc = userlocCol.addPlacemark(location.getPosition(), ImageProvider.fromResource(context, SData.USER_MARKER));
            else userloc.setGeometry(location.getPosition());

            userloc.setOpacity(1f);
            if (moveMark != null) placeLine(moveLineCol, userloc.getGeometry(), moveMark.getGeometry());
            SData.userLocation.postValue(userloc.getGeometry());
            myApplication.getCli().PKTUserCord(userloc.getGeometry());
        }

        @Override
	/*обновление статуса местоположения.
	входные значения: статус местоположения.
  	возвращаемые значения: - .*/ 
        public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {}
    };
	/*слушатель нажатия на метку????.
	входные значения: обьект на карте, координаты нажатия.
  	возвращаемые значения: true/false.*/ 
    private final MapObjectTapListener delLst = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            return delMarker(mapObject);
        }
    };
	
	/*удаление метки.
	входные значения: обьект удаления.
  	возвращаемые значения: true/false.*/ 
    private boolean delMarker(MapObject mapObject) {
        for (LinkedHashMap.Entry<Integer, PlacemarkMapObject> it : markers.entrySet()) {
            if (it.getValue() != mapObject) continue;

            myApplication.getCli().PKTMarkerRemove(SData.primGrId, it.getKey());
            markers.remove(it.getKey());

            it.getValue().getParent().remove(mapObject);

            if (it.getValue().getParent() == moveMarkerCol) moveMark = null;
            return true;
        }

        return false;
    }

	/*нажатие на карту????.
	входные значения: ???карта??точка нажатия???.
  	возвращаемые значения: - .*/ 
    private final InputListener tl = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            int type, color;

            if (curMarkerType == SData.MOVE_MARKER)           type = 0;
            else if (curMarkerType == SData.ATTENTION_MARKER) type = 1;
            else if (curMarkerType == SData.CAR_MARKER)       type = 2;
            else if (curMarkerType == SData.DANGER_MARKER)    type = 3;
            else if (curMarkerType == SData.DEATH_MARKER)     type = 4;
            else if (curMarkerType == SData.FIRE_MARKER)      type = 5;
            else if (curMarkerType == SData.FISH_MARKER)      type = 6;
            else if (curMarkerType == SData.FOOD_MARKER)      type = 7;
            else if (curMarkerType == SData.HAPPY_MARKER)     type = 8;
            else if (curMarkerType == SData.HOUSE_MARKER)     type = 9;
            else if (curMarkerType == SData.PILZ_MARKER)      type = 10;
            else if (curMarkerType == SData.SECRET_MARKER)    type = 11;
            else if (curMarkerType == SData.WET_MARKER)       type = 12;
            else if (curMarkerType == SData.WORK_MARKER)      type = 13;
            else return;

//            placePoint(marksCol, new MarkersInfo(0, type, point));
            myApplication.getCli().PKTMarkerCreate(type, 1, "TEST", point);
        }

        @Override
	/*обработка долгого нажатия.
	входные значения: карта, точка нажатия.
  	возвращаемые значения: - .*/ 
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {}
    };

	/*наблюдатель за метками участников.
	входные значения: информация об участнике.
  	возвращаемые значения: - .*/ 
    Observer<UserInfo> memberMarkersObs = new Observer<>() {
        @Override
        public void onChanged(UserInfo userInfo) {
            PlacemarkMapObject point = members.get(userInfo.getId());
            if (point == null) {
                point = placePoint(memberlocCol, new MarkersInfo(0, userInfo.getId() == SData.primGrLeadId ? SData.LEADER_MARKER : SData.MEMBER_MARKER, new Point(userInfo.getLatitude(), userInfo.getLongitude())));
                members.put(userInfo.getId(), point);
                return;
            }

            point.setGeometry(new Point(userInfo.getLatitude(), userInfo.getLongitude()));
        }
    };

	/*наблюдатель за метками.
	входные значения: информация о метках.
  	возвращаемые значения: - .*/ 
    Observer<ArrayList<MarkersInfo>> markersObserver = new Observer<>() {
        @Override
        public void onChanged(ArrayList<MarkersInfo> markersInfo) {
            for (int i = 0; i < markersInfo.size(); i++) {
                MarkersInfo mark = markersInfo.get(i);
                PlacemarkMapObject point = markers.get(mark.getId());

                if (point != null) {
                    point.setGeometry(mark.getPoint());
                    if (mark.getType() == 0 && userloc != null) placeLine(moveLineCol, mark.getPoint(), userloc.getGeometry());
                    return;
                }

                point = placePoint(marksCol, mark);
                markers.put(mark.getId(), point);
                marks.add(point);
            }
        }
    };

	/*наблюдатель за удалением меток.
	входные значения: число???.
  	возвращаемые значения: - .*/ 
    Observer<Integer> markRem = new Observer<Integer>() {
        @Override
        public void onChanged(Integer integer) {
            PlacemarkMapObject obj;
            obj = markers.get(integer);
            if (obj == null) return;

            obj.getParent().remove(obj);
            if (obj.getParent() == moveMarkerCol) moveMark = null;

            markers.remove(integer);
        }
    };

	/*констурктор.*/
    public MapHelper(String key, Context c, MyApplication myApp) {
        APIkey = key;
        context = c;
        myApplication = myApp;
        initializeAPI();
    }

	/*инициализация АПИ карт.*/
    public void initializeAPI() {
        MapKitFactory.setApiKey(APIkey);
        MapKitFactory.initialize(context);
        isInitAPI = true;
    }

	/*??????????????????опять окошки*/
    public void init(View view, int id, LifecycleOwner onw) {
        initMap(view, id);
        initCols();
        isInit = true;

        SData.userPoint.observe(onw, memberMarkersObs);
        SData.marker.observe(onw, markersObserver);
        SData.markerRem.observe(onw, markRem);

        locationManager = null;
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

    public void loadMarkers() {
        MarkersInfo mark;
        PlacemarkMapObject point;

        for (int i = 0; i < SData.marks.size(); i++) {
            mark = SData.marks.get(i);

            point = placePoint(marksCol, mark);
            markers.put(mark.getId(), point);
            marks.add(point);
        }
    }

    public void onStop() {
        lineCol = null;
        marksCol = null;
        userloc = null;
        moveLineCol = null;
        memberlocCol = null;
        moveMarkerCol = null;

        moveMark = null;

        markers.clear();
        members.clear();
        marks.clear();

        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        locationManager.unsubscribe(myLocationListener);
    }

    public void onStart() {
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
        subscribeToLocationUpdate();
    }

	/*установка метки движения.*/
    private PlacemarkMapObject placeMoveMark(MarkersInfo markersInfo) {
        if (markersInfo == null) return null;

        if (moveMark == null) moveMark = moveMarkerCol.addPlacemark(markersInfo.getPoint(), ImageProvider.fromResource(context, markersInfo.getType()));
        else moveMark.setGeometry(markersInfo.getPoint());

        if (userloc != null) placeLine(moveLineCol, userloc.getGeometry(), moveMark.getGeometry());
        return moveMark;
    }

	/*установка точки??*/
    private PlacemarkMapObject placePoint(MapObjectCollection col, MarkersInfo markersInfo) {
        if (markersInfo == null) return null;
        if (markersInfo.getType() == SData.MOVE_MARKER) return placeMoveMark(markersInfo);

        PlacemarkMapObject placeMark;
        placeMark = col.addPlacemark(markersInfo.getPoint(), ImageProvider.fromResource(context, markersInfo.getType()));
        placeMark.setOpacity(1f);

        if (col != userlocCol) placeMark.addTapListener(delLst);

        return placeMark;
    }

	/*установка линии между меткой движения и геолокацией пользователя.*/
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
