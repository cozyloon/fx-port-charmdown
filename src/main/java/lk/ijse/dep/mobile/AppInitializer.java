package lk.ijse.dep.mobile;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Rectangle2D vb = Screen.getPrimary().getVisualBounds();
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/CameraView.fxml"));
        root.setPrefWidth(vb.getWidth());
        root.setPrefHeight(vb.getHeight());
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE){
                    Services.get(LifecycleService.class).get().shutdown();
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
