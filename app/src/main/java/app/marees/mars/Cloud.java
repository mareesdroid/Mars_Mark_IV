package app.marees.mars;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class Cloud extends FragmentActivity {


    private ViewPager mypage;
    private MyPager mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);

        // Initilization
        mypage = (ViewPager) findViewById(R.id.viewpager);

        mAdapter = new MyPager(getSupportFragmentManager());

        mypage.setAdapter(mAdapter);

    }

}
