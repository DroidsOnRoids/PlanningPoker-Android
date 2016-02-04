package droidsonroids.pl.planningpoker;

public class Client {

    private final String mName;
    private String mStoryPoints;
    private String mEndpointId;

    public Client(final String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public String getStoryPoints() {
        return mStoryPoints;
    }

    public void setStoryPoints(final String storyPoints) {
        mStoryPoints = storyPoints;
    }

    public String getEndpointId() {
        return mEndpointId;
    }

    public void setEndpointId(final String endpointId) {
        mEndpointId = endpointId;
    }
}
