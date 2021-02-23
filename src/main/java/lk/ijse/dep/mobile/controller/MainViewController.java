package lk.ijse.dep.mobile.controller;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.StorageService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class MainViewController {
    public TextArea txtInput;
    public TextArea txtOutput;

    public void initialize(){
        
    }

    public void btnSaveFile_OnAction(ActionEvent actionEvent) {
        Optional<StorageService> optStorageService = Services.get(StorageService.class);
        if (optStorageService.isPresent()) {
            StorageService storageService = optStorageService.get();
            if (storageService.isExternalStorageWritable()) {
                Optional<File> publicStorage = storageService.getPublicStorage("/dep-fx");

                publicStorage.get().mkdir();
                File file = new File(publicStorage.get(), "native.db");
                try (FileWriter fw = new FileWriter(file);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(txtInput.getText());
                } catch (IOException exception) {
                    new Alert(Alert.AlertType.ERROR, "Failed to write").show();
                    exception.printStackTrace();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "App doesn't have permission to write").show();
            }
        }
    }

    public void btnRead_OnAction(ActionEvent actionEvent) {
        Optional<StorageService> optStorageService = Services.get(StorageService.class);
        if (optStorageService.isPresent()) {
            StorageService storageService = optStorageService.get();
            if (storageService.isExternalStorageReadable()) {
                Optional<File> publicStorage = storageService.getPublicStorage("/dep-fx/native.db");

                if (publicStorage.get().exists()) {
                    try (FileReader fileReader = new FileReader(publicStorage.get());
                        BufferedReader br = new BufferedReader(fileReader)){

                        String content = "";
                        String line = null;

                        while ((line = br.readLine()) != null){
                            content += line + '\n';
                        }
                        txtOutput.setText(content);

                    }
                     catch (IOException exception) {
                        new Alert(Alert.AlertType.ERROR, "Failed to read").show();
                        exception.printStackTrace();
                    }

                } else {
                    new Alert(Alert.AlertType.ERROR, "There is no file to read yet.").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "App doesn't have permission to read").show();
            }
        }
    }
}
