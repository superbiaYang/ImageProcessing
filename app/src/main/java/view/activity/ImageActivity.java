package view.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import superbiayang.imageprocessing.PicInfo;
import superbiayang.imageprocessing.R;
import view.component.ZoomImageView;

public class ImageActivity extends Activity {

    static PicInfo pic;

    public static void setPic(PicInfo src) {
        pic = src;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        if (pic == null) {
            return;
        }

        int[] pixels = pic.getPixels();
        int width = pic.getWidth();
        int height = pic.getHeight();
        ZoomImageView image = (ZoomImageView) findViewById(R.id.zoom_imageView);
        image.setImageBitmap(Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888));
    }
}
