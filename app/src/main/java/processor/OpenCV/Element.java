package processor.OpenCV;

/**
 * Created by SuperbiaYang on 2016/7/10.
 */
public class Element {
    public static final int MORPH_RECT = 0;
    public static final int MORPH_CROSS = 1;
    public static final int MORPH_ELLIPSE = 2;

    public static final int DEFAULT_TYPE;
    public static final int DEFAULT_SIZE = 3;

    static {
        DEFAULT_TYPE = MORPH_RECT;
    }
}
