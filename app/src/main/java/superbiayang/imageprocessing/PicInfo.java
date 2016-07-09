package superbiayang.imageprocessing;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Created by SuperbiaYang on 2016/6/11.
 */

public class PicInfo {
    private int[] pixels;
    private int width;
    private int height;
    private PicType picType;

    public PicInfo() {
        width = 0;
        height = 0;
        pixels = null;
        picType = PicType.NONE;
    }

    public PicInfo(int[] pixels, int width, int height, PicType picType) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.picType = picType;
    }

    public PicInfo(@NonNull Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int[width * height];
        picType = PicType.COLOR;
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    public PicType getPicType() {
        return picType;
    }

    public void setPicType(PicType picType) {
        this.picType = picType;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void copy(PicInfo info) {
        this.pixels = info.getPixels().clone();
        this.width = info.getWidth();
        this.height = info.getHeight();
        this.picType = info.getPicType();
    }

    public int getPixelsNum() {
        return width * height;
    }

    public enum PicType {
        NONE,
        COLOR,
        GRAY,
        BINARY
    }
}
