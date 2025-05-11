package com.example.squadmaps;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/*класс активити для регистрации и авторизации.*/
public class LoginActivity extends AppCompatActivity implements Itest {
    private DatabaseHelper databaseHelper;
    private MyApplication myApplication;
    private boolean flg = true;

    private final Observer<Boolean> observer = new Observer<>() {
        @Override
        /*наблюдение статуса регистрации.
 	    входные значения: текущий статус регистрации.
  	    возвращаемые значения: - .*/ 
        public void onChanged(Boolean aBoolean) {
            if (Boolean.TRUE.equals(SData.isReg.getValue())) {
                if (!databaseHelper.checklogin(SData.login)) databaseHelper.insertData(SData.login, SData.password, SData.userName);
                Toast.makeText(MyApplication.getContext(), "Вы успешно вошли в систему", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }
    };

    @Override
    /*создание активити.
 	входные значения: сохраненное состояние.
  	возвращаемые значения: - .*/ 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        myApplication = (MyApplication) getApplicationContext();
        databaseHelper = new DatabaseHelper(LoginActivity.this);

        SData.isReg.observe(this, observer);
        if (Boolean.FALSE.equals(SData.isReg.getValue())) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment(myApplication)).commit();
        else startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    /*сообщение из фрагмента.
 	входные значения: - .
  	возвращаемые значения: - .*/ 
    public void msgFromFragment() {
        if (flg) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment(myApplication)).commit();
        else getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegistrationFragment(myApplication)).commit();

        flg = !flg;
    }
}
