package processor;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by SuperbiaYang on 2016/5/31.
 */

public class Grayscale {
    public static final int COLOR_THRESHOLD = 256;
    public static final int HISTOGRAM_HEIGHT = 100;

    public static Return generate(int[] src, int width, int height, GrayType type) {
        Return ret = new Return();
        ret.histogram = new int[COLOR_THRESHOLD];
        ret.pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pos = x + y * width;
                int gray = 0;
                switch (type) {
                    case RED:
                        gray = Color.red(src[pos]);
                        break;
                    case GREEN:
                        gray = Color.green(src[pos]);
                        break;
                    case BLUE:
                        gray = Color.blue(src[pos]);
                        break;
                    case AVG:
                        gray = (Color.red(src[pos]) +
                                Color.green(src[pos]) +
                                Color.blue(src[pos])) / 3;
                        break;
                    case OPENCV:
                        gray = (int) (0.072169 * Color.blue(src[pos]) +
                                0.715160 * Color.green(src[pos]) +
                                0.212671 * Color.red(src[pos]));
                        break;
                    case BIO:
                        gray = (int) (0.11 * Color.blue(src[pos]) +
                                0.59 * Color.green(src[pos]) +
                                0.3 * Color.red(src[pos]));
                        break;
                    default:
                        break;
                }
                ret.histogram[gray]++;
                ret.pixels[pos] = Color.rgb(gray, gray, gray);
            }
        }

        return ret;
    }

    public static Bitmap Histogram(int[] histogram) {
        int[] pixels = new int[COLOR_THRESHOLD * HISTOGRAM_HEIGHT];
        int max = 0;
        for (int i = 0; i < COLOR_THRESHOLD; i++) {
            if (histogram[i] > max) {
                max = histogram[i];
            }
            for (int j = 0; j < HISTOGRAM_HEIGHT; j++) {
                pixels[i + COLOR_THRESHOLD * j] = Color.WHITE;
            }
        }
        for (int x = 0; x < COLOR_THRESHOLD; x++) {
            int height = histogram[x] * HISTOGRAM_HEIGHT / max;
            for (int y = HISTOGRAM_HEIGHT - height; y < HISTOGRAM_HEIGHT; y++) {
                pixels[x + COLOR_THRESHOLD * y] = Color.BLACK;
            }
        }
        return Bitmap.createBitmap(pixels, COLOR_THRESHOLD, HISTOGRAM_HEIGHT, Bitmap.Config.RGB_565);
    }


    public enum GrayType {
        RED,        //gray = red
        GREEN,      //gray = green
        BLUE,       //gray = blue
        AVG,        //gray = (red + green + blue) / 3
        OPENCV,     //gray = 0.072169 * blue + 0.715160 * green + 0.212671 * red
        BIO,        //gray = 0.11 * blue + 0.59 * green + 0.3 * red
    }

    public static class Return {
        public int[] pixels;
        public int[] histogram;
    }
}
