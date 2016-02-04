package droidsonroids.pl.planningpoker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

public class HostActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TrueHostAdapter mHostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mHostAdapter = new TrueHostAdapter();
        mRecyclerView.setAdapter(mHostAdapter);

        initToolbar();
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Host");
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
}
