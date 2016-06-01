package processor.OpenCV;

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

    public static native void medianBlur(int[] src, int[] dst, int width, int height, int size);

    public static native void meanBlur(int[] src, int[] dst, int width, int height, int size);

    public static native void gaussianBlur(int[] src, int[] dst, int width, int height, int size, double sigma);

    public static native void sobel(int[] src, int[] dst, int width, int height);

    public static native void prewitt(int[] src, int[] dst, int width, int height);

    public static native void roberts(int[] src, int[] dst, int width, int height);
}
