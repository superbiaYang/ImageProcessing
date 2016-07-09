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

        for (int y = -newHeight / 2; y < newHeight / 2; y++) {
            for (int x = -newWidth / 2; x < newWidth / 2; x++) {
                int original_x = (int) (x * Math.cos(ang) - y * Math.sin(ang));
                int original_y = (int) (x * Math.sin(ang) + y * Math.cos(ang));
                original_x += width / 2;
                original_y += height / 2;
                int cur_x = x + newWidth / 2;
                int cur_y = y + newHeight / 2;

                if (original_x < 0 || original_x >= width || original_y < 0 || original_y >= height) {
                    ret[cur_x + cur_y * newWidth] = 0xFF000000;
                } else {
                    ret[cur_x + cur_y * newWidth] = src[original_x + original_y * width];
                }
            }
        }
        return new PicInfo(ret, newWidth, newHeight, pic.getPicType());
    }

    public static PicInfo bilinearInterpolationRotate(PicInfo pic, int degree) {
        int width = pic.getWidth();
        int height = pic.getHeight();
        int[] src = pic.getPixels();
        int newWidth = 0;
        int newHeight = 0;
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

        int[] ret = new int[newWidth * newHeight];
        for (int y = -newHeight / 2; y < newHeight / 2; y++) {
            for (int x = -newWidth / 2; x < newWidth / 2; x++) {
                double xxxx = x * Math.cos(ang) - y * Math.sin(ang);
                double yyyy = x * Math.sin(ang) + y * Math.cos(ang);
                xxxx += width / 2;
                yyyy += height / 2;
                int xxx = x + newWidth / 2;
                int yyy = y + newHeight / 2;
                if (xxxx < 0 || xxxx >= width || yyyy < 0 || yyyy >= height) {
                    ret[xxx + yyy * newWidth] = 0xFF000000;
                } else {
                    int x0 = (int) Math.floor(xxxx);
                    int x1 = Math.min(x0 + 1, width - 1);
                    int y0 = (int) Math.floor(yyyy);
                    int y1 = Math.min(y0 + 1, height - 1);
                    int color00 = src[x0 + y0 * width];
                    int color10 = src[x1 + y0 * width];
                    int color01 = src[x0 + y1 * width];
                    int color11 = src[x1 + y1 * width];
                    int alpha = (int) (((color00 & 0xFF000000) >>> 24) * (xxxx - x0) * (yyyy - y0) +
                            ((color01 & 0xFF000000) >>> 24) * (xxxx - x0) * (y1 - yyyy) +
                            ((color10 & 0xFF000000) >>> 24) * (x1 - xxxx) * (yyyy - y0) +
                            ((color11 & 0xFF000000) >>> 24) * (x1 - xxxx) * (y1 - yyyy));
                    int red = (int) (((color00 & 0x00FF0000) >>> 16) * (xxxx - x0) * (yyyy - y0) +
                            ((color01 & 0x00FF0000) >>> 16) * (xxxx - x0) * (y1 - yyyy) +
                            ((color10 & 0x00FF0000) >>> 16) * (x1 - xxxx) * (yyyy - y0) +
                            ((color11 & 0x00FF0000) >>> 16) * (x1 - xxxx) * (y1 - yyyy));
                    int green = (int) (((color00 & 0x0000FF00) >>> 8) * (xxxx - x0) * (yyyy - y0) +
                            ((color01 & 0x0000FF00) >>> 8) * (xxxx - x0) * (y1 - yyyy) +
                            ((color10 & 0x0000FF00) >>> 8) * (x1 - xxxx) * (yyyy - y0) +
                            ((color11 & 0x0000FF00) >>> 8) * (x1 - xxxx) * (y1 - yyyy));
                    int blue = (int) ((color00 & 0x000000FF) * (xxxx - x0) * (yyyy - y0) +
                            (color01 & 0x000000FF) * (xxxx - x0) * (y1 - yyyy) +
                            (color10 & 0x000000FF) * (x1 - xxxx) * (yyyy - y0) +
                            (color11 & 0x000000FF) * (x1 - xxxx) * (y1 - yyyy));

                    ret[xxx + yyy * newWidth] = (alpha << 24) | (red << 16) | (green << 8) | blue;
                }
            }
        }
        return new PicInfo(ret, newWidth, newHeight, pic.getPicType());
    }

    public static PicInfo nearestNeighborResize(PicInfo pic, int newWidth, int newHeight) {
        int width = pic.getWidth();
        int height = pic.getHeight();
        int[] pixels = pic.getPixels();
        int[] ret = new int[newWidth * newHeight];
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int oldx = Math.min(Math.round((float) (x) * width / newWidth), width - 1);
                int oldy = Math.min(Math.round((float) (y) * height / newHeight), height - 1);
                ret[x + y * newWidth] = pixels[oldx + oldy * width];
            }
        }
        return new PicInfo(ret, newWidth, newHeight, pic.getPicType());
    }

    public static PicInfo bilinearInterpolationResize(PicInfo pic, int newWidth, int newHeight) {
        int width = pic.getWidth();
        int height = pic.getHeight();
        int[] src = pic.getPixels();
        int[] ret = new int[newWidth * newHeight];
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                float oldx = (float) (x) * width / newWidth;
                float oldy = (float) (y) * height / newHeight;
                int x0 = (int) Math.floor(oldx);
                int x1 = Math.min(x0 + 1, width - 1);
                int y0 = (int) Math.floor(oldy);
                int y1 = Math.min(y0 + 1, height - 1);
                int color00 = src[x0 + y0 * width];
                int color10 = src[x1 + y0 * width];
                int color01 = src[x0 + y1 * width];
                int color11 = src[x1 + y1 * width];
                int alpha = (int) (((color00 & 0xFF000000) >>> 24) * (oldx - x0) * (oldy - y0) +
                        ((color01 & 0xFF000000) >>> 24) * (oldx - x0) * (y1 - oldy) +
                        ((color10 & 0xFF000000) >>> 24) * (x1 - oldx) * (oldy - y0) +
                        ((color11 & 0xFF000000) >>> 24) * (x1 - oldx) * (y1 - oldy));
                int red = (int) (((color00 & 0x00FF0000) >>> 16) * (oldx - x0) * (oldy - y0) +
                        ((color01 & 0x00FF0000) >>> 16) * (oldx - x0) * (y1 - oldy) +
                        ((color10 & 0x00FF0000) >>> 16) * (x1 - oldx) * (oldy - y0) +
                        ((color11 & 0x00FF0000) >>> 16) * (x1 - oldx) * (y1 - oldy));
                int green = (int) (((color00 & 0x0000FF00) >>> 8) * (oldx - x0) * (oldy - y0) +
                        ((color01 & 0x0000FF00) >>> 8) * (oldx - x0) * (y1 - oldy) +
                        ((color10 & 0x0000FF00) >>> 8) * (x1 - oldx) * (oldy - y0) +
                        ((color11 & 0x0000FF00) >>> 8) * (x1 - oldx) * (y1 - oldy));
                int blue = (int) ((color00 & 0x000000FF) * (oldx - x0) * (oldy - y0) +
                        (color01 & 0x000000FF) * (oldx - x0) * (y1 - oldy) +
                        (color10 & 0x000000FF) * (x1 - oldx) * (oldy - y0) +
                        (color11 & 0x000000FF) * (x1 - oldx) * (y1 - oldy));

                ret[x + y * newWidth] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        return new PicInfo(ret, newWidth, newHeight, pic.getPicType());
    }
}
