package processor.OpenCV;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by SuperbiaYang on 2016/5/30.
 * Provide the image filter function based on OpenCV
 * Include:
 * Median blur
 * Mean blur
 * Gaussian blur
 * Sobel
 * Prewitt
 * Roberts
 */
public class Filter {
    static {
        System.loadLibrary("OpenCVHelper");
    }

    public static Bitmap medianBlur(@NonNull Bitmap srcBitmap, int size) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        medianBlur(src, dst, width, height, size);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void medianBlur(int[] src, int[] dst, int width, int height, int size);

    public static Bitmap meanBlur(@NonNull Bitmap srcBitmap, int size) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        meanBlur(src, dst, width, height, size);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void meanBlur(int[] src, int[] dst, int width, int height, int size);

    public static Bitmap gaussianBlur(@NonNull Bitmap srcBitmap, int size, double sigma) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        gaussianBlur(src, dst, width, height, size, sigma);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void gaussianBlur(int[] src, int[] dst, int width, int height, int size, double sigma);

    public static Bitmap sobel(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        sobel(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void sobel(int[] src, int[] dst, int width, int height);

    public static Bitmap prewitt(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        prewitt(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void prewitt(int[] src, int[] dst, int width, int height);

    public static Bitmap roberts(@NonNull Bitmap srcBitmap) {
        int height = srcBitmap.getHeight();
        int width = srcBitmap.getWidth();
        int[] src = new int[height * width];
        int[] dst = new int[height * width];
        srcBitmap.getPixels(src, 0, width, 0, 0, width, height);
        roberts(src, dst, width, height);
        return Bitmap.createBitmap(dst, width, height, Bitmap.Config.ARGB_8888);
    }

    private static native void roberts(int[] src, int[] dst, int width, int height);
}
