package droidsonroids.pl.planningpoker;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChooseCardActivity extends AppCompatActivity {

    FragmentPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        ViewPager viewPager = (ViewPager) findViewById(R.id.card_view_pager);
        mPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);



    }
}
