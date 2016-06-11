package processor.OpenCV;

/**
 * Created by SuperbiaYang on 2016/6/3.
 */
public class GrayscaleMorphology {
    static {
        System.loadLibrary("OpenCVHelper");
    }

    public static native void edge(int[] src, int[] dst, int width, int height);

    public static native void OBR(int[] src, int[] dst, int width, int height, int n);

    public static native void CBR(int[] src, int[] dst, int width, int height, int n);
}
