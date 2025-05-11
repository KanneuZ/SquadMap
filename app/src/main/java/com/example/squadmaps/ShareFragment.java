package com.example.squadmaps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*класс для передачи фрагмента.*/
public class ShareFragment extends Fragment {

    public ShareFragment() {
        // необходимый пустой конструктор.
    }
    /*метод для создания экземпляра фрагмента.
	входные значения: параметр 1, параметр 2.
  	возвращаемые значения: новый экземпляр.*/
    public static ShareFragment newInstance(String param1, String param2) {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /*создание фрагмента.
	входные значения: сохранненый статус фрагмента.
  	возвращаемые значения: - .*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /*вью верчу я на хую. спать хочется :х*/
    /*НЕ ЗАБУДЬ УДАЛИТЬ ЭТО ВСЕ ТОЛЬКО САВЕЛЬЕВ НЕ ОДОБРИТ*/
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share, container, false);
    }
}
