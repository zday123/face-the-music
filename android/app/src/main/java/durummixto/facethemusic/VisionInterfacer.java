package durummixto.facethemusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VisionInterfacer {

    public static Map<String, String> getResponses(Context context) throws Exception{

        // Instantiates a client
        Drawable d = context.getResources().getDrawable(R.drawable.sadpic);
        Log.d("yeet", d.toString());
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] bitmapdata = stream.toByteArray();
        ByteString imgBytes = ByteString.copyFrom(bitmapdata);
        List<AnnotateImageRequest> requests = new ArrayList<>();

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);
        Log.d("yote", "" + bitmapdata.length);

//        InputStream is = context.getResources().openRawResource(R.raw.token);

        HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        JsonFactory JSON_FACTORY = new JacksonFactory();
        List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/androidpublisher");


        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId("fb88340f10801a8b298a9418e4cbfbd4fac2a44e")
                .setServiceAccountScopes(scopes)
                .setServiceAccountPrivateKeyFromP12File(StreamUtil.stream2file(context.getResources().openRawResource(R.raw.auth)))
                .build();

        Log.d("aa","aa");
        Date da = Date.from(Instant.now());
            da.setYear(Date.from(Instant.now()).getYear() + 1);
        GoogleCredentials sac = ServiceAccountCredentials.newBuilder()
                .setPrivateKey(credential.getServiceAccountPrivateKey())
                .setPrivateKeyId(credential.getServiceAccountPrivateKeyId())
                .setClientEmail(credential.getServiceAccountId())
                .setScopes(scopes)
                .setAccessToken(new AccessToken(credential.getAccessToken(), da))
                .build();
        Log.d("ahhh", "as");
//        String apiKey = ApiKeyStore.key;
//        InputStream authStream = new ByteArrayInputStream(apiKey.getBytes(StandardCharsets.UTF_8));
//        Log.d("yote", authStream.toString());
//        CredentialsProvider credentialsProvider = null;
//        try {
//            credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(is));
//        } catch (Exception e) {
//            Log.d("yeet", e.getMessage());
//        }
//        ImageAnnotatorSettings imageAnnotatorSettings = null;
//        try {
//            imageAnnotatorSettings =
//                    ImageAnnotatorSettings.newBuilder()
//                            .setCredentialsProvider(credentialsProvider)
//                            .build();
//        } catch (Exception e) {
//            Log.d("yeet", e.getMessage());
//        }
//
//        GoogleCredential credential = new GoogleCredential().setAccessToken("ya29.Gls8BiBLwTz2U9No8b-AwpDewveJjERtXQVQhItlEWFo48_xlKrOzeMHKoQp0LzC71NDHkPRO7g1ppde8fsvsZK-a_TupawDBt9LPlQeg0U1fHPNXabjfnF3HwaY");
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(sac);
        ImageAnnotatorSettings imageAnnotatorSettings =
                    ImageAnnotatorSettings.newBuilder().setCredentialsProvider(credentialsProvider)
                            .build();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//        Vision vision = new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential).build();
        try {
            try (ImageAnnotatorClient client =
                         ImageAnnotatorClient.create(imageAnnotatorSettings)){
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        Log.d("yeet", "Error: " + res.getError().getMessage());
                    }

                    // For full list of available annotations, see http://g.co/cloud/vision/docs
                    for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
                        Log.d("yeet",
                                String.format("anger: %s\njoy: %s\nsurprise: %s\nposition: %s",
                                        annotation.getAngerLikelihood(),
                                        annotation.getJoyLikelihood(),
                                        annotation.getSurpriseLikelihood(),
                                        annotation.getBoundingPoly()));
                    }
                }
            }
        } catch (Exception e) {
            Log.d("yeet", e.getMessage());
        }
        return null;
    }
}