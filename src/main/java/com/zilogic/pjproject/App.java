package com.zilogic.pjproject;

import com.zilogic.pjproject.utils.UserData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.EventType;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    void close() {
        new MyApp().deinit();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("MainStage"), 640, 480);
        stage.setScene(scene);
        stage.show();

        stage.addEventHandler(EventType.ROOT, (e) -> {
            try {
                if (e.getEventType().getName().equals("WINDOW_HIDING")) {
                    UserData user = new UserData("guna", "guna@gmail.com");
                    stage.setUserData(user);
                    close();
                    Platform.runLater(() -> {
                        MainStageController.incomingThread.stop();
                        MainStageController.closingUI.stop();
                    });
                }
            } catch (Exception ex) {
                System.out.println("App Exception");
            }
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }

}
