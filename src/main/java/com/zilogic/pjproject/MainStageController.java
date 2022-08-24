package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import static com.zilogic.pjproject.MyAccount.INCOMINGCALL;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AuthCredInfo;

public class MainStageController implements Initializable {

//Account user authication
    AuthCredInfo auth;
    boolean exitIncomingCall = false;
    //MyApp has all utility function are created...
    private static MyApp app = new MyApp();
    //which is used to show all the account in the list
    @FXML
    public static Stage addAccountStage;
    @FXML
    public static Stage outGoingCallStage;
    @FXML
    public static Stage add_buddy_stage;
    @FXML
    public static Stage message_Stage;
    private static MyObserver observer = new MyObserver();
    public static MyAccount account = null;
    private static AccountConfig accCfg = null;
    @FXML
    public static BorderPane borderPane;
    static Stage incoming_stage = null;
    static int i = 0;
    @FXML
    JFXButton incomingCallBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            app.init((MyAppObserver) observer, ".", true);
            loadingIncomingUI();
            dis_ConnectCallStage();
        } catch (Exception ex) {
            System.err.println(" Excpetion while loading the runWorker");
        }
    }

    @FXML
    public void loadIncomingUI() {
        try {
            incoming_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("IncomingCall.fxml"));
            Scene scene = new Scene(root);
            incoming_stage.setScene(scene);
            incoming_stage.setTitle("Incomingcall");
            incoming_stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void loadmessage() throws Exception {
        try {
            message_Stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Message.fxml"));
            Scene scene = new Scene(root);
            message_Stage.setScene(scene);
            message_Stage.setTitle("message");
            message_Stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    @param methid is used to verfify the incoming call is Arrived or not
     */
    static Task<Void> task;

    @FXML
    public synchronized void loadingIncomingUI() {

        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Thread.sleep(1000);
                    try {
                        System.out.println("INCOMING CALL");
                        if (INCOMINGCALL == 1) {
                            INCOMINGCALL++;
                            System.out.println("INCOMING CALL fired");
                            Platform.runLater(() -> {
                                try{
                                IncomingCallController.incoming_Call_Ringtone();
                                IncomingCallController ic=new IncomingCallController();
                                loadIncomingUI();
                                }catch(Exception e){e.getMessage();}});
                        }
                    } catch (Exception e) {
                        System.out.println("Exception while loading the incoming call ");
                        System.out.println(e.getMessage());
                    }
                }
                //   return null;
            }

        };
        new Thread(task).start();
    }

    /*
    *creating the Thread for verfify and close the stage for the Incoming and OutGoing call stage 
     */
    static Task<Void> dis_connect;

    void disConnect_UI() {
        try{
        if (MainStageController.incoming_stage.isShowing()) {
            IncomingCallController.player.stopTransmit(IncomingCallController.ring_pay_back);
            MainStageController.incoming_stage.close();
        }}catch(Exception e){e.getMessage();}
    }

    @FXML
    public synchronized void dis_ConnectCallStage() {

        dis_connect = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Thread.sleep(1000);
                    try {
                        System.out.println("DISCCONECT CALL");
                        if (MyCall.DISCONNECTCALL == 6) {
                            MyCall.DISCONNECTCALL = 0;
                            System.out.println("DISCCONECT STAGE ");
                            Platform.runLater(() -> {
                                disConnect_UI();
                            });
                        }
                    } catch (Exception e) {
                        System.out.println("Exception while loading the incoming call ");
                        System.out.println(e.getMessage());
                    }
                }
            }

        };
        new Thread(dis_connect).start();
    }

    @FXML

    public void accountButton() throws IOException {

        addAccountStage = new Stage();
        System.out.println("Account Button is Clicked....");
        Parent root = FXMLLoader.load(getClass().getResource("AddAccount.fxml"));
        Scene scene = new Scene(root);
        addAccountStage.setScene(scene);
        addAccountStage.showAndWait();
    }

    @FXML
    public void callButton() throws IOException {
        outGoingCallStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("outGoingCall.fxml"));
        Scene scene = new Scene(root);
        outGoingCallStage.setScene(scene);
        outGoingCallStage.showAndWait();
    }

    @FXML
    public void buddyButton() throws Exception {

        add_buddy_stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddBuddy.fxml"));
        Scene scene = new Scene(root);
        add_buddy_stage.setScene(scene);
        add_buddy_stage.setTitle("ADD BUDDY");
        add_buddy_stage.showAndWait();
    }

  

    /*
    @param method is used for creating the account using the External Thread for polling the Event
     */
    @FXML
    public static MyAccount createAccount() throws Exception {
        MyApp.accList.clear();
        MyAccountConfig accCfg = new MyAccountConfig();
        AddAccount addaccount = AddAccountController.userdetails.get(0);
        AuthCredInfo auth = new AuthCredInfo("digest", "*", addaccount.getUsername(), 0, addaccount.getPassword());
        AccountConfig accCon = new AccountConfig();
        accCon.setIdUri("sip:" + addaccount.getUsername() + "@" + addaccount.getDomainAddress().trim());
        accCon.getRegConfig().setRegistrarUri("sip:" + addaccount.getDomainAddress().trim());
        accCon.getSipConfig().getAuthCreds().add(auth);
        accCon.getNatConfig().setIceEnabled(true);
        accCon.getVideoConfig().setAutoTransmitOutgoing(true);
        accCon.getVideoConfig().setAutoShowIncoming(true);
        account = app.addAcc(accCon);
        app.saveConfig("pjsua2.json");
        System.out.println("========================================");
        MyApp.accCfgs.add(accCfg);
        for (MyAccount acc : MyApp.accList) {
            AccountInfo accinfo = acc.getInfo();
            if (acc.isDefault()) {
                System.out.println("account uri : " + acc.getInfo().getUri());
            }
            acc.setDefault();
//            System.out.println(" Registeration Status Expiry time : " + acc.getInfo().getRegExpiresSec());
//            System.out.println(" Account default : " + acc.isDefault() + "    ");
//            System.out.println(acc.isValid());
//            acc.getInfo().setOnlineStatus(true);
//            System.out.println(accinfo.getOnlineStatus() + "Account status  text :" + accinfo.getOnlineStatusText());
        }
        if (account != null) {
            return account;
        } else {
            return null;
        }
    }
}
