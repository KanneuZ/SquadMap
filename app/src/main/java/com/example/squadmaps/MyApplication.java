package com.example.squadmaps;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyApplication extends android.app.Application {
    private final String LOG_TAG = "MyApplication";

    private final String LocalHOST = "192.168.1.29";
    private final String HOST = "squadmap.ydns.eu";
    private final int PORT = 4000;

    private Client cli  = null;
    private static MyApplication instance;

    public Client getCli() {
        return cli;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        connect();
    }

    public static Context getContext() {
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        disconnected();
    }

    private void connect() {
        cli = new Client(HOST, PORT);
        try {
            cli.openConnection();
        } catch (Exception e) {
            Log.e(LOG_TAG, ""+e.getMessage());
            cli = null;
        }
    }

    private void disconnected() {
        cli.closeConnection();
    }
}
