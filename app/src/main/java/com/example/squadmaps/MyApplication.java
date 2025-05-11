package com.example.squadmaps;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*класс*/
public class MyApplication extends android.app.Application {
    private final String LOG_TAG = "MyApplication";

    private final String LocalHOST = "192.168.1.29";
    private final String HOST = "squadmap.ydns.eu";
    private final int PORT = 4000;

    private Client cli  = null;
    private static MyApplication instance;

    /*возвращает клиент для работы с сервером.
	входные значения: - .
  	возвращаемые значения: клиент.*/
    public Client getCli() {
        return cli;
    }

    @Override
    /*создание приложения??.
	входные значения: - .
  	возвращаемые значения: - .*/
    public void onCreate() {
        instance = this;
        super.onCreate();
        connect();
    }
    
	/*получение контекста.
	входные значения: - .
  	возвращаемые значения: пример.*/
    public static Context getContext() {
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
	/*завершение работы приложения???.
	входные значения: - .
  	возвращаемые значения: -.*/
    public void onTerminate() {
        super.onTerminate();
        disconnected();
    }
    
	/*метод устанавливает соединение с сервером.
	входные значения: - .
  	возвращаемые значения: - .*/
    private void connect() {
        cli = new Client(HOST, PORT);
        try {
            cli.openConnection();
        } catch (Exception e) {
            Log.e(LOG_TAG, ""+e.getMessage());
            cli = null;
        }
    }
    
	/*метод закрывает соединение с сервером.
	входные значения: - .
  	возвращаемые значения: - .*/
    private void disconnected() {
        cli.closeConnection();
    }
}
