package durummixto.facethemusic;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.spotify.android.appremote.*;
import com.spotify.protocol.*;


=======
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.List;
>>>>>>> fe5221707d35f24efa857c0447ba765056cdd6b8

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "dc8addce37f04b198a0ac367af5964ed";
    private static final String REDIRECT_URI = "com.durummixto.facethemusic://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CameraManager c = getBaseContext().getSystemService(CameraManager.class);
        String[] camIdList = null;
        try {
            camIdList = c.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.println(6, "camera access", "OH SHIT");
        }
        //c.openCamera(camIdList[0],);
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
