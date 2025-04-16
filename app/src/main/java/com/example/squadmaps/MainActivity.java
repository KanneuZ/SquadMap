package com.example.squadmaps;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private final String MAPKIT_API_KEY = "791f3cbd-ad64-40f4-bb25-147e090b2751";
    private TextView tv;
    private TextView coord;

    private MapHelper mapHelper;
    private MyApplication myApplication;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApplication = (MyApplication) getApplicationContext();
        mapHelper = new MapHelper(MAPKIT_API_KEY, this, myApplication);
        if (!mapHelper.isInit) mapHelper.initializeAPI();

//         map API
//        MapKitFactory.setApiKey(MAPKIT_API_KEY);
//        MapKitFactory.initialize(this);

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

            if (id == R.id.profile) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(mapHelper)).commit();
            else if (id == R.id.settings) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            else if (id == R.id.info) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
            else if (id == R.id.group) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroupFragment()).commit();

            return true;
        });

        DatabaseHelper db = new DatabaseHelper(MyApplication.getContext());

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tv = headerView.findViewById(R.id.username);
        coord = headerView.findViewById(R.id.user_coord);
        tv.setText(db.getUsername());
    }

    private void func() {
        setContentView(R.layout.nav_header);

    }
}