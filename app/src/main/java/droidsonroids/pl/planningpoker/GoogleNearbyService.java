package droidsonroids.pl.planningpoker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleNearbyService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Connections.MessageListener,
        Connections.ConnectionRequestListener, Connections.EndpointDiscoveryListener {

    private static final String TAG = GoogleNearbyService.class.getSimpleName();

    private static volatile GoogleNearbyService INSTANCE;

    private static int[] NETWORK_TYPES = {
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_ETHERNET
    };

    private GoogleApiClient mGoogleApiClient;
    private boolean mIsHost;
    private String mServiceId;
    private Context mApplicationContext;
    private NearbyHostCallback mNearbyHostCallback;

    private NearbyDiscoveryCallback mNearbyDiscoveryCallback;
    private String mHostName;

    public static final long NO_TIMEOUT = 0L;
    private long mTimeout;

    private NearbyClientCallback mNearbyClientCallback;
    private Host mHost;
    private String mClientName;

    private enum Message {
        CARD_SELECTED, FLIP_CARDS
    }

    public static GoogleNearbyService getInstance() {
        if (INSTANCE == null) {
            synchronized (GoogleNearbyService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GoogleNearbyService();
                }
            }
        }

        return INSTANCE;
    }

    public void initialize(final Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();

        mApplicationContext = context;

        mServiceId = context.getString(R.string.service_id);
    }

    @Override
    public void onConnected(final Bundle bundle) {
        if (mNearbyHostCallback != null) {
            startAdvertisingAfterConnectionEstablished();
        } else if (mNearbyDiscoveryCallback != null) {
            startDiscoveryAfterConnectionEstablished();
        } else if (mNearbyClientCallback != null) {
            connectToAfterEstablished();
        }
    }

    @Override
    public void onConnectionSuspended(final int i) {
        Log.d(TAG, "onConnectionSuspended() called with: " + "i = [" + i + "]");
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called with: " + "connectionResult = [" + connectionResult + "]");
    }

    public void disconnect() {
        if (mIsHost) {
            Nearby.Connections.stopAdvertising(mGoogleApiClient);
        }


        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void startAdvertising(final String hostName, final NearbyHostCallback callback) {
        if (!isConnectedToNetwork()) {
            return;
        }

        mIsHost = true;
        mNearbyHostCallback = callback;
        mHostName = hostName;
        mGoogleApiClient.connect();
    }

    public void startAdvertisingAfterConnectionEstablished() {
        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(mServiceId));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        Nearby.Connections
                .startAdvertising(mGoogleApiClient, mHostName, appMetadata, NO_TIMEOUT, this)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
                    @Override
                    public void onResult(Connections.StartAdvertisingResult result) {
                        if (mNearbyHostCallback == null) {
                            return;
                        }

                        if (result.getStatus().isSuccess()) {
                            mNearbyHostCallback.onAdvertisingSuccess();
                        } else {
                            int statusCode = result.getStatus().getStatusCode();
                            mNearbyHostCallback.onAdvertisingFailed(statusCode);
                        }
                    }
                });
    }

    public void startDiscovery(final NearbyDiscoveryCallback clientCallback) {
        startDiscovery(NO_TIMEOUT, clientCallback);
    }

    public void startDiscovery(final long timeout, final NearbyDiscoveryCallback clientCallback) {
        if (!isConnectedToNetwork()) {
            return;
        }

        mTimeout = timeout;
        mNearbyDiscoveryCallback = clientCallback;
        mGoogleApiClient.connect();
    }

    private void startDiscoveryAfterConnectionEstablished() {
        mIsHost = false;

        Nearby.Connections
                .startDiscovery(mGoogleApiClient, mServiceId, mTimeout, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (mNearbyDiscoveryCallback == null) {
                            return;
                        }

                        if (status.isSuccess()) {
                            mNearbyDiscoveryCallback.onDiscoveringSuccess();
                        } else {
                            int statusCode = status.getStatus().getStatusCode();
                            mNearbyDiscoveryCallback.onDiscoveringFailed(statusCode);
                        }
                    }
                });
    }

    public void connectTo(final Host host, final String clientName, final NearbyClientCallback clientCallback) {
        if (!isConnectedToNetwork()) {
            return;
        }

        mHost = host;
        mClientName = clientName;
        mNearbyClientCallback = clientCallback;
        mGoogleApiClient.connect();
    }

    private void connectToAfterEstablished() {
        Nearby.Connections.sendConnectionRequest(mGoogleApiClient, mClientName, mHost.getEndpointId(), null,
                new Connections.ConnectionResponseCallback() {
                    @Override
                    public void onConnectionResponse(String remoteEndpointId, Status status,
                            byte[] bytes) {
                        if (status.isSuccess()) {
                            // Successful connection
                        } else {
                            // Failed connection
                        }
                    }
                }, this);
    }

    private boolean isConnectedToNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        for (int networkType : NETWORK_TYPES) {
            NetworkInfo info = connManager.getNetworkInfo(networkType);
            if (info != null && info.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConnectionRequest(final String remoteEndpointId, final String cliendId, final String clientName, final byte[] payload) {
        if (mIsHost) {
            Nearby.Connections
                    .acceptConnectionRequest(mGoogleApiClient, remoteEndpointId, payload, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (mNearbyHostCallback == null) {
                                return;
                            }

                            if (status.isSuccess()) {
                                mNearbyHostCallback.onConnectionAccepted(new Client(cliendId, clientName));
                            } else {
                                int statusCode = status.getStatusCode();
                                mNearbyHostCallback.onConnectionFailed(statusCode);
                            }
                        }
                    });
        } else {
            Nearby.Connections.rejectConnectionRequest(mGoogleApiClient, remoteEndpointId);
        }
    }

    public void sendCardSelectedMessage(final String hostId, final String cardText) {
        final String message = Message.CARD_SELECTED + "|" + cardText;
        Nearby.Connections.sendReliableMessage(mGoogleApiClient, hostId, message.getBytes());
    }

    public void sendFlipCardsMessage(final String clientId) {
        final String message = Message.FLIP_CARDS.toString();
        Nearby.Connections.sendReliableMessage(mGoogleApiClient, clientId, message.getBytes());
    }

    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onEndpointFound(endpointId, endpointName);
        }
    }

    @Override
    public void onEndpointLost(final String endpointId) {
        if (mNearbyDiscoveryCallback != null) {
            mNearbyDiscoveryCallback.onEndpointLost(endpointId);
        }
    }

    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        final String[] data = Arrays.toString(payload).split("|");
        final Message messageType = Message.valueOf(data[0]);
        switch (messageType) {
            case CARD_SELECTED:
                final String cardText = data[1];
                if (mNearbyHostCallback != null) {
                    mNearbyHostCallback.onCardSelected(endpointId, cardText);
                }
                break;
            case FLIP_CARDS:
                if (mNearbyClientCallback != null) {
                    mNearbyClientCallback.onFlipCard();
                }
                break;
        }
    }

    @Override
    public void onDisconnected(final String endpointId) {
        mNearbyHostCallback = null;
        mNearbyDiscoveryCallback = null;
        mNearbyClientCallback = null;
    }

    public interface NearbyHostCallback {
        void onAdvertisingSuccess();

        void onAdvertisingFailed(final int statusCode);

        void onConnectionAccepted(final Client client);

        void onConnectionFailed(final int statusCode);

        void onCardSelected(final String clientId, final String cardText);
    }

    public interface NearbyDiscoveryCallback {
        void onDiscoveringSuccess();

        void onDiscoveringFailed(final int statusCode);

        void onEndpointFound(final String endpointId, final String endpointName);

        void onEndpointLost(final String endpointId);
    }

    public interface NearbyClientCallback {
        void onConnectionSuccess();

        void onConnectionError();

        void onFlipCard();
    }
}
