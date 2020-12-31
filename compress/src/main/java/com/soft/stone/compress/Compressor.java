package com.soft.stone.compress;

import ohos.agp.components.Image;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.PixelMap.InitializationOptions;
import ohos.media.image.common.ImageInfo;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import java.io.File;

public class Compressor {

    private Compressor() {}

    /**
     * 传入已经指向图片的组件，可以将图片尺寸缩小为原来的一半
     *
     * @param image 界面的图片组件
     * @return 位图
     */
    public static PixelMap halfTheSize(Image image) {
        PixelMap pixelMap = image.getPixelMap();
        ImageInfo imageInfo = pixelMap.getImageInfo();
        Size size = imageInfo.size;
        InitializationOptions initOptions = new PixelMap.InitializationOptions();
        initOptions.size = new Size(size.width / 2, size.height / 2);
        return PixelMap.create(pixelMap, initOptions);
    }

    /**
     * 传入已经指向图片的组件，传入像素格式，会按照传入的像素格式进行显示，像素格式仅支持ARGB_8888/RGB_565/UNKNOWN
     *
     * @param image 界面的图片组件
     * @param pixelFormat 像素格式
     * @return
     */
    public static PixelMap compress(Image image, PixelFormat pixelFormat) {
        PixelMap pixelMap = image.getPixelMap();
        InitializationOptions initOptions = new PixelMap.InitializationOptions();
        initOptions.pixelFormat = pixelFormat;
        return PixelMap.create(pixelMap, initOptions);
    }

    /**
     * 此功能还未验证
     *
     * 传入图片，传入像素格式，会按照传入的像素格式进行显示，像素格式仅支持ARGB_8888/RGB_565/UNKNOWN
     *
     * @param filePath 文件路径
     * @param pixelFormat 像素格式
     * @return PixelMap
     */
    public static PixelMap compress(File filePath, PixelFormat pixelFormat) {
        ImageSource imageSource = ImageSource.create(filePath, null);
        return defaultCompress(imageSource, pixelFormat);
    }

    private static PixelMap defaultCompress(ImageSource imageSource, PixelFormat pixelFormat) {
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredPixelFormat = pixelFormat;
        return imageSource.createPixelmap(decodingOptions);
    }
}
