package com.example.squadmaps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationFragment extends Fragment {

	private DatabaseHelper databaseHelper;
	private MyApplication myApplication;
	private Activity activity;
	private Context context;

	private Button btnReg;
	private EditText edtLogin;
	private EditText edtPassword;
	private EditText edtUsername;

	View.OnClickListener lst = v -> {
		String login, password, username;

		login = edtLogin.getText().toString().trim();
		password = edtPassword.getText().toString().trim();
		username = edtUsername.getText().toString().trim();

		reg(login, password, username);
	};

	public RegistrationFragment(MyApplication myApp) { myApplication = myApp; }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		activity = (Activity) context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_registration, container, false);

		context = view.getContext();

		edtLogin = view.findViewById(R.id.edtLogin);
		edtPassword = view.findViewById(R.id.edtPassword);
		edtUsername = view.findViewById(R.id.edtUsername);

		btnReg = view.findViewById(R.id.btnReg);
		btnReg.setOnClickListener(lst);

		return view;
	}

	private void reg(String login, String password, String username) {
		if (login.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }

		SData.login = login;
		SData.password = password;
		SData.userName = username;

		/* TODO проверки на некорректный ввод */

		myApplication.getCli().PKTReg(login, password, username);
	}
}