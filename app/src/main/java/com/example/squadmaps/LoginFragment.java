package com.example.squadmaps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

	private MyApplication myApplication;
	private Activity activity;
	private Context context;

	private EditText edtLogin;
	private EditText edtPassword;

	private Button btnGoto;
	private Button btnLog;

	View.OnClickListener lstGoto = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			((Itest) activity).msgFromFragment();
		}
	};

	View.OnClickListener lstLog = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String login, password;

			login = edtLogin.getText().toString().trim();
			password = edtPassword.getText().toString().trim();

			if (login.isEmpty() || password.isEmpty()) {
				Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show();
				return;
			}

			Log.i("123", login+" "+password);

			log(login, password);
		}
	};

	public LoginFragment(MyApplication myApp) { myApplication = myApp; }

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
		View view = inflater.inflate(R.layout.fragment_login, container, false);

		context = view.getContext();

		edtLogin = view.findViewById(R.id.edtLogin);
		edtPassword = view.findViewById(R.id.edtPassword);

		btnGoto = view.findViewById(R.id.btnGoto);
		btnGoto.setOnClickListener(lstGoto);

		btnLog = view.findViewById(R.id.btnLogin);
		btnLog.setOnClickListener(lstLog);

		return view;
	}

	private void log(String login, String password) {
		if (login.isEmpty() || password.isEmpty()) {
			Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_SHORT).show();
			return;
		}

		SData.login = login;
		SData.password = password;

		myApplication.getCli().PKTLogin(login, password);
	}
}