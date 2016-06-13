package processor;

import android.util.Log;

import superbiayang.imageprocessing.PicInfo;

/**
 * Created by SuperbiaYang on 2016/6/11.
 */
public class Geometry {
    public static PicInfo nearestNeighborRotate(PicInfo pic, int degree) {
        int width = pic.getWidth();
        int height = pic.getHeight();
        int[] src = pic.getPixels();
        int newWidth = 0;
        int newHeight = 0;
        int newCenter_x = 0;
        int newCenter_y = 0;

        int n = (int) (degree / 360);
        degree = degree - n * 360;
        if (degree < 0) {
            degree += 360;
        }
        double ang = Math.toRadians(degree);

        if (degree == 180 || degree == 0 || degree == 360) {
            newWidth = width;
            newHeight = height;
        } else if (degree == 90 || degree == 270) {
            newWidth = height;
            newHeight = width;
        } else {
            newWidth = (int) (width * Math.abs(Math.cos(ang)) + height * Math.abs(Math.sin(ang)));
            newHeight = (int) (width * Math.abs(Math.sin(ang)) + height * Math.abs(Math.cos(ang)));
        }

        newCenter_x = newWidth / 2;
        newCenter_y = newHeight / 2;

        int[] ret = new int[newWidth * newHeight];

        Log.i("w,h", newWidth + "," + newHeight + "," + width + "," + height + "," + src.length + "," + ret.length);
        for (int y = -newHeight / 2; y < newHeight / 2; y++) {
            for (int x = -newWidth / 2; x < newWidth / 2; x++) {
                int original_x = (int) (x * Math.cos(ang) - y * Math.sin(ang));
                int original_y = (int) (x * Math.sin(ang) + y * Math.cos(ang));
                original_x += width / 2;
                original_y += height / 2;
                int cur_x = x + newWidth / 2;
                int cur_y = y + newHeight / 2;
                //Log.i("x y", cur_x + "," + cur_y + "," + original_x + "," + original_y);

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
