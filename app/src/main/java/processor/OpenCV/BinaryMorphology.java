package processor.OpenCV;

/**
 * Created by SuperbiaYang on 2016/6/2.
 */
public class BinaryMorphology {
    static {
        System.loadLibrary("OpenCVHelper");
    }

    public static native void distanceTransform(int[] src, int[] dst, int width, int height);

    public static native void skeleton(int[] src, int[] dst, int[] skeleton, int width, int height);

    public static native void reconstruct(int[] skeleton, int[] dst, int width, int height);

    public static native void conditionalDilation(int[] src, int[] mask, int[] dst, int width, int height);

}
