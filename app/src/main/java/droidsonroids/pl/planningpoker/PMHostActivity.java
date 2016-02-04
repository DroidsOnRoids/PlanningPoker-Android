package droidsonroids.pl.planningpoker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

public class PMHostActivity extends AppCompatActivity implements GoogleNearbyService.NearbyHostCallback {

    private static final String EXTRA_HOST = "EXTRA_HOST";
    private RecyclerView mRecyclerView;
    private PMHostAdapter mHostAdapter;
    private Host mHost;

    private GoogleNearbyService mGoogleNearbyService;

    public static Intent getStartIntent(final Context context, final Host host) {
        Intent intent = new Intent(context, PMHostActivity.class);
        intent.putExtra(EXTRA_HOST, host);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        mHost = (Host)getIntent().getExtras().getSerializable(EXTRA_HOST);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mHostAdapter = new PMHostAdapter();
        mRecyclerView.setAdapter(mHostAdapter);

        mGoogleNearbyService = new GoogleNearbyService();
        mGoogleNearbyService.initialize(this);

        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleNearbyService.startAdvertising(mHost.getEndpointName(), this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleNearbyService.disconnect();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle(mHost.getEndpointName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public void onAdvertisingSuccess() {
        //no-op
    }

    @Override
    public void onAdvertisingFailed(final int statusCode) {
        //no-op
    }

    @Override
    public void onConnectionAccepted(final Client client) {
        mHostAdapter.addClient(client);
    }

    @Override
    public void onConnectionFailed(final int statusCode) {

    }

    @Override
    public void onCardSelected(final String clientId, final String cardText) {
        mHostAdapter.updateClientStoryPoints(clientId, cardText);
    }
}
