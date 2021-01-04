package com.example.app.slice;

import com.example.app.ResourceTable;
import com.soft.stone.compress.Compressor;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImagePacker;
import ohos.media.image.PixelMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "APP");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button button = (Button) findComponentById(ResourceTable.Id_button);
        if (button != null) {
            // 为按钮设置点击回调
            button.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    Image image = (Image) findComponentById(ResourceTable.Id_image1);
                    PixelMap pixelMap = image.getPixelMap();
                    ImagePacker imagePacker = ImagePacker.create();
                    FileOutputStream outputStream = null;
                    File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "tmp.png");
                    try {
                        // 模拟器的照相机照出来的图片有问题，自己临时写一张图片
                        outputStream = new FileOutputStream(file);
                        ImagePacker.PackingOptions packingOptions = new ImagePacker.PackingOptions();
                        packingOptions.quality = 100;
                        boolean result = imagePacker.initializePacking(outputStream, packingOptions);
                        result = imagePacker.addImage(pixelMap);
                        long dataSize = imagePacker.finalizePacking();
                        HiLog.error(LOG_LABEL, "old size..." + file.length() +  " ...b");
                        /*
                         * 默认压缩
                         * File newFile = Compressor.defaultCompress(file);
                         */
                        // 自定义压缩

                        File newFile = Compressor.customCompress(getContext(), file, 1000, 500, 60);
                        Text text = (Text) findComponentById(ResourceTable.Id_text);
                        text.setText(newFile.length() + " b");
                        HiLog.error(LOG_LABEL, "new size..." + newFile.length() +  " ...b");
                        PixelMap newPixelMap = Compressor.decode(newFile);
                        image.setPixelMap(newPixelMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}