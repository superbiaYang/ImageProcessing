package superbiayang.imageprocessing;

import android.graphics.Bitmap;

/**
 * Created by SuperbiaYang on 2016/6/1.
 */
public class PicManager {
    public final static int NO_THRESHOLD = -1;
    private int color[];
    private int gray[];
    private int threshold;
    private int binary[];
    private int width;
    private int height;

    public PicManager() {
        color = null;
        gray = null;
        binary = null;
        threshold = NO_THRESHOLD;
        width = 0;
        height = 0;
    }

    public int getPixelsNum() {
        return width * height;
    }

    public void init(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        color = new int[width * height];
        bitmap.getPixels(color, 0, width, 0, 0, width, height);
        gray = null;
        binary = null;
        threshold = NO_THRESHOLD;
    }

    public Bitmap getBinaryBitmap() {
        if (binary == null) {
            return null;
        }
        return Bitmap.createBitmap(binary, width, height, Bitmap.Config.ARGB_8888);
    }

    public int[] getColorPixels() {
        return color;
    }

    public int[] getGrayPixels() {
        return gray;
    }

    public int[] getBinaryPixels() {
        return binary;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
