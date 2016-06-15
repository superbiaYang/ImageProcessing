package processor;

import superbiayang.imageprocessing.PicInfo;

/**
 * Created by SuperbiaYang on 2016/6/11.
 */
public class Geometry {

    public static PicInfo nearestNeighborRotate(PicInfo pic, int degree) {
        int width = pic.getWidth();
        int height = pic.getHeight();
        int[] src = pic.getPixels();
        int newWidth, newHeight;

        int n = degree / 360;
        degree = degree - n * 360;
        if (degree < 0) {
            degree += 360;
        }
        double ang = Math.toRadians(degree);

        double sin = Math.sin(ang);
        double cos = Math.cos(ang);

        if (degree == 180 || degree == 0 || degree == 360) {
            newWidth = width;
            newHeight = height;
        } else if (degree == 90 || degree == 270) {
            //noinspection SuspiciousNameCombination
            newWidth = height;
            //noinspection SuspiciousNameCombination
            newHeight = width;
        } else {
            newWidth = (int) (width * Math.abs(cos) + height * Math.abs(sin));
            newHeight = (int) (width * Math.abs(sin) + height * Math.abs(cos));
        }

        int[] ret = new int[newWidth * newHeight];

        int newCentral_x = newWidth / 2;
        int newCentral_y = newHeight / 2;

        int central_x = width / 2;
        int central_y = height / 2;

        int i = 0;
        for (int y = -newCentral_y; y < newCentral_y; y++) {
            for (int x = -newCentral_x; x < newCentral_x; x++) {
                int original_x = (int) (x * cos - y * sin);
                int original_y = (int) (x * sin + y * cos);
                original_x += central_x;
                original_y += central_y;
                int cur_x = x + newCentral_x;
                int cur_y = y + newCentral_y;

                if (original_x < 0 || original_x >= width || original_y < 0 || original_y >= height) {
                    ret[cur_x + cur_y * newWidth] = 0xFF000000;
                } else {
                    ret[cur_x + cur_y * newWidth] = src[original_x + original_y * width];
                }
            }
        }
        return new PicInfo(ret, newWidth, newHeight, pic.getPicType());
    }

    public static void bilinearInterpolationRotate(PicInfo pic, int degree) {

    }
}
