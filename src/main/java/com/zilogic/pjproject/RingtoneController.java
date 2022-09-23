package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author guna
 */
public class RingtoneController implements Initializable {

    @FXML
    private JFXListView<String> ringtones;
    @FXML
    private JFXButton add;
    ObservableList<String> ring_obs = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadallRingtone();
        } catch (Exception ex) {
        }
    }

    private void loadallRingtone() throws Exception {
        ring_obs.add(file.getAbsolutePath());
        ringtones.setItems(ring_obs);
        ringtones.setOnMouseClicked(e -> {
            System.out.println("Event : " + e.getPickResult());
            ringtones.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            try {
//                IncomingCallController.player.createPlayer(ringtones.getSelectionModel().getSelectedItem(), pjmedia_file_player_option.PJMEDIA_FILE_NO_LOOP);
            } catch (Exception ex) {
            }
        });
    }
    FileChooser ringtoneSelection = new FileChooser();
    private File file;

    @FXML
    private void addRingTone(ActionEvent event) throws Exception {
        ringtoneSelection.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("wav  formatted file", ".wav"));
        ringtoneSelection.setTitle("Ringtone");
        Node node = (Node) event.getSource();
        Window window = new Window() {
        };
        getFile(window);
    }

    private File getFile(Window window) {
        file = ringtoneSelection.showOpenDialog(window);
        return file.getAbsoluteFile();
    }

}
