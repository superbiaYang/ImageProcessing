package processor;

/**
 * Created by SuperbiaYang on 2016/6/1.
 */
public class Binary {
    public static int otsu(int[] histogram) {
        int threshold = 1; // 阈值

        double sum = 0;
        int n = 0;
        for (int k = 0; k <= 255; k++) {
            sum += (double) k * histogram[k];
            n += histogram[k];
        }

        double csum = 0;
        int n1 = 0;
        double fmax = -1.0;
        for (int k = 0; k < 255; k++) {
            n1 += histogram[k];
            if (n1 == 0) {
                continue;
            }
            int n2 = n - n1;
            if (n2 == 0) {
                break;
            }
            csum += (double) k * histogram[k];
            double m1 = csum / n1;
            double m2 = (sum - csum) / n2;
            double sb = n1 * n2 * (m1 - m2) * (m1 - m2);
            if (sb > fmax) {
                fmax = sb;
                threshold = k;
            }
        }
        return threshold;
    }

    public static int[] generate(int[] src, int width, int height, int min, int max) {
        int[] dst = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = x + y * width;
                int gray = src[i] & 0xFF;
                dst[i] = (gray < min || gray > max) ? 0xFFFFFFFF : 0xFF000000;
            }
        }
        return dst;
    }
}
