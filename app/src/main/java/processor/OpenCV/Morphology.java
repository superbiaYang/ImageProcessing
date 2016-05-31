package processor.OpenCV;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by SuperbiaYang on 2016/5/31.
 */
public class Morphology {
    static {
        System.loadLibrary("OpenCVHelper");
    }

    public static Bitmap erode(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        erode(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void erode(int[] src, int[] dst, int width, int height);

    public static Bitmap dilate(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        dilate(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void dilate(int[] src, int[] dst, int width, int height);

    public static Bitmap open(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        open(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void open(int[] src, int[] dst, int width, int height);

    public static Bitmap close(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        close(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void close(int[] src, int[] dst, int width, int height);
}
