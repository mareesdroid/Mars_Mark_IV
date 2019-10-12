package app.marees.mars;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import app.marees.NewsPager;


public class News extends FragmentActivity {
    private ViewPager mypage;
    private NewsPager mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        // Initilization
        mypage = (ViewPager) findViewById(R.id.viewpager2);

        mAdapter = new NewsPager(getSupportFragmentManager());

        mypage.setAdapter(mAdapter);

    }
}
