package processor.OpenCV;

/**
 * Created by SuperbiaYang on 2016/6/3.
 */
public class GrayscaleMorphology {
    static {
        System.loadLibrary("OpenCVHelper");
    }

    public static native void standardGradient(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void externalGradient(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void internalGradient(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void OBR(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void CBR(int[] src, int[] dst, int width, int height, int elemSize, int elemType);
}
