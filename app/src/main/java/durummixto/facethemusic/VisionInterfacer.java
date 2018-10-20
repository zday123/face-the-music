package durummixto.facethemusic;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;

public class QuickstartSample {

    public static void main(String[] args) throws Exception{
        getResponses(ImageProcessor.getByteString(args[0]));
    }
    public static void getResponses(ByteString imgBytes, Context context) throws Exception {
        // Instantiates a client
        Drawable d = context.getResources().getDrawable(R.drawable.sadpic);
            

        getResponses(ImageProcessor.getByteString());

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            System.out.printf("%s : %s\n", k, v.toString()));
                }
            }
        }
    }
}