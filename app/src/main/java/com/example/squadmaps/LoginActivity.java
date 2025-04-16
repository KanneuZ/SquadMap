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

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private Button btnLog;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtLogin;
    private String login = null;
    private String password = null;
    private String username = null;

//    LiveData<Boolean> flg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        databaseHelper = new DatabaseHelper(LoginActivity.this);

        btnLog = findViewById(R.id.btnLogin);
        edtLogin = findViewById(R.id.edtLogin);
        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUsername);

        if (myApplication.getCli().isRegistered()) startActivity(new Intent(LoginActivity.this, MainActivity.class));

        btnLog.setOnClickListener(v -> {
            login = edtLogin.getText().toString().trim();
            password = edtPassword.getText().toString().trim();
            username = edtUsername.getText().toString().trim();

            Log.i("onCreate", "onCreate: 123");
            if (login.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "All fields must be not empty", Toast.LENGTH_SHORT).show();
                return;
            }

            myApplication.getCli().PKTReg(login, password, username);


//            if (!databaseHelper.checklogin(login)) databaseHelper.insertData(login, password, username);
//            else {
//                boolean isExist = databaseHelper.check(login, password, username);
//
//                if (isExist) {
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                } else {
//                    edtPassword.setText(null);
//                    Toast.makeText(LoginActivity.this, "Login failed. Some fields wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
        });

        Client.isReg.observe(this, aBoolean -> {
            if (Client.isReg.getValue()) {
                if (login != null && !databaseHelper.checklogin(login)) databaseHelper.insertData(login, password, username);
                Toast.makeText(MyApplication.getContext(), "Вы успешно вошли в систему", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
}