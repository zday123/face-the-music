package durummixto.facethemusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.spotify.android.appremote.*;
import com.spotify.protocol.*;



public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "dc8addce37f04b198a0ac367af5964ed";
    private static final String REDIRECT_URI = "com.durummixto.facethemusic://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =(new ConnectionParams.Builder(CLIENT_ID))
                .setRedirectUri(REDIRECT_URI).showAuthView(true).build();
    }
    private void connected() {

    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
