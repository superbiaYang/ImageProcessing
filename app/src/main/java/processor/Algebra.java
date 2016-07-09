package processor;

import superbiayang.imageprocessing.PicInfo;

/**
 * Created by SuperbiaYang on 2016/7/9.
 */
public class Algebra {
    public static PicInfo add(PicInfo pic_a, PicInfo pic_b) {
        int[] pixels_a = pic_a.getPixels();
        int[] pixels_b = pic_b.getPixels();
        int[] ret = new int[pic_a.getPixelsNum()];
        for (int x = 0; x < pic_a.getWidth(); x++) {
            for (int y = 0; y < pic_a.getHeight(); y++) {
                int color_a = pixels_a[x + y * pic_a.getWidth()];
                int color_b = 0;
                if (x < pic_b.getWidth() && y < pic_b.getHeight()) {
                    color_b = pixels_b[x + y * pic_b.getWidth()];
                }
                int alpha = (((color_a & 0xFF000000) >>> 24) + ((color_b & 0xFF000000) >>> 24)) / 2;
                int red = (((color_a & 0x00FF0000) >>> 16) + ((color_b & 0x00FF0000) >>> 16)) / 2;
                int green = (((color_a & 0x0000FF00) >>> 8) + ((color_b & 0x0000FF00) >>> 8)) / 2;
                int blue = ((color_a & 0x000000FF) + (color_b & 0x000000FF)) / 2;

                ret[x + y * pic_a.getWidth()] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        PicInfo.PicType type = PicInfo.PicType.BINARY;
        if (pic_a.getPicType() == PicInfo.PicType.COLOR ||
                pic_b.getPicType() == PicInfo.PicType.COLOR) {
            type = PicInfo.PicType.COLOR;
        } else if (pic_a.getPicType() == PicInfo.PicType.GRAY ||
                pic_b.getPicType() == PicInfo.PicType.GRAY) {
            type = PicInfo.PicType.GRAY;
        }
        return new PicInfo(ret, pic_a.getWidth(), pic_a.getHeight(), type);
    }

    public static PicInfo sub(PicInfo pic_a, PicInfo pic_b) {
        int[] pixels_a = pic_a.getPixels();
        int[] pixels_b = pic_b.getPixels();
        int[] ret = new int[pic_a.getPixelsNum()];
        for (int x = 0; x < pic_a.getWidth(); x++) {
            for (int y = 0; y < pic_a.getHeight(); y++) {
                int color_a = pixels_a[x + y * pic_a.getWidth()];
                int color_b = 0;
                if (x < pic_b.getWidth() && y < pic_b.getHeight()) {
                    color_b = pixels_b[x + y * pic_b.getWidth()];
                }
                int alpha = ((color_a & 0xFF000000) >>> 24) - ((color_b & 0xFF000000) >>> 24);
                int red = ((color_a & 0x00FF0000) >>> 16) - ((color_b & 0x00FF0000) >>> 16);
                int green = ((color_a & 0x0000FF00) >>> 8) - ((color_b & 0x0000FF00) >>> 8);
                int blue = (color_a & 0x000000FF) - (color_b & 0x000000FF);
                alpha = Math.max(alpha, 0);
                red = Math.max(red, 0);
                green = Math.max(green, 0);
                blue = Math.max(blue, 0);

                ret[x + y * pic_a.getWidth()] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        PicInfo.PicType type = PicInfo.PicType.BINARY;
        if (pic_a.getPicType() == PicInfo.PicType.COLOR ||
                pic_b.getPicType() == PicInfo.PicType.COLOR) {
            type = PicInfo.PicType.COLOR;
        } else if (pic_a.getPicType() == PicInfo.PicType.GRAY ||
                pic_b.getPicType() == PicInfo.PicType.GRAY) {
            type = PicInfo.PicType.GRAY;
        }
        return new PicInfo(ret, pic_a.getWidth(), pic_a.getHeight(), type);
    }

    public static PicInfo multi(PicInfo pic_a, PicInfo pic_b) {
        int[] pixels_a = pic_a.getPixels();
        int[] pixels_b = pic_b.getPixels();
        int[] ret = new int[pic_a.getPixelsNum()];
        for (int x = 0; x < pic_a.getWidth(); x++) {
            for (int y = 0; y < pic_a.getHeight(); y++) {
                int color_a = pixels_a[x + y * pic_a.getWidth()];
                int color_b = 0;
                if (x < pic_b.getWidth() && y < pic_b.getHeight()) {
                    color_b = pixels_b[x + y * pic_b.getWidth()];
                }
                int alpha = ((color_a & 0xFF000000) >>> 24) * ((color_b & 0xFF000000) >>> 24);
                int red = ((color_a & 0x00FF0000) >>> 16) * ((color_b & 0x00FF0000) >>> 16);
                int green = ((color_a & 0x0000FF00) >>> 8) * ((color_b & 0x0000FF00) >>> 8);
                int blue = (color_a & 0x000000FF) * (color_b & 0x000000FF);
                alpha = Math.max(Math.min(alpha, 0xFF), 0);
                red = Math.max(Math.min(red, 0xFF), 0);
                green = Math.max(Math.min(green, 0xFF), 0);
                blue = Math.max(Math.min(blue, 0xFF), 0);

                ret[x + y * pic_a.getWidth()] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
        }
        PicInfo.PicType type = PicInfo.PicType.BINARY;
        if (pic_a.getPicType() == PicInfo.PicType.COLOR ||
                pic_b.getPicType() == PicInfo.PicType.COLOR) {
            type = PicInfo.PicType.COLOR;
        } else if (pic_a.getPicType() == PicInfo.PicType.GRAY ||
                pic_b.getPicType() == PicInfo.PicType.GRAY) {
            type = PicInfo.PicType.GRAY;
        }
        return new PicInfo(ret, pic_a.getWidth(), pic_a.getHeight(), type);
    }
}
