package processor;

import android.support.annotation.NonNull;

/**
 * Created by SuperbiaYang on 2016/6/2.
 */
public class Basic {
    public final static int MASK_RED = 0xFFFF0000;
    public final static int MASK_GREEN = 0xFF00FF00;
    public final static int MASK_BLUE = 0xFF0000FF;

    public static int[] channelSeparate(@NonNull int[] src, int mask) {
        int[] dst = new int[src.length];
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i] & mask;
        }
        return dst;
    }
}
