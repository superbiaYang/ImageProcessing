package processor.OpenCV;

/**
 * Created by SuperbiaYang on 2016/5/31.
 */
public class Morphology {
    static {
        System.loadLibrary("OpenCVHelper");
    }

    public static native void erode(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void dilate(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void open(int[] src, int[] dst, int width, int height, int elemSize, int elemType);

    public static native void close(int[] src, int[] dst, int width, int height, int elemSize, int elemType);
}
