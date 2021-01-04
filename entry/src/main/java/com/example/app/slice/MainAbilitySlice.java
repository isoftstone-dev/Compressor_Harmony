package com.example.app.slice;

import com.example.app.ResourceTable;
import com.soft.stone.compress.Compressor;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImagePacker;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.io.*;
import java.util.UUID;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "APP");

    private String tmpName = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        // 请求文件的读取权限
        String[] permissions = {"ohos.permission.READ_USER_STORAGE"};
        requestPermissionsFromUser(permissions, 0);

        // 获取压缩按钮并绑定事件
        Button button = (Button) findComponentById(ResourceTable.Id_button);
        if (button != null) {
            // 为按钮设置点击回调
            button.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    try {
                        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + tmpName);
                        HiLog.error(LOG_LABEL, "old size..." + file.length() +  " ...b");

                        // 默认压缩
                        // File newFile = Compressor.defaultCompress(file);

                        // 自定义压缩
                        File newFile = Compressor.customCompress(getContext(), file, 500, 1000, 60);
                        Text text = (Text) findComponentById(ResourceTable.Id_text);
                        text.setText("size: " + newFile.length() + " b");
                        HiLog.error(LOG_LABEL, "new size..." + newFile.length() +  " ...b");
                        PixelMap newPixelMap = Compressor.decode(newFile);
                        Image image = (Image) findComponentById(ResourceTable.Id_image1);
                        image.setPixelMap(newPixelMap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        // 获取选择图片按钮并绑定事件
        Button chooseButton = (Button) findComponentById(ResourceTable.Id_choose_button);
        if (chooseButton != null) {
            // 为按钮设置点击回调
            chooseButton.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    DataAbilityHelper helper = DataAbilityHelper.creator(getContext());
                    try {
                        ResultSet resultSet = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, null, null);
                        while (resultSet != null && resultSet.goToNextRow()) {
                            // 互殴媒体库的图片
                            int id = resultSet.getInt(resultSet.getColumnIndexForName(AVStorage.Images.Media.ID));
                            HiLog.error(LOG_LABEL, "id:..." + id +  " ...");
                            Uri uri = Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, "" + id);
                            // 根据图片的uri打开文件并保存到临时目录中
                            FileDescriptor fileDescriptor = helper.openFile(uri, "r");
                            ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
                            decodingOpts.sampleSize = ImageSource.DecodingOptions.DEFAULT_SAMPLE_SIZE;
                            ImageSource imageSource = ImageSource.create(fileDescriptor, null);
                            PixelMap pixelMap = imageSource.createThumbnailPixelmap(decodingOpts, true);
                            ImagePacker imagePacker = ImagePacker.create();
                            tmpName = UUID.randomUUID().toString();
                            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + tmpName);
                            FileOutputStream outputStream = new FileOutputStream(file);
                            ImagePacker.PackingOptions packingOptions = new ImagePacker.PackingOptions();
                            packingOptions.quality = 100;
                            boolean result = imagePacker.initializePacking(outputStream, packingOptions);
                            result = imagePacker.addImage(pixelMap);
                            long dataSize = imagePacker.finalizePacking();
                            // 显示图片和图片大小
                            Text text = (Text) findComponentById(ResourceTable.Id_text);
                            text.setText("size: " + file.length() + " b");
                            Image image = (Image) findComponentById(ResourceTable.Id_image1);
                            image.setPixelMap(pixelMap);
                        }
                    } catch (DataAbilityRemoteException | FileNotFoundException e) {
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