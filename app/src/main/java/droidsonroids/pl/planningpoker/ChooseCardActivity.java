package droidsonroids.pl.planningpoker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChooseCardActivity extends AppCompatActivity implements GoogleNearbyService.NearbyClientCallback {

    private static final String EXTRA_HOST = "EXTRA_HOST";
    private static final String EXTRA_CLIENT_NAME = "EXTRA_CLIENT_NAME";

    private FragmentPagerAdapter mPagerAdapter;
    private PokerViewPager mViewPager;

    public static Intent getStartIntent(final Context context, final Host host, final String clientName) {
        Intent intent = new Intent(context, ChooseCardActivity.class);
        intent.putExtra(EXTRA_HOST, host);
        intent.putExtra(EXTRA_CLIENT_NAME, clientName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        final Host host = (Host) getIntent().getExtras().getSerializable(EXTRA_HOST);
        getSupportActionBar().setTitle(host.getEndpointName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (PokerViewPager) findViewById(R.id.card_view_pager);
        mPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
        mViewPager = (PokerViewPager) findViewById(R.id.card_view_pager);
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        final Bundle extras = getIntent().getExtras();
        final Host host = (Host) extras.getSerializable(EXTRA_HOST);
        final String clientName = extras.getString(EXTRA_CLIENT_NAME);
        GoogleNearbyService.getInstance().connectTo(host, clientName, this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        GoogleNearbyService.getInstance().disconnect();
        super.onStop();
    }

    @Subscribe
    public void onEvent(CardClickedEvent event) {
        System.out.println("CLICKED");
        mViewPager.setPagingEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onConnectionSuccess() {
        //no-op
    }

    @Override
    public void onConnectionError() {
        Toast.makeText(getApplicationContext(), "Cannot connect to host...", Toast.LENGTH_LONG).show();
        finish();
    }
}
