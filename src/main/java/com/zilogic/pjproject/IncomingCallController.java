package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AudioMediaPlayer;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjmedia_file_player_option;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;

public class IncomingCallController implements Initializable {

    @FXML
    FadeTransition ft;
    @FXML
    private JFXButton answer;
    @FXML
    private AnchorPane pane;
    @FXML
    private JFXButton hangUp;

    @FXML
    static Button accept = new Button();
    @FXML
    static Button up = new Button();
    @FXML
    static Button hold = new Button();
    @FXML
    static Button merge = new Button();
    @FXML
    static Button videoCall = new Button();

    MyCall call = MyAccount.currentCall;

    static AudioMediaPlayer player;
    static AudioMedia ring_pay_back;

    @FXML
    void answer(ActionEvent event) throws Exception {
        CallOpParam prm = new CallOpParam(true);
        Platform.runLater(() -> {
            try {
                IncomingCallController.player.stopTransmit(IncomingCallController.ring_pay_back);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        call.answer(prm);
        System.out.println("ANSWER");
    }

    @FXML
    void hangUp(ActionEvent event) throws Exception {
        System.out.println("HANGUP");
        if (call != null) {
            Platform.runLater(() -> {
                try {
                    IncomingCallController.player.stopTransmit(IncomingCallController.ring_pay_back);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
//            MainStageController.task.run();
            CallOpParam prm = new CallOpParam(true);
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            call.hangup(prm);
            MainStageController.incoming_stage.close();
        }
    }

    @FXML
    void displayUI() {
        pane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff5d48, #ff5d48)");
        ft = new FadeTransition(Duration.millis(1000), pane);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }

    @FXML
    void muteCall(ActionEvent event) throws Exception {
        if (call != null) {
            System.out.println("Current Call Instance :" + call.getInfo().toString());
            call.audioMedia.adjustRxLevel(0);
            MyApp.ep.libHandleEvents(10);
            System.out.println("current call mute : " + call.audioMedia.getTxLevel());
        }
    }

    @FXML
    void unMute_Call() throws Exception {
        System.out.println("unmute call");
        if (call != null) {
            CallOpParam prm = new CallOpParam(true);
            call.audioMedia.adjustRxLevel((float) 1);
            MyApp.ep.libHandleEvents(10);
        }
    }

    @FXML
    void hold() throws Exception {
        System.out.println(" Hold the Call");
        if (call != null) {
            CallOpParam callOp = new CallOpParam(true);
            callOp.setStatusCode(pjsua_call_flag.PJSUA_CALL_UPDATE_CONTACT);
            callOp.setReason("hold");
            call.setHold(callOp);
            MyApp.ep.libHandleEvents(10);
        }
    }

    //method is used for unHold the call
    @FXML
    void unHold() throws Exception {
        if (call != null) {
            System.out.println("call is Unloaded");
            CallOpParam callOp = new CallOpParam(true);
            callOp.getOpt().setFlag(1);
            call.reinvite(callOp);
            MyApp.ep.libHandleEvents(10);
        }
    }

    /*
    @param is used to create the ringtone for OncomingCall
     */
    static void incoming_Call_Ringtone() throws Exception {
        ring_pay_back = MyApp.ep.audDevManager().getPlaybackDevMedia();
        player = new AudioMediaPlayer();
        player.createPlayer("/home/user/NetBeansProjects/PjProject/src/main/resources/com/zilogic/pjproject/Tum-Tum-MassTamilan.fm.wav", pjmedia_file_player_option.PJMEDIA_FILE_NO_LOOP);
        player.startTransmit(ring_pay_back);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(new Task() {
            @Override
            protected Void call() throws Exception {
                displayUI();
                return null;
            }
        }).start();
    }
}
