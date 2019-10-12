package app.marees.mars.MarsService;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import app.marees.mars.Singletons.Myapp;
import app.marees.mars.room.MyViewModel;

public class DummyFragment extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModel mars;
        MyViewModel connect;
        connect = ViewModelProviders.of(this).get(MyViewModel.class);
        mars = ViewModelProviders.of(this).get(MyViewModel.class);
        Myapp.setMyViewModel(mars);
        Myapp.setMyViewModel2(connect);
        finish();
    }
}
