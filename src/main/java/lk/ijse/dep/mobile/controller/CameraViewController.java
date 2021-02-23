package lk.ijse.dep.mobile.controller;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.PicturesService;
import com.gluonhq.charm.down.plugins.StorageService;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.Optional;

public class CameraViewController {
    public ImageView img;

    public void btnOpenCamera_OnAction(ActionEvent actionEvent) {
        Optional<PicturesService> optPicturesService = Services.get(PicturesService.class);
        Optional<StorageService> optStorageService = Services.get(StorageService.class);
        if (optPicturesService.isPresent()) {
            PicturesService picturesService = optPicturesService.get();
            Optional<Image> optImage = picturesService.takePhoto(true);
            if (optImage.isPresent()) {
                img.setImage(optImage.get());

                File srcFile = picturesService.getImageFile().get();
                File destFile = optStorageService.get().getPublicStorage("/dep-fx" + File.separator + srcFile.getName()).get();

                try (FileInputStream fis = new FileInputStream(srcFile);
                     BufferedInputStream bis = new BufferedInputStream(fis);
                     FileOutputStream fos = new FileOutputStream(destFile);
                     BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                    byte[] bytes = new byte[fis.available()];
                    bis.read(bytes);
                    bos.write(bytes);
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Failed to write the image").show();
                    e.printStackTrace();
                }

            } else {
                img.setImage(null);
            }
        }

    }
}
