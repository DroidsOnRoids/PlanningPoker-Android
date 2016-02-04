package droidsonroids.pl.planningpoker;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChooseCardActivity extends AppCompatActivity {

    FragmentPagerAdapter mPagerAdapter;
    private PokerViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        mViewPager = (PokerViewPager) findViewById(R.id.card_view_pager);
        mPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Subscribe
    public void onEvent(CardClickedEvent event) {
        System.out.println("CLICKED");
        mViewPager.setPagingEnabled(false);
    }
}
