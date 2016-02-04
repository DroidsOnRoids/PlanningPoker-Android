package droidsonroids.pl.planningpoker;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements GoogleNearbyService.NearbyDiscoveryCallback, HostAdapter.OnHostClickListener {

    private RecyclerView mRecyclerView;
    private HostAdapter mHostAdapter;
    private LinearLayout mLayoutContainer;
    private EditText mEditName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initList();
    }

    private void findViews() {
        mLayoutContainer = (LinearLayout) findViewById(R.id.layout_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mEditName = (EditText) findViewById(R.id.edit_name);
    }

    private void initList() {
        mHostAdapter = new HostAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHostAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleNearbyService.getInstance().startDiscovery(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleNearbyService.getInstance().disconnect();
    }

    @Override
    public void onDiscoveringSuccess() {
        //no-op
    }

    @Override
    public void onDiscoveringFailed(final int statusCode) {
        Snackbar.make(mLayoutContainer, "Discovery failed", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEndpointFound(final String endpointId, final String endpointName) {
        mHostAdapter.addHost(endpointId, endpointName);
    }

    @Override
    public void onEndpointLost(final String endpointId) {
        mHostAdapter.removeHost(endpointId);
    }

    @Override
    public void onHostClick(final Host host) {
        startActivity(ChooseCardActivity.getStartIntent(this, host, mEditName.getText().toString().trim()));
    }
}
