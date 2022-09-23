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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;

public class OutGoingCallController implements Initializable {

    MyCall currentCall = null;
    private MyObserver observer = new MyObserver();
    Task callStatustask;
    Thread callStatusThread = new Thread(this.callStatustask);
    @FXML
    Label callStatus;

    public Label getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Label callStatus) {
        this.callStatus = callStatus;
    }

    public MyCall getCurrentCall() {
        return currentCall;
    }

    @FXML
    private Circle circle;

    @FXML
    private JFXButton hangUp;

//    @FXML
//    private JFXButton mute;

    @FXML
    private JFXButton unHoldCall;

    @FXML
    private JFXButton unMute;
    @FXML
    private JFXButton holdCall;

    @FXML
    private TextField text;

    @FXML
    FadeTransition ft;
    @FXML
    private Label messageStatus;

    @FXML
    private TextField textmes;

    @FXML
    private JFXButton send;
    @FXML
    private Pane c;
    int i = 0;
    @FXML
    private JFXButton call;

    private void updateing_callStatus() throws InterruptedException {
        Thread updateStatus = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    var updater = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MyApp.ep.libHandleEvents(10L);
                                System.out.println("Call status" + currentCall.getInfo().getStateText());
                                callStatus.setText(currentCall.getInfo().getStateText() + " " + currentCall.getInfo().getConnectDuration().getSec());
                            } catch (Exception ex) {

                            }
                        }
                    };
                    while (currentCall.getInfo().getState() != 6) {
                        Thread.sleep(1000);
                        Platform.runLater(updater);
                    }
                } catch (Exception e) {
                }
            }
        });
        updateStatus.start();
    }

    public Thread getCallStatusThread() {
        return callStatusThread;
    }

    void call() {
        try {
            c.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%,#b5086b, #FFFFFF)");
            ft = new FadeTransition(Duration.millis(2000), c);
            ft.setFromValue(1.0);
            ft.setToValue(0.6);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        } catch (Exception e) {
        }
    }


    @FXML
    void hangUpCall() throws Exception {
        if (currentCall == null) {
            hangUp.setDisable(true);
        }
        if (currentCall != null) {
            hangUp.setDisable(false);
            hangUp.setFocusTraversable(true);
            System.out.println(" Call : " + call.toString());
            CallOpParam callParam = new CallOpParam();
            callParam.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            currentCall.hangup(callParam);
            System.out.println("hangup");
            MyApp.ep.libHandleEvents(10L);
            MainStageController.outGoingCallStage.close();
        }
    }

    @FXML
    void muteCall(ActionEvent event) throws Exception {
        if (currentCall != null) {
            currentCall.audioMedia.adjustRxLevel(0);
            MyApp.ep.libHandleEvents(10L);
            System.out.println("current call mute : " + currentCall.audioMedia.getTxLevel());
        }
    }

    @FXML
    void unMute_Call() throws Exception {
        System.out.println("unmute call");
        if (currentCall != null) {
            CallOpParam prm = new CallOpParam(true);
            currentCall.audioMedia.adjustRxLevel((float) 1);
            MyApp.ep.libHandleEvents(10L);
        }
    }

    @FXML
    void hold() throws Exception {
        System.out.println(" Hold the Call");
        if (currentCall != null) {
            CallOpParam callOp = new CallOpParam(true);
            callOp.setStatusCode(pjsua_call_flag.PJSUA_CALL_UPDATE_CONTACT);
            callOp.setReason("hold");
            currentCall.setHold(callOp);
            MyApp.ep.libHandleEvents(10L);
        }
    }

    //method is used for unHold the call
    @FXML
    void unHold() throws Exception {
        if (currentCall != null) {
            System.out.println("call is Unloaded");
            CallOpParam callOp = new CallOpParam(true);
            callOp.getOpt().setFlag(1);
            currentCall.reinvite(callOp);
            MyApp.ep.libHandleEvents(10L);
        }
    }

    @FXML
    public void makeCall() throws InterruptedException {

        Platform.runLater(() -> {
            try {
                MyAccount acc = MyApp.accList.get(0);
                acc.setDefault();
                currentCall = new MyCall(acc, 0);
                try {
                    CallOpParam prm = new CallOpParam();
                    prm.getOpt().setAudioCount(1);
                    prm.getOpt().setVideoCount(1);
                    currentCall.makeCall("sip:" + text.getText().trim() + "@" + acc.getInfo().getUri().substring(9), prm);
                } catch (Exception e) {
                    System.out.println("VIDEO CALL");
                }
                callStatusThread.start();
                call.setDisable(true);
            } catch (Exception ex) {
            }
        });

    }

    private void analyseCallState() {

        callStatustask = new Task() {
            @Override
            protected Object call() throws Exception {
                while (true) {
                    if (getCurrentCall() != null) {
                        Platform.runLater(() -> {
                            try {
                                callStatus.setText(MyCall.CALLSTATE.concat(String.valueOf(MyCall.CALLDURATION)));
                            } catch (Exception e) {
                                System.out.println(e.getCause());
                            }
                        });
                    }
                }
//                return null;
            }
        };

    }

    @FXML
    void send(ActionEvent event) {
        //sending the instant message
        if (currentCall != null) {
            //sending the instant Message
            try {
                SendInstantMessageParam prm = new SendInstantMessageParam();
                prm.setContentType("text/plain");
                prm.setContent(textmes.getText());
                currentCall.sendInstantMessage(prm);
                messageStatus.setText("sended   :)");
            } catch (Exception e) {
                messageStatus.setText("failed --");
            } finally {
                textmes.setText("");
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            updateing_callStatus();
        } catch (InterruptedException ex) {

        }
        call();
    }
}
