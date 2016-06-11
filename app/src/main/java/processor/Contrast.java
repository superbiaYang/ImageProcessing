package processor;

import android.graphics.Color;

/**
 * Created by SuperbiaYang on 2016/6/10.
 */
public class Contrast {

    private static int fixGray(int gray) {
        gray = Math.min(gray, 0xFF);
        gray = Math.max(gray, 0);
        return gray;
    }

    public static void linear(int[] src, int[] dst, double k, double b) {
        for (int i = 0; i < src.length; i++) {
            int red = Color.red(src[i]);
            red = (int) (k * red + b);
            int green = Color.green(src[i]);
            green = (int) (k * green + b);
            int blue = Color.blue(src[i]);
            blue = (int) (k * blue + b);
            dst[i] = Color.rgb(fixGray(red), fixGray(green), fixGray(blue));
        }
    }

    public static void log(int[] src, int[] dst, double a, double b, double c) {
        for (int i = 0; i < src.length; i++) {
            int red = Color.red(src[i]);
            red = (int) (a * (Math.log(red) + 1) / Math.log(b) + c);
            int green = Color.green(src[i]);
            green = (int) (a * (Math.log(green) + 1) / Math.log(b) + c);
            int blue = Color.blue(src[i]);
            blue = (int) (a * (Math.log(blue) + 1) / Math.log(b) + c);
            dst[i] = Color.rgb(fixGray(red), fixGray(green), fixGray(blue));
        }
    }

    public static void pow(int[] src, int[] dst, double a, double b, double c) {
        for (int i = 0; i < src.length; i++) {
            int red = Color.red(src[i]);
            if (red < 10) {
                int j = 1;
                j++;
            }
            red = (int) (Math.pow(b, c * red + a) - 1);
            int green = Color.green(src[i]);
            green = (int) (Math.pow(b, c * green + a) - 1);
            int blue = Color.blue(src[i]);
            blue = (int) (int) (Math.pow(b, c * blue + a) - 1);
            dst[i] = Color.rgb(fixGray(red), fixGray(green), fixGray(blue));
        }
    }
}
