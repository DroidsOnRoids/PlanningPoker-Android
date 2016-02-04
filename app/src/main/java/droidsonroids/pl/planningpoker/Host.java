package droidsonroids.pl.planningpoker;

import java.io.Serializable;

public class Host implements Serializable {
    private final String mEndpointId;
    private final String mEndpointName;

    public Host(final String endpointId, final String endpointName) {
        mEndpointId = endpointId;
        mEndpointName = endpointName;
    }

    public String getEndpointId() {
        return mEndpointId;
    }

    public String getEndpointName() {
        return mEndpointName;
    }
}
