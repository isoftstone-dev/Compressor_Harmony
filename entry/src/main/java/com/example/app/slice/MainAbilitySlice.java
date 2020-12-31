package com.example.app.slice;

import com.example.app.ResourceTable;
import com.soft.stone.compress.Compressor;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;

public class MainAbilitySlice extends AbilitySlice {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00202, "APP");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Button button = (Button) findComponentById(ResourceTable.Id_button);
        HiLog.info(label, "... " + this.getCacheDir());
        if (button != null) {
            // 为按钮设置点击回调
            button.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    Image image = (Image) findComponentById(ResourceTable.Id_image1);
                    //PixelMap newPixelMap = Compressor.halfTheSize(image);
                    PixelMap newPixelMap = Compressor.compress(image, PixelFormat.RGB_565);
                    image.setPixelMap(newPixelMap);
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
