package durummixto.facethemusic;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;



import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "3236da481a644c8da2ad80febdaafa1c:d0f70090179f46179ef47bc37f6052f7";
    private static final String REDIRECT_URI = "com.durummixto.facethemusic://callback";
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
            Log.println(Log.DEBUG, "camera access", "OH SHIT");
            System.exit(1);
        }
        //c.openCamera(camIdList[0],);
    }
    @Override
    protected void onStart() {
        super.onStart();
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
        mSpotifyAppRemote.getPlayerApi().play("spotify:user:spotify:playlist:37i9" +
                "dQZF1DX2sUQwD7tbmL");
    }
    @Override
    protected void onStop() {
        super.onStop();
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
