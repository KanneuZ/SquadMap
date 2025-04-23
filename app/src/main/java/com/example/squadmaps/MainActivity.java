package com.example.squadmaps;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.yandex.mapkit.geometry.Point;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private final String MAPKIT_API_KEY = "791f3cbd-ad64-40f4-bb25-147e090b2751";
    private TextView tv;
    private TextView coord;

    private MapHelper mapHelper;
    private MyApplication myApplication;

    private int curId = R.id.profile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApplication = (MyApplication) getApplicationContext();
        mapHelper = new MapHelper(MAPKIT_API_KEY, this, myApplication);
        if (!mapHelper.isInitAPI) mapHelper.initializeAPI();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(mapHelper)).commit();

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == curId) return true;
            else if (id == R.id.profile) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(mapHelper)).commit();
            else if (id == R.id.settings) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            else if (id == R.id.info) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
            else if (id == R.id.group) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroupFragment()).commit();

            curId = id;
            return true;
        });

        SData.primGrName.observe(this, obsGroup);
        SData.userLocation.observe(this, obsCoords);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tv = headerView.findViewById(R.id.username);
        coord = headerView.findViewById(R.id.user_coord);
        TextView tv2 = headerView.findViewById(R.id.prim_gr);
        tv2.setText(SData.primGrName.getValue());
        tv.setText(SData.userName);
    }

    private final Observer<String> obsGroup = s -> {
		Log.d("TAG", "onChanged: " + s);
		NavigationView navigationView = findViewById(R.id.nav_view);
		View headerView = navigationView.getHeaderView(0);
		TextView tv = headerView.findViewById(R.id.prim_gr);
		tv.setText(s);
	};

    private final Observer<Point> obsCoords = p -> {
        Log.d("TAG", "onChanged: " + p);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tv = headerView.findViewById(R.id.user_coord);

        tv.setText(p.getLatitude()+" "+p.getLongitude());
    };
}