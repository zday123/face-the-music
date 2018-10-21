package durummixto.facethemusic;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.SpotifyAppRemote;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "3236da481a644c8da2ad80febdaafa1c";
    private static final String REDIRECT_URI = "testschema://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private static final int REQUEST_CODE = 1337;
//
//    AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
//
//    builder.setScopes(new String[]{"streaming"});
//    AuthenticationRequest request = builder.build();
//
//    AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CameraManager c = getBaseContext().getSystemService(CameraManager.class);
        String[] camIdList = null;
        try {
            camIdList = c.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.d("camera access", "OH DARN");
            System.exit(1);
        }
        //c.openCamera(camIdList[0],);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =(new ConnectionParams.Builder(CLIENT_ID))
                .setRedirectUri(REDIRECT_URI).showAuthView(true).build();
        try {
            Map<String, String> map = VisionInterfacer.getResponses(this);

        } catch(Exception e) {
            Log.e("Vision error", "yeet");
        }
    }
    private void connected() {
        ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build();
        SpotifyAppRemote.connect(this, connectionParams, new Connector.ConnectionListener() {

            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d("MainActivity", "Connected! YaY!");

                //Now you can start interacting with App Remote
                connected();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
            }
        });
    }
    private void connected() {
//        mSpotifyAppRemote.getPlayerApi().play("spotify:user:spotify:playlist:37i9dQZF1DX7K31D69s4M1");
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState()
                .setEventCallback(new Subscription.EventCallback<PlayerState>() {
                    @Override
                    public void onEvent(PlayerState playerState) {
                        final Track track = playerState.track;
                        if (track != null) {
                            Log.d("MainActivity", track.name + " by " + track.artist.name);
                        }
                    }
                });
    }
    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response =
                    AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    break;
                case ERROR:
                    break;
                default:
            }
        }
    }
}
