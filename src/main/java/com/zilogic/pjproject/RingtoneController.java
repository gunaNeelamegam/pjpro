package com.zilogic.pjproject;

import com.jfoenix.controls.JFXListView;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import org.pjsip.pjsua2.pjmedia_file_player_option;

/**
 *
 * @author guna
 */
public class RingtoneController implements Initializable {

    @FXML
    private JFXListView<String> ringtonels;

    ObservableList<String> ring_obs = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadallRingtone();
    }

    private void loadallRingtone() {
        ring_obs.add("/home/user/NetBeansProjects/PjProject/src/main/resources/com/zilogic/pjproject/Tum-Tum-MassTamilan.fm.wav");
        ringtonels.setItems(ring_obs);
        ringtonels.setOnMouseClicked(e -> {
            System.out.println("Event : " + e.getPickResult());
            ringtonels.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            try {
                IncomingCallController.player.createPlayer(ringtonels.getSelectionModel().getSelectedItem(), pjmedia_file_player_option.PJMEDIA_FILE_NO_LOOP);
            } catch (Exception ex) {
            }
        });
    }

    @FXML
    private void addRingTone() throws Exception {
        FileChooser ringtoneSelection = new FileChooser();
        ringtoneSelection.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".wav", ring_obs));
        ringtoneSelection.setInitialDirectory(new File("~/Downloads"));

    }

}
