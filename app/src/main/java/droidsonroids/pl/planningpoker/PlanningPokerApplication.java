package droidsonroids.pl.planningpoker;

import android.app.Application;

public class PlanningPokerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //GoogleNearbyService.getInstance().initialize(this);
    }
}
