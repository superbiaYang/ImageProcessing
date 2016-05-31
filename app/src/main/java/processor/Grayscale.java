package processor;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by SuperbiaYang on 2016/5/31.
 */

public class Grayscale {
    public static final int COLOR_THRESHOLD = 256;
    public static final int HISTOGRAM_HEIGHT = 100;

    public static Return generate(Bitmap source, GrayType type) {
        Return ret = new Return();
        ret.histogram = new int[COLOR_THRESHOLD];

        int[] pixels = new int[source.getWidth() * source.getHeight()];
        source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int pos = x + y * source.getWidth();
                int gray = 0;
                switch (type) {
                    case RED:
                        gray = Color.red(pixels[pos]);
                        break;
                    case GREEN:
                        gray = Color.green(pixels[pos]);
                        break;
                    case BLUE:
                        gray = Color.blue(pixels[pos]);
                        break;
                    case AVG:
                        gray = (Color.red(pixels[pos]) +
                                Color.green(pixels[pos]) +
                                Color.blue(pixels[pos])) / 3;
                        break;
                    case OPENCV:
                        gray = (int) (0.072169 * Color.blue(pixels[pos]) +
                                0.715160 * Color.green(pixels[pos]) +
                                0.212671 * Color.red(pixels[pos]));
                        break;
                    case BIO:
                        gray = (int) (0.11 * Color.blue(pixels[pos]) +
                                0.59 * Color.green(pixels[pos]) +
                                0.3 * Color.red(pixels[pos]));
                        break;
                    default:
                        break;
                }
                ret.histogram[gray]++;
                pixels[pos] = Color.rgb(gray, gray, gray);
            }
        }
        ret.bitmap = Bitmap.createBitmap(pixels, source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
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
        public Bitmap bitmap;
        public int[] histogram;
    }
}
