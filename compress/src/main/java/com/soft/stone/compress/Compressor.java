package com.soft.stone.compress;

import ohos.media.image.ImagePacker;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Compressor {

    private Compressor() {}

    /**
     * 将图片转换成像素
     *
     * @param file 文件目录
     * @return 像素
     */
    public static PixelMap decode(File file) {
        ImageSource imageSource = ImageSource.create(file, null);
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.sampleSize = ImageSource.DecodingOptions.DEFAULT_SAMPLE_SIZE;
        return imageSource.createPixelmap(decodingOpts);
    }

    /**
     * 默认压缩
     *
     * @param file 临时文件
     * @return 临时文件
     * @throws IOException IO异常
     */
    public static File defaultCompress(File file) throws IOException {
        PixelMap pixelMap = decode(file, 612,816);
        ImagePacker imagePacker = ImagePacker.create();
        ImagePacker.PackingOptions options = new ImagePacker.PackingOptions();
        options.quality = 80;
        imagePacker.initializePacking(new FileOutputStream(file), options);
        imagePacker.addImage(pixelMap);
        imagePacker.finalizePacking();
        return file;
    }

    /**
     * 自定义压缩
     *
     * @param file 文件目录
     * @param width 需要显示的宽度
     * @param height 需要显示的高度
     * @param quality 图片压缩质量
     * @return 文件目录
     * @throws IOException IO异常
     */
    public static File customCompress(File file, int width, int height, int quality) throws IOException {
        PixelMap pixelMap = decode(file, width,height);
        ImagePacker imagePacker = ImagePacker.create();
        ImagePacker.PackingOptions options = new ImagePacker.PackingOptions();
        options.quality = quality;
        imagePacker.initializePacking(new FileOutputStream(file), options);
        imagePacker.addImage(pixelMap);
        imagePacker.finalizePacking();
        return file;
    }

    private static PixelMap decode(File file, int width, int height) {
        ImageSource imageSource = ImageSource.create(file, null);
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(width, height);
        return imageSource.createPixelmap(decodingOpts);
    }
}
