package durummixto.facethemusic;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.SpotifyAppRemote;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "dc8addce37f04b198a0ac367af5964ed";
    private static final String REDIRECT_URI = "com.durummixto.facethemusic://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private static final int REQUEST_CODE = 1337;

    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CameraManager c = getBaseContext().getSystemService(CameraManager.class);
        String[] camIdList = null;
        try {
            camIdList = c.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.d("camera access", "OH SHIT");
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
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
